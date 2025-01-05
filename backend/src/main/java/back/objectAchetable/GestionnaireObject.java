package back.objectAchetable;

import java.util.*;

import back.moteur.Ergol;

public class GestionnaireObject {
    private final List<ObjectAchetable> objectAchetables = new ArrayList<>();
    private final Map<String, ObjectAchetable> objectAchetablesMap = new HashMap<>();

    public void initialiserObject() {
        //carburant
        initialisationCarburant();

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

    public void initialisationCarburant() {
        CarburantAchetable hydrogen = new CarburantAchetable.Builder()
                .setNom("Hydrogen liquide")
                .setPrix(1000)
                .setEstAchetable(true)
                .setCarburant(Ergol.HYDROGENE)
                .setQuantite(500.0)
                .build();

        CarburantAchetable oxygen = new CarburantAchetable.Builder()
                .setNom("Oxygen liquide")
                .setPrix(1000)
                .setEstAchetable(true)
                .setCarburant(Ergol.OXYGEN)
                .setQuantite(300.0)
                .build();

        CarburantAchetable methane = new CarburantAchetable.Builder()
                .setNom("Methane liquide")
                .setPrix(1000)
                .setEstAchetable(true)
                .setCarburant(Ergol.METHANES)
                .setQuantite(400.0)
                .build();

        ajouterObjectAchetable(hydrogen);
        ajouterObjectAchetable(oxygen);
        ajouterObjectAchetable(methane);
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