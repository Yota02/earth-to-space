package back.fusee;

import java.util.List;

import back.chargeUtile.ChargeUtile;
import back.fusee.booster.Booster;
import back.moteur.Moteur;

public class Fusee {

    private String nom;
    private double taille;
    private double diametre;

    private double poidsTotal;
    private double altitudeMax;

    // private List<Booster> boosters;
    private Booster boosterPrincipal;

    private List<ChargeUtile> poidChargeUtiles;

    private boolean systemeSecurite;

    private int etat;

    // Constructeur privé pour forcer l'utilisation du Builder
    public Fusee(String nom, double taille, Booster boosterPrincipal, List<ChargeUtile> poidChargeUtiles,  boolean systemeSecurite) {
        this.nom = nom;
        this.taille = taille;
        this.diametre = getDiametre();
        this.poidsTotal = getPoidsTotal();
        this.altitudeMax = getAltitudeMax();
        //this.boosters = boosters;
        this.boosterPrincipal = boosterPrincipal;
        this.poidChargeUtiles = poidChargeUtiles;
        this.systemeSecurite = systemeSecurite;
        this.etat = 0;
    }

    public void decoler() {
        System.out.println("Décollage de la fusée " + nom + "...");
    }

    public void exploser() {
        System.out.println("Boom... Fusée " + nom + " a explosé...");
    }

    public void atterir() {
        System.out.println("Fusée " + nom + " a atterri...");
    }

    public void orbite() {
        System.out.println("La fusée " + nom + " est en orbite.");
    }

    // Getters
    public String getNom() {
        return nom;
    }

    public double getTaille() {
        return taille + boosterPrincipal.getTaille();
    }

    public double getDiametre() {
        return boosterPrincipal.getTaille();
    }

    public double getPoidsTotal() {
        return boosterPrincipal.getPoids();
    }

    public double getAltitudeMax() {
        double rep = 0;
        for(Moteur m : boosterPrincipal.getMoteur()){
            rep += m.getPousseeMax();
        }
        rep -= poidsTotal;
        return rep;
    }

    public List<ChargeUtile> getPoidChargeUtiles() {
        return poidChargeUtiles;
    }

    public boolean isSystemeSecurite() {
        return systemeSecurite;
    }

    public int getEtat() {
        return etat;
    }

}