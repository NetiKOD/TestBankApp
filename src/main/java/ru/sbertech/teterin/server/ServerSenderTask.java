package ru.sbertech.teterin.server;

import ru.sbertech.teterin.common.PaymentDocument;

import java.io.IOException;
import java.io.ObjectOutputStream;

class ServerSenderTask {
    synchronized void sendNewTaskToClient(int codeBank, PaymentDocument paymentDocument) {
        BankClient client = Server.banks.get(codeBank);
        transaction(paymentDocument);
        try {
            ObjectOutputStream writer = new ObjectOutputStream(client.getSocket().getOutputStream());
            writer.writeObject(paymentDocument);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void transaction(PaymentDocument document) {
        Server.account.getAccountConcurrentHashMap().get(document.getCreditAcc()).credit(document.getAmount());
        Server.account.getAccountConcurrentHashMap().get(document.getDebitAcc()).debit(document.getAmount());

        Server.account.getListAllAccounts();
    }
}
