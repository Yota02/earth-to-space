package back.chargeUtile;

public class ChargeUtile {

    private double poids;
    private String nom;
    private double volume;

    public ChargeUtile(double poids, String nom, double volume) {
        this.poids = poids;
        this.nom = nom;
        this.volume = volume;
    }

    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }
}
