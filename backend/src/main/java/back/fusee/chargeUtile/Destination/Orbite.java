package back.fusee.chargeUtile.Destination;

public enum Orbite {

    BASSE("Orbite basse terrestre"),
    GEOSTATIONNAIRE("Orbite géostationnaire"),
    LUNAIRE("Orbite lunaire"),
    MARS_ORBITALE("Orbite autour de Mars"),
    JUPITER_ORBITALE("Orbite autour de Jupiter"),
    HELIOCENTRIQUE("Orbite héliocentrique"), // Autour du Soleil
    INTERSTELLAIRE("Orbite interstellaire"), // Trajectoire vers d'autres étoiles
    POLAIRE("Orbite polaire"); // Orbitant autour de la Terre en passant par les pôles

    private final String nom;

    Orbite(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
}
