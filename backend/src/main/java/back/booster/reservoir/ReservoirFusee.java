package back.booster.reservoir;

public class ReservoirFusee extends Reservoir {

    private ReservoirFusee(Builder builder) {
        super(builder);
    }
    
    public static class Builder extends Reservoir.Builder<Builder> {

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public ReservoirFusee build() {
            return new ReservoirFusee(this);
        }
    }
    
}
