<template>
    <div>
      <h1 class="text-3xl font-bold text-center mb-8">Magasin</h1>
  
      <ShopDashboard />
    </div>
  </template>
  
  <script>
  import ShopDashboard from "../components/ShopDashboard.vue";
  
  export default {
    name: "ShopDashView",
    components: {
        ShopDashboard
    }
  };
  </script>
  
  <style scoped>

  </style>
  