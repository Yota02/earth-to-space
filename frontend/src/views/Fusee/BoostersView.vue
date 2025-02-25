<script>
import BoostersList from '../../components/Fusee/BoosterList.vue';
import BoosterDetail from '../../components/Fusee/BoosterDetail.vue';
import FuseeDisplay from '../../components/Fusee/boosterListAssemblage.vue';

export default {
  name: 'BoostersApp',
  components: {
    BoostersList,
    BoosterDetail,
    FuseeDisplay
  },
  data() {
    return {
      websocket: null,
      isConnected: false,
      boosterModels: [], // Pour stocker les modèles de boosters
      activeBoosters: [], // Pour stocker les boosters actifs
      selectedBooster: null,
      pollingInterval: null
    };
  },
  mounted() {
    this.connectWebSocket();
  },
  beforeUnmount() {
    this.disconnectWebSocket();
  },
  methods: {
    selectBooster(booster) {
      this.selectedBooster = booster;
    },
    handleProduireBooster(boosterId) {
      if (this.websocket && this.websocket.readyState === WebSocket.OPEN) {
        const message = JSON.stringify({
          action: "produireBooster",
          boosterId: boosterId
        });
        this.websocket.send(message);
      } else {
        console.error("WebSocket n'est pas ouvert ou non défini");
        // Tentative de reconnexion
        this.connectWebSocket();
        setTimeout(() => {
          if (this.websocket && this.websocket.readyState === WebSocket.OPEN) {
            const message = JSON.stringify({
              action: "produireBooster",
              boosterId: boosterId
            });
            this.websocket.send(message);
          }
        }, 1000);
      }
    },
    connectWebSocket() {
      try {
        console.log("Tentative de connexion WebSocket...");
        this.websocket = new WebSocket('ws://localhost:3232');

        this.websocket.onopen = () => {
          this.isConnected = true;

          // Demander les données des modèles de boosters
          this.websocket.send(JSON.stringify({ action: 'getBoosterModelsState' }));

          // Demander les données des boosters actifs
          this.websocket.send(JSON.stringify({ action: 'getBoostersState' }));

          // Demander périodiquement les mises à jour
          this.pollingInterval = setInterval(() => {
            if (this.isConnected) {
              this.websocket.send(JSON.stringify({ action: 'getBoosterModelsState' }));
              this.websocket.send(JSON.stringify({ action: 'getBoostersState' }));
            }
          }, 5000);
        };

        this.websocket.onmessage = (event) => {
          console.log('Message WebSocket reçu:', event.data);
          try {
            const data = JSON.parse(event.data);
            console.log("Données analysées:", data);

            if (data.boosters && Array.isArray(data.boosters)) {
              if (data.action === 'boosterModelsState') {
                console.log("Modèles de boosters reçus :", data.boosters);
                this.boosterModels = data.boosters;
              } else if (data.action === 'boostersState') {
                console.log("Boosters actifs reçus :", data.boosters);
                this.activeBoosters = data.boosters;
              }
            } else {
              console.warn("Données inattendues reçues:", data);
            }
          } catch (error) {
            console.error('Erreur lors du traitement des données WebSocket:', error);
            console.error('Données problématiques:', event.data);
          }
        };

        this.websocket.onerror = (error) => {
          console.error('Erreur WebSocket:', error);
          this.isConnected = false;
        };

        this.websocket.onclose = (event) => {
          console.log('Connexion WebSocket fermée avec code:', event.code, 'raison:', event.reason);
          this.isConnected = false;

          // Annuler le polling en cas de déconnexion
          if (this.pollingInterval) {
            clearInterval(this.pollingInterval);
          }

          // Tentative de reconnexion après 5 secondes
          setTimeout(() => {
            if (!this.isConnected) {
              console.log("Tentative de reconnexion WebSocket...");
              this.connectWebSocket();
            }
          }, 5000);
        };
      } catch (error) {
        console.error('Erreur lors de la création de la connexion WebSocket:', error);
      }
    },
    disconnectWebSocket() {
      if (this.pollingInterval) {
        clearInterval(this.pollingInterval);
      }

      if (this.websocket) {
        this.websocket.close();
        this.websocket = null;
      }
      this.isConnected = false;
    }
  }
};
</script>

<template>
  <div class="boosters-app">
    <div class="app-container">
      <div class="boosters-list-container">
        <div class="add-booster-button">
          <router-link to="/formulaireBooster" class="nav-button">
            <img src="../../assets/img/icone/plus.png" alt="formulaireBooster" class="nav-icon">
            <span class="nav-text">Nouveau booster</span>
          </router-link>
        </div>
        <BoostersList :boosters="boosterModels"
          :selectedBoosterId="selectedBooster ? (selectedBooster.id || selectedBooster.nom) : null"
          @select-booster="selectBooster" />
      </div>

      <div class="booster-detail-container" v-if="selectedBooster">
        <BoosterDetail :booster="selectedBooster" @produire-booster="handleProduireBooster" />
      </div>

      <div class="no-booster-selected" v-else>
        <p>Sélectionnez un booster pour voir ses détails</p>
      </div>

      <div class="fusee-display-wrapper">
        <FuseeDisplay :boosters="activeBoosters" />
      </div>
    </div>
  </div>
</template>

<style>
.boosters-app {
  font-family: Arial, sans-serif;
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 10px;
  box-sizing: border-box;
}

.app-container {
  display: grid;
  grid-template-columns: 1fr 1.5fr 1fr;
  gap: 20px;
  width: 100%;
  max-width: 1400px;
  height: 100%;
}

.boosters-list-container,
.booster-detail-container,
.fusee-display-wrapper {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 15px;
  overflow: auto;
  min-height: 300px;
}

.booster-detail-container {
  max-height: 80vh;
}

.no-booster-selected {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  background-color: #f5f5f5;
  border-radius: 8px;
  color: #666;
  font-size: 1.2rem;
  padding: 40px;
}

.nav-button-container {
  position: fixed;
  right: 20px;
  top: 20px;
  /* Déplace le bouton vers le haut */
  bottom: unset;
  /* Retire la position du bas */
}

.boosters-list-container {
  position: relative; /* Assurez-vous que le conteneur est positionné de manière relative */
}

.add-booster-button {
  position: absolute; /* Position absolue par rapport à .boosters-list-container */
  top: 20px; /* Ajustez selon vos préférences */
  right: 20px; /* Ajustez selon vos préférences */
}

.nav-button {
  display: flex;
  align-items: center;
  background-color: #007bff;
  color: white;
  padding: 10px 15px;
  border-radius: 5px;
  text-decoration: none;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  transition: background-color 0.3s ease;
}

.nav-button:hover {
  background-color: #0056b3;
}

.nav-icon {
  width: 20px;
  height: 20px;
  margin-right: 10px;
}

.nav-button:hover {
  background-color: #0056b3;
}

@media (max-width: 1024px) {
  .app-container {
    grid-template-columns: 1fr 1fr;
  }

  .booster-detail-container {
    grid-column: span 1;
  }
}

@media (max-width: 768px) {
  .app-container {
    grid-template-columns: 1fr;
  }

  .booster-detail-container {
    max-height: 60vh;
    grid-column: span 1;
  }

  .nav-button-container {
    right: 10px;
    bottom: 10px;
  }
}
</style>