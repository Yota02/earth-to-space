package back;

public abstract class ObjectDeblocable {

    Boolean debloquer = false;

    public void debloquer(){
        this.debloquer = true;
    }

    public Boolean estDebloquer(){
        return debloquer;
    }
    
}
