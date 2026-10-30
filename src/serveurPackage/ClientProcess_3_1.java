package serveurPackage;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Un thread = un client. Contient le dialogue qui était dans main() du Server du TP2-1.
 */
public class ClientProcess_3_1 extends Thread {

    private Socket socket;
    private int numero;

    public ClientProcess_3_1(Socket socket, int numero) {
        this.socket = socket;
        this.numero = numero;
    }

    @Override
    public void run() {
        try {
            // Flux de communication (octets) puis flux de traitement (texte)
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true); // true = envoi automatique à chaque println

            // c) envoyer à chaque client son numéro d'ordre (1re ligne reçue par le client)
            pw.println("Bienvenue, vous êtes le client n°" + numero);

            // a) Le client envoie 3 lignes : opérande 1, opérateur, opérande 2
            String operande1 = br.readLine();
            while (operande1 != null && !operande1.equals("quit")) {
                String operateur = br.readLine();
                String operande2 = br.readLine();
                if (operateur == null || operande2 == null) {
                    break; // le client s'est déconnecté au milieu d'une opération
                }
                System.out.println("[client n°" + numero + "] Opération reçue : "
                        + operande1 + " " + operateur + " " + operande2);

                // b) Le serveur calcule
                String reponse;
                try {
                    reponse = calculer(Double.parseDouble(operande1), operateur,
                            Double.parseDouble(operande2));
                } catch (NumberFormatException e) {
                    reponse = "Erreur : opérande invalide";
                }

                // c) Le serveur envoie le résultat
                pw.println(reponse);
                System.out.println("[client n°" + numero + "] Réponse envoyée : " + reponse);

                operande1 = br.readLine(); // opération suivante, ou "quit"
            }

            System.out.println("Client n°" + numero + " déconnecté (fin de la communication)");
        } catch (IOException e) {
            System.out.println("Erreur client n°" + numero + " : " + e.getMessage());
        } finally {
            try {
                socket.close(); // chaque thread ferme SA socket (jamais la ServerSocket)
            } catch (IOException e) {
                // rien à faire
            }
        }
    }

    // Effectue l'opération et renvoie le texte à afficher chez le client
    static String calculer(double a, String operateur, double b) {
        double r;
        switch (operateur) {
            case "+":
                r = a + b;
                break;
            case "-":
                r = a - b;
                break;
            case "*":
                r = a * b;
                break;
            case "/":
                if (b == 0) {
                    return "Erreur : division par zéro impossible";
                }
                r = a / b;
                break;
            default:
                return "Erreur : opérateur inconnu";
        }
        //return formater(a) + " " + operateur + " " + formater(b) + " = " + formater(r);
        return a + " " + operateur + " " + b + " = " + r;
    }

    // 1375.0 s'affiche 1375 ; 3.5 reste 3.5
    static String formater(double v) {
        if (v == Math.rint(v) && !Double.isInfinite(v)) {
            return String.valueOf((long) v);
        }
        return String.valueOf(v);
    }
}