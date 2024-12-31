package back.moteur;

public class MoteurConcret extends Moteur {

    public MoteurConcret(Builder builder) {
        super(builder);
    }

    public float calculerEfficacite() {
        return puissance * rendement / 100; 
    }

    @Override
    public String toString() {
        return super.toString() + ", Efficacité=" + calculerEfficacite() + " kN";
    }

    public static class Builder extends Moteur.Builder<Builder> {
        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public MoteurConcret build() {
            return new MoteurConcret(this);
        }
    }
}
