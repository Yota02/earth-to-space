
import back.booster.reservoir.ReservoirPose;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ReservoirPoseTest {

    private ReservoirPose reservoir;

    @BeforeEach
    void setUp() {
        reservoir = new ReservoirPose.Builder()
                .setNom("Reservoir Test")
                .setQuantite(0.0)
                .setQuantiteTotal(100.0)
                .build();
    }

    @Test
    void testAjoutErgolSansDepassement() {
        reservoir.ajouterErgol(50.0);
        assertEquals(50.0, reservoir.getQuantite(), "La quantité d'ergol devrait être 50.");
    }

    @Test
    void testAjoutErgolAvecDepassement() {
        reservoir.ajouterErgol(150.0);
        assertEquals(0.0, reservoir.getQuantite(),
                "La quantité d'ergol ne doit pas changer si on essaie de dépasser la capacité maximale.");
    }

    @Test
    void testAjoutErgolPresqueMaximal() {
        reservoir.ajouterErgol(90.0);
        assertEquals(90.0, reservoir.getQuantite(), "La quantité d'ergol devrait être 90.");

        reservoir.ajouterErgol(10.0);
        assertEquals(100.0, reservoir.getQuantite(), "La quantité d'ergol devrait être 100 après ajout.");
    }

    @Test
    void testAjoutErgolExactementMaximal() {
        reservoir.ajouterErgol(100.0);
        assertEquals(100.0, reservoir.getQuantite(), "La quantité d'ergol devrait être égale à la capacité maximale.");
    }

    @Test
    void testAjoutErgolAvecQuantiteInitiale() {
        reservoir.ajouterErgol(50.0);
        assertEquals(50.0, reservoir.getQuantite(), "La quantité initiale devrait être 50.");

        reservoir.ajouterErgol(60.0);
        assertEquals(50.0, reservoir.getQuantite(),
                "La quantité ne devrait pas dépasser 50, car le total excéderait la capacité maximale.");
    }
}
