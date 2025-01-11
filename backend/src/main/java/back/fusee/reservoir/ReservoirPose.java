package back.fusee.reservoir;

public class ReservoirPose extends Reservoir{

    private ReservoirPose(Builder builder) {
        super(builder);
    }
    
    public static class Builder extends Reservoir.Builder<Builder> {

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
