<template>
    <div class="max-w-4xl mx-auto p-8">
      <h1 class="text-3xl font-semibold mb-8 text-center text-gray-800">Détails du Booster</h1>
  
      <div class="bg-white p-6 rounded-lg shadow-lg">
        <!-- Informations principales -->
        <div class="mb-4 flex items-center">
          <span class="font-medium text-lg text-gray-900 w-40">Nom :</span>
          <span class="text-gray-700">{{ booster.nom }}</span>
        </div>
  
        <div class="mb-4 flex items-center">
          <span class="font-medium text-lg text-gray-900 w-40">Taille :</span>
          <span class="text-gray-700">{{ booster.taille }} m</span>
        </div>
  
        <div class="mb-4 flex items-center">
          <span class="font-medium text-lg text-gray-900 w-40">Diamètre :</span>
          <span class="text-gray-700">{{ booster.diametre }} m</span>
        </div>
  
        <div class="mb-4 flex items-center">
          <span class="font-medium text-lg text-gray-900 w-40">Altitude Max :</span>
          <span class="text-gray-700">{{ booster.altitudeMax }} km</span>
        </div>
  
        <div class="mb-4 flex items-center">
          <span class="font-medium text-lg text-gray-900 w-40">Poids Total :</span>
          <span class="text-gray-700">{{ booster.poids }} kg</span>
        </div>
  
        <!-- Spécifications supplémentaires -->
        <div class="mb-4 flex items-center">
          <span class="font-medium text-lg text-gray-900 w-40">Prototype :</span>
          <span class="text-gray-700">{{ booster.estPrototype ? 'Oui' : 'Non' }}</span>
        </div>
  
        <div class="mb-4 flex items-center">
          <span class="font-medium text-lg text-gray-900 w-40">Réutilisable :</span>
          <span class="text-gray-700">{{ booster.estReetulisable ? 'Oui' : 'Non' }}</span>
        </div>
  
        <div class="mb-4 flex items-center">
          <span class="font-medium text-lg text-gray-900 w-40">Système Auto-Destruction :</span>
          <span class="text-gray-700">{{ booster.aSystèmeAutoDestruction ? 'Oui' : 'Non' }}</span>
        </div>
  
        <!-- Historique des lancements -->
        <div class="mb-6">
          <h2 class="font-semibold text-lg text-gray-900">Historique des Lancements :</h2>
          <ul class="list-disc pl-6">
            <li v-for="lancement in booster.historiquesLancement" :key="lancement" class="text-gray-700">
              {{ lancement }}
            </li>
          </ul>
          <p v-if="booster.historiquesLancement.length === 0" class="text-gray-500">
            Aucun lancement enregistré.
          </p>
        </div>
      </div>
  
      <!-- Lien Retour -->
      <div class="mt-8 text-center">
        <router-link to="/boosters" class="text-blue-600 hover:underline text-lg">
          Retour à la liste des boosters
        </router-link>
      </div>
    </div>
  </template>
  
  <script>
  import { ref, onMounted } from "vue";
  import { useRoute } from "vue-router";
  
  export default {
    name: "BoosterDetails",
    setup() {
      const route = useRoute();
      const booster = ref({
        nom: "",
        taille: 0,
        diametre: 0,
        poids: 0,
        altitudeMax: 0,
        estPrototype: false,
        estReetulisable: false,
        aSystèmeAutoDestruction: false,
        historiquesLancement: [],
      });
  
      onMounted(() => {
        // Charger les données depuis les paramètres de la route
        const nom = route.query.nom || "Nom non spécifié";
        const taille = route.query.taille ? parseFloat(route.query.taille) : 0;
        const diametre = route.query.diametre ? parseFloat(route.query.diametre) : 0;
        const poids = route.query.poids ? parseFloat(route.query.poids) : 0;
        const altitudeMax = route.query.altitudeMax ? parseFloat(route.query.altitudeMax) : 0;
        const estPrototype = route.query.estPrototype === "true";
        const estReetulisable = route.query.estReetulisable === "true";
        const aSystèmeAutoDestruction = route.query.aSystèmeAutoDestruction === "true";
  
        // Historique des lancements (JSON string)
        const historiquesLancement = route.query.historiquesLancement
          ? JSON.parse(route.query.historiquesLancement)
          : [];
  
        booster.value = {
          nom,
          taille,
          diametre,
          poids,
          altitudeMax,
          estPrototype,
          estReetulisable,
          aSystèmeAutoDestruction,
          historiquesLancement,
        };
      });
  
      return { booster };
    },
  };
  </script>
  
  <style scoped>
  .text-gray-700 {
    font-size: 1.125rem;
  }
  .text-gray-900 {
    font-size: 1.125rem;
  }
  .bg-white {
    background-color: #ffffff;
  }
  .shadow-lg {
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  .mb-4 {
    margin-bottom: 1rem;
  }
  .mb-6 {
    margin-bottom: 1.5rem;
  }
  .mb-8 {
    margin-bottom: 2rem;
  }
  .text-blue-600 {
    color: #2563eb;
  }
  .text-lg {
    font-size: 1.125rem;
  }
  </style>