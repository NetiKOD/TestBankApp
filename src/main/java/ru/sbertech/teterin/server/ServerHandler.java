package ru.sbertech.teterin.server;

import ru.sbertech.teterin.common.Constant;
import ru.sbertech.teterin.common.PaymentDocument;

import java.math.BigDecimal;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ServerHandler implements Runnable {
    static BlockingQueue<PaymentDocument> tasks = new ArrayBlockingQueue<>(Constant.NUMBER_OF_TASK_CELLS);
    private ServerSenderTask serverSenderTask;

    ServerHandler(ServerSenderTask serverSenderTask) {
        this.serverSenderTask = serverSenderTask;
    }

    @Override
    public void run() {
        PaymentDocument doc;
        try {
            while (true) {
                doc = tasks.take();
                int codeBankCreditor = Constant.getCodeBankHolder(doc.getCreditAcc());
                int codeBankDebtor = Constant.getCodeBankHolder(doc.getDebitAcc());
                if (codeBankCreditor == codeBankDebtor) {
                    if ((validOperation(doc.getCreditAcc(), doc.getAmount())) >= 0) {
                        serverSenderTask.sendNewTaskToClient(codeBankCreditor, doc);
                    } else {
                        System.out.println("Error: insufficient funds");
                    }
                } else {
                    long corAccCredit = getCorrespondAccount(codeBankCreditor, codeBankDebtor);
                    long corAccDebit = getCorrespondAccount(codeBankDebtor, codeBankCreditor);

                    if (validOperation(doc.getCreditAcc(), doc.getAmount()) >= 0 && (validOperation(corAccDebit, doc.getAmount()) >= 0)) {
                        serverSenderTask.sendNewTaskToClient(codeBankCreditor, new PaymentDocument(doc.getCreditAcc(), corAccCredit, doc.getAmount().doubleValue()));
                        serverSenderTask.sendNewTaskToClient(codeBankDebtor, new PaymentDocument(corAccDebit, doc.getDebitAcc(), doc.getAmount().doubleValue()));

                    } else {
                        System.out.println("Error: insufficient funds");
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int validOperation(long account, BigDecimal amount) {
        return Server.account.getAccountConcurrentHashMap().get(account).getBalance().compareTo(amount);
    }

    private long getCorrespondAccount(int codeBankCreditor, int codeBankDebtor) {
        return (long) (codeBankCreditor * (Math.pow(10, (Constant.CAPACITY_CODE_BANK_CORRESPOND + Constant.CAPACITY_NUMBER_ACCOUNT)))
                + codeBankDebtor * (Math.pow(10, (Constant.CAPACITY_NUMBER_ACCOUNT))) + 1);
    }
}
