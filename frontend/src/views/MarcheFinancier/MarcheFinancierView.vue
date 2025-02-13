<template>
    <MarketChart :marche="marcheFinancier" />
</template>
  
<script>
  import MarketChart from '../../components/MarcheFinancier/MarketChart.vue'
  
  export default {
    components: {
      MarketChart
    }
  }
</script>