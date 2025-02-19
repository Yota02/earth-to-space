<template>
  <div class="production-container">
    <div class="production-list">
      <AffichageUsineProductionList />
    </div>
    <div class="production-pieces">
      <AffichageProductionParPiece />
    </div>
  </div>
</template>

<script>
import AffichageUsineProductionList from "../../components/Production/affichageUsineProductionList.vue";
import AffichageProductionParPiece from "../../components/Production/AffichageProductionParPiece.vue";

export default {
  name: "ProductionView",
  components: {
    AffichageUsineProductionList,
    AffichageProductionParPiece
  }
};
</script>

<style scoped>
.production-container {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 5px;
  padding: 5px;
}

/* Pour les écrans de moins de 1024px */
@media (max-width: 1024px) {
  .production-container {
    grid-template-columns: 1fr;
  }
}

.production-list {
  min-width: 0; /* Évite le débordement du contenu */
}

.production-pieces {
  min-width: 0;
  height: fit-content;
}

h3 {
  color: white;
}
</style>