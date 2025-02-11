package back.recherche;

import java.util.ArrayList;
import java.util.List;

import back.Batiment.*;

public class GestionaireRechercheDeblocage {

    BatimentManager batimentManager;

    public GestionaireRechercheDeblocage(BatimentManager batimentManager) {
        this.batimentManager = batimentManager;
    }

    public List<RechercheDeblocage> initeHangarAssemblageRecherche() {
        List<RechercheDeblocage> res = new ArrayList<>();

        res.add(new RechercheDeblocage(100, "High Bay", 100, "Deblocage du High Bay, peut accueillir 3 Fusée",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.HANGAR_ASSEMBLAGE, 2,
                batimentManager.getBatiment("HighBay")));
        res.add(new RechercheDeblocage(100, "Mega Bay", 150, "Deblocage du Mega Bay, peut accueillir 5 Fusée",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.HANGAR_ASSEMBLAGE, 3,
                batimentManager.getBatiment("MegaBay")));
        res.add(new RechercheDeblocage(200, "Giga Bay", 200, "Deblocage du Giga Bay, peut accueillir 20 Fusée",
                CategorieRecherche.BATIMENTS, SousTypeRecherche.HANGAR_ASSEMBLAGE, 4,
                batimentManager.getBatiment("GigaBay")));
        return res;
    }


}
