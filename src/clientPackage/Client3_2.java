package clientPackage;

import commonPackage.operation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client3_2 {
    static final Pattern SYNTAXE = Pattern.compile(
            "^\\s*(-?\\d+(?:\\.\\d+)?)\\s*([+\\-*/])\\s*(-?\\d+(?:\\.\\d+)?)\\s*$");

    public static void main(String[] args) {
        // Usage : java Client3_2 [adresse_ip_du_serveur]   (par défaut : localhost)
        String hote = (args.length > 0) ? args[0] : "localhost";

        try {
            Socket socket = new Socket(hote, 1234);
            System.out.println("Connecté au serveur " + hote);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Scanner sc = new Scanner(System.in);

            System.out.println("Serveur : " + ois.readObject());

            while (true) {
                System.out.print("Donnez une opération (ex : 55 * 25) ou quit : ");
                String saisie = sc.nextLine().trim();
                if (saisie.equals("quit")) {
                    oos.writeObject(new operation(0, "quit", 0));
                    oos.flush();
                    break;
                }

                Matcher m = SYNTAXE.matcher(saisie);
                if (!m.matches()) {
                    System.out.println("Syntaxe invalide, rien n'est envoyé.");
                    continue;
                }

                operation op = new operation(Double.parseDouble(m.group(1)), m.group(2),
                        Double.parseDouble(m.group(3)));
                oos.writeObject(op);
                oos.flush();

                operation rep = (operation) ois.readObject();
                if (rep.getErreur() != null) {
                    System.out.println("Erreur : " + rep.getErreur());
                } else {
                    System.out.println("Résultat : " + rep + " = " + rep.getResultat());
                }
            }
            System.out.println("Fin de la communication");
            sc.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur côté client : " + e.getMessage());
        }
    }
}
