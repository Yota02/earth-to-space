package back.objectAchetable;

import java.util.*;

import back.Metaux.Materiaux;

public class GestionnaireObject {
    private List<ObjectAchetable> objectTotals = new ArrayList<>();;
    private List<ObjectAchetable> objectAcheter = new ArrayList<>();;

    private final Map<String, List<ObjectAchetable>> objectAchetablesMap = new HashMap<>();
    private final List<ObjectAchetable> objectAchetables = new ArrayList<>();

    public GestionnaireObject(){
        initialiserObject();
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
        MateriauxAchetable fer = new MateriauxAchetable.Builder()
                .setNom(Materiaux.FER.getNom())
                .setPrix(10000000)
                .setMateriau(Materiaux.FER)
                .setEstAchetable(true)
                .build();

        MateriauxAchetable acier = new MateriauxAchetable.Builder()
                .setNom(Materiaux.ACIER.getNom())
                .setPrix(1000)
                .setMateriau(Materiaux.ACIER)
                .setEstAchetable(true)
                .build();

        ajouterObjectAchetable(acier);
        ajouterObjectAchetable(fer);
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
