package back.fusee.reservoir;

public class ReservoirPose extends Reservoir{

    private int prix;

    private ReservoirPose(Builder builder) {
        super(builder);
        this.prix =  10000; //builder.prix;
    }

    public int getPrix(){
        return prix;
    }
    
    public static class Builder extends Reservoir.Builder<Builder> {

        private int prix;

        public Builder setQuantite(int prix) {
            this.prix = prix;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ReservoirPose build() {
            return new ReservoirPose(this);
        }
    }
}
