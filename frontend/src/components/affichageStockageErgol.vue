<template>
  <div class="p-6 max-w-7xl mx-auto">
    <!-- État d'erreur -->
    <div v-if="connectionStatus === 'error'" class="p-6 text-center text-red-600">
      Erreur de connexion au serveur. Veuillez rafraîchir la page.
    </div>

    <!-- État de chargement -->
    <div v-else-if="connectionStatus === 'connecting'" class="p-6 text-center">
      Connexion au serveur...
    </div>

    <!-- Contenu principal -->
    <template v-else>
      <div class="fuel-container" v-if="fuelObjects.length > 0">
        <div v-for="fuel in fuelObjects" :key="fuel.nom" class="fuel-item">
          <h4 class="font-semibold text-lg mb-2">{{ fuel.nom }}</h4>

          <!-- Réservoir -->
          <div class="reservoir">
            <div class="tank">
              <div class="fuel-level" :style="{
                height: calculateFillPercentage(fuel) + '%',
                backgroundColor: calculateFillPercentage(fuel) > 0 ? getFuelColor(calculateFillPercentage(fuel)) : '#e5e7eb'
              }"></div>
            </div>
            <div class="info mt-3">
              <p class="text-sm text-gray-600">
                Quantité: {{ fuel.quantiteStock?.toFixed(2) || 0 }} L
              </p>
              <p class="text-sm text-gray-600">
                Capacité: {{ fuel.capaciteMax?.toFixed(2) || 0 }} L
              </p>
              <p class="text-sm text-gray-600">Prix: {{ fuel.prix }} crédits/L</p>
            </div>
          </div>

          <!-- Boutons d'action -->
          <div class="flex gap-2 mt-3">
            <button @click="handlePurchase(fuel.nom)"
              class="flex-1 px-4 py-2 bg-blue-600 text-white hover:bg-blue-700 rounded-md transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              :disabled="gameData.argent < fuel.prix">
              Acheter
            </button>
            <button @click="handleSell(fuel.nom)"
              class="flex-1 px-4 py-2 bg-red-600 text-white hover:bg-red-700 rounded-md transition-colors">
              Vendre
            </button>
          </div>

          <!-- Bouton Ajouter un réservoir spécifique au type de carburant -->
          <button @click="() => handleAddReservoir(fuel.type)"
            class="w-full mt-4 px-4 py-2 bg-green-600 text-white hover:bg-green-700 rounded-md transition-colors">
            Ajouter un réservoir
          </button>
        </div>
      </div>
      <div v-else>
        Aucun carburant disponible.
      </div>
    </template>
  </div>
</template>

<script>
export default {
  data() {
    return {
      ws: null,
      connectionStatus: 'connecting', // statut de la connexion
      gameData: {
        carburants: [], // tableau pour stocker les carburants
        argent: 1000 // exemple de l'argent disponible
      },
      updateInterval: null // pour garder la référence de l'intervalle
    };
  },

  computed: {
    fuelObjects() {
      return this.gameData.carburants; // Retourne les carburants à afficher
    }
  },

  methods: {
    // Initialisation de la connexion WebSocket
    initWebSocket() {
      this.ws = new WebSocket('ws://localhost:3232'); // Adresse du serveur WebSocket

      // Ouverture de la connexion
      this.ws.onopen = () => {
        this.connectionStatus = 'connected'; // Si la connexion est réussie
        this.requestCarburants(); // Demande des carburants après la connexion

        // Appeler getCarburants toutes les secondes
        this.updateInterval = setInterval(() => {
          this.requestCarburants();
        }, 1000); // 1000 ms = 1 seconde
      };

      // Fermeture de la connexion
      this.ws.onclose = () => {
        this.connectionStatus = 'error'; // En cas d'erreur de connexion
        clearInterval(this.updateInterval); // Arrêter l'intervalle si la connexion se ferme
      };

      // Réception d'un message
      this.ws.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data); // On analyse le message JSON du serveur

          // Si le message contient l'état des carburants
          if (data.action === "carburantsState" && Array.isArray(data.carburants)) {
            // Met à jour les carburants
            data.carburants.forEach(fuel => {
              fuel.quantiteStock = fuel.quantiteStock || 0;  // Définit 0 si non défini
              fuel.capaciteMax = fuel.capaciteMax || 100;    // Définit 100 si non défini
            });

            this.gameData.carburants = data.carburants; // Mettez à jour le tableau carburants
          } else {
            console.warn("Les données reçues ne contiennent pas 'carburants' ou ne sont pas valides.");
          }
        } catch (error) {
          console.error('Erreur lors du parsing du message:', error);
        }
      };
    },

    requestCarburants() {
      if (this.ws && this.ws.readyState === WebSocket.OPEN) {
        this.ws.send(JSON.stringify({
          action: 'getCarburants'
        }));
      }
    },

    calculateFillPercentage(fuel) {
      if (!fuel.quantiteStock || !fuel.capaciteMax) return 0;
      return Math.min((fuel.quantiteStock / fuel.capaciteMax) * 100, 100);
    },

    getFuelColor(percentage) {
      if (percentage > 66) return '#22c55e';  // Vert
      if (percentage > 33) return '#eab308';  // Jaune
      return '#ef4444';  // Rouge
    },

    handlePurchase(fuelName) {
      if (this.ws && this.ws.readyState === WebSocket.OPEN) {
        this.ws.send(JSON.stringify({
          action: 'buyCarburant',
          name: fuelName
        }));
      }
    },

    handleSell(fuelName) {
      if (this.ws && this.ws.readyState === WebSocket.OPEN) {
        this.ws.send(JSON.stringify({
          action: 'sellCarburant',
          name: fuelName
        }));
      }
    },

    handleAddReservoir(fuelType) {
      if (this.ws && this.ws.readyState === WebSocket.OPEN) {
        this.ws.send(JSON.stringify({
          action: 'addReservoir',
          fuelType: fuelType
        }));
      }
    }
  },

  mounted() {
    this.initWebSocket(); 
  },

  beforeUnmount() {
    if (this.ws) {
      this.ws.close();
    }

    // Arrêter l'intervalle si le composant est démonté
    if (this.updateInterval) {
      clearInterval(this.updateInterval);
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
