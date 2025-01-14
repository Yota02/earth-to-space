package back.Ressources_Humaines;

import java.util.Random;

public class Personne {

    private String nom;
    private String prenom;
    private int age;
    private String sexe;
    private Boolean malade;

    private static final String[] NOMS = {
        "Dupont", "Martin", "Lemoine", "Lefevre", "Garcia", "Renard", "Dubois", "Cousin", "Lemoine", "Petit"
    };

    private static final String[] PRENOMS_HOMMES = {
        "Pierre", "Paul", "Jacques", "Louis", "Henri", "Alexandre", "Thomas", "Nicolas", "Mathieu", "Victor"
    };

    private static final String[] PRENOMS_FEMMES = {
        "Marie", "Claire", "Sophie", "Julie", "Camille", "Elise", "Aurélie", "Laura", "Valérie", "Chloé"
    };

    private static final String[] SEXES = {"Homme", "Femme"};

    public Personne() {
        Random random = new Random();
        this.nom = NOMS[random.nextInt(NOMS.length)];
        this.sexe = SEXES[random.nextInt(SEXES.length)];
        this.malade = false;

        if (this.sexe.equals("Homme")) {
            this.prenom = PRENOMS_HOMMES[random.nextInt(PRENOMS_HOMMES.length)];
        } else {
            this.prenom = PRENOMS_FEMMES[random.nextInt(PRENOMS_FEMMES.length)];
        }

        this.age = random.nextInt(63) + 18;
    }

    public Boolean estMalade(){
        return malade;
    }

    public String getPrenom(){
        return prenom;
    }

    public int getAge(){
        return age;
    }

    public String getSexe() {
        return sexe;
    }

    public int getSalaire(){
        return 1200;
    }
}
