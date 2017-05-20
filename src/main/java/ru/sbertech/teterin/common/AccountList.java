package ru.sbertech.teterin.common;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class AccountList implements Serializable {
    private ConcurrentHashMap<Long, Account> accountConcurrentHashMap;

    public AccountList() {
        this.accountConcurrentHashMap = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<Long, Account> getAccountConcurrentHashMap() {
        return accountConcurrentHashMap;
    }

    public void addAccount(Account account) {
        accountConcurrentHashMap.put(account.getNumberAccount(), account);
    }

    public void addListAccount(ConcurrentHashMap<Long, Account> accountList) {
        this.accountConcurrentHashMap.putAll(accountList);
    }

    public void getListAllAccounts() {
        System.out.println("================================");
        for (Account account : getAccountConcurrentHashMap().values()) {
            System.out.println(account.toString());
        }
        System.out.println("================================");
    }
}
