package back.objectAchetable;

import org.json.JSONObject;

import back.Jeu;
import back.Metaux.Materiaux;

public class MateriauxAchetable extends ObjectAchetable {

    Materiaux materiau;

    private MateriauxAchetable(Builder builder) {
        super(builder);
        this.materiau = builder.materiau;
    }

    @Override
    public void effectuerAchat(Jeu jeu) {

    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("nom", this.getNom());
        json.put("materiau", this.materiau);
        json.put("prix", this.getPrix());
        json.put("estAchetable", this.getEstAchetable());

        return json;
    }

    public static class Builder extends ObjectAchetable.Builder<Builder> {
        private Materiaux materiau; 

        public Builder setMateriau(Materiaux materiau) {
            this.materiau = materiau;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public MateriauxAchetable build() {
            return new MateriauxAchetable(this);
        }
    }


}
