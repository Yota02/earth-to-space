package back.recherche;

import back.ObjectDeblocable;

public class RechercheDeblocage extends Recherche{

    ObjectDeblocable objectDeblocable;

    public RechercheDeblocage(int prix, String nom, double temps, String description, CategorieRecherche categorie,
            SousTypeRecherche sousType, int niveau, TypeEffet typeEffet, ObjectDeblocable objectDeblocable) {
        super(prix, nom, temps, description, categorie, sousType, niveau, typeEffet);
        this.objectDeblocable = objectDeblocable;   
    }

    public ObjectDeblocable getobjectDeblocable(){
        return objectDeblocable;
    }

    
}
