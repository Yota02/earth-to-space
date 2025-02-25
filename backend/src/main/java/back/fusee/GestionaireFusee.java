package back.fusee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import back.fusee.booster.Booster;
import back.fusee.booster.BoosterModel;

public class GestionaireFusee {

    private Map<BoosterModel, List<Booster>> boosterMap;
    private List<BoosterModel> boosterModels;

    private List<Booster> boosterListAttente;

    public GestionaireFusee() {
        boosterMap = new HashMap<>();
        boosterModels = new ArrayList<>();
        boosterListAttente = new ArrayList<>();
    }

    public Map<BoosterModel, List<Booster>> getBoosterMap() {
        return boosterMap;
    }
    
    public List<BoosterModel> getBoosterModels() {
        return boosterModels;
    }
    
    public void ajouterBoosterModel(BoosterModel boosterModel) {
        if (boosterModel == null || boosterModel.getNom() == null) {
            throw new IllegalArgumentException("Le boosterModel et son nom ne peuvent pas être null");
        }
        
        boosterModels.add(boosterModel);
        boosterMap.putIfAbsent(boosterModel, new ArrayList<>());
    }

    public void ajouterBooster(Booster booster) {
        if (booster == null || booster.getNom() == null) {
            throw new IllegalArgumentException("Le booster et son nom ne peuvent pas être null");
        }
        
        // Extraire la partie avant le '-'
        String nomBooster = booster.getNom().split("-")[0].trim();
    
        // Trouver le modèle correspondant
        BoosterModel model = getBoosterModelParNom(nomBooster);
        if (model != null) {
            boosterMap.computeIfAbsent(model, k -> new ArrayList<>()).add(booster);
        } else {
            throw new IllegalArgumentException("Aucun modèle de booster correspondant trouvé pour: " + booster.getNom());
        }
    }
    

    public List<Booster> getBoosterListAttente(){
        return boosterListAttente;
    }
    
    public void ajouterBoosterAttente(Booster booster){

        ajouterBooster(booster);
        boosterListAttente.add(booster);

        /* if(booster.estOperationel()){
            booster.
        } */
    }

    public BoosterModel getBoosterModelParNom(String nom) {
        for (BoosterModel model : boosterModels) {
            if (model.getNom().equals(nom)) {
                return model;
            }
        }
        return null;
    }

    public List<Booster> getBoostersParType(BoosterModel model) {
        return boosterMap.getOrDefault(model, new ArrayList<>());
    }
    
    public List<Booster> getBoostersParNomModel(String nomModel) {
        BoosterModel model = getBoosterModelParNom(nomModel);
        if (model != null) {
            return boosterMap.getOrDefault(model, new ArrayList<>());
        }
        return new ArrayList<>();
    }

    public List<Booster> getTousLesBoosters() {
        List<Booster> tousBoosters = new ArrayList<>();
        for (List<Booster> boosters : boosterMap.values()) {
            tousBoosters.addAll(boosters);
        }
        return tousBoosters;
    }
}