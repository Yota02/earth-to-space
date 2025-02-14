<template>
    <div class="production-container">
      <h2>Usines de Production</h2>
      
      <!-- Liste des usines de production -->
      <div class="usines-list">
        <div v-for="usine in usinesProduction" :key="usine.nom" class="usine-card">
          <h3>{{ usine.nom }}</h3>
          <div class="usine-details">
            <p><strong>Superficie:</strong> {{ usine.superficie }} m²</p>
            <p><strong>Temps de construction:</strong> {{ usine.tempsConstruction }} jours</p>
            <p><strong>Matériaux requis:</strong> {{ usine.materiauxEnEntree }}</p>
            <p><strong>Production:</strong> {{ usine.pieceProduite }}</p>
            <p><strong>Quantité produite:</strong> {{ usine.quantiteProduite }} unités</p>
          </div>
        </div>
      </div>
  
      <!-- Message si aucune usine -->
      <div v-if="usinesProduction.length === 0" class="no-data">
        Aucune usine de production disponible
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'ProductionView',
    data() {
      return {
        usinesProduction: [],
        socket: null,
        updateInterval: null
      };
    },
    methods: {
      updateProductionState() {
        this.socket.send(JSON.stringify({
          action: "getProduction"
        }));
      },
  
      handleWebSocketMessage(event) {
        try {
          const data = JSON.parse(event.data);
          console.log("Données reçues:", data); // Pour le débogage
          
          if (data.action === "productionState") { // Changé pour correspondre à l'action reçue
            this.usinesProduction = data.production; // Accède au tableau 'production'
          }
        } catch (error) {
          console.error("Erreur lors du traitement des données WebSocket:", error);
        }
      }
    },
    mounted() {
      this.socket = new WebSocket("ws://localhost:3232");
      this.socket.onmessage = this.handleWebSocketMessage;
  
      this.socket.onopen = () => {
        console.log("WebSocket connecté"); // Pour le débogage
        this.updateProductionState();
        this.updateInterval = setInterval(() => {
          this.updateProductionState();
        }, 30000);
      };
  
      this.socket.onerror = (error) => {
        console.error("Erreur de connexion WebSocket:", error);
      };
    },
    beforeUnmount() {
      if (this.socket) {
        this.socket.close();
      }
      if (this.updateInterval) {
        clearInterval(this.updateInterval);
      }
    }
  };
  </script>
  
  <style scoped>
  .production-container {
    padding: 20px;
  }
  
  .usines-list {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    gap: 20px;
    margin-top: 20px;
  }
  
  .usine-card {
    border: 1px solid #ddd;
    border-radius: 8px;
    padding: 15px;
    background-color: #f8f9fa;
  }
  
  .usine-card h3 {
    margin-top: 0;
    margin-bottom: 15px;
    color: #2c3e50;
  }
  
  .usine-details p {
    margin: 8px 0;
  }
  
  .usine-details strong {
    color: #486581;
  }
  
  .no-data {
    text-align: center;
    padding: 20px;
    color: #666;
    font-style: italic;
  }
  </style>