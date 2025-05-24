package com.nalvey.the_alvey_bank.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.nalvey.the_alvey_bank.entity.Transaction;
import com.nalvey.the_alvey_bank.entity.User;

import org.springframework.stereotype.Component;
import java.io.OutputStream;

import com.nalvey.the_alvey_bank.repository.TransactionRepository;
import com.nalvey.the_alvey_bank.repository.UserRepository;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@AllArgsConstructor

public class BankStatement {

    private static final Logger log = LoggerFactory.getLogger(BankStatement.class);

    private TransactionRepository transactionRepository;
    private UserRepository userRepository;

    private static final String FILE = "/Users/navidalvey/Desktop/Banking_Application/MyStatements.pdf";

    // retrieve list of transactions within a date range given an account number
    // generate a PDF file of transactions
    // send the file via email

    // initialize list of transactions to generate statement using initialized variables accountNumber, startDate, & endDate
    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {

        // retrieve startDate & endDate from string form, then convert into general MM/DD/YY format
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        // generate list using data from Transaction.java file
        // use Lambda function to filter transactions that are equal to the desired accountNumber, startDate, & endDate
        List<Transaction> transactionList = transactionRepository.findAll().stream()
            .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
            .filter(transaction -> transaction.getCreatedAt() != null && transaction.getCreatedAt().isEqual(start))
            .filter(transaction -> transaction.getCreatedAt() != null && transaction.getCreatedAt().isEqual(end))
            .collect(Collectors.toList()); // since .stream() is used, must collect the filtered stream into a list

        // Generate elements of bank statement to be used
        User user = userRepository.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName() + " " + user.getLastName();
        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("setting sie of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // Create a table with a singular column to house the title "The Alvey Bank"
        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("The Alvey Bank"));
        bankName.setBorder(0); // sets border to none, default is border width 1
        bankName.setBackgroundColor(BaseColor.CYAN);
        bankName.setPadding(20f);

        // In the same table & column as before, create a new cell (new row) that houses the bank address
        PdfPCell bankAddress = new PdfPCell(new Phrase("800 Campbell Rd, Dallas, TX"));
        bankAddress.setBorder(0);
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        // Create a new table under the previous that has 2 columns
        // First column will display Start/End Date
        // Second column will display STATEMENT OF ACCOUNT, Customer Name, Customer Address
        // The order of cell creation in relation to columns goes from left to right, next row, left to right, etc... 
        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
        customerInfo.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);
        PdfPCell stopDate = new PdfPCell(new Phrase("End Date: " + endDate));
        stopDate.setBorder(0);
        PdfPCell name = new PdfPCell(new Phrase("Customer Name: " + customerName));
        name.setBorder(0);
        PdfPCell space = new PdfPCell();
        space.setBorder(0);
        PdfPCell address = new PdfPCell(new Phrase("Customer Address: " + user.getAddress()));
        address.setBorder(0);
        
        // Create a new table under the previous that has 4 columns
        // First column contains the date
        // Second column contains the TRANSACTION TYPE
        // Third column contains the TRANSACTION AMOUNT
        // Fourth column contains the STATUS
        PdfPTable transactionsTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("Date"));
        date.setBackgroundColor(BaseColor.CYAN);
        date.setBorder(0);
        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBackgroundColor(BaseColor.CYAN);
        transactionType.setBorder(0);
        PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        transactionAmount.setBackgroundColor(BaseColor.CYAN);
        transactionAmount.setBorder(0);
        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBackgroundColor(BaseColor.CYAN);
        status.setBorder(0);
        
        // Add the first cell in each column that contains the first piece of data from each variable
        transactionsTable.addCell(date);
        transactionsTable.addCell(transactionType);
        transactionsTable.addCell(transactionAmount);
        transactionsTable.addCell(status);

        // Get each piece of information from each transaction within the List
        // Populate the column by adding new cells and populate the new cells with data from the List
        // Convert the date and amount into strings for easier processing
        transactionList.forEach(transaction -> {
            transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
            transactionsTable.addCell(new Phrase(transaction.getTransactionType()));
            transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
            transactionsTable.addCell(new Phrase(transaction.getStatus()));
        });
        
        // Populate the cells of the statementInfo table with following variables
        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(stopDate);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        // Populate the document with the following tables
        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionsTable);

        document.close();

        return transactionList;
    }        
}
