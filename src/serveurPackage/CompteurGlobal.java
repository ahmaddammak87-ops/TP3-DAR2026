package serveurPackage;

public class CompteurGlobal {
    private static int compteur = 0;

    public static synchronized int incrementer() {
        compteur++;
        return compteur; // valeur lue dans la même section critique
    }
}
