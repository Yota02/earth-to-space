<template>
    <div>
      <h1 class="text-3xl font-bold text-center mb-8">Tableau de bord de la Recherche</h1>
  
      <!-- Intégration du composant ResearchDashboard -->
      <ProgrammeFormulaire />
    </div>
  </template>
  
  <script>
  // Importation du composant ResearchDashboard
  import ProgrammeFormulaire from "../components/ProgrammeFormulaire.vue";
  
  export default {
    name: "ProgrammeView",
    components: {
        ProgrammeFormulaire
    }
  };
  </script>
  
  <style scoped>

  </style>
  