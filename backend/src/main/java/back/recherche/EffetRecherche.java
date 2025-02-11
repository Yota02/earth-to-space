package back.recherche;

import java.util.function.Consumer;

class EffetRecherche {
    private String nom;
    private String description;
    private Consumer<Recherche> action;

    public EffetRecherche(String nom, String description, Consumer<Recherche> action) {
        this.nom = nom;
        this.description = description;
        this.action = action;
    }

    public void appliquerEffet(Recherche recherche) {
        action.accept(recherche);
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }
}