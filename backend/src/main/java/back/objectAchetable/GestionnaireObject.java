package back.objectAchetable;

import java.util.*;

public class GestionnaireObject {
    private final List<ObjectAchetable> objectAchetables = new ArrayList<>();
    private final Map<String, ObjectAchetable> objectAchetablesMap = new HashMap<>();

    public void initialiserObject() {

        //metaux
        initialisationMetaux();
    }

    public void initialisationMetaux() {
        Metaux fer = new Metaux.Builder()
                .setNom("Fer")
                .setPrix(10000000)
                .setEstAchetable(true)
                .build();

        Metaux acier = new Metaux.Builder()
                .setNom("Acier")
                .setPrix(1000)
                .setEstAchetable(true)
                .build();

        ajouterObjectAchetable(acier);
        ajouterObjectAchetable(fer);
    }

    private void ajouterObjectAchetable(ObjectAchetable object) {
        objectAchetables.add(object);
        objectAchetablesMap.put(object.getNom(), object);
    }

    public ObjectAchetable getObject(String nom) {
        return objectAchetablesMap.get(nom);
    }

    public List<ObjectAchetable> getObjects() {
        return Collections.unmodifiableList(objectAchetables);
    }
}