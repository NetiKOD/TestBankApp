package ru.sbertech.teterin.common;

import java.io.Serializable;

public class CorrespondAccount extends Account implements Serializable {
    private static int Id = 0;

    public CorrespondAccount(int codeBankHolder, int codeBankCorrespond, double amount) {
        super("CorrespondAccount", codeBankHolder, amount);
        int numAcc = ++Id;
        this.numberAccount = (long) (codeBankHolder * (Math.pow(10, (Constant.CAPACITY_CODE_BANK_CORRESPOND + Constant.CAPACITY_NUMBER_ACCOUNT))) + codeBankCorrespond * Math.pow(10, Constant.CAPACITY_NUMBER_ACCOUNT) + numAcc);
    }
}
