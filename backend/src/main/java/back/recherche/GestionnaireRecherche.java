package back.recherche;

import java.util.*;

public class GestionnaireRecherche {
    private final List<Recherche> rechercheTotal = new ArrayList<>();
    private final Map<String, Recherche> recherchesMap = new HashMap<>();

    public void initialiserRecherches() {
        initRechercheEnergie();

    }

    public void initRecherchesPanneauxSolaires() {
        ajouterRecherche(new Recherche(1000, "Panneaux Solaires de Niveau 1", 5.0, "", CategorieRecherche.ENERGIE, SousTypeRecherche.PANNEAUX_SOLAIRES,  1));
        ajouterRecherche(new Recherche(1500, "Panneaux Solaires de Niveau 2", 7.0, "", CategorieRecherche.ENERGIE, SousTypeRecherche.PANNEAUX_SOLAIRES, 2));
        ajouterRecherche(new Recherche(2000, "Panneaux Solaires de Niveau 3", 9.0, "", CategorieRecherche.ENERGIE, SousTypeRecherche.PANNEAUX_SOLAIRES,  3));
        ajouterRecherche(new Recherche(2500, "Panneaux Solaires de Niveau 4", 12.0, "", CategorieRecherche.ENERGIE, SousTypeRecherche.PANNEAUX_SOLAIRES, 4));
        ajouterRecherche(new Recherche(3000, "Panneaux Solaires de Niveau 5", 15.0, "", CategorieRecherche.ENERGIE, SousTypeRecherche.PANNEAUX_SOLAIRES, 5));
        ajouterRecherche(new Recherche(4000, "Panneaux Solaires de Niveau 6", 18.0, "", CategorieRecherche.ENERGIE, SousTypeRecherche.PANNEAUX_SOLAIRES,  6));
        ajouterRecherche(new Recherche(5000, "Panneaux Solaires de Niveau 7", 22.0, "", CategorieRecherche.ENERGIE, SousTypeRecherche.PANNEAUX_SOLAIRES, 7));
        ajouterRecherche(new Recherche(6000, "Panneaux Solaires de Niveau 8", 25.0, "", CategorieRecherche.ENERGIE, SousTypeRecherche.PANNEAUX_SOLAIRES, 8));
    }
    
    public void initRechercheEnergie(){
        initRecherchesPanneauxSolaires();
    
    }

    public Recherche getRecherche(String nom) {
        return recherchesMap.get(nom);
    }

    private void ajouterRecherche(Recherche recherche) {
        rechercheTotal.add(recherche);
        recherchesMap.put(recherche.getNom(), recherche);
    }

    public List<Recherche> getRecherches() {
        return Collections.unmodifiableList(rechercheTotal);
    }

    public void demarrerRecherche(int rechercheName) {
        Recherche recherche = findRechercheById(rechercheName);

        recherche.setEtat(1);
    }

    public void rechercheParMoi(){
        for(Recherche r : rechercheTotal){
            if(r.getEtat() == 1){
                r.ajouterProgression(r.getNbprogressionParMoi());

                if(r.getEtat() == 1 && r.getProgression() == 100){
                    r.setEtat(2);
                }
            }
        }
    }

    private Recherche findRechercheById(int id) {
        for (Recherche recherche : rechercheTotal) {
            if (recherche.getId() == id) {
                return recherche;
            }
        }
        return null;
    }


}