<template>
  <div class="politique-container">
      <div class="politique-column">
          <Subvention/>
      </div>
      <div class="politique-column">
          <Objectif/>
      </div>
  </div>
</template>

<script>
import Subvention from '../../components/Politique/subvention.vue';
import Objectif from '../../components/Politique/objectif.vue';

export default {
  components: { 
      Subvention,
      Objectif
  }
};
</script>

<style scoped>
.politique-container {
  display: flex;
  gap: 2rem;
  padding: 2rem;
  min-height: 100vh;
}

.politique-column {
  flex: 1;
  min-width: 0; /* Empêche le débordement sur petits écrans */
}

/* Responsive design pour les petits écrans */
@media (max-width: 1024px) {
  .politique-container {
      flex-direction: column;
  }
  
  .politique-column {
      width: 100%;
  }
}
</style>