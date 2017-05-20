package ru.sbertech.teterin.server;

import ru.sbertech.teterin.common.AccountList;
import ru.sbertech.teterin.common.Constant;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    static Map<Integer, BankClient> banks = new HashMap<>();
    static AccountList account = new AccountList();

    public static void main(String[] args) {
        Server sbc = new Server();
        System.out.println("server start");
        ServerSenderTask serverSenderTask = new ServerSenderTask();
        ServerHandler serverHandler = new ServerHandler(serverSenderTask);
        new Thread(serverHandler).start();
        sbc.startServer();
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(Constant.PORT)) {
            while (true) {
                Socket bankSocket = serverSocket.accept();
                BankClient bankClient = new BankClient(bankSocket);
                new Thread(bankClient).start();
                System.out.println("new client");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void printClients() {
        for (BankClient account : banks.values()) {
            System.out.println(account.toString());
        }
    }
}
