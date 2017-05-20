package ru.sbertech.teterin.common;

public class Constant {
    public static final int CAPACITY_NUMBER_ACCOUNT = 6;
    public static final int CAPACITY_CODE_BANK_CORRESPOND = 2;
    public static final int NUMBER_OF_TASK_CELLS = 50;
    public static final int PORT = 2341;
    public static final String HOST = "localhost";


    public static int getCodeBankHolder(long account) {
        long tempNumber = account;
        int numberOfDigits = 0;
        do {
            tempNumber /= 10;
            numberOfDigits++;
        }
        while (tempNumber >= 10);
        int codeBankHolder = 0;
        switch (numberOfDigits) {
            case 6:
                codeBankHolder = (int) Math.floor(account / Math.pow(10, Constant.CAPACITY_NUMBER_ACCOUNT));
                break;
            case 8:
                codeBankHolder = (int) Math.floor(account / Math.pow(10, Constant.CAPACITY_CODE_BANK_CORRESPOND + Constant.CAPACITY_NUMBER_ACCOUNT));
                break;
        }
        return codeBankHolder;
    }
}
