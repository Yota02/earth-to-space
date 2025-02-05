package back.fusee;

public abstract class Ifusee {

    boolean operationnel = false;
    double progression = 0;

    public double getProgression(){
        return progression;
    }

    public boolean estOperationel(){
        return operationnel;
    }

    public void construire(double taux){
        this.progression += taux;
    }

    public void setProgression(double nb){
        this.progression = nb;
    }

    public void setOperationel(Boolean op){
        this.operationnel = op;
    }
    
}