<template>
  <div class="market-container">
    <!-- Colonne de gauche (1/4) pour les constructions -->
    <div class="construction-sidebar">
      <BuildingConstruction :buildings="batimentsEnConstruction" />
    </div>

    <!-- Contenu principal (2/4) -->
    <div class="market-main">
      <BuildingTypeList :buildings="batimentsDisponibles" :money="gameState.argent" :types="availableTypes"
        @purchase="acheterBatiment" @type-change="handleTypeChange" @select-building="selectBuilding" />
    </div>

    <!-- Panneau de détails (1/4) -->
    <div class="detail-sidebar">
      <BuildingDetail :selectedBuilding="selectedBuilding" :canAfford="canAffordSelected" @purchase="acheterBatiment" />
    </div>
  </div>
</template>

<style scoped>
.market-container {
  display: grid;
  grid-template-columns: minmax(250px, 1fr) minmax(400px, 2fr) minmax(250px, 1fr);
  gap: 1rem;
  padding: 1rem;
  height: calc(100vh - 2rem);
  width: 100%;
  overflow: hidden;
  /* Empêche le scroll global */
}

.construction-sidebar,
.market-main,
.detail-sidebar {
  min-height: 0;
  /* Important pour le scroll */
  overflow-y: auto;
  background-color: #f8fafc;
  border-radius: 0.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* Responsive design pour les petits écrans */
@media (max-width: 1024px) {
  .market-container {
    grid-template-columns: 250px 1fr 250px;
  }
}

@media (max-width: 768px) {
  .market-container {
    grid-template-columns: 100%;
    grid-template-rows: auto 1fr auto;
    height: auto;
  }

  .construction-sidebar {
    max-height: 300px;
  }

  .detail-sidebar {
    max-height: 400px;
  }
}
</style>
<script>
import BuildingTypeList from '../../components/Batiment/BuildingTypeList.vue'
import BuildingConstruction from '../../components/Batiment/BuildingConstruction.vue'
import BuildingDetail from '../../components/Batiment/BuildingDetail.vue'


export default {
  name: 'BuildingMarket',
  components: {
    BuildingTypeList,
    BuildingConstruction,
    BuildingDetail
  },
  data() {
    return {
      selectedBuilding: null,
      socket: null,
      gameState: {
        argent: 0,
        date: null
      },
      batimentsDisponibles: [],
      batimentsEnConstruction: [],
      currentType: ''
    }
  },
  computed: {
    availableTypes() {
      return [...new Set(this.batimentsDisponibles.map(b => b.type))].sort()
    },
    canAffordSelected() {
      return this.selectedBuilding ? this.gameState.argent >= this.selectedBuilding.cout : false
    }
  },
  methods: {
    selectBuilding(building) {
      this.selectedBuilding = building
    },

    initWebSocket() {
      this.socket = new WebSocket('ws://localhost:3232')

      this.socket.onopen = () => {
        console.log('✅ Connecté au serveur WebSocket')
        this.getBatimentsState()
      }

      this.socket.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);

          if (data.argent !== undefined) {
            this.gameState.argent = data.argent;
          }
          if (data.date !== undefined) {
            this.gameState.date = data.date;
          }

          if (data.action === "batimentsState") {
            this.batimentsDisponibles = data.batimentsDisponibles || [];
            this.batimentsEnConstruction = data.batimentsEnConstruction || [];
          }

          if (data.action === "buyResponse") {
            if (data.success) {
              console.log(`✅ Achat réussi: ${data.message}`);
            } else {
              console.warn(`❌ Achat échoué: ${data.message}`);
            }
          }
        } catch (error) {
          console.error('❌ Erreur lors du parsing JSON:', error);
        }
      }

      this.socket.onerror = (error) => {
        console.error("❌ Erreur WebSocket:", error);
      };

      this.socket.onclose = () => {
        console.warn("⚠️ Connexion WebSocket fermée");
      };
    },

    getBatimentsState() {
      if (this.socket && this.socket.readyState === WebSocket.OPEN) {
        const message = { action: "getBatiment" };
        this.socket.send(JSON.stringify(message));
      }
    },

    handleTypeChange(newType) {
      this.currentType = newType;
    },

    acheterBatiment(batimentData) {
      if (this.gameState.argent < batimentData.batiment.cout) {
        console.warn("❌ Achat impossible, fonds insuffisants.");
        return;
      }

      if (this.socket && this.socket.readyState === WebSocket.OPEN) {
        // Envoyer directement l'objet batimentData qui contient déjà la structure correcte
        this.socket.send(JSON.stringify(batimentData));

        // Demander immédiatement la mise à jour de l'état
        this.getBatimentsState();
      } else {
        console.error("❌ WebSocket non connecté.");
      }
    }
  },
  mounted() {
    this.initWebSocket();

    setInterval(() => {
        this.getBatimentsState();
    }, 1000);
  },
  beforeUnmount() {
    if (this.socket) {
      this.socket.close();
    }
  }
}
</script>
