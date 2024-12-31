package back.moteur;

public enum Carburant {

    AZOTE("Azote liquide", 0.5),
    OXYGEN("Oxygène liquide", 1.2),

    METHANES("Méthane liquide", 1.8),
    IONIQUE("Ionique", 0.8),

    NUCLEAIRE("Nucléaire", 0.3),
    HYDROGENE("Hydrogène liquide", 2.0),
    HELIUM("Hélium liquide", 0.1),

    PROPULSION_ELECTRIQUE("Propulsion électrique", 0.0),

    ALCOOL("Alcool éthylique", 1.5),
    GAZ_NATUREL("Gaz naturel", 1.6);

    private final String nom;
    private final double coefficientExplosif;

    Carburant(String description, double coefficientExplosif) {
        this.nom = description;
        this.coefficientExplosif = coefficientExplosif;
    }

    public String getNom() {
        return nom;
    }

    public double getCoefficientExplosif() {
        return coefficientExplosif;
    }

    @Override
    public String toString() {
        return name() + " (" + getNom() + ", Coefficient explosif: " + coefficientExplosif + ")";
    }
}
