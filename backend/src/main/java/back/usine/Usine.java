package back.usine;

import back.fusee.Fusee;
import back.fusee.moteur.Moteur;

import java.time.LocalDate;

public abstract class Usine {

    private String nom;
    private float superficie; // en m²
    private int capaciteProductionMensuelle; // Nombre maximum de fusées par mois
    private int fuséesProduitesCeMois;

    public Usine(String nom, float superficie, int capaciteProductionMensuelle) {
        this.nom = nom;
        this.superficie = superficie;
        this.capaciteProductionMensuelle = capaciteProductionMensuelle;
        this.fuséesProduitesCeMois = 0;
    }

    public abstract Fusee creerFusee(String nom, float taille, float poidsTotal, Moteur moteur,
                                     float carburantRestant, float poidChargeUtile, boolean systemeSecurite,
                                     LocalDate dateLancement, String destination, String typeMission, int etat);

    
    public boolean peutProduire() {
        return fuséesProduitesCeMois < capaciteProductionMensuelle;
    }


    public void produireFusee() {
        if (peutProduire()) {
            fuséesProduitesCeMois++;
        } else {
            throw new IllegalStateException("Capacité de production atteinte pour ce mois !");
        }
    }

    public void reinitialiserProductionMensuelle() {
        fuséesProduitesCeMois = 0;
    }

    @Override
    public String toString() {
        return "Usine: " + nom + ", Superficie: " + superficie + " m², Capacité mensuelle: " + capaciteProductionMensuelle;
    }
}
