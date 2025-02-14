<template>
  <div class="dashboard">
    <div class="container">
      <!-- État d'erreur -->
      <div v-if="connectionStatus === 'error'" class="connection-status error-state">
        <p>Erreur de connexion au serveur. Veuillez rafraîchir la page.</p>
      </div>

      <!-- État de chargement -->
      <div v-if="connectionStatus === 'connecting'" class="connection-status loading-state">
        <p>Connexion au serveur en cours...</p>
      </div>

      <!-- Contenu principal -->
      <template v-else>

        <div class="objects-grid">
          <div v-for="object in gameData.objectsAchetables" 
               :key="object.nom" 
               class="object-card">
            <h4>{{ object.nom }}</h4>
            <div class="object-info">
              <p>
                <span>Prix:</span>
                <span>{{ formatMoney(object.prix) }} €</span>
              </p>
              <p v-if="object.type === 'carburant'">
                <span>Stock:</span>
                <span>{{ formatNumber(object.quantiteStock) }}</span>
              </p>
            </div>
            <div class="button-group">
              <button 
                @click="handlePurchase(object.nom)"
                class="btn btn-buy"
                :disabled="gameData.argent < object.prix">
                Acheter
              </button>
              <button 
                @click="handleSell(object.nom)"
                class="btn btn-sell">
                Vendre
              </button>
            </div>
          </div>
        </div>
      </template>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ShopDashboard',
  
  data() {
    return {
      gameData: {
        argent: 0,
        objectsAchetables: []
      },
      websocket: null,
      connectionStatus: 'connecting'
    }
  },

  methods: {
    formatMoney(value) {
      return value.toLocaleString('fr-FR')
    },

    formatNumber(value) {
      return value?.toFixed(2) || '0'
    },

    sendAction(action, payload = {}) {
      if (this.websocket?.readyState === WebSocket.OPEN) {
        this.websocket.send(JSON.stringify({ action, ...payload }))
      }
    },

    handlePurchase(objectName) {
      this.sendAction("buyObject", { name: objectName })
    },

    handleSell(objectName) {
      this.sendAction("sellObject", { name: objectName })
    },

    fetchObjectsAchetables() {
      this.sendAction("getObjectsAchetables")
    },

    initializeWebSocket() {
      this.websocket = new WebSocket("ws://localhost:3232")

      this.websocket.onopen = () => {
        this.connectionStatus = 'connected'
        this.fetchObjectsAchetables()
      }

      this.websocket.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data)
          
          if (data.error) {
            console.error("Server error:", data.error)
            return
          }

          if (data.action === "getObjectsAchetables") {
            this.gameData.objectsAchetables = data.objectsAchetables || []
          }

          if (data.argent !== undefined) {
            this.gameData.argent = data.argent
          }
        } catch (error) {
          console.error("WebSocket error:", error)
        }
      }

      this.websocket.onerror = () => {
        this.connectionStatus = 'error'
      }

      this.websocket.onclose = () => {
        this.connectionStatus = 'disconnected'
      }
    }
  },

  mounted() {
    this.initializeWebSocket()
  },

  beforeUnmount() {
    this.websocket?.close()
  }
}
</script>

<style scoped>
.dashboard {
  min-height: 100vh;
  background-color: #f8f9fa;
  color: #1a1a1a;
}

.container {
  max-width: 1280px;
  margin: 0 auto;
  padding: 2rem;
}

.connection-status {
  text-align: center;
  padding: 1rem;
  margin-bottom: 2rem;
  border-radius: 0.5rem;
  font-weight: 500;
}

.error-state {
  background-color: #fee2e2;
  color: #dc2626;
}

.loading-state {
  background-color: #ffffff;
  animation: pulse 1.5s infinite;
}

.money-display {
  text-align: center;
  margin-bottom: 2rem;
  padding: 1.5rem;
  background-color: #ffffff;
  border-radius: 0.75rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.money-display h2 {
  font-size: 1.75rem;
  font-weight: 600;
}

.objects-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
  padding: 1rem 0;
}

.object-card {
  background-color: #ffffff;
  border-radius: 1rem;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  border: 1px solid #e0e0e0;
}

.object-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.object-card h4 {
  font-size: 1.25rem;
  margin-bottom: 1rem;
  font-weight: 600;
}

.object-info {
  color: #666666;
  margin-bottom: 1.5rem;
}

.object-info p {
  display: flex;
  justify-content: space-between;
  margin: 0.5rem 0;
  padding: 0.5rem 0;
  border-bottom: 1px solid #e0e0e0;
}

.object-info p:last-child {
  border-bottom: none;
}

.button-group {
  display: flex;
  gap: 0.75rem;
}

.btn {
  flex: 1;
  padding: 0.75rem 1rem;
  border: none;
  border-radius: 0.5rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  color: white;
  font-size: 0.95rem;
}

.btn:active {
  transform: scale(0.98);
}

.btn-buy {
  background-color: #2563eb;
}

.btn-buy:hover:not(:disabled) {
  background-color: #1d4ed8;
}

.btn-buy:disabled {
  background-color: #9ca3af;
  cursor: not-allowed;
  opacity: 0.7;
}

.btn-sell {
  background-color: #dc2626;
}

.btn-sell:hover {
  background-color: #b91c1c;
}

@keyframes pulse {
  0% { opacity: 1; }
  50% { opacity: 0.5; }
  100% { opacity: 1; }
}

@media (max-width: 768px) {
  .container {
    padding: 1rem;
  }
  
  .objects-grid {
    grid-template-columns: 1fr;
  }
  
  .money-display h2 {
    font-size: 1.5rem;
  }

  .object-card {
    padding: 1.25rem;
  }
}
</style>