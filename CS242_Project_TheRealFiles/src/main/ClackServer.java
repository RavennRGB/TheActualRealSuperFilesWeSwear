package main;

import data.ClackData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InputMismatchException;

public class ClackServer {
    int port;
    private static final int DEFAULT_PORT = 6969;
    boolean closeConnection;
    public ClackData dataToReceiveFromClient;
    public ClackData dataToSendToClient;
    ObjectOutputStream outToClient;
    ObjectInputStream inFromClient;


    public ClackServer(int port) {
        if (port < 1024)
            throw new IllegalArgumentException("Port Number must be greater than 1024.");
        else
            this.port = port;
        this.closeConnection = false;
        this.dataToReceiveFromClient = null;
        this.dataToSendToClient = null;
        this.outToClient = null;
        this.inFromClient = null;


    }

    public ClackServer() {
        this(DEFAULT_PORT);
    }

    public void start() {
        try {
            ServerSocket sskt = new ServerSocket(port);
            System.out.println("Waiting to accept client");
            Socket clientSkt = sskt.accept();
            System.out.println("accepted");
            this.outToClient = new ObjectOutputStream(clientSkt.getOutputStream());
            this.inFromClient = new ObjectInputStream(clientSkt.getInputStream());

            while (!this.closeConnection) {
                receiveData();
                this.dataToSendToClient = this.dataToReceiveFromClient;
                //this.dataToReceiveFromClient = this.dataToSendToClient;
                sendData();
            }
            sskt.close();
            clientSkt.close();
            this.outToClient.close();
            this.inFromClient.close();
        } catch (IOException ioe) {
            System.err.println("IO Exception occurred");
        }

    }

    public void receiveData() {
        try {
            this.dataToReceiveFromClient = (ClackData) this.inFromClient.readObject();
            //this.inFromClient.readObject(this.dataToReceiveFromClient);
        } catch (IOException ioe) {
            System.err.println("IO Exception");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("class not found");
        } catch (RuntimeException rte) {
            System.err.println("runtime Exception");
        }

    }

    public void sendData() {
        try {

            //this.dataToSendToClient = this.dataToReceiveFromClient;
            this.outToClient.writeObject(this.dataToSendToClient);

        } catch (IOException ioe) {
            System.err.println("IO Exception");
        } catch (RuntimeException rte) {
            System.err.println("runtime Exception");
        }
    }

    public int getPort() {
        return this.port;
    }


    public int hashCode() {
        return 17 * (1 + this.port);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ClackServer)) {
            return false;
        }

        ClackServer otherClackServer = (ClackServer) other;
        return (this.port == otherClackServer.port && this.closeConnection == otherClackServer.closeConnection);
    }

    public String toString() {
        //return ("Port: " + this.port);
        return "This instance of ClackServer has the following properties:\n"
                + "Port number: " + this.port + "\n"
                + "Connection status: " + (this.closeConnection ? "Closed" : "Open") + "\n"
                + "Data to receive from the client: " + this.dataToReceiveFromClient + "\n"
                + "Data to send to the client: " + this.dataToSendToClient + "\n";
    }


    public static void main(String[] args) {
        try {
        ClackServer server;
        if (args.length == 0) {
            server = new ClackServer();
        } else {

                int port = Integer.parseInt(args[0]);
                server = new ClackServer(port);

        server.start();
    }
        } catch(NumberFormatException ime){System.err.println("Incorrect input");}
    }
}

