package back.Ressources_Humaines;

public abstract class Personne {
    
    protected static int compteurId = 0;
    protected int clePrimaire;
    protected String nom;
    protected String prenom;
    protected int age;
    protected String sexe;
    protected Boolean malade;

    protected static final String[] NOMS = {
        "Dupont", "Martin", "Lemoine", "Lefevre", "Garcia", "Renard", "Dubois", "Cousin", "Petit", "Morel",
        "Bertrand", "Lambert", "Rousseau", "Chevalier", "Gautier", "Blanc", "Muller", "Michel", "Robin", "Fontaine",
        "Laurent", "Boucher", "Gérard", "Collet", "Dufresne", "Reynaud", "Perrot", "Lemoine", "Arnaud", "Delacroix",
        "Marchand", "Noël", "Fischer", "Benoît", "Fernandez", "Schneider", "Giraud", "Meunier", "Roy", "Charpentier"
    };
    
    protected static final String[] PRENOMS_HOMMES = {
        "Pierre", "Paul", "Jacques", "Louis", "Henri", "Alexandre", "Thomas", "Nicolas", "Mathieu", "Victor",
        "Antoine", "François", "Maxime", "Guillaume", "Benoît", "Adrien", "Lucas", "Simon", "Damien", "Julien",
        "Raphaël", "Théo", "Gabriel", "Clément", "Hugo", "Sébastien", "Yann", "Vincent", "Étienne", "Tristan",
        "Jean", "Loïc", "Dorian", "Kevin", "Philippe", "Romain", "Sylvain", "Geoffrey", "Baptiste", "David"
    };
    
    protected static final String[] PRENOMS_FEMMES = {
        "Marie", "Claire", "Sophie", "Julie", "Camille", "Elise", "Aurélie", "Laura", "Valérie", "Chloé",
        "Alice", "Sarah", "Emma", "Charlotte", "Lucie", "Manon", "Eva", "Léa", "Justine", "Margaux",
        "Anaïs", "Florence", "Céline", "Amandine", "Marion", "Isabelle", "Delphine", "Hélène", "Jessica", "Caroline",
        "Mélanie", "Nathalie", "Gabrielle", "Christine", "Olivia", "Sabrina", "Élodie", "Fanny", "Vanessa", "Audrey"
    };
    
    protected static final String[] SEXES = { "Homme", "Femme" };

    @Override
    public boolean equals(Object obj) {
        Personne personne = (Personne) obj;
        return clePrimaire == personne.getClePrimaire();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(clePrimaire);
    }

    public int getClePrimaire() {
        return clePrimaire;
    }

    public Boolean estMalade() {
        return malade;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public int getAge() {
        return age;
    }

    public String getSexe() {
        return sexe;
    }

    public int getSalaire() {
        return 1200;
    }

    // Méthode pour afficher un résumé des informations de la personne
    public String toString() {
        return "ID: " + clePrimaire + ", Nom: " + prenom + " " + nom + ", Age: " + age + ", Sexe: " + sexe;
    }
    
}
