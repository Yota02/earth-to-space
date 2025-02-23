<template>
    <div class="p-6">
      <h1 class="text-2xl font-bold mb-4">Liste des Boosters</h1>
  
      <!-- Formulaire d'ajout -->
      <form @submit.prevent="addBooster" class="bg-gray-100 p-4 rounded-lg shadow-md mb-6">
        <div class="grid grid-cols-2 gap-4">
          <input v-model="newBooster.nom" placeholder="Nom du Booster" class="p-2 border rounded" required />
          <input v-model.number="newBooster.taille" type="number" placeholder="Taille (m)" class="p-2 border rounded" required />
          <input v-model.number="newBooster.diametre" type="number" placeholder="Diamètre (m)" class="p-2 border rounded" required />
          <input v-model.number="newBooster.poidsAVide" type="number" placeholder="Poids à Vide (kg)" class="p-2 border rounded" required />
          <input v-model.number="newBooster.altitudeMax" type="number" placeholder="Altitude Max (m)" class="p-2 border rounded" required />
          <input v-model.number="newBooster.vitesseMax" type="number" placeholder="Vitesse Max (m/s)" class="p-2 border rounded" required />
        </div>
        <button type="submit" class="mt-4 bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
          Ajouter Booster
        </button>
      </form>
  
      <!-- Affichage des boosters -->
      <div class="grid grid-cols-3 gap-4">
        <div v-for="booster in boosters" :key="booster.nom" class="bg-white p-4 rounded-lg shadow-md">
          <h2 class="text-lg font-semibold">{{ booster.nom }}</h2>
          <p><strong>Taille :</strong> {{ booster.taille }} m</p>
          <p><strong>Diamètre :</strong> {{ booster.diametre }} m</p>
          <p><strong>Poids à Vide :</strong> {{ booster.poidsAVide }} kg</p>
          <p><strong>Altitude Max :</strong> {{ booster.altitudeMax }} m</p>
          <p><strong>Vitesse Max :</strong> {{ booster.vitesseMax }} m/s</p>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        boosters: [], // Liste des boosters
        newBooster: {
          nom: '',
          taille: null,
          diametre: null,
          poidsAVide: null,
          altitudeMax: null,
          vitesseMax: null,
        },
      };
    },
    methods: {
      addBooster() {
        if (this.newBooster.nom && this.newBooster.taille) {
          this.boosters.push({ ...this.newBooster });
          this.resetForm();
        }
      },
      resetForm() {
        this.newBooster = {
          nom: '',
          taille: null,
          diametre: null,
          poidsAVide: null,
          altitudeMax: null,
          vitesseMax: null,
        };
      },
      async fetchBoosters() {
        try {
          // Remplace avec un appel API si besoin
          const response = await fetch('URL_DE_TON_API');
          this.boosters = await response.json();
        } catch (error) {
          console.error('Erreur lors du chargement des boosters:', error);
        }
      }
    },
    mounted() {
      this.fetchBoosters();
    },
  };
  </script>
  
  <style scoped>
  input {
    width: 100%;
  }
  </style>
  