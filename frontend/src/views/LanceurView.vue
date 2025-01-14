<template>
    <div>
      <LanceurGeneral />

      <div class="test">
          <router-link to="/fusee-details" @click="closeMenu">Lanceur Details</router-link>
      </div>
      
    </div>
</template>
  
<script>
  import LanceurGeneral from "../components/LanceurGeneral.vue";
  
  export default {
    name: "LanceurView",
    components: {
      LanceurGeneral
    }
  };
</script>
  
<style scoped>
.test {
  margin-left: 500px;
}

</style>
  