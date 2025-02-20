package back.objectAchetable;

import java.util.*;

public class GestionnaireObject {
    private List<ObjectAchetable> objectTotals = new ArrayList<>();

    private List<ObjectAchetable> objectAcheter = new ArrayList<>();
    private GestionaireMateriaux gestionaireMateriaux = new GestionaireMateriaux();

    private final Map<String, List<ObjectAchetable>> objectAchetablesMap = new HashMap<>();
    private final List<ObjectAchetable> objectAchetables = new ArrayList<>();

    public GestionnaireObject() {
        initialiserObject();
    }

    private final Map<String, Integer> stockMateriaux = new HashMap<>();

    public Map<String, Integer> getStockMateriaux() {
        return stockMateriaux;
    }

    public void ajouterMateriau(String nom, int quantite) {
        stockMateriaux.merge(nom, quantite, Integer::sum);
    }

    public int getQuantiteMateriau(String nom) {
        return stockMateriaux.getOrDefault(nom, 0);
    }

    public MateriauxAchetable getMateriau(String nom) {
        return gestionaireMateriaux.getMateriaux(nom);
    }

    public void initialiserObject() {

        // Métaux
        initialisationMetaux();
    }

    public ObjectAchetable findObjectByName(String name) {
        for (ObjectAchetable objectAchetable : objectAchetables) {
            if (objectAchetable.getNom().equals(name)) {
                return objectAchetable;
            }
        }
        return null;
    }

    public List<ObjectAchetable> getObjectAchetables() {
        return objectAchetables;
    }

    public List<ObjectAchetable> getObjectAcheter() {
        return objectAcheter;
    }

    private void initialisationMetaux() {
        gestionaireMateriaux.getMateriaux().forEach(this::ajouterObjectAchetable);
    }

    private void ajouterObjectAchetable(ObjectAchetable object) {
        objectAchetables.add(object);
        objectAchetablesMap.computeIfAbsent(object.getNom(), k -> new ArrayList<>()).add(object);
    }

    public List<ObjectAchetable> getObject(String nom) {
        return objectAchetablesMap.getOrDefault(nom, Collections.emptyList());
    }

    public List<ObjectAchetable> getObjects() {
        return Collections.unmodifiableList(objectAchetables);
    }
}
