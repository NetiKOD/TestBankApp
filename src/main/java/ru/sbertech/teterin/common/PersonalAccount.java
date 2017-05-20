package ru.sbertech.teterin.common;

import java.io.Serializable;

public class PersonalAccount extends Account implements Serializable {
    private static int id = 0;

    public PersonalAccount(int codeBank, double amount) {
        super("PersonalAccount", codeBank, amount);
        int number = ++id;
        this.numberAccount = (long) (codeBank * Math.pow(10, Constant.CAPACITY_NUMBER_ACCOUNT) + number);
    }

    public static void setId(int id) {
        PersonalAccount.id = id;
    }
}
