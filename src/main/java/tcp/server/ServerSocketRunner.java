package tcp.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketRunner {

  public static void main(String[] args) throws Exception {
    ServerSocket serverSocket = new ServerSocket(9000);

    while (true) {
      Socket socket = serverSocket.accept();

      new Thread(() -> {
        System.out.println("New client connected");
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));) {

          String inputLine, outputLine;
          out.println("Hello client!");

          do {
            inputLine = in.readLine();
            System.out.println("Received: " + inputLine);
            out.println("Message Received");
          } while (!"bye".equals(inputLine));

          System.out.println("Closing connection...");
          socket.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }).start();

    }
  }
}
