package back.politique;

public enum Proprietaire {
    NASA("National Aeronautics and Space Administration"),
    ESA("European Space Agency"),
    SPACEX("Space Exploration Technologies Corp."),
    ROSCOSMOS("Russian Space Agency"),
    CNSA("China National Space Administration"),
    ISRO("Indian Space Research Organisation"),
    JAXA("Japan Aerospace Exploration Agency"),
    BLUE_ORIGIN("Blue Origin"),
    BOEING("Boeing Space Division"),
    UNKNOWN("Organisation inconnue");

    private final String nomComplet;

    // Constructeur
    Proprietaire(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    // Getter pour récupérer le nom complet
    public String getNomComplet() {
        return nomComplet;
    }

    @Override
    public String toString() {
        return this.name() + " (" + nomComplet + ")";
    }
}
