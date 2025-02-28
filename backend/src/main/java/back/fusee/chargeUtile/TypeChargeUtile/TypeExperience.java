package back.fusee.chargeUtile.TypeChargeUtile;

public enum TypeExperience implements TypeChargeUtileInterface{

    EXPERIENCE_PHYSIQUE("Expérience physique"),
    EXPERIENCE_CHIMIQUE("Expérience chimique"),
    EXPERIENCE_BIOLOGIQUE("Expérience biologique");

    private final String description;

    TypeExperience(String description) {
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
