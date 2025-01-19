package back.mission;

public enum TypeMission {
    
    EXPLORATION("Exploration"),
    SATELLITE("Lancement de satellite"),
    TRANSPORT("Transport de cargo"),
    SCIENTIFIQUE("Mission scientifique"),
    COMMERCIALE("Mission commerciale"),
    MILITAIRE("Mission militaire"),
    TOURISME_SPATIAL("Tourisme spatial"),
    RECONNAISSANCE("Reconnaissance"),
    TEST("Test de véhicule spatial"),
    COLONISATION("Colonisation de planète");

    private final String description;

    TypeMission(String description) {
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
