<template>
  <div>
    <h1 class="text-3xl font-bold text-center mb-8">Ergol</h1>
    <AffichageStockageErgol />
  </div>
</template>

<script>
import AffichageStockageErgol from "../components/affichageStockageErgol.vue";

export default {
  name: "ErgolPage", 
  components: {
    AffichageStockageErgol
  }
};
</script>