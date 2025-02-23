package back.fusee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import back.fusee.booster.Booster;

public class GestionaireFusee {

    private Map<String, List<Booster>> boosterMap;

    public GestionaireFusee() {
        boosterMap = new HashMap<>();
    }

    public Map<String, List<Booster>> getBoosterMap() {
        return boosterMap;
    }

    public void ajouterBooster(Booster booster) {
        if (booster == null || booster.getNom() == null) {
            throw new IllegalArgumentException("Le booster et son nom ne peuvent pas être null");
        }
        System.out.println(booster.toString());

        boosterMap.computeIfAbsent(booster.getNom(), k -> new ArrayList<>()).add(booster);
    }

    public List<Booster> getBoostersParType(String type) {
        return boosterMap.getOrDefault(type, new ArrayList<>());
    }

    public List<Booster> getTousLesBoosters() {
        List<Booster> tousBoosters = new ArrayList<>();
        for (List<Booster> boosters : boosterMap.values()) {
            tousBoosters.addAll(boosters);
        }
        return tousBoosters;
    }
}
