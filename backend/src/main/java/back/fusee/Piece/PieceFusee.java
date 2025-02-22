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

    public static PieceFusee fromNom(String nom) {
        for (PieceFusee piece : values()) {
            if (piece.nom.equalsIgnoreCase(nom)) {
                return piece;
            }
        }
        throw new IllegalArgumentException("Aucune pièce correspondante pour : " + nom);
    }
}
