package ru.sbertech.teterin.server;

import ru.sbertech.teterin.common.Account;
import ru.sbertech.teterin.common.AccountList;
import ru.sbertech.teterin.common.PaymentDocument;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class BankClient implements Runnable {
    private Socket socket;
    private int codeBank;

    BankClient(Socket socket) {
        this.socket = socket;
    }

    private int getCodeBank() {
        return codeBank;
    }

    private void setCodeBank(int codeBank) {
        this.codeBank = codeBank;
    }

    Socket getSocket() {
        return socket;
    }

    private void addMapClientForSelf() {
        Server.banks.put(getCodeBank(), this);
        Server.printClients();
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            setValidCodeBank(inputStream);
            while (true) {
                Object message = inputStream.readObject();
                if (message instanceof PaymentDocument) {
                    PaymentDocument doc = (PaymentDocument) message;
                    ServerHandler.tasks.add(doc);
                }
                if (message instanceof Account) {
                    Account accountList = (Account) message;
                    Server.account.addAccount(accountList);
                    Server.account.getListAllAccounts();
                }
            }
        } catch (IOException e) {
            System.out.printf("Client (Bank code: %s) is disconnect", codeBank);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setValidCodeBank(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {
        int temp = (Integer) inputStream.readObject();
        AccountList accountList = (AccountList) inputStream.readObject();
        setCodeBank(temp);
        addMapClientForSelf();
        Server.account.addListAccount(accountList.getAccountConcurrentHashMap());
    }

    @Override
    public String toString() {
        return "BankClient {" +
                "codeBank = " + codeBank +
                '}';
    }
}
