<template>
    <div class="w-1/3 bg-white rounded-lg shadow p-4">
      <h2 class="text-xl font-bold mb-4">Constructions en cours</h2>
      <div class="space-y-4">
        <div v-for="building in buildings" 
             :key="building.nom" 
             class="p-4 border rounded-lg">
          <h3 class="font-semibold">{{ building.nom }}</h3>
          <div class="mt-2 space-y-2">
            <ConstructionProgress 
              :etat="building.etat"
              :temps-restant="getTempsRestant(building)"
            />
          </div>
        </div>
        <div v-if="buildings.length === 0" 
             class="text-gray-500 text-center py-4">
          Aucune construction en cours
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import ConstructionProgress from './ConstructionProgress.vue'
  
  export default {
    name: 'BuildingConstruction',
    components: {
      ConstructionProgress
    },
    props: {
      buildings: {
        type: Array,
        required: true
      }
    },
    methods: {
      getTempsRestant(building) {
        const tempsRestant = building.tempsConstruction * (100 - building.etat) / 100
        return `Temps restant: ${Math.ceil(tempsRestant)} mois`
      }
    }
  }
  </script>