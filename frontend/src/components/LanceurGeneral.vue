<template>
    <div class="max-w-2xl mx-auto p-6">
      <h1 class="text-2xl font-bold mb-6 text-center">Lanceurs</h1>    
      
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
  