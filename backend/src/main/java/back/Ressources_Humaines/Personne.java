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
            "Dupont", "Martin", "Lemoine", "Lefevre", "Garcia", "Renard", "Dubois", "Cousin", "Lemoine", "Petit"
    };

    protected static final String[] PRENOMS_HOMMES = {
            "Pierre", "Paul", "Jacques", "Louis", "Henri", "Alexandre", "Thomas", "Nicolas", "Mathieu", "Victor"
    };

    protected static final String[] PRENOMS_FEMMES = {
            "Marie", "Claire", "Sophie", "Julie", "Camille", "Elise", "Aurélie", "Laura", "Valérie", "Chloé"
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
