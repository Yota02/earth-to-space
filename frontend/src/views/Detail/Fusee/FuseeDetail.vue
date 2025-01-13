<template>
    <div class="max-w-3xl mx-auto p-8">
      <h1 class="text-3xl font-semibold mb-8 text-center text-gray-800">Détails de la Fusée</h1>
  
      <div class="bg-white p-6 rounded-lg shadow-lg">
        <div class="mb-4 flex items-center">
          <span class="font-medium text-lg text-gray-900 w-40">Nom :</span>
          <span class="text-gray-700">{{ fusee.nom }}</span>
        </div>
  
        <div class="mb-4 flex items-center">
          <span class="font-medium text-lg text-gray-900 w-40">Taille :</span>
          <span class="text-gray-700">{{ fusee.taille }} m</span>
        </div>
  
        <div class="mb-4 flex items-center">
          <span class="font-medium text-lg text-gray-900 w-40">Diamètre :</span>
          <span class="text-gray-700">{{ fusee.diametre }} m</span>
        </div>
  
        <div class="mb-4 flex items-center">
          <span class="font-medium text-lg text-gray-900 w-40">Poids Total :</span>
          <span class="text-gray-700">{{ fusee.poidsTotal }} kg</span>
        </div>
  
        <div class="mb-4 flex items-center">
          <span class="font-medium text-lg text-gray-900 w-40">Altitude Max :</span>
          <span class="text-gray-700">{{ fusee.altitudeMax }} km</span>
        </div>
  
        <div class="mb-4 flex items-center">
          <span class="font-medium text-lg text-gray-900 w-40">Système de Sécurité :</span>
          <span class="text-gray-700">{{ fusee.systemeSecurite ? 'Oui' : 'Non' }}</span>
        </div>
  
        <div class="mb-6">
          <h2 class="font-semibold text-lg text-gray-900">Charges Utiles :</h2>
          <ul class="list-disc pl-6">
            <li v-for="charge in fusee.poidChargeUtiles" :key="charge.nom" class="text-gray-700">
              {{ charge.nom }} - {{ charge.poids }} kg
            </li>
          </ul>
        </div>
      </div>
  
      <!-- Lien Retour -->
      <div class="mt-8 text-center">
        <router-link to="/fusées" class="text-blue-600 hover:underline text-lg">
          Retour à la liste des fusées
        </router-link>
      </div>
    </div>
  </template>
  
  <script>
  import { ref, onMounted } from "vue";
  import { useRoute } from "vue-router";
  
  export default {
    name: "FuseeDetails",
    setup() {
      const route = useRoute();
      const fusee = ref({
        nom: "",
        taille: 0,
        diametre: 0,
        poidsTotal: 0,
        altitudeMax: 0,
        systemeSecurite: false,
        poidChargeUtiles: [],
      });
  
      onMounted(() => {
        // Chargement des données à partir des paramètres de la route
        const nom = route.query.nom || "Nom non spécifié";
        const taille = route.query.taille ? parseFloat(route.query.taille) : 0;
        const diametre = route.query.diametre ? parseFloat(route.query.diametre) : 0;
        const poidsTotal = route.query.poidsTotal ? parseFloat(route.query.poidsTotal) : 0;
        const altitudeMax = route.query.altitudeMax ? parseFloat(route.query.altitudeMax) : 0;
        const systemeSecurite = route.query.systemeSecurite === "true";
  
        // Charges utiles (données formatées en JSON dans la query string)
        const poidChargeUtiles = route.query.poidChargeUtiles
          ? JSON.parse(route.query.poidChargeUtiles)
          : [];
  
        fusee.value = {
          nom,
          taille,
          diametre,
          poidsTotal,
          altitudeMax,
          systemeSecurite,
          poidChargeUtiles,
        };
      });
  
      return { fusee };
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