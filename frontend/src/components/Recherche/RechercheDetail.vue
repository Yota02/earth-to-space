<template>
    <div class="research-detail-container">
        <div v-if="connectionError" class="connection-status error">
            Erreur de connexion au serveur
        </div>

        <div v-if="selectedRecherche" class="research-detail">
            <div class="research-image">
                <img :src="getImageUrl(selectedRecherche.sousCategorie, selectedRecherche.categorie)"
                    :alt="selectedRecherche.nom">
            </div>
            <div class="research-info">
                <h2>{{ selectedRecherche.nom }}</h2>
                <div class="research-metadata">
                    <p><strong>Catégorie : </strong> {{ selectedRecherche.categorie }}</p>
                    <p><strong>Sous-Catégorie : </strong> {{ selectedRecherche.sousCategorie }}</p>
                    <p><strong>Progression : </strong>
                        <span :style="{ color: getProgressColor(selectedRecherche) }">
                            {{ selectedRecherche.progression }}%
                        </span>
                    </p>
                </div>
                <div class="research-description">
                    <h3>Description</h3>
                    <p>{{ selectedRecherche.description || 'Aucune description disponible.' }}</p>
                </div>
                <div class="research-details">
                    <h3>Détails</h3>
                    <ul>
                        <li v-if="selectedRecherche.dureeEstimee">
                            <strong>Durée estimée : </strong> {{ selectedRecherche.dureeEstimee }} jours
                        </li>
                        <li v-if="selectedRecherche.coutEstime">
                            <strong>Coût estimé : </strong> {{ selectedRecherche.coutEstime }} €
                        </li>
                        <li v-if="selectedRecherche.responsable">
                            <strong>Responsable : </strong> {{ selectedRecherche.responsable }}
                        </li>
                    </ul>
                </div>

                <div class="research-actions">
                    <!-- Afficher "Annuler" si la recherche est en cours -->
                    <button v-if="selectedRecherche.etat === 1" @click="annulerRecherche" class="cancel-button">
                        Annuler
                    </button>

                    <!-- Afficher "Démarrer" si la recherche n'est pas en cours -->
                    <button v-else @click="demarrerRecherche"
                        :disabled="selectedRecherche.progression >= 100 || connectionError">
                        {{ selectedRecherche.progression >= 100 ? 'Terminé' : 'Démarrer' }}
                    </button>
                </div>
            </div>
        </div>
        <div v-else class="no-selection">
            <p>Sélectionnez une recherche pour voir les détails</p>
        </div>
    </div>
</template>

<script>
export default {
    name: 'ResearchDetail',
    props: {
        selectedRecherche: {
            type: Object,
            default: null
        }
    },
    data() {
        return {
            socket: null,
            connectionError: false,
            rechercheEnCours: false,
            defaultImages: {
                // Catégories par défaut
                "PROPULSION": "src/assets/img/bandeau/propulsion.jpg",
                "EXPLORATION": "src/assets/img/bandeau/exploration.jpg",
                "ENERGIE": "src/assets/img/bandeau/energie.jpg",
                "ROBOTIQUE": "src/assets/img/bandeau/robotique.jpg",
                "TRANSPORT_HABITE": "src/assets/img/bandeau/transport.jpg",
                "COLONISATION": "src/assets/img/bandeau/colonisation.jpg",
                "BATIMENTS": "src/assets/img/bandeau/batiments.jpg",
                "RH": "src/assets/img/bandeau/rh.jpg",
                "ASTRONOMIE": "src/assets/img/bandeau/observatoire.jpg",
                "MATERIAUX": "src/assets/img/bandeau/materiaux.jpg",
                "EXOBIOLOGIE": "src/assets/img/bandeau/exobiologie.webp",
                "FINANCIER": "src/assets/img/bandeau/financier.jpg",

                // Sous-catégories spécifiques
                "MOTEURS": "src/assets/img/bandeau/moteur.png",
                "FUEL": "src/assets/img/bandeau/fuel.png",
                "CAPTEURS": "src/assets/img/bandeau/capteur.jpg",
                "PANNEAUX_SOLAIRES": "src/assets/img/bandeau/panneau_solaire.png",
                "REACTEURS": "src/assets/img/bandeau/reacteur.png",
                "BATTERIES": "src/assets/img/bandeau/batterie.png",
                "AUTOMATISATION": "src/assets/img/bandeau/automatisation.png",
                "SYSTÈMES_AUTONOMES": "",
                "SYSTEMES_DE_TRANSPORT": "src/assets/img/bandeau/navette.jpg",
                "VIE_SPATIALE": "src/assets/img/bandeau/station_spatial.png",
                "INFRASTRUCTURES": "/images/colonisation-infrastructures.jpg",
                "HABITATS": "src/assets/img/bandeau/habitats.jpg",
                "AGRICULTURE_SPATIALE": "src/assets/img/bandeau/agriculture.jpg",
                "CONSTRUCTION": "src/assets/img/bandeau/construction.webp",
                "STOCKAGE": "src/assets/img/bandeau/hangar_fusee.jpg",
                "HANGAR_ASSEMBLAGE" : "src/assets/img/bandeau/megabay.jpg",
            }
        }
    },
    created() {
        this.initWebSocket();
    },
    methods: {
        initWebSocket() {
            try {
                this.socket = new WebSocket("ws://localhost:3232");
                this.socket.onmessage = this.handleWebSocketMessage;

                this.socket.onopen = () => {
                    console.log('WebSocket connection established');
                    this.connectionError = false;
                };

                this.socket.onerror = (error) => {
                    console.error('WebSocket error:', error);
                    this.connectionError = true;
                };

                this.socket.onclose = (event) => {
                    console.log('WebSocket connection closed:', event.code, event.reason);
                    this.connectionError = true;
                    setTimeout(() => this.initWebSocket(), 5000);
                };

                this.socket.onmessage = (event) => {
                    try {
                        const response = JSON.parse(event.data);
                        console.log('Received WebSocket message:', response);

                        if (response.action === 'startResearchSuccess') {
                            console.log('Research started successfully:', response.name);
                            this.rechercheEnCours = true;
                            this.$emit('research-started', response.name);
                        } else if (response.action === 'cancelResearchSuccess') {
                            console.log('Research cancelled:', response.name);
                            this.rechercheEnCours = false;
                        } else if (response.error) {
                            console.error('Server error:', response.error);
                        }
                    } catch (e) {
                        console.error('Error parsing WebSocket message:', e);
                    }
                };
            } catch (e) {
                console.error('Error initializing WebSocket:', e);
                this.connectionError = true;
            }
        },

        demarrerRecherche() {
            if (!this.socket || this.socket.readyState !== WebSocket.OPEN) {
                console.error('WebSocket is not connected');
                this.connectionError = true;
                return;
            }

            try {
                const message = {
                    type: "startResearch",
                    name: this.selectedRecherche.nom
                };
                console.log('Sending research start command:', message);
                this.socket.send(JSON.stringify(message));
            } catch (e) {
                console.error('Error sending research start command:', e);
                this.connectionError = true;
            }
        },

        annulerRecherche() {
            if (!this.socket || this.socket.readyState !== WebSocket.OPEN) {
                console.error('WebSocket is not connected');
                this.connectionError = true;
                return;
            }

            try {
                const message = {
                    type: "cancelResearch",
                    name: this.selectedRecherche.nom
                };
                console.log('Sending research cancel command:', message);
                this.socket.send(JSON.stringify(message));
            } catch (e) {
                console.error('Error sending research cancel command:', e);
                this.connectionError = true;
            }
        },

        getImageUrl(sousCategorie, categorie) {
            return this.defaultImages[sousCategorie] ||
                this.defaultImages[categorie] ||
                "@/../../assets/1.png";
        },

        getProgressColor(recherche) {
            return recherche.progression >= 100 ? '#4CAF50' : '#FF9800';
        }
    },
    beforeDestroy() {
        if (this.socket) {
            this.socket.close();
            console.log('WebSocket connection closed on component destroy');
        }
    },
    watch: {
        selectedRecherche(newVal) {
            console.log('Selected research changed:', newVal);
        }
    }
};
</script>

<style scoped>
.research-detail-container {
    background-color: #f4f4f4;
    border-radius: 10px;
    padding: 20px;
    width: 400px;
    max-height: 80vh;
    overflow-y: auto;
}

.connection-status.error {
    background-color: #ff5252;
    color: white;
    padding: 10px;
    margin-bottom: 15px;
    border-radius: 5px;
    text-align: center;
}

.research-image img {
    width: 100%;
    height: 250px;
    object-fit: cover;
    border-radius: 10px;
    margin-bottom: 15px;
}

.research-actions button {
    width: 100%;
    padding: 10px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.research-actions button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
}

/* Bouton "Démarrer" */
.research-actions button:not(.cancel-button) {
    background-color: #4CAF50;
    color: white;
}

.research-actions button:not(.cancel-button):hover:not(:disabled) {
    background-color: #45a049;
}

/* Bouton "Annuler" */
.cancel-button {
    background-color: #d32f2f;
    color: white;
}

.cancel-button:hover {
    background-color: #b71c1c;
}
</style>