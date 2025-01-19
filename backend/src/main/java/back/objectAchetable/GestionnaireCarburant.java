package back.objectAchetable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import back.fusee.moteur.Ergol;

public class GestionnaireCarburant {
    private final List<CarburantAchetable> CarburantAchetables = new ArrayList<>();
    private final Map<String, CarburantAchetable> CarburantAchetablesMap = new HashMap<>();

    public void initialiserObject() {
        //carburant
        initialisationCarburant();
    }

    public void initialisationCarburant() {
        CarburantAchetable hydrogen = new CarburantAchetable.Builder()
                .setNom("Hydrogen liquide")
                .setPrix(1000)
                .setEstAchetable(true)
                .setCarburant(Ergol.HYDROGENE)
                .setQuantite(100.0)
                .build();

        CarburantAchetable oxygen = new CarburantAchetable.Builder()
                .setNom("Oxygen liquide")
                .setPrix(1000)
                .setEstAchetable(true)
                .setCarburant(Ergol.OXYGEN)
                .setQuantite(100.0)
                .build();

        CarburantAchetable methane = new CarburantAchetable.Builder()
                .setNom("Methane liquide")
                .setPrix(1000)
                .setEstAchetable(true)
                .setCarburant(Ergol.METHANES)
                .setQuantite(100.0)
                .build();

                ajouterCarburantAchetable(hydrogen);
                ajouterCarburantAchetable(oxygen);
                ajouterCarburantAchetable(methane);
    }

    private void ajouterCarburantAchetable(CarburantAchetable object) {
        CarburantAchetables.add(object);
        CarburantAchetablesMap.put(object.getNom(), object);
    }

    public CarburantAchetable getObject(String nom) {
        return CarburantAchetablesMap.get(nom);
    }

    public List<CarburantAchetable> getObjects() {
        return Collections.unmodifiableList(CarburantAchetables);
    }
}
