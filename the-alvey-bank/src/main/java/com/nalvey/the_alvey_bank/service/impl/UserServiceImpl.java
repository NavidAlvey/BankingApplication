package com.nalvey.the_alvey_bank.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nalvey.the_alvey_bank.config.JwtTokenProvider;
import com.nalvey.the_alvey_bank.dto.AccountInfo;
import com.nalvey.the_alvey_bank.dto.BankResponse;
import com.nalvey.the_alvey_bank.dto.CreditDebitRequest;
import com.nalvey.the_alvey_bank.dto.EmailDetails;
import com.nalvey.the_alvey_bank.dto.InquiryRequest;
import com.nalvey.the_alvey_bank.dto.LoginDto;
import com.nalvey.the_alvey_bank.dto.TransactionDto;
import com.nalvey.the_alvey_bank.dto.TransferRequest;
import com.nalvey.the_alvey_bank.dto.UserRequest;
import com.nalvey.the_alvey_bank.entity.Role;
import com.nalvey.the_alvey_bank.entity.User;
import com.nalvey.the_alvey_bank.repository.UserRepository;
import com.nalvey.the_alvey_bank.utils.AccountUtils;

@Service

public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

// --- NEW ACCOUNT CREATION -------------------------------------------------------------------------------------------------------
    @Override
    public BankResponse createAccount(UserRequest userRequest) {
         // Creating an account - saving a new user into the DB
         // Check if user already has an account
        if (userRepository.existsByEmail(userRequest.getEmail())) {
             return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                .accountInfo(null)
                .build();
        }        

        User newUser = User.builder()
            .firstName(userRequest.getFirstName())
            .lastName(userRequest.getLastName())
            .otherName(userRequest.getOtherName())
            .gender(userRequest.getGender())
            .address(userRequest.getAddress())
            .stateOfOrigin(userRequest.getStateOfOrigin())
            .accountNumber(AccountUtils.generateAccountNumber())
            .accountBalance(BigDecimal.ZERO)
            .email(userRequest.getEmail())
            .password(passwordEncoder.encode(userRequest.getPassword()))
            .phoneNumber(userRequest.getPhoneNumber())
            .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
            .status("ACTIVE")
            .role(Role.valueOf("ROLE_ADMIN"))
            .build();

        User savedUser = userRepository.save(newUser);
        //Send email alert
        EmailDetails emailDetails = EmailDetails.builder()
            .recipient(savedUser.getEmail())
            .subject("ACCOUNT CREATION")
            .messageBody("Congradulations! Your account has been successfully created! \n The following are your account details:\n" +
            "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " aka: " + savedUser.getOtherName() + 
            "\nAccount Number: " + savedUser.getAccountNumber())
            .build();
        emailService.sendEmailAlert(emailDetails);
        
        //Display bank response to generating new user
        return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
            .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
            .accountInfo(AccountInfo.builder()
                    .accountBalance(savedUser.getAccountBalance())
                    .accountNumber(savedUser.getAccountNumber())
                    .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                    .build())
            .build();

    }
    // --- SECURED ACCOUNT LOGIN W/ TOKEN -------------------------------------------------------------------------------------------------------
    public BankResponse login(LoginDto loginDto) {
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        EmailDetails loginAlert = EmailDetails.builder()
            .subject("You're logged in!")
            .recipient(loginDto.getEmail())
            .messageBody("You logged into your account. If you did not initiate this request, please contact your bank.")
            .build();

        emailService.sendEmailAlert(loginAlert);
        return BankResponse.builder()
            .responseCode("Login Success")
            .responseMessage(jwtTokenProvider.generateToken(authentication)) 
            .build();
    }
// --- BALANCE INQUIRY -------------------------------------------------------------------------------------------------------
    @Override
    public BankResponse balanceInquiry(InquiryRequest request) {
        // Check if provided account number exists in the DB
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                .accountInfo(null) // set to null because account number does not exist
                .build();
        }

        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
            .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
            .accountInfo(AccountInfo.builder()
                .accountBalance(foundUser.getAccountBalance())
                .accountNumber(request.getAccountNumber())
                .accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
                .build())
            .build();
    }
// --- NAME INQUIRY -------------------------------------------------------------------------------------------------------
    @Override
    public String nameInquiry(InquiryRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName();
    }
// --- CREDIT ACCOUNT -------------------------------------------------------------------------------------------------------
    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        // checking if the account exists
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                .accountInfo(null) // set to null because account number does not exist
                .build();
        }
        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount())); // adding BigDecimals is different than adding integers -> must use .add to append
        userRepository.save(userToCredit);
        
        // Save Transaction
        TransactionDto transactionDto = TransactionDto.builder()
            .accountNumber(userToCredit.getAccountNumber())
            .transactionType("CREDIT")
            .amount(request.getAmount())
            .build();
        transactionService.saveTransaction(transactionDto);
        
        return BankResponse.builder()
            .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
            .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
            .accountInfo(AccountInfo.builder()
                .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName())
                .accountBalance(userToCredit.getAccountBalance())
                .accountNumber(request.getAccountNumber())
                .build())
            .build();
    }
// --- DEBIT ACCOUNT -------------------------------------------------------------------------------------------------------
    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        // check if the account exists
        // check if the amount you intend to withdraw is no more than the current account balance
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                .accountInfo(null) // set to null because account number does not exist
                .build();
        }
        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        BigInteger availabeBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();
        if (availabeBalance.intValue() < debitAmount.intValue()) { 
            return BankResponse.builder()
                .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                .accountInfo(null)
                .build();
        } else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);
   
            // Save Transaction
            TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToDebit.getAccountNumber())
                .transactionType("DEBIT")
                .amount(request.getAmount())
                .build();
            transactionService.saveTransaction(transactionDto);

            return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                    .accountNumber(request.getAccountNumber())
                    .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName())
                    .accountBalance(userToDebit.getAccountBalance())
                    .build())
                .build();
        }
    }
// --- TRANSFER DEBIT TO CREDIT -------------------------------------------------------------------------------------------------------
    @Override
    public BankResponse transfer(TransferRequest request) {
        // get the account to debit (check if it exists)
        boolean isSourceAccountNumber = userRepository.existsByAccountNumber(request.getSourceAccountNumber());
        // get the account to credit (check if it exists)
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if (!isSourceAccountNumber) {
            return BankResponse.builder()
                .responseCode(AccountUtils.DEBIT_ACCOUNT_NOT_FOUND_CODE)
                .responseMessage(AccountUtils.DEBIT_ACCOUNT_NOT_FOUND_MESSAGE)
                .accountInfo(null) // set to null because account number does not exist
                .build();
        }
        if (!isDestinationAccountExist) {
            return BankResponse.builder()
                .responseCode(AccountUtils.CREDIT_ACCOUNT_NOT_FOUND_CODE)
                .responseMessage(AccountUtils.CREDIT_ACCOUNT_NOT_FOUND_MESSAGE)
                .accountInfo(null) // set to null because account number does not exist
                .build();
        }
        // check if the amount debitting is not more than the current account balance
        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance()) > 0) { // if true returns 1, if false returns -1
            return BankResponse.builder()
                .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                .accountInfo(null)
                .build();
        }

        // debit the account
        sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
        String sourceUsername = sourceAccountUser.getFirstName() + " " + sourceAccountUser.getLastName();
        userRepository.save(sourceAccountUser); // save changes to database
        EmailDetails debitAlert = EmailDetails.builder()
            .subject("DEBIT ALERT")
            .recipient(sourceAccountUser.getEmail())
            .messageBody("The sum of " + request.getAmount() + " has been deducted from your account! Your current balance is " + sourceAccountUser.getAccountBalance())
            .build();
        
        emailService.sendEmailAlert(debitAlert);

        // credit the account
        User destinationAccuntUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationAccuntUser.setAccountBalance(destinationAccuntUser.getAccountBalance().add(request.getAmount()));
        // String recipientUsername = destinationAccuntUser.getFirstName() + " " + destinationAccuntUser.getLastName();
        userRepository.save(destinationAccuntUser); // save changes to database
        EmailDetails creditAlert = EmailDetails.builder()
            .subject("CREDIT ALERT")
            .recipient(sourceAccountUser.getEmail())
            .messageBody("The sum of " + request.getAmount() + " has been sent to your account from " + sourceUsername + "! Your current balance is " + sourceAccountUser.getAccountBalance())
            .build();
        
        emailService.sendEmailAlert(creditAlert);

        // Save Transaction
        TransactionDto transactionDto = TransactionDto.builder()
            .accountNumber(destinationAccuntUser.getAccountNumber())
            .transactionType("TRANSFER")
            .amount(request.getAmount())
            .build();
        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
            .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
            .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
            .accountInfo(null)
            .build();
    
    }
}
