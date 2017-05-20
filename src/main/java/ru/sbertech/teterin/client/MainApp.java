package ru.sbertech.teterin.client;

import ru.sbertech.teterin.common.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainApp {
    private Bank bank;
    private ServerSender coordinator;

    public static void main(String[] args) {
        MainApp app = new MainApp();
        app.startApp();
    }

    private void startApp() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String nameBank = null;
        int codeBank = 0;
        try {
            System.out.println("Enter name for Bank");
            nameBank = reader.readLine();
            System.out.println("Enter code for bank");
            codeBank = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        bank = new Bank(nameBank, codeBank);
        bank.loadData();
        int size = Bank.getAccountList().getAccountConcurrentHashMap().size();
        PersonalAccount.setId(size);
        Socket socket = getSocket();

        DocumentHandler documentHandler = new DocumentHandler(bank);
        coordinator = new ServerSender(bank, socket);
        ServerListener customerListener = new ServerListener(socket, documentHandler);
        new Thread(documentHandler).start();
        Bank.accountList.getListAllAccounts();
        new Thread(coordinator).start();
        new Thread(customerListener).start();



        try {
            cycleOfOperations(reader, codeBank, documentHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Socket getSocket() {
        Socket socket = null;
        try {
            socket = new Socket(Constant.HOST, Constant.PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }

    private void cycleOfOperations(BufferedReader reader, int codeBank, DocumentHandler documentHandler) throws IOException {
        while (true) {
            System.out.println("Enter command: ");
            String operation = reader.readLine();
            operation = operation.toUpperCase();
            switch (operation) {
                case "AL":
                    seeListAllAccounts();
                    break;
                case "ADPA":
                    addPersonalAccount(reader);
                    break;
                case "ADCA":
                    addCorrespondAccount(reader, codeBank);
                    break;
                case "CPD":
                    createPaymentDocument(reader, documentHandler);
                    break;
                case "SAVE":
                    bank.saveData();
                    break;
                case "EXIT":
                    exitCommand();
                    break;
                default:
                    System.out.println("this command no support");
            }
        }
    }

    private void createPaymentDocument(BufferedReader reader, DocumentHandler documentHandler) throws IOException {
        System.out.println("Enter data");
        String payDoc = reader.readLine();
        String[] param = payDoc.split(" ");
        long creditAcc = Long.parseLong(param[0]);
        long debitAcc = Long.parseLong(param[1]);
        double amount = Double.parseDouble(param[2]);
        PaymentDocument paymentDocument = new PaymentDocument(creditAcc, debitAcc, amount);
        documentHandler.addDocument(paymentDocument);
    }

    private void seeListAllAccounts() {
        Bank.accountList.getListAllAccounts();
        System.out.println("Operation success done");
    }

    private void addCorrespondAccount(BufferedReader reader, int codeBank) throws IOException {
        System.out.println("Enter code bank for CA & Balance");
        String s = reader.readLine();
        String sParam[] = s.split(" ");
        int codeBankForCA = Integer.parseInt(sParam[0]);
        double amountForCA = Double.parseDouble(sParam[1]);
        CorrespondAccount correspondAccount = new CorrespondAccount(codeBank, codeBankForCA, amountForCA);
        Bank.accountList.addAccount(correspondAccount);
        coordinator.sendAccountList(correspondAccount);
    }

    private void addPersonalAccount(BufferedReader reader) throws IOException {
        System.out.println("Enter account deposit for new Personal Account");
        String numAccount = reader.readLine();
        double amountForPersonalAcc = Double.parseDouble(numAccount);
        PersonalAccount personalAccount = new PersonalAccount(bank.getCodeBank(), amountForPersonalAcc);
        Bank.accountList.addAccount(personalAccount);
        coordinator.sendAccountList(personalAccount);
    }

    private void exitCommand() {
        bank.saveData();
        System.exit(0);
    }
}
