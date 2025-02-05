package back.Ressources_Humaines;

import java.util.Random;

public class Ingenieur extends PersonneSimple {
    
    private int talent; // sur 10

    public Ingenieur(){
        super();
        talent = new Random().nextInt(10) + 1;
    }

    public int getTalent() {
        return talent;
    }

    @Override
    public int getSalaire(){
        return talent * super.getSalaire() * 2;
    }


}
