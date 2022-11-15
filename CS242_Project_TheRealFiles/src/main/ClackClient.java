package main;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;
import data.ClackData;
import data.FileClackData;
import data.MessageClackData;

import static data.ClackData.*;
import static data.FileClackData.*;

public class ClackClient {
    public String userName;
    public String hostName;
    public static final int DEFAULT_PORT = 6969;
    public int port;
    public boolean closeConnection;
    public Scanner inFromStd;
    protected static final String DEFAULTKEY = "JHGJASKJ";
    ClackData dataToSendToServer;
    ClackData dataToReceiveFromServer;
//    ClackData dataToReceiveFromServer = null;
//    ClackData dataToSendToServer = null;

    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;
//    ObjectOutputStream outToServer = null;
//    ObjectInputStream inFromServer = null;


    public ClackClient(String userName, String hostName, int port) {
        if (userName != null)
            this.userName = userName;
        else throw new IllegalArgumentException("User Name is not acceptable");
        if (hostName != null)
            this.hostName = hostName;
        else throw new IllegalArgumentException("Host Name is not acceptable");
        if (!(port < 1024))
            this.port = port;
        else throw new IllegalArgumentException("Port is not acceptable");

        this.closeConnection = false;
        this.dataToReceiveFromServer = null;
        this.dataToSendToServer = null;
        this.outToServer = null;
        this.inFromServer = null;
    }


    public ClackClient(String userName, String hostName) throws IllegalArgumentException {
        this(userName, hostName, DEFAULT_PORT);
    }

    public ClackClient(String userName) throws IllegalArgumentException {
        this(userName, "localhost");
    }

    public ClackClient() throws IllegalArgumentException {
        this("anon");
    }

    public void start() {
        inFromStd = new Scanner(System.in);

        try {

            Socket skt = new Socket(hostName, port);
            // Socket skt = new Socket(hostName, DEFAULT_PORT);
            System.out.println("test");
            // PrintWriter outToServer = new PrintWriter(skt.getOutputStream(), true);
            // BufferedReader inFromServer = new BufferedReader(new InputStreamReader(skt.getInputStream()));
            this.outToServer = new ObjectOutputStream(skt.getOutputStream());
            this.inFromServer = new ObjectInputStream(skt.getInputStream());

            while (!this.closeConnection) {
                System.out.println("connection open");
                readClientData();
                this.dataToReceiveFromServer = this.dataToSendToServer;
                System.out.println("ye");
                if (inFromStd == null) {
                    System.out.println("No input detected");
                } else {
                    System.out.println("send data");
                    sendData();
                    System.out.println("receive data");
                    receiveData();
                    System.out.println("print");
                    printData();


                    // outToServer.println("From Server: Echo--" + inFromStd);
                }

                // dataToReceiveFromServer = dataToSendToServer;
                //  printData();
            }
            this.inFromStd.close();
            this.outToServer.close();
            this.inFromServer.close();
            skt.close();


        } catch (IOException ioe) {
            System.err.println("Try that again");

        }
    }

    public void readClientData() {
        System.out.println("Input desired choice");
        String nextString = inFromStd.next();
        try {
            if (nextString.equals("DONE")) {
                this.closeConnection = true;
                this.dataToSendToServer = new MessageClackData(this.userName, nextString, DEFAULTKEY,
                        ClackData.CONSTANT_LOGOUT);
                System.exit(0);

            } else if (nextString.equals("LISTUSERS")) { //nothing rn
                // else if (inFromStd.equals(CONSTANT_LOGOUT )) { // nothing rn

            } else if (nextString.equals("SENDFILE")) {
                System.out.println("Input file name: ");
                String filename = this.inFromStd.next();
                this.dataToSendToServer = new FileClackData(this.userName, filename, CONSTANT_SENDFILE);
                try {
                    ((FileClackData) this.dataToSendToServer).readFileContents(DEFAULTKEY);
                } catch (IOException ioe) {
                    System.err.println("IO Exception");
                    this.dataToSendToServer = null;
                }
            } else {
                String message = nextString + this.inFromStd.nextLine();
                // dataToSendToServer = new MessageClackData();
                System.out.println("reached else. not listusers or send file");
                this.dataToSendToServer = new MessageClackData(this.userName, message, DEFAULTKEY, ClackData.CONSTANT_SENDMESSAGE);
            }

        } catch (RuntimeException rte) {
            System.err.println("runtime error");
        }
    }

    public void sendData() {
        try {
            System.out.println("reached before try in send data");
            this.outToServer.writeObject(this.dataToSendToServer);
            System.out.println("reached try in send data");
        } catch (IOException ioe) {
            // catch (Exception e) {
            System.err.println("IO Exception");
        } catch (RuntimeException rte) {
            System.err.println("runtime Exception");
        }
    }

    public void receiveData() {
        try {
//            ClackData dataToReceiveFromServer = (ClackData) inFromServer.readObject();
            System.out.println("reached before try in receive data");
            this.dataToReceiveFromServer = (ClackData) this.inFromServer.readObject();
            System.out.println("reached try in receive data");
        } catch (IOException ioe) {
            System.err.println("IO Exception");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("class not found");
        } catch (RuntimeException rte) {
            System.err.println("runtime Exception");
        }
    }
    // inFromServer();

    public void printData() {

        if (this.dataToReceiveFromServer != null) {
            System.out.println("From: " + this.dataToReceiveFromServer.getUserName());
            System.out.println("Date: " + this.dataToReceiveFromServer.getDate());
            System.out.println("Data: " + this.dataToReceiveFromServer.getData(DEFAULTKEY));
            System.out.println();
        }
        //System.out.println(dataToReceiveFromServer);
    }

    ;


    public String getUserName() {
        return this.userName;
    }

    public String getHostName() {
        return this.hostName;
    }

    public int getPort() {
        return this.port;
    }

    public int hashCode() {
        int result = 5;
        result = 17 * result + this.userName.hashCode();
        result = 17 * result + this.hostName.hashCode();
        result = 17 * result + this.port;
        return result;
    }

    public boolean equals(Object other) {
        ClackClient otherClackClient = (ClackClient) other;
        return (this.userName == otherClackClient.userName && this.hostName == otherClackClient.hostName && this.port == otherClackClient.port);
    }

    public String toString() {
        return ("Username: " + this.userName + '\n' + "Host Name: " + this.hostName + '\n' + "Port: " + this.port);
    }

    public static void main(String[] args) {
        try{
        ClackClient client;
        if (args.length == 0) {
            client = new ClackClient();
        } else {
            Scanner scan = new Scanner(args[0]);
            scan.useDelimiter("@|:");

            String username = scan.next();

            if (scan.hasNext()) {

                String hostname = scan.next();
                System.out.println(hostname);
                if (scan.hasNext()) {
//                    int port = Integer.parseInt(args[0]);
                    int port = Integer.parseInt(scan.next());
                    System.out.println(port);
                    client = new ClackClient(username, hostname, port);
                } else {client = new ClackClient(username, hostname);}
                } else {
                    client = new ClackClient(username);
                }

            client.start();
            }

        } catch(NumberFormatException ime){System.err.println("Incorrect input");}
        }

    }


