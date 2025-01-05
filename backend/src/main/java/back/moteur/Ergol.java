package back.moteur;

public enum Ergol {

    AZOTE("Azote liquide", 0.5, 0.8, 0.5, -200.0, 1.2, 50.0, 0.01),
    OXYGEN("Oxygène liquide", 1.2, 1.1, 1.2, -183.0, 2.0, 55.0, 0.02),
    METHANES("Méthane liquide", 1.8, 0.9, 1.5, -161.0, 0.9, 55.5, 0.02),

    IONIQUE("Ionique", 0.8, 2.0, 0.3, 25.0, 100.0, 300.0, 0.0),

    NUCLEAIRE("Nucléaire", 0.3, 5.0, 0.1, 1000.0, 500.0, 1000.0, 0.0),
    HYDROGENE("Hydrogène liquide", 2.0, 0.7, 2.0, -253.0, 3.0, 120.0, 0.0),

    HELIUM("Hélium liquide", 0.1, 0.5, 0.05, -269.0, 5.0, 25.0, 0.0),
    PROPULSION_ELECTRIQUE("Propulsion électrique", 0.0, 10.0, 0.0, 25.0, 0.5, 200.0, 0.0),

    ALCOOL("Alcool éthylique", 1.5, 0.85, 1.4, 20.0, 1.0, 30.0, 0.05),
    GAZ_NATUREL("Gaz naturel", 1.6, 0.95, 1.3, -162.0, 1.5, 50.0, 0.02),
    
    KEROSENE("Kérosène", 2.1, 0.9, 1.6, 25.0, 0.8, 43.0, 0.25),
    BIODIESEL("Biodiesel", 1.7, 1.0, 1.2, 25.0, 0.9, 37.0, 0.15);

    private final String nom;
    private final Double coefficientExplosif;
    private final Double rendement;
    private final Double poid;
    private final Double temperatureOptimale;
    private final Double coutUnitaire;
    private final Double densiteEnergetique;
    private final Double emissionsCO2;

    Ergol(String description, double coefficientExplosif, double rendement, double poid, double temperatureOptimale, double coutUnitaire, double densiteEnergetique, double emissionsCO2) {
        this.nom = description;
        this.coefficientExplosif = coefficientExplosif;
        this.rendement = rendement;
        this.poid = poid;
        this.temperatureOptimale = temperatureOptimale;
        this.coutUnitaire = coutUnitaire;
        this.densiteEnergetique = densiteEnergetique;
        this.emissionsCO2 = emissionsCO2;
    }

    public String getNom() {
        return nom;
    }

    public double getCoefficientExplosif() {
        return coefficientExplosif;
    }

    public double getRendement() {
        return rendement;
    }

    public double getPoid() {
        return poid;
    }

    public double getTemperatureOptimale() {
        return temperatureOptimale;
    }

    public double getCoutUnitaire() {
        return coutUnitaire;
    }

    public double getDensiteEnergetique() {
        return densiteEnergetique;
    }

    public double getEmissionsCO2() {
        return emissionsCO2;
    }

    @Override
    public String toString() {
        return name() + " (" + getNom() + ", Coefficient explosif: " + coefficientExplosif + ", Rendement: " + rendement + ", Poids: " + poid + ", Température optimale: " + temperatureOptimale + "°C, Coût unitaire: " + coutUnitaire + " €/kg, Densité énergétique: " + densiteEnergetique + " MJ/kg, Émissions CO2: " + emissionsCO2 + " kg CO2/kg)";
    }
}
