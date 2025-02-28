package back.fusee.chargeUtile.TypeChargeUtile;

public enum TypeCargo implements TypeChargeUtileInterface {

    CARGO_RAVITAILLEMENT("Cargo de ravitaillement");

    private final String description;

    TypeCargo(String description) {
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