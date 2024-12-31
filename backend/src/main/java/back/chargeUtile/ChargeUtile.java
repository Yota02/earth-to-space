package back.chargeUtile;

public abstract class ChargeUtile {

    protected float poids;
    protected String nom;
    protected float volume;

    public ChargeUtile(float poids, String nom, float volume) {
        this.poids = poids;
        this.nom = nom;
        this.volume = volume;
    }

    public float getPoids() {
        return poids;
    }

    public void setPoids(float poids) {
        this.poids = poids;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
}
