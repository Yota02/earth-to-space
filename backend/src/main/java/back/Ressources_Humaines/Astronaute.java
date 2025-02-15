package back.Ressources_Humaines;

import java.util.Random;

public class Astronaute extends PersonneSimple {
    
    private int experience; // sur 20
    private int talent; // sur 10

    public Astronaute(){
        super();
        talent = new Random().nextInt(10) + 1;
        experience = 0;
    }

    public int getExperience() {
        return experience;
    }

    public void entrainer() {
        if(experience < 20){
            experience++;
        }
    }

    @Override
    public int getSalaire(){
        return talent * super.getSalaire() * 3;
    }

}
