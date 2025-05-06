package main.java.SimpleLoadBalancer.Servers;

import java.io.*;
import java.net.Socket;

public class Server1 extends AbstractServer {

    public static void main(String[] args) {
        new Server1(8081).startServer();
    }

    public Server1(int port) {
        super(port);
    }

    @Override
    protected void handleClientRequest(Socket clientSocket) {
        try (
                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            String message = (String) inputStream.readObject();
            System.out.println("Backend Server 8081 received: " + message);

            String response = "Processed by Backend Server 8081: " + message;
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
