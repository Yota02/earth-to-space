package back.mission;

public enum Destination {
    
    TERRE("Terre"),
    ORBITE("Orbite terrestre"),
    LUNE("Lune"),
    MARS("Mars"),
    VENUS("Vénus"),
    JUPITER("Jupiter"),
    SATURNE("Saturne"),
    ASTEROIDES("Ceinture d'astéroïdes"),
    ESPACE_LOINTAIN("Espace lointain");
    
    private final String description;

    Destination(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
