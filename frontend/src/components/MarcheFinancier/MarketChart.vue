<template>
  <div class="market-chart-container">
    <div class="market-selector">
      <select v-model="selectedMarket" class="market-select">
        <option v-for="marche in marches" :key="marche.nom" :value="marche.nom">
          {{ marche.nom }}
        </option>
      </select>
    </div>

    <div v-if="currentMarket" class="market-content">
      <h2>{{ currentMarket.nom }}</h2>
      <div class="chart-wrapper">
        <canvas ref="marketChart"></canvas>
      </div>
      <div class="market-stats">
        <div v-for="(item, index) in currentMarket.partMarche" :key="index" class="company-stat">
          <span class="company-name">{{ item.entreprise.nom }}</span>
          <span class="company-share">{{ (item.part * 100).toFixed(1) }}%</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Chart from 'chart.js/auto'

export default {
  name: 'MarketChart',
  data() {
    return {
      socket: null,
      updateInterval: null,
      chart: null,
      marches: [],
      selectedMarket: null
    }
  },
  computed: {
    currentMarket() {
      return this.marches.find(m => m.nom === this.selectedMarket);
    }
  },
  watch: {
    selectedMarket() {
      this.updateChart();
    }
  },
  methods: {
    generateChartColors(count) {
      const colors = []
      for (let i = 0; i < count; i++) {
        const hue = (i * 137.5) % 360
        colors.push(`hsl(${hue}, 70%, 60%)`)
      }
      return colors
    },

    updateMarketState() {
      this.socket.send(JSON.stringify({
        action: "MarcheFinancierState"
      }));
    },

    updateMarketState() {
      if (this.socket && this.socket.readyState === WebSocket.OPEN) {
        console.log("Envoi de la requête de mise à jour du marché");
        this.socket.send(JSON.stringify({
          action: "getMarcheFinancierState"
        }));
      }
    },

    handleWebSocketMessage(event) {
      try {
        const data = JSON.parse(event.data);
        if (data.action === "getMarcheFinancierState") {
          this.marches = data.marches;
          if (!this.selectedMarket && this.marches.length > 0) {
            this.selectedMarket = this.marches[0].nom;
          }
          // Attendre que le DOM soit mis à jour avant d'appeler updateChart
          this.$nextTick(() => {
            this.updateChart();
          });
        }
      } catch (error) {
        console.error("Erreur lors du traitement des données WebSocket:", error);
      }
    },

    updateChart() {
      if (!this.currentMarket || !this.$refs.marketChart) return;

      const ctx = this.$refs.marketChart.getContext('2d')

      if (this.chart) {
        this.chart.destroy()
      }

      const partMarche = this.currentMarket.partMarche;
      const labels = partMarche.map(item => item.entreprise.nom)
      const data = partMarche.map(item => item.part * 100)
      const backgroundColor = this.generateChartColors(labels.length)

      this.chart = new Chart(ctx, {
        type: 'pie',
        data: {
          labels: labels,
          datasets: [{
            data: data,
            backgroundColor: backgroundColor,
            borderColor: 'white',
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          plugins: {
            legend: {
              position: 'right',
              labels: {
                font: {
                  size: 14
                }
              }
            },
            tooltip: {
              callbacks: {
                label: (context) => {
                  const entreprise = partMarche[context.dataIndex].entreprise
                  return [
                    `${entreprise.nom} (${entreprise.pays})`,
                    `Part de marché: ${context.formattedValue}%`,
                    `Capitalisation: ${entreprise.capitalisationBoursiere.toLocaleString()} M€`,
                    `Cours: ${entreprise.coursAction.toLocaleString()} €`,
                    `Dividende/Action: ${entreprise.dividendeParAction.toLocaleString()} €`,
                    `Variation: ${(entreprise.variationCours * 100).toFixed(2)}%`
                  ]
                }
              }
            }
          }
        }
      })
    }
  },
  beforeDestroy() {
    if (this.updateInterval) {
      clearInterval(this.updateInterval);
    }
    if (this.socket) {
      this.socket.close();
    }
  },
  mounted() {
    console.log("Montage du composant MarketChart");
    this.socket = new WebSocket("ws://localhost:3232");

    this.socket.onopen = () => {
      console.log("WebSocket connecté");

      setTimeout(() => {
        this.updateMarketState();
      }, 100);
      this.updateInterval = setInterval(() => {
        this.updateMarketState();
      }, 30000);
    };

    this.socket.onmessage = (event) => {
      console.log("Message WebSocket reçu:", event.data);
      this.handleWebSocketMessage(event);
    };

    this.socket.onerror = (error) => {
      console.error("Erreur WebSocket:", error);
    };

    this.socket.onclose = () => {
      console.log("WebSocket fermé");
    };


  }

}
</script>

<style scoped>
.market-chart-container {
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.market-selector {
  margin-bottom: 20px;
}

.market-select {
  padding: 8px 12px;
  border-radius: 4px;
  border: 1px solid #ddd;
  font-size: 16px;
  width: 200px;
}

.chart-wrapper {
  position: relative;
  height: 400px;
  margin: 20px 0;
}

.market-stats {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 16px;
  margin-top: 20px;
}

.company-stat {
  display: flex;
  justify-content: space-between;
  padding: 8px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.company-name {
  font-weight: bold;
}

.company-share {
  color: #666;
}

.market-content {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}
</style>