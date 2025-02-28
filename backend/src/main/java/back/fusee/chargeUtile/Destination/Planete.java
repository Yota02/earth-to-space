package back.fusee.chargeUtile.Destination;

public enum Planete {

    MERCURE("Planète Mercure"),
    VENUS("Planète Vénus"),
    TERRE("Planète Terre", "Lune"),
    MARS("Planète Mars", "Phobos", "Deimos"),
    JUPITER("Planète Jupiter", "Io", "Europe", "Ganymède", "Callisto"),
    SATURNE("Planète Saturne", "Titan", "Encelade", "Rhea", "Mimas"),
    URANUS("Planète Uranus", "Miranda", "Ariel", "Umbriel", "Titania", "Oberon"),
    NEPTUNE("Planète Neptune", "Triton", "Nereid");
    
    private final String nom;
    private final String[] lunes;

    Planete(String nom, String... lunes) {
        this.nom = nom;
        this.lunes = lunes;
    }

    public String getNom() {
        return nom;
    }

    public String[] getLunes() {
        return lunes;
    }
}
