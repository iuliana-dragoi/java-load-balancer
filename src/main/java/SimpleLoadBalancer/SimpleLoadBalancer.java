package main.java.SimpleLoadBalancer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleLoadBalancer {

    private static final List<String> backendServers = Arrays.asList("localhost", "localhost");

    private static final int[] backendPorts = {8081, 8082};

    private static final AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws IOException {
        new SimpleLoadBalancer().start();
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Load balancer running on port 8080");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (
                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream())
        ) {
            String message = (String) inputStream.readObject();
            System.out.println("Load Balancer received: " + message);

            int backendIndex = counter.getAndIncrement() % backendServers.size();
            String backendServer = backendServers.get(backendIndex);
            int backendPort = backendPorts[backendIndex];

            try (
                    Socket backendSocket = new Socket(backendServer, backendPort);
                    ObjectOutputStream backendOut = new ObjectOutputStream(backendSocket.getOutputStream());
                    ObjectInputStream backendIn = new ObjectInputStream(backendSocket.getInputStream())
            ) {
                backendOut.writeObject(message);
                backendOut.flush();

                String backendResponse = (String) backendIn.readObject();
                outputStream.writeObject(backendResponse);
                outputStream.flush();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
