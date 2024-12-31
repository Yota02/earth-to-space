<script>
export default {
  name: "ResearchDashboard",
  data() {
    return {
      gameData: {
        argent: 0,
        pointsRecherche: 0,
        recherches: []
      },
      socket: null
    };
  },
  computed: {
    recherchesByCategory() {
      return this.gameData.recherches.reduce((acc, recherche) => {
        const category = recherche.type;
        console.log("Catégorie de la recherche : ", category); 
        if (!acc[category]) {
          acc[category] = [];
        }
        acc[category].push(recherche);
        return acc;
      }, {});
    }
  },
  methods: {
    handleResearchAction(researchName) {
      console.log("action" + researchName);
      const socket = new WebSocket("ws://localhost:3232");
      socket.onopen = () => {
        socket.send(JSON.stringify({ action: "startResearch", name: researchName }));
        socket.close();
      };
    }
  },
  mounted() {
    this.socket = new WebSocket("ws://localhost:3232");
      
    this.socket.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        console.log("Données reçues :", data);

        if (!data.recherches || !Array.isArray(data.recherches)) {
          console.error("Recherches manquantes dans les données reçues !");
          return;
        }

        this.gameData = {
          ...this.gameData,
          argent: data.argent,
          pointsRecherche: data.pointsRecherche,
          recherches: data.recherches
        };
      } catch (error) {
        console.error("Erreur lors du traitement des données WebSocket :", error);
      }
    };

    this.socket.onclose = () => {
      console.log("WebSocket fermé.");
    };
  },
  beforeUnmount() {
    if (this.socket) {
      this.socket.close();
    }
  }
};
</script>

<template>
    <div class="p-6 max-w-7xl mx-auto">
      <!-- Affichage de l'argent -->

      <!-- Affichage des pointsRecherche -->
      <div class="mb-8">
        <p class="text-2xl font-bold">Points de Recherche: {{ gameData.pointsRecherche }}</p>
      </div>
  
      <!-- Recherches par catégorie -->
      <div class="mb-8">
        <div v-for="(researches, category) in recherchesByCategory" :key="category" class="mb-8">
          <h3 class="text-xl font-bold mb-4 text-gray-800 capitalize">{{ category }}</h3>
          <div class="space-y-6">
            <div
              v-for="recherche in researches"
              :key="recherche.nom"
              class="p-4 border rounded-lg shadow-sm hover:shadow-md transition-shadow"
            >
              <h4 class="font-semibold text-lg mb-2">{{ recherche.nom }}</h4>
              <div class="space-y-1 text-sm text-gray-600">
                <p>Prix: {{ recherche.prix }}</p>
                <p>Temps: {{ recherche.temps }}</p>
                <p>État: {{ recherche.etat }}</p>
                <!-- Barre de progression -->
                <div class="w-full bg-gray-200 rounded-full h-2.5 mt-2">
                  <div
                    class="bg-blue-600 h-2.5 rounded-full transition-all duration-300"
                    :style="{ width: recherche.progression + '%' }"
                  />
                </div>
              </div>
              <button
                @click="handleResearchAction(recherche.nom)"
                class="mt-3 w-full px-4 py-2"
                :class="{
                  'bg-blue-600 text-white hover:bg-blue-700': recherche.etat === 'Non commencé',
                  'bg-gray-300 text-gray-500 cursor-not-allowed': recherche.etat !== 'Non commencé'
                }"
                :disabled="recherche.etat !== 'Non commencé'"
              >
                {{ recherche.etat === 'Non commencé' ? 'Démarrer' : recherche.etat === 'en cours' ? 'En cours' : recherche.etat === 'terminée' ? 'Terminé' : recherche.etat }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
<style>

</style> 