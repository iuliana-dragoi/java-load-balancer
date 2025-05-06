package main.java.SimpleLoadBalancer.Servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer {

    protected int port;

    public AbstractServer(int port) {
        this.port = port;
    }

    protected void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println("Backend server running on port " + this.port);

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleClientRequest(socket)).start();
            }
        } catch (IOException e) {
            System.err.println("Error in BackendServer: " + e.getMessage());
        }
    }

    protected abstract void handleClientRequest(Socket clientSocket);
}
