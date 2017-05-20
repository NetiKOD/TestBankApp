package ru.sbertech.teterin.client;

import ru.sbertech.teterin.common.Account;
import ru.sbertech.teterin.common.Constant;
import ru.sbertech.teterin.common.PaymentDocument;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ServerSender implements Runnable {
    private static BlockingQueue<PaymentDocument> documentQueue = new ArrayBlockingQueue<>(Constant.NUMBER_OF_TASK_CELLS);
    private Bank bankCodeCreator;
    private Socket socket;
    private ObjectOutputStream writer;


    ServerSender(Bank bankCreator, Socket socket) {
        this.bankCodeCreator = bankCreator;
        this.socket = socket;
    }

    static void addRequests(PaymentDocument paymentDocument) {
        documentQueue.add(paymentDocument);
    }

    @Override
    public void run() {
        try {
            System.out.println("Connect");
            writer = new ObjectOutputStream(socket.getOutputStream());
            authorizationOfBank();
            while (true) {
                PaymentDocument paymentDocument = documentQueue.take();
                transferToServer(paymentDocument);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void authorizationOfBank() throws IOException {
        writer.writeObject(bankCodeCreator.getCodeBank());
        writer.flush();
        writer.writeObject(Bank.accountList);
        writer.flush();
    }

    private void sendRequest(PaymentDocument message) throws IOException {
        writer.writeObject(message);
        writer.flush();
    }

    void sendAccountList(Account account) throws IOException {
        writer.writeObject(account);
        writer.flush();
    }

    private void transferToServer(PaymentDocument paymentDocument) throws InterruptedException, IOException {
        sendRequest(paymentDocument);
    }
}
