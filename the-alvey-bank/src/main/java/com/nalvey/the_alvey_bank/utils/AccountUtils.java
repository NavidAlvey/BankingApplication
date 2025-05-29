package com.nalvey.the_alvey_bank.utils;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;


// ************************** 
// ****** ACCOUNT DETAILS ***
// **************************
public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE =  "This account already exists!";
    
    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created!";
    
    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User with the provided account number does not exist.";
    
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_MESSAGE = "User account found!";

    public static final String ACCOUNT_CREDITED_SUCCESS_CODE = "005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User account has been credited successfully!";

    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance";

    public static final String ACCOUNT_DEBITED_SUCCESS_CODE = "007";
    public static final String ACCOUNT_DEBITED_SUCCESS_MESSAGE = "User account has been debited successfully!";

    public static final String DEBIT_ACCOUNT_NOT_FOUND_CODE = "008";
    public static final String DEBIT_ACCOUNT_NOT_FOUND_MESSAGE = "The account you want to debit from was not found, please try again.";

    public static final String CREDIT_ACCOUNT_NOT_FOUND_CODE = "009";
    public static final String CREDIT_ACCOUNT_NOT_FOUND_MESSAGE = "The account you want to credit towards was not found, please try again.";

    public static final String TRANSFER_SUCCESSFUL_CODE = "010";
    public static final String TRANSFER_SUCCESSFUL_MESSAGE = "Transfer Successful!";

    public static final String OTP_SENT_CODE = "011";
    public static final String OTP_SENT_MESSAGE = "OTP has been successfully sent to user's email!";

    public static final String OTP_NOT_SENT_CODE = "012";
    public static final String OTP_NOT_SENT_MESSAGE = "OTP has not been sent to user's email!";

    public static final String OTP_EXPIRED_CODE = "013";
    public static final String OTP_EXPIRED_MESSAGE = "The OTP has expired!";

    public static final String OTP_INVALID_CODE = "014";
    public static final String OTP_INVALID_MESSAGE = "The OTP entered is invalid!";

    public static final String OTP_VALID_CODE = "015";
    public static final String OTP_VALID_MESSAGE = "This OTP is valid!";

    public static String generateAccountNumber() {

        LocalDate dateAccountCreated = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        String formattedDate = dateAccountCreated.format(formatter);

        // Extract the last two digits from year (ie: 2025 = 25)
        int lastTwoDigitsOfYear= dateAccountCreated.getYear() % 100;

        // Set range of random numbers: 100,000 <------------> 999,999
        int min = 100000;
        int max = 999999;
        // Generate random number between min and max
        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);
        // Convert the randNumber into strings, then concatenate them
        String randomNumber = String.valueOf(randNumber);
        StringBuilder accountNumber = new StringBuilder();
        
        return accountNumber.append(formattedDate).append(String.format("%02d", lastTwoDigitsOfYear)).append(randomNumber).toString();
    }

    public static String generateOtp() {
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        int count = 0;
        while (count < 8) {
            otp.append(random.nextInt(10));
            ++count;
        }
        return otp.toString();
    }
}
