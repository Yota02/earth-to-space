package gui;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import back.Jeu;
import com.google.gson.Gson;

/**
 * Extension de la classe Jeu qui lit les instructions dans une file d'attente
 * et envoie l'état du jeu à la classe GameServer (permet de communiquer avec
 * l'interface graphique)
 */
public class JeuWebsocket extends Jeu {
    /**
     * File d'attente des instructions recues par le serveur
     */
    private final BlockingQueue<String> inputQueue;

    public JeuWebsocket(String[] nomsJoueurs) {
        super(nomsJoueurs);
        inputQueue = new LinkedBlockingQueue<>();
    }

    public JeuWebsocket getJeu(){
        return this;
    }

    @Override
    public String lireLigne() {
        try {
            return inputQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addInput(String input) {
        inputQueue.add(input);
    }
}
