package serveurPackage;

import commonPackage.operation;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientProcess_3_2 extends Thread {
    private Socket socket;
    private int numero;

    public ClientProcess_3_2(Socket socket, int numero) {
        this.socket = socket;
        this.numero = numero;
    }

    @Override
    public void run() {
        try {
            // Le flux de sortie est créé et "flushé" en premier (évite l'interblocage des en-têtes)
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject("Bienvenue, vous êtes le client n°" + numero);
            oos.flush();

            while (true) {
                operation op = (operation) ois.readObject();

                if (op.getOperateur().equals("quit")) {
                    break;
                }
                System.out.println("[client n°" + numero + "] Opération reçue : " + op);

                calculer(op);

                // Section critique : incrément + affichage de la nouvelle valeur
                int total = CompteurGlobal.incrementer();
                System.out.println("[client n°" + numero + "] Résultat : "
                        + (op.getErreur() == null ? op.getResultat() : op.getErreur())
                        + " | Total opérations traitées : " + total);

                oos.reset();          // évite le cache d'objets d'ObjectOutputStream
                oos.writeObject(op);
                oos.flush();
            }
            System.out.println("Client n°" + numero + " déconnecté");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Client n°" + numero + " : connexion terminée (" + e.getMessage() + ")");
        } finally {
            try { socket.close(); } catch (IOException e) { /* rien */ }
        }
    }

    private static void calculer(operation op) {
        double a = op.getOperande1(), b = op.getOperande2();
        switch (op.getOperateur()) {
            case "+": op.setResultat(a + b); break;
            case "-": op.setResultat(a - b); break;
            case "*": op.setResultat(a * b); break;
            case "/":
                if (b == 0) op.setErreur("Division par zéro impossible");
                else op.setResultat(a / b);
                break;
            default: op.setErreur("Opérateur inconnu");
        }
    }
}
