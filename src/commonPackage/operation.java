package commonPackage;

import java.io.Serializable;

public class operation implements Serializable {
    private static final long serialVersionUID = 1L;

    private double operande1;
    private double operande2;
    private String operateur;
    private double resultat;
    private String erreur; // null si tout va bien

    public operation(double operande1, String operateur, double operande2) {
        this.operande1 = operande1;
        this.operateur = operateur;
        this.operande2 = operande2;
    }

    public double getOperande1() { return operande1; }
    public double getOperande2() { return operande2; }
    public String getOperateur() { return operateur; }
    public double getResultat() { return resultat; }
    public void setResultat(double resultat) { this.resultat = resultat; }
    public String getErreur() { return erreur; }
    public void setErreur(String erreur) { this.erreur = erreur; }

    @Override
    public String toString() {
        return operande1 + " " + operateur + " " + operande2;
    }
}
