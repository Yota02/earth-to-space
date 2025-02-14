<template>
    <div>

      <AffichageUsineProductionList />
      
    </div>
  </template>
  
  <script>
  import AffichageUsineProductionList from "../../components/Production/affichageUsineProductionList.vue";
  
  export default {
    name: "ProductionView", 
    components: {
        AffichageUsineProductionList,
    }
  };
  </script>
  
  <style scoped>
  h3 {
    color: white;
  }
  
  
  </style>