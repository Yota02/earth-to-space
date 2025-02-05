package back.Ressources_Humaines;

import java.util.Random;

public class PersonneSimple extends Personne {

    public PersonneSimple() {
        Random random = new Random();
        this.nom = NOMS[random.nextInt(NOMS.length)];
        this.sexe = SEXES[random.nextInt(SEXES.length)];
        this.malade = false;

        if (this.sexe.equals("Homme")) {
            this.prenom = PRENOMS_HOMMES[random.nextInt(PRENOMS_HOMMES.length)];
        } else {
            this.prenom = PRENOMS_FEMMES[random.nextInt(PRENOMS_FEMMES.length)];
        }

        this.age = random.nextInt(50) + 18;
        this.clePrimaire = generateClePrimaire();
    }

    private int generateClePrimaire() {
        return ++compteurId;
    }

}
