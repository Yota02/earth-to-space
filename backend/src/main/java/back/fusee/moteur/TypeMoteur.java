package back.fusee.moteur;

public enum TypeMoteur {

    NUCLEAIRE("Nucleaire"),
    CHIMIQUE("Chimique"),
    ELECTRIQUE("Electrique"),
    IONIQUE("Ionique"),
    SOLIDE("Solide");

    private String type;

    TypeMoteur(String type) {
        this.type = type;
    }
    
}
