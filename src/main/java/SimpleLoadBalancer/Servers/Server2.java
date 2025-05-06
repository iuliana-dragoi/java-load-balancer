package main.java.SimpleLoadBalancer.Servers;

import java.io.*;
import java.net.Socket;

public class Server2 extends AbstractServer {

    public static void main(String[] args) {
        new Server2(8082).startServer();
    }

    public Server2(int port) {
        super(port);
    }

    @Override
    protected void handleClientRequest(Socket clientSocket) {
        try (
                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            String message = (String) inputStream.readObject();
            System.out.println("Backend Server 8082 received: " + message);

            String response = "Processed by Backend Server 8082: " + message;
            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
