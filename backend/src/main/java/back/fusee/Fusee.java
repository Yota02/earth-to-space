package back.fusee;

import java.util.List;

import back.fusee.booster.Booster;
import back.fusee.chargeUtile.ChargeUtile;
import back.fusee.moteur.Moteur;
public class Fusee {
    private String nom;
    private double taille;
    private double diametre;
    private double poidsTotal;
    private double altitudeMax;
    private Booster boosterPrincipal;
    private List<ChargeUtile> poidChargeUtiles;
    private boolean systemeSecurite;
    private int etat;

    public Fusee(String nom, double taille, Booster boosterPrincipal, List<ChargeUtile> poidChargeUtiles, boolean systemeSecurite) {
        this.nom = nom;
        this.taille = taille;
        this.boosterPrincipal = boosterPrincipal; 
        this.poidChargeUtiles = poidChargeUtiles;
        this.systemeSecurite = systemeSecurite;
        this.etat = 0;
        
        // Calculate these after all fields are initialized
        this.diametre = calculateDiametre();
        this.poidsTotal = calculatePoidsTotal();
        this.altitudeMax = calculateAltitudeMax();
    }

    private double calculateDiametre() {
        return boosterPrincipal != null ? boosterPrincipal.getDiametre() : 10.0;
    }

    private double calculatePoidsTotal() {
        double total = 0;
        if (boosterPrincipal != null) {
            total += boosterPrincipal.getPoids();
        }
        if (poidChargeUtiles != null) {
            for (ChargeUtile charge : poidChargeUtiles) {
                total += charge.getPoids();
            }
        }
        return total;
    }

    private double calculateAltitudeMax() {
        if (boosterPrincipal == null || boosterPrincipal.getMoteur() == null) {
            return 0.0;
        }
        
        double totalPoussee = 0;
        for (Moteur m : boosterPrincipal.getMoteur()) {
            if (m != null) {
                totalPoussee += m.getPousseeMax();
            }
        }
        return this.poidsTotal - totalPoussee;
    }

    // Rest of your methods remain the same
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
        return taille;
    }

    public double getDiametre() {
        return diametre;
    }    

    public double getPoidsTotal() {
        return poidsTotal;
    }

    public double getAltitudeMax() {
        return altitudeMax;
    }

    public Booster getBoosterPrincipal() {
        return boosterPrincipal;
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