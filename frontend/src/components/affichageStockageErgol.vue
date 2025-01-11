<template>
    <div class="p-6 max-w-7xl mx-auto">
      <!-- Error State -->
      <div v-if="connectionStatus === 'error'" class="p-6 text-center text-red-600">
        Error connecting to server. Please refresh the page.
      </div>
  
      <!-- Loading State -->
      <div v-else-if="connectionStatus === 'connecting'" class="p-6 text-center">
        Connecting to server...
      </div>
  
      <!-- Main Content -->
      <template v-else>
        <div class="fuel-container">
          <div
            v-for="object in fuelObjects"
            :key="object.nom"
            class="fuel-item"
          >
            <h4 class="font-semibold text-lg mb-2">{{ object.nom }}</h4>
  
            <!-- Réservoir -->
            <div class="reservoir">
              <div class="tank">
                <div
                  class="fuel-level"
                  :style="{
                    height: calculateFillPercentage(object) + '%',
                    backgroundColor: getFuelColor(calculateFillPercentage(object))
                  }"
                ></div>
              </div>
              <div class="info mt-3">
                <p class="text-sm text-gray-600">
                  Quantité: {{ object.quantiteStock?.toFixed(2) || 0 }} L
                </p>
                <p class="text-sm text-gray-600">Prix: {{ object.prix }} crédits/L</p>
              </div>
            </div>
  
            <!-- Boutons -->
            <div class="flex gap-2 mt-3">
              <button
                @click="handlePurchase(object.nom)"
                class="flex-1 px-4 py-2 bg-blue-600 text-white hover:bg-blue-700 rounded-md transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                :disabled="gameData.argent < object.prix"
              >
                Acheter
              </button>
              <button
                @click="handleSell(object.nom)"
                class="flex-1 px-4 py-2 bg-red-600 text-white hover:bg-red-700 rounded-md transition-colors"
              >
                Vendre
              </button>
            </div>
          </div>
        </div>
      </template>
    </div>
  </template>
  
  <script>
export default {
  name: 'AffichageStockageErgol',
  data() {
    return {
      gameData: {
        argent: 0,
        objectsAchetables: []
      },
      websocket: null,
      connectionStatus: 'connecting'
    };
  },
  computed: {
    fuelObjects() {
      if (!this.gameData || !this.gameData.objectsAchetables) {
        console.warn('Game data or objectsAchetables is undefined');
        return [];
      }
      return this.gameData.objectsAchetables.filter(obj => obj && obj.type === 'carburant');
    }
  },
  methods: {
    initializeWebSocket() {
      this.websocket = new WebSocket("ws://localhost:3232");

      this.websocket.onopen = () => {
        this.connectionStatus = 'connected';
        console.log("WebSocket connection established");
      };

      this.websocket.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          console.log("Received data:", data); // Debug log

          if (data.error) {
            console.error("Server error:", data.error);
            return;
          }

          // Vérification des données reçues
          if (!data.objectsAchetables) {
            console.warn("objectsAchetables is missing in received data");
            data.objectsAchetables = []; // Valeur par défaut
          }

          if (!Array.isArray(data.objectsAchetables)) {
            console.warn("objectsAchetables is not an array, received:", typeof data.objectsAchetables);
            data.objectsAchetables = []; // Conversion en tableau vide
          }

          // Mise à jour des données
          this.gameData = {
            argent: data.argent || 0,
            objectsAchetables: data.objectsAchetables
          };

        } catch (error) {
          console.error("Error processing WebSocket data:", error);
          this.connectionStatus = 'error';
        }
      };

      this.websocket.onerror = (error) => {
        console.error("WebSocket error:", error);
        this.connectionStatus = 'error';
      };

      this.websocket.onclose = () => {
        console.log("WebSocket connection closed");
        this.connectionStatus = 'disconnected';
        // Tentative de reconnexion après un délai
        setTimeout(() => {
          if (this.connectionStatus === 'disconnected') {
            this.initializeWebSocket();
          }
        }, 5000);
      };
    }
  }
};
  </script>
  
  <style scoped>
  .fuel-container {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    gap: 2rem;
    justify-content: center;
    align-items: flex-start;
  }
  
  .fuel-item {
    flex: 0 1 auto;
    min-width: 200px;
    max-width: 250px;
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 1rem;
    border: 1px solid #e5e7eb;
    border-radius: 0.5rem;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  }
  
  .reservoir {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-bottom: 1rem;
  }
  
  .tank {
    position: relative;
    width: 60px;
    height: 200px;
    border: 2px solid #444;
    background-color: #f3f4f6;
    border-radius: 6px;
    overflow: hidden;
  }
  
  .fuel-level {
    position: absolute;
    bottom: 0;
    width: 100%;
    transition: height 0.5s, background-color 0.5s;
  }
  
  .info {
    text-align: center;
    margin-top: 0.5rem;
  }
  </style>