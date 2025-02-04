package back.usine;

public class Usine {

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
