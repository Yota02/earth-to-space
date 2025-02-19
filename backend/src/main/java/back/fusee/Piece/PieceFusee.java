package back.fusee.Piece;

public enum PieceFusee {
    
    MOTEUR("Moteur"),
    RESERVOIR("Réservoir"),
    COQUE("Coque"),
    PANNEAUX_SOLAIRES("Panneaux solaires"),
    REACTEUR ("Réacteur"),
    CAPTEUR ("Capteur"),
    HABITAT ("Habitat"),
    MODULES_VAISSEAU ("Modules vaisseau");

    private final String nom;

    PieceFusee(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return this.nom;
    }

}
