<template>
  <div class="home">
    <EntrepriseInfo />
    <h1>
      Bienvenue dans Earth To Space
    </h1>
  </div>
</template>

<script lang="ts">
import EntrepriseInfo from '../components/Entreprise/entrepriseInfo.vue';

export default {
  name: 'Home',
  components: {
    EntrepriseInfo
  }
};
</script>

<style scoped>
.home {
  padding: 20px;
}
</style>