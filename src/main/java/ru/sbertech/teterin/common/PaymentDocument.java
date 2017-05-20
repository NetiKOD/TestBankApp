package ru.sbertech.teterin.common;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaymentDocument implements Serializable {
    private long creditAcc;
    private long debitAcc;
    private BigDecimal amount;

    public PaymentDocument(long creditAcc, long debitAcc, double amount) {
        this.creditAcc = creditAcc;
        this.debitAcc = debitAcc;
        this.amount = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    public long getCreditAcc() {
        return creditAcc;
    }

    public long getDebitAcc() {
        return debitAcc;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return creditAcc + " " + debitAcc + " " + amount;
    }
}
