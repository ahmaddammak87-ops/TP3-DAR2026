package serveurPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMT_3_2 {
    static final int PORT = 1234;

    public static void main(String[] args) {
        int numero = 0;
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Serveur calculatrice en attente de clients sur le port " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                numero++;
                System.out.println("Client n°" + numero + " connecté, adresse : "
                        + socket.getRemoteSocketAddress());
                new ClientProcess_3_2(socket, numero).start();
            }
        } catch (IOException e) {
            System.out.println("Erreur côté serveur : " + e.getMessage());
        }
    }
}
