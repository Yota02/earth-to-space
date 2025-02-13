<template>
  <div>
    <h1 class="text-3xl font-bold text-center mb-8">Ergol</h1>

    <AffichageStockageErgol />

    <div class="usine-container">
      <UsineCarburant />
    </div>
    
  </div>
</template>

<script>
import AffichageStockageErgol from "../components/Ergol/affichageStockageErgol.vue";
import UsineCarburant from "../components/Ergol/UsineCarburant.vue";

export default {
  name: "ErgolPage", 
  components: {
    AffichageStockageErgol,
    UsineCarburant
  }
};
</script>

<style scoped>
h3 {
  color: white;
}

.usine-container {
  position: absolute;
  top: 0;
  left: 0;
  margin-top: 100px;
  max-width: 100px;
  width: 100%;
  max-height: 300px;
}

</style>