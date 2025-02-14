<template>
  <div class="market-chart-container">
    <div class="market-content">
      <div class="chart-wrapper">
        <MarketChart :marche="marcheFinancier" />
      </div>
      <div class="market-stats">
        <div class="company-stat">
          <span class="company-name">Nom de l'entreprise</span>
          <span class="company-share">Valeur</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import MarketChart from '../../components/MarcheFinancier/MarketChart.vue'

export default {
  components: {
    MarketChart
  }
}
</script>

<style>
.market-chart-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 80%;
  height: 80%;
}

.market-selector {
  margin-bottom: 1rem;
}

.market-select {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 0.25rem;
}

.market-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.chart-wrapper {
  width: 100%;
  max-width: 800px;
  margin-bottom: 1rem;
}

.market-stats {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}

.company-stat {
  display: flex;
  align-items: center;
  margin: 0.5rem;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 0.25rem;
}

.company-name {
  margin-right: 1rem;
}

.company-share {
  font-weight: bold;
}
</style>
