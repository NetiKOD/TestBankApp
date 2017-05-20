package ru.sbertech.teterin.common;

import java.io.Serializable;
import java.math.BigDecimal;

public abstract class Account implements Serializable {

    long numberAccount;
    private final String typeAccount;
    private final int codeBank;
    private BigDecimal balance;

    Account(String typeAccount, int codeBank, double balance) {
        this.typeAccount = typeAccount;
        this.codeBank = codeBank;
        this.balance = new BigDecimal(balance).setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    long getNumberAccount() {
        return numberAccount;
    }

    private String getTypeAccount() {
        return typeAccount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean credit(BigDecimal credit)
    {
        if (credit.compareTo(getBalance()) <= 0)
        {
            balance = balance.subtract(credit);
            return true;
        }
        return false;
    }

    public void debit(BigDecimal debit)
    {
        balance = balance.add(debit);
    }

    @Override
    public String toString() {
        return getTypeAccount() + "{" +
                "AccountID = " + numberAccount +
                ", balance = " + balance +
                '}';
    }
}
