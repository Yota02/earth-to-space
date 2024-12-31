package back.usine;

import java.util.HashMap;
import java.util.Map;

public class UsineManager {

    private static Map<Integer, Usine> usines = new HashMap<>();

    public static void ajouterUsine(int tier, Usine usine) {
        usines.put(tier, usine);
    }

    public static Usine getUsine(int tier) {
        for (int i = tier; i >= 1; i--) { // Priorité à l'usine de même niveau ou supérieur
            if (usines.containsKey(i)) {
                return usines.get(i);
            }
        }
        throw new IllegalArgumentException("Aucune usine disponible pour le tier " + tier);
    }
}
