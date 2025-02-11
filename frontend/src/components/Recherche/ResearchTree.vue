<template>
    <div class="research-tree">
        <ul v-if="groupedRecherches.length">
            <li v-for="categorie in groupedRecherches" :key="categorie.nom">
                <span @click="toggleCategory(categorie.nom)">
                    {{ categorie.nom }}
                </span>
                <ul v-if="openedCategories.includes(categorie.nom)">
                    <li v-for="sousCategorie in categorie.sousCategories" :key="sousCategorie.nom">
                        <div class="sous-categorie" @click="toggleSousCategorie(sousCategorie.nom)">
                            <img :src="getSousCategorieImage(sousCategorie.nom)" alt="Icône" class="sous-categorie-img" />
                            <span>{{ sousCategorie.nom }}</span>
                        </div>
                        <ul v-if="openedSousCategories.includes(sousCategorie.nom)">
                            <li v-for="recherche in sousCategorie.recherches" 
                                :key="recherche.id" 
                                @click="selectRecherche(recherche)">
                                {{ recherche.nom }}
                            </li>
                        </ul>
                    </li>
                </ul>
            </li>
        </ul>
        <p v-else>Aucune recherche disponible.</p>
    </div>
</template>

<script>
export default {
    props: {
        recherches: {
            type: Array,
            required: true
        }
    },
    data() {
        return {
            openedCategories: [],
            openedSousCategories: [],
            sousCategorieImages: {
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
        };
    },
    computed: {
        groupedRecherches() {
            if (!this.recherches || this.recherches.length === 0) return [];

            const grouped = {};

            this.recherches.forEach(recherche => {
                if (!grouped[recherche.categorie]) {
                    grouped[recherche.categorie] = {
                        nom: recherche.categorie,
                        sousCategories: {}
                    };
                }

                if (!grouped[recherche.categorie].sousCategories[recherche.sousCategorie]) {
                    grouped[recherche.categorie].sousCategories[recherche.sousCategorie] = {
                        nom: recherche.sousCategorie,
                        recherches: []
                    };
                }

                grouped[recherche.categorie].sousCategories[recherche.sousCategorie].recherches.push(recherche);
            });

            return Object.values(grouped).map(categorie => ({
                ...categorie,
                sousCategories: Object.values(categorie.sousCategories)
            }));
        }
    },
    methods: {
        toggleCategory(category) {
            if (this.openedCategories.includes(category)) {
                this.openedCategories = this.openedCategories.filter(c => c !== category);
            } else {
                this.openedCategories.push(category);
            }
        },
        toggleSousCategorie(sousCategorie) {
            if (this.openedSousCategories.includes(sousCategorie)) {
                this.openedSousCategories = this.openedSousCategories.filter(sc => sc !== sousCategorie);
            } else {
                this.openedSousCategories.push(sousCategorie);
            }
        },
        selectRecherche(recherche) {
            this.$emit('select-recherche', recherche);
        },
        getSousCategorieImage(sousCategorie) {
            return this.sousCategorieImages[sousCategorie] || 'src/assets/img/default.png';
        }
    }
};
</script>

<style scoped>
.research-tree ul {
    list-style-type: none;
    padding-left: 15px;
}

.research-tree li {
    cursor: pointer;
    margin: 5px 0;
}

.research-tree span {
    font-weight: bold;
    color: #2c3e50;
}

.research-tree span:hover {
    color: #3498db;
}

.sous-categorie {
    display: flex;
    align-items: center;
    cursor: pointer;
}

.sous-categorie-img {
    width: 25px;
    height: 25px;
    margin-right: 10px;
    border-radius: 5px;
    object-fit: cover;
}
</style>
