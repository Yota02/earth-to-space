<template>
  <div class="fond">
    <div class="p-6 max-w-7xl mx-auto">
      <div v-if="connectionStatus === 'error'" class="p-6 text-center text-red-500 text-lg font-semibold">
        Erreur de connexion au serveur. Veuillez rafraîchir la page.
      </div>

      <div v-else-if="connectionStatus === 'connecting'" class="p-6 text-center text-gray-600">
        Connexion au serveur...
      </div>

      <template v-else>
        <div class="fuel-container" v-if="fuelObjects.length > 0">
          <div v-for="fuel in fuelObjects" :key="fuel.nom" class="fuel-item bg-white shadow-md rounded-lg p-6">
            <div class="flex items-center gap-3">
              <h4 class="font-semibold text-xl text-gray-800 mb-4">{{ fuel.nom }}</h4>
            </div>

            <div class="reservoir">
              <div class="tank">
                <!-- Image du carburant sur le tank -->
                <img :src="getFuelImage(fuel.type)" alt="fuel image" class="fuel-image">
                <div class="fuel-level" :style="{
                  height: calculateFillPercentage(fuel) + '%',
                  backgroundColor: calculateFillPercentage(fuel) > 0 ? getFuelColor(calculateFillPercentage(fuel)) : '#e5e7eb'
                }"></div>
              </div>
            </div>

            <div class="info mt-4 text-gray-700">
              <p>Quantité: <span class="font-medium">{{ fuel.quantiteStock?.toFixed(2) || 0 }} L</span></p>
              <p>Capacité: <span class="font-medium">{{ fuel.capaciteMax?.toFixed(2) || 0 }} L</span></p>
              <p>Prix: <span class="font-medium">{{ fuel.prix }} crédits/L</span></p>
            </div>

            <div class="flex gap-3 mt-4 w-full">
              <button @click="handlePurchase(fuel.nom)"
                class="flex-1 px-4 py-2 bg-blue-500 text-white font-medium rounded-lg shadow hover:bg-blue-600 transition"
                :disabled="gameData.argent < fuel.prix">
                Acheter
              </button>
              <button @click="handleSell(fuel.nom)"
                class="flex-1 px-4 py-2 bg-red-500 text-white font-medium rounded-lg shadow hover:bg-red-600 transition">
                Vendre
              </button>
            </div>

            <div class="mt-4 w-full">
              <input type="number" :value="monthlyDemands[fuel.nom] || 0"
                @input="(e) => handleMonthlyDemandInput(fuel, e.target.value)" @change="updateMonthlyDemand(fuel)"
                placeholder="Réservoirs/mois"
                class="w-full p-2 border border-gray-300 rounded-lg shadow-sm text-center">
            </div>

            <button @click="() => handleAddReservoir(fuel.type)"
              class="w-full mt-4 px-4 py-2 bg-green-500 text-white font-medium rounded-lg shadow hover:bg-green-600 transition">
              Ajouter un réservoir
            </button>
          </div>
        </div>
        <div v-else class="text-center text-gray-500 text-lg">
          Aucun carburant disponible.
        </div>
      </template>
    </div>
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
        argent: 100000
      },
      updateInterval: null,  // pour garder la référence de l'intervalle
      monthlyDemands: {},
    };
  },

  computed: {
    fuelObjects() {
      return this.gameData.carburants; // Retourne les carburants à afficher
    }
  },

  methods: {

    updateMonthlyDemand(fuel) {
      // Implémentez ici la logique pour mettre à jour la demande mensuelle si nécessaire
      console.log(`Demande mensuelle mise à jour pour ${fuel.nom}: ${this.monthlyDemands[fuel.nom]}L`);
    },

    sendMonthlyDemands() {
      if (this.ws && this.ws.readyState === WebSocket.OPEN) {
        const demands = this.gameData.carburants
          .filter(fuel => fuel.monthlyDemand > 0)
          .map(fuel => ({
            name: fuel.nom,
            monthlyDemand: fuel.monthlyDemand 
          }));

        if (demands.length > 0) {
          this.ws.send(JSON.stringify({
            action: 'updateMonthlyDemand',
            data: demands  
          }));
        }
      }
    },

    getFuelImage(fuelName) {
      const images = {
        "AZOTE": "src/assets/img/icone/ergol/n2.png",
        "OXYGEN": "src/assets/img/icone/ergol/o2.png",
        "METHANES": "src/assets/img/icone/ergol/ch4.png",
        "IONIQUE": "src/assets/img/icone/ergol/ion.png",
        "NUCLEAIRE": "src/assets/img/icone/ergol/nuclear.png",
        "HYDROGENE": "src/assets/img/icone/ergol/h2.png",
        "HELIUM": "src/assets/img/icone/ergol/he.png",
        "PROPULSION_ELECTRIQUE": "src/assets/img/icone/ergol/electric.png",
        "ALCOOL": "src/assets/img/icone/ergol/ethanol.png",
        "GAZ_NATUREL": "src/assets/img/icone/ergol/gas.png",
        "KEROSENE": "src/assets/img/icone/ergol/kerosene.png",
        "BIODIESEL": "src/assets/img/icone/ergol/biodiesel.png",
      };
      return images[fuelName] || "src/assets/img/icone/ergol/default.png";
    },

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

        this.monthlyUpdateInterval = setInterval(() => {
          this.sendMonthlyDemands();
        }, 10000);
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

          if (data.action === "updateMonthlyDemandSuccess") {
            console.log(`Demande mensuelle mise à jour pour ${data.name}: ${data.quantity}L`);
          }

          // Si le message contient l'état des carburants
          if (data.action === "carburantsState" && Array.isArray(data.carburants)) {
            this.gameData.carburants = data.carburants.map(fuel => ({
              ...fuel,
              quantiteStock: fuel.quantiteStock || 0,
              capaciteMax: fuel.capaciteMax || 100,
              monthlyDemand: this.monthlyDemands[fuel.nom] || 0
            }));
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

    handleMonthlyDemandInput(fuel, value) {
      this.monthlyDemands[fuel.nom] = parseInt(value) || 0;
      fuel.monthlyDemand = this.monthlyDemands[fuel.nom];
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
    this.updateMonthlyDemandInterval = setInterval(() => {
      this.sendMonthlyDemands();
    }, 30000);
  },

  beforeUnmount() {
    if (this.ws) {
      this.ws.close();
    }

    // Arrêter l'intervalle si le composant est démonté
    if (this.updateInterval) {
      clearInterval(this.updateInterval);
    }

    if (this.monthlyUpdateInterval) {
      clearInterval(this.monthlyUpdateInterval);
    }
  }
};

</script>

<style scoped>
.fuel-container {
  display: flex;
  flex-wrap: wrap;
  gap: 2rem;
  justify-content: center;
  margin-top: 2rem;
}

.fuel-item {
  width: 280px;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-radius: 15px;
  background-color: white;
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
}

.fuel-item:hover {
  transform: translateY(-10px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
}

.tank {
  position: relative;
  width: 120px;
  height: 220px;
  border-radius: 8px;
  overflow: hidden;
  background-color: #e5e7eb;
  /* Fond du réservoir */
  margin: 0 auto;
  margin-top: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.fuel-image {
  position: absolute;
  top: 10px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: auto;
  z-index: 1;
}

.fuel-level {
  position: absolute;
  bottom: 0;
  width: 100%;
  transition: height 0.5s, background-color 0.5s;
  border-radius: 0 0 8px 8px;
}

.fond {
  font-family: 'Roboto', sans-serif;
}

.info {
  font-size: 0.9rem;
  color: #333;
  line-height: 1.5;
  margin-top: 12px;
}

.info p {
  margin-bottom: 6px;
}

button:disabled {
  opacity: 0.6;
}

button {
  font-size: 0.95rem;
  font-weight: 600;
  padding: 12px;
  border-radius: 8px;
  border: none;
  cursor: pointer;
  transition: transform 0.2s ease, background-color 0.3s ease, box-shadow 0.3s ease;
  text-transform: uppercase;
  margin-top: 12px;
  margin-left: 10px;
}

button:hover {
  transform: translateY(-4px);
  background-color: #374151;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

button.bg-blue-500 {
  background-color: #3b82f6;
}

button.bg-blue-500:hover {
  background-color: #2563eb;
}

button.bg-red-500 {
  background-color: #ef4444;
}

button.bg-red-500:hover {
  background-color: #dc2626;
}

button.bg-green-500 {
  background-color: #10b981;
}

button.bg-green-500:hover {
  background-color: #059669;
}

input {
  width: 100%;
  padding: 12px;
  border: 2px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
  margin-top: 12px;
  transition: border-color 0.3s ease, transform 0.2s ease;
}

input:focus {
  border-color: #3b82f6;
  transform: scale(1.02);
}

input::placeholder {
  color: #888;
}

input[type="number"] {
  -moz-appearance: textfield;
}

input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

input[type="number"]:focus {
  outline: none;
}

.text-center {
  text-align: center;
}

.text-lg {
  font-size: 1.125rem;
}

.text-gray-500 {
  color: #6b7280;
}

.text-gray-600 {
  color: #4b5563;
}

.text-gray-700 {
  color: #374151;
}

.font-semibold {
  font-weight: 600;
}

.font-medium {
  font-weight: 500;
}

.text-xl {
  font-size: 1.25rem;
}

.font-bold {
  font-weight: bold;
}
</style>