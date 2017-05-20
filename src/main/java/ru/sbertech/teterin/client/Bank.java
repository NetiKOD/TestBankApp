package ru.sbertech.teterin.client;

import ru.sbertech.teterin.common.Account;
import ru.sbertech.teterin.common.AccountList;
import ru.sbertech.teterin.common.PaymentDocument;

import java.io.*;

class Bank {
    static AccountList accountList = new AccountList();
    private final int codeBank;
    private String name;

    Bank(String name, int codeBank) {
        this.name = name;
        this.codeBank = codeBank;
    }

    int getCodeBank() {
        return codeBank;
    }

    static AccountList getAccountList() {
        return accountList;
    }

    void saveData() {
        String fileName = getCodeBank() + ".dat";
        try (FileOutputStream fos = new FileOutputStream(fileName); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(accountList);
            oos.flush();
        } catch (IOException e) {
            System.out.println("Error write to file");
            e.printStackTrace();
        }

        System.out.println("Successfully saved data");
    }

    void loadData() {
        String fileName = getCodeBank() + ".dat";
        try (FileInputStream fis = new FileInputStream(fileName); ObjectInputStream ois = new ObjectInputStream(fis)) {
            accountList = (AccountList) ois.readObject();
            System.out.println("Data loaded successfully");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error read to file");
            e.printStackTrace();
        }
    }

    synchronized boolean transactionInOurBank(PaymentDocument document) {
        Account credit = accountList.getAccountConcurrentHashMap().get(document.getCreditAcc());
        Account debit = accountList.getAccountConcurrentHashMap().get(document.getDebitAcc());
        if (credit.credit(document.getAmount())) {
            debit.debit(document.getAmount());
        } else {
            System.out.println("The operation failed. The credit account does not have the right amount");
            return false;
        }
        System.out.println("The operation was successfully completed");

        return true;
    }
}
