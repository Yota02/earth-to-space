<template>
    <div class="chart-container">
      <canvas ref="myChart"></canvas>
    </div>
  </template>
  
  <script>
  import { onMounted, ref } from 'vue';
  import Chart from 'chart.js/auto'; // Importer Chart.js
  
  export default {
    name: 'ChartComponent',
    setup() {
      // Référence vers le canvas
      const chartCanvas = ref(null);
  
      // Fonction pour créer le graphique
      const createChart = () => {
        new Chart(chartCanvas.value, {
          type: 'bar', // Type de graphique (barres)
          data: {
            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'], // Labels des mois
            datasets: [
              {
                label: 'Ventes mensuelles',
                data: [12, 19, 3, 5, 2, 3], // Données pour chaque mois
                backgroundColor: 'rgba(75, 192, 192, 0.2)', // Couleur des barres
                borderColor: 'rgba(75, 192, 192, 1)', // Couleur de bordure des barres
                borderWidth: 1, // Bordure des barres
              },
            ],
          },
          options: {
            scales: {
              y: {
                beginAtZero: true, // Commence l'axe Y à zéro
              },
            },
          },
        });
      };
  
      // Créer le graphique après le montage du composant
      onMounted(() => {
        createChart();
      });
  
      return {
        chartCanvas,
      };
    },
  };
  </script>
  
  <style scoped>
  .chart-container {
    max-width: 800px;
    margin: auto;
    padding: 20px;
  }
  </style>  