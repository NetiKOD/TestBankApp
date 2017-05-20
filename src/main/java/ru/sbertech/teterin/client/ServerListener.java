package ru.sbertech.teterin.client;

import ru.sbertech.teterin.common.PaymentDocument;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerListener implements Runnable {
    private Socket socket;
    private DocumentHandler handler;

    ServerListener(Socket socket, DocumentHandler handler) {
        this.socket = socket;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream reader = new ObjectInputStream(socket.getInputStream());
            PaymentDocument taskFromServer;
            while (true) {
                taskFromServer = (PaymentDocument) reader.readObject();
                PaymentDocument paymentDocument = new PaymentDocument(taskFromServer.getCreditAcc(), taskFromServer.getDebitAcc(), taskFromServer.getAmount().doubleValue());
                handler.transfer(paymentDocument);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
