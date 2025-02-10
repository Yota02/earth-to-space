<template>
    <div class="recherches-list-container">
        <div v-if="recherches.length" class="categories-grid">
            <div v-for="(sousCategories, categorie) in groupedRecherches" :key="categorie" 
                 class="categorie"
                 :style="{ backgroundColor: getCategoryColor(categorie) }">
                <h3>{{ categorie }}</h3>
                <div v-for="(recherches, sousCategorie) in sousCategories" 
                     :key="sousCategorie" 
                     class="sous-categorie"
                     :style="{ backgroundColor: getSousCategorieColor(categorie, sousCategorie) }">
                    <h4>{{ sousCategorie }}</h4>
                    <ul>
                        <li 
                            v-for="recherche in recherches" 
                            :key="recherche.id"
                            @click="$emit('select-recherche', recherche)"
                            class="recherche-item"
                        >
                            <strong>{{ recherche.nom }}</strong> -
                            <span :style="{ color: getRechercheColor(recherche) }">
                                {{ recherche.progression }}%
                            </span>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <p v-else>Aucune recherche en cours.</p>
    </div>
</template>

<script>
export default {
    props: {
        recherches: {
            type: Array,
            default: () => []
        }
    },
    computed: {
        groupedRecherches() {
            return this.recherches.reduce((acc, recherche) => {
                if (!acc[recherche.categorie]) {
                    acc[recherche.categorie] = {};
                }
                if (!acc[recherche.categorie][recherche.sousCategorie]) {
                    acc[recherche.categorie][recherche.sousCategorie] = [];
                }
                acc[recherche.categorie][recherche.sousCategorie].push(recherche);
                return acc;
            }, {});
        }
    },
    methods: {
        getRechercheColor(recherche) {
            return recherche.progression >= 100 ? '#4CAF50' : '#FF9800';
        },
        getCategoryColor(categorie) {
            const colors = {
                "PROPULSION": "#1E88E5",
                "NAVIGATION": "#0288D1",
                "ENERGIE": "#FFEB3B",
                "COMMUNICATIONS": "#7E57C2",
                "EXPLORATION": "#8E24AA",
                "TRANSPORT_HABITE": "#FF5722",
                "COLONISATION": "#4CAF50",
                "BATIMENTS": "#607D8B",
                "RH": "#FF9800",
                "FINANCIER": "#FF7043",
                "MATERIAUX": "#795548",
                "ROBOTIQUE": "#009688",
                "INTELLIGENCE_ARTIFICIELLE": "#00BCD4",
                "EXOBIOLOGIE": "#FF4081",
            };
            return colors[categorie] || "#BDBDBD";
        },
        getSousCategorieColor(categorie, sousCategorie) {
            const subColors = {
                "PANNEAUX_SOLAIRES": "#FBC02D",
                "REACTEURS": "#F57F17",
                "BATTERIES": "#F9A825",
                "AUTOMATISATION": "#00796B",
                "SYSTÈMES_AUTONOMES": "#004D40",
                "SYSTEMES_DE_TRANSPORT": "#D84315",
                "VIE_SPATIALE": "#BF360C",
                "INFRASTRUCTURES": "#388E3C",
                "HABITATS": "#2E7D32",
                "AGRICULTURE_SPATIALE": "#1B5E20",
                "CONSTRUCTION": "#455A64",
                "STOCKAGE": "#37474F",
                "MOTEURS": "#0288D1",
                "FUEL": "#0288C1",
                "SYSTEMES_DE_PROPULSION": "#1976D2",
                "CAPTEURS": "#8E24AA",
            };
            return subColors[sousCategorie] || "transparent";
        }
    }
};
</script>

<style scoped>
.recherches-list-container {
    flex: 2;
    overflow-y: auto;
    padding-right: 20px;
}

.categories-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
    width: 100%;
}

.categorie {
    padding: 10px;
    border-radius: 5px;
    color: white;
}

.sous-categorie {
    margin-top: 10px;
    padding: 5px;
    border-radius: 5px;
}

.sous-categorie ul {
    list-style-type: none;
    padding-left: 0;
}

.recherche-item {
    margin: 5px 0;
    cursor: pointer;
    padding: 5px;
    transition: background-color 0.3s ease;
}

.recherche-item:hover {
    background-color: rgba(255,255,255,0.2);
}
</style>