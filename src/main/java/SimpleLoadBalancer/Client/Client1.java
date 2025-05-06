package main.java.SimpleLoadBalancer.Client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client1 {

    public static void main(String[] args) throws IOException {
        new Client1().start();
    }

    public void start() throws IOException {
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            for (int i = 0; i < 5; i++) {
                socket = new Socket(host.getHostName(), 8080);
                oos = new ObjectOutputStream(socket.getOutputStream());

                System.out.println("Sending request to Socket Server");
                if(i==4) oos.writeObject("exit");
                else oos.writeObject("" + i);

                ois = new ObjectInputStream(socket.getInputStream());
                String message = (String) ois.readObject();
                System.out.println("Message: " + message);

                ois.close();
                oos.close();
                Thread.sleep(5000);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
