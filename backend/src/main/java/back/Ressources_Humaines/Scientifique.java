package back.Ressources_Humaines;

import java.util.Random;

public class Scientifique extends Personne {

    private int talent; // sur 10

    public Scientifique(){
        super();
        talent = new Random().nextInt(10) + 1;
    }

    public int getTalent() {
        return talent;
    }

    @Override
    public int getSalaire(){
        return talent * super.getSalaire();
    }
}
