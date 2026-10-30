package serveurPackage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class ServerMT_3_1 {


    /**
     * Extension du Server du TP2-1 : le thread principal ne fait plus que accepter
     * les clients. Le dialogue (calcul) est confié à un ClientProcess par client.
     */


        static final int PORT = 1234;

        public static void main(String[] args) {
            int numero = 0; // numéro d'ordre de connexion
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                System.out.println("Je suis un serveur en attente la connexion d'un client ");

                while (true) {
                    Socket socket = serverSocket.accept(); // bloque jusqu'au prochain client
                    numero++;

                    // b) afficher l'adresse IP (et le port) + le numéro d'ordre du client
                    System.out.println("Client n°" + numero + " connecté, adresse : "
                            + socket.getRemoteSocketAddress());

                    // un thread dédié par client, qui reçoit SA socket et SON numéro
                    new ClientProcess_3_1(socket, numero).start();
                }
                // pas de serverSocket.close() : la boucle est infinie
            } catch (IOException e) {
                System.out.println("Erreur côté serveur : " + e.getMessage());
            }
        }

    }
