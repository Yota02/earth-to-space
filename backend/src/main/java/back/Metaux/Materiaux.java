package back.Metaux;

public enum Materiaux {

    ALUMINIUM("Aluminium", 2710, 660.3, 870),
    TITANE("Titane", 4500, 1668, 462),
    ACIER("Acier", 7850, 1510, 500),
    CARBONE("Carbone composite", 1600, 3650, 1400),
    FER("Fer", 8000, 1450, 16.0), 
    
    INOX("Inox", 8000, 1450, 16.0), 

    INCONEL("Inconel", 8440, 1390, 14.2),
    CUIVRE("Cuivre", 8960, 1085, 398),

    NICKEL("Nickel", 8908, 1455, 90.7),
    BERILLIUM("Béryllium", 1850, 1287, 216),
    TANTALE("Tantale", 16600, 3017, 57);

    private final String nom;                // nom 
    private final double densite;            // Densité en kg/m³
    private final double pointFusion;        // Point de fusion en °C
    private final double resistanceThermique; // Résistance thermique en W/(m·K)

    Materiaux(String nom, double densite, double pointFusion, double resistanceThermique) {
        this.nom = nom;
        this.densite = densite;
        this.pointFusion = pointFusion;
        this.resistanceThermique = resistanceThermique;
    }

    public String getNom() {
        return nom;
    }

    public double getDensite() {
        return densite;
    }

    public double getPointFusion() {
        return pointFusion;
    }

    public double getResistanceThermique() {
        return resistanceThermique;
    }

    @Override
    public String toString() {
        return name() + " (" + nom + ", Densité: " + densite + " kg/m³, Point de fusion: " +
               pointFusion + " °C, Résistance thermique: " + resistanceThermique + " W/(m·K))";
    }
}
