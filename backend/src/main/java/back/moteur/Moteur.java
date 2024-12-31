package back.moteur;

public abstract class Moteur {

    protected String nom;
    protected float puissance; 
    public Carburant carburant; 
    protected float rendement; 
    protected int anneeFabrication;

    protected Moteur(Builder<?> builder) {
        this.nom = builder.nom;
        this.puissance = builder.puissance;
        this.carburant = builder.carburant;
        this.rendement = builder.rendement;
        this.anneeFabrication = builder.anneeFabrication;
    }

    public String getNom() {
        return nom;
    }

    public float getPuissance() {
        return puissance;
    }

    public Carburant getCarburant() {
        return carburant;
    }

    public float getRendement() {
        return rendement;
    }

    public int getAnneeFabrication() {
        return anneeFabrication;
    }

    public String toString() {
        return "Moteur [nom=" + nom + ", puissance=" + puissance + " kN, carburant=" + carburant +
                ", rendement=" + rendement + "%, anneeFabrication=" + anneeFabrication + "]";
    }

    public static abstract class Builder<T extends Builder<T>> {

        private String nom;
        private float puissance;
        private Carburant carburant;
        private float rendement;
        private int anneeFabrication;

        public T nom(String nom) {
            this.nom = nom;
            return self();
        }

        public T puissance(float puissance) {
            this.puissance = puissance;
            return self();
        }

        public T carburant(Carburant carburant) {
            this.carburant = carburant;
            return self();
        }

        public T rendement(float rendement) {
            this.rendement = rendement;
            return self();
        }

        public T anneeFabrication(int anneeFabrication) {
            this.anneeFabrication = anneeFabrication;
            return self();
        }

        protected abstract T self();

        public abstract Moteur build();
    }
}
