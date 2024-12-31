<template>
    <div>
      <h1 class="text-3xl font-bold text-center mb-8">Tableau de bord de la Recherche</h1>
  
      <!-- Intégration du composant ResearchDashboard -->
      <ResearchDashboard />
    </div>
  </template>
  
  <script>
  // Importation du composant ResearchDashboard
  import ResearchDashboard from "../components/ResearchDashboard.vue";
  
  export default {
    name: "ResearchView",
    components: {
      ResearchDashboard
    }
  };
  </script>
  
  <style scoped>
  /* Vous pouvez ajouter des styles spécifiques à la vue ResearchView ici */
  </style>
  