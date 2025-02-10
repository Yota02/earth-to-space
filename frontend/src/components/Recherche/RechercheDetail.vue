<template>
    <div class="research-detail-container">
        <div v-if="selectedRecherche" class="research-detail">
            <div class="research-image">
                <img :src="getImageUrl(selectedRecherche.sousCategorie, selectedRecherche.categorie)" :alt="selectedRecherche.nom">
            </div>
            <div class="research-info">
                <h2>{{ selectedRecherche.nom }}</h2>
                <div class="research-metadata">
                    <p><strong>Catégorie:</strong> {{ selectedRecherche.categorie }}</p>
                    <p><strong>Sous-Catégorie:</strong> {{ selectedRecherche.sousCategorie }}</p>
                    <p><strong>Progression:</strong>
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
                            <strong>Durée estimée:</strong> {{ selectedRecherche.dureeEstimee }} jours
                        </li>
                        <li v-if="selectedRecherche.coutEstime">
                            <strong>Coût estimé:</strong> {{ selectedRecherche.coutEstime }} €
                        </li>
                        <li v-if="selectedRecherche.responsable">
                            <strong>Responsable:</strong> {{ selectedRecherche.responsable }}
                        </li>
                    </ul>
                </div>
                <div class="research-actions">
                    <button @click="demarerRecherche" :disabled="selectedRecherche.progression >= 100">
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
    props: {
        selectedRecherche: {
            type: Object,
            default: null
        }
    },
    methods: {
        getImageUrl(sousCategorie, categorie) {
            const defaultImages = {
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
                "FINANCIER" : "src/assets/img/bandeau/financier.jpg",

                // Sous-catégories spécifiques
                "MOTEURS": "src/assets/img/bandeau/moteur.png",
                "FUEL": "src/assets/img/bandeau/fuel.png",
                "CAPTEURS": "src/assets/img/bandeau/capteur.jpg",
                "PANNEAUX_SOLAIRES": "src/assets/img/bandeau/panneau_solaire.png",
                "REACTEURS": "src/assets/img/bandeau/reacteur.png",
                "BATTERIES": "src/assets/img/bandeau/batterie.png",
                "AUTOMATISATION": "src/assets/img/bandeau/automatisation.png",
                "SYSTÈMES_AUTONOMES": "/images/robotique-systemes.jpg",
                "SYSTEMES_DE_TRANSPORT": "src/assets/img/bandeau/navette.jpg",
                "VIE_SPATIALE": "/images/transport-vie.jpg",
                "INFRASTRUCTURES": "/images/colonisation-infrastructures.jpg",
                "HABITATS": "src/assets/img/bandeau/habitats.jpg",
                "AGRICULTURE_SPATIALE": "src/assets/img/bandeau/agriculture.jpg",
                "CONSTRUCTION": "/images/batiments-construction.jpg",
                "STOCKAGE": "/images/batiments-stockage.jpg"
            };

            return defaultImages[sousCategorie] || defaultImages[categorie] || "@/../../assets/1.png";
        },
        getProgressColor(recherche) {
            return recherche.progression >= 100 ? '#4CAF50' : '#FF9800';
        },
        demarerRecherche() {
            this.$emit('start-recherche', this.selectedRecherche);
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

.research-image img {
    width: 100%;
    height: 250px;
    object-fit: cover;
    border-radius: 10px;
    margin-bottom: 15px;
}

.research-info h2 {
    margin-bottom: 15px;
    color: #333;
}

.research-metadata {
    background-color: #e9e9e9;
    padding: 10px;
    border-radius: 5px;
    margin-bottom: 15px;
}

.research-description,
.research-details {
    margin-bottom: 15px;
}

.research-details ul {
    list-style-type: none;
    padding: 0;
}

.research-details li {
    margin-bottom: 10px;
}

.research-actions button {
    width: 100%;
    padding: 10px;
    background-color: #4CAF50;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
}

.research-actions button:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
}

.no-selection {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    color: #888;
}
</style>