<template>
    <div class="research-tree">
        <ul>
            <li v-for="categorie in groupedRecherches" :key="categorie.nom">
                <span @click="toggleCategory(categorie.nom)">
                    {{ categorie.nom }}
                </span>
                <ul v-if="openedCategories.includes(categorie.nom)">
                    <li v-for="sousCategorie in categorie.sousCategories" :key="sousCategorie.nom">
                        <span @click="toggleSousCategorie(sousCategorie.nom)">
                            {{ sousCategorie.nom }}
                        </span>
                        <ul v-if="openedSousCategories.includes(sousCategorie.nom)">
                            <li v-for="recherche in sousCategorie.recherches" :key="recherche.id" @click="selectRecherche(recherche)">
                                {{ recherche.nom }}
                            </li>
                        </ul>
                    </li>
                </ul>
            </li>
        </ul>
    </div>
</template>

<script>
export default {
    props: {
        recherches: Array
    },
    data() {
        return {
            openedCategories: [],
            openedSousCategories: []
        };
    },
    computed: {
        groupedRecherches() {
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
</style>
