package back.objectAchetable;

import back.Jeu;

public class Metaux extends ObjectAchetable {

    private Metaux(Builder builder) {
        super(builder);
    }

    @Override
    public void effectuerAchat(Jeu jeu) {

    }

    public static class Builder extends ObjectAchetable.Builder<Builder> {

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Metaux build() {
            return new Metaux(this);
        }
    }
}
