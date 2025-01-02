<template>
    <div class="max-w-2xl mx-auto p-6">
      <h1 class="text-2xl font-bold mb-6 text-center">Lanceurs</h1>
  
      <div class="p-4 bg-gray-50 rounded-lg">
        <p class="text-gray-700"><strong>Nom:</strong> {{ programme.nom }}</p>
        <p class="text-gray-700"><strong>Objectif:</strong> {{ programme.objectif }}</p>
      </div>
  
      <div class="mt-6 text-center">
        <router-link
          to="/"
          class="text-blue-600 hover:underline"
        >
          Retour à la liste des programmes
        </router-link>
      </div>
    </div>
  </template>
  
  <script>
  import { ref, onMounted } from "vue";
  import { useRoute } from "vue-router";
  
  export default {
    name: "ProgrammeDetails",
    setup() {
      const route = useRoute();
      const programme = ref({
        nom: "",
        objectif: ""
      });
  
      onMounted(() => {
        const nom = route.query.nom || "Nom non spécifié";
        const objectif = route.query.objectif || "Objectif non spécifié";
        programme.value = { nom, objectif };
      });
  
      return { programme };
    }
  };
  </script>
  
  <style scoped>
  .text-gray-700 {
    font-size: 1.125rem;
  }
  </style>
  