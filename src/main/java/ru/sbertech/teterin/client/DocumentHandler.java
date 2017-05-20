package ru.sbertech.teterin.client;

import ru.sbertech.teterin.common.Constant;
import ru.sbertech.teterin.common.PaymentDocument;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DocumentHandler implements Runnable {
    private int codeBankCreator;
    private Bank bank;
    private BlockingQueue<PaymentDocument> documentBlockingQueue;

    DocumentHandler(Bank bank) {
        this.documentBlockingQueue = new ArrayBlockingQueue<>(Constant.NUMBER_OF_TASK_CELLS);
        this.bank = bank;
        this.codeBankCreator = bank.getCodeBank();
    }

    private int getCodeBankCreator() {
        return codeBankCreator;
    }

    private BlockingQueue<PaymentDocument> getDocumentBlockingQueue() {
        return documentBlockingQueue;
    }

    void addDocument(PaymentDocument paymentDocument) {
        getDocumentBlockingQueue().add(paymentDocument);
        System.out.println("Document successfully added");
    }

    @Override
    public void run() {
        try {
            handler();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handler() throws InterruptedException {
        while (true) {
            PaymentDocument paymentDocument = getDocumentBlockingQueue().take();
            transfer(paymentDocument);
        }
    }

    boolean transfer(PaymentDocument paymentDocument) {
        long creditAcc = paymentDocument.getCreditAcc();
        long debitAcc = paymentDocument.getDebitAcc();
        if (checkOurAccount(creditAcc) && checkOurAccount(debitAcc)) {
            return bank.transactionInOurBank(paymentDocument);
        } else {
            retransmissionRequests(paymentDocument);
            return false;
        }
    }

    private boolean checkOurAccount(long account) {
        int codeBankHolder = Constant.getCodeBankHolder(account);
        return codeBankHolder == getCodeBankCreator();
    }

    private void retransmissionRequests(PaymentDocument paymentDocument) {
        ServerSender.addRequests(paymentDocument);
    }
}
