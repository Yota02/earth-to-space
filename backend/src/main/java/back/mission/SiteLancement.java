package back.mission;

public enum SiteLancement {
    
    // Sites de lancement célèbres
    CAP_CANAVERAL("Cap Canaveral", "USA"),
    KOUROU("Centre Spatial Guyanais", "Guyane Française"),
    BAICONUR("Baïkonour", "Kazakhstan"),
    VANDENBERG("Vandenberg Space Force Base", "USA"),
    JAXA_TANEGASHIMA("Tanegashima Space Center", "Japon"),
    ZHUKOVSKY("Zhukovsky Cosmodrome", "Russie"),
    WENCHANG("Wenchang Satellite Launch Center", "Chine");

    private final String nom;
    private final String pays;

    // Constructeur pour initialiser les valeurs
    SiteLancement(String nom, String pays) {
        this.nom = nom;
        this.pays = pays;
    }

    // Méthodes d'accès
    public String getNom() {
        return nom;
    }

    public String getPays() {
        return pays;
    }

    @Override
    public String toString() {
        return nom + " (" + pays + ")";
    }
}
