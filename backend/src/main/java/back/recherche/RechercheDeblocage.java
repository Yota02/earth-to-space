package back.recherche;

import back.ObjectDeblocable;

public class RechercheDeblocage extends Recherche{

    ObjectDeblocable objectAchetable;

    public RechercheDeblocage(int prix, String nom, double temps, String description, CategorieRecherche categorie,
            SousTypeRecherche sousType, int niveau, ObjectDeblocable objectAchetable) {
        super(prix, nom, temps, description, categorie, sousType, niveau);
        this.objectAchetable = objectAchetable;   
    }

    public ObjectDeblocable getObjectAchetable(){
        return objectAchetable;
    }

    
}
