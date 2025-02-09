<template>
  <div class="subventions-page">
    <h1>Subventions</h1>
    <div class="subventions-container">
      <!-- Subventions actives -->
      <div class="subventions-section">
        <h3 class="subventions-title">Subventions actives</h3>
        <div class="subventions-list" v-if="subventionsActives.length > 0">
          <div v-for="(subvention, index) in subventionsActives" :key="subvention.id" class="subvention-item">
            <div class="subvention-info">
              <span class="subvention-id">#{{ subvention.id }}</span>
              <span class="subvention-name">{{ subvention.nom }}</span>
              <div class="subvention-details">
                <span class="subvention-quantity">{{ formatNumber(subvention.quantite) }}$</span>
                <div class="duration-bar-container">
                  <div class="duration-bar" :style="{ width: calculateProgressBar(subvention) + '%' }">
                    <span class="duration-text">{{ formatTimeRemaining(subvention.dateFin) }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="no-subventions">
          Aucune subvention active
        </div>
      </div>

      <!-- Subventions disponibles -->
      <div class="subventions-section">
        <h3 class="subventions-title">Subventions disponibles</h3>
        <div class="subventions-list" v-if="subventionsDisponibles.length > 0">
          <div v-for="(subvention, index) in subventionsDisponibles" :key="subvention.id"
            class="subvention-item available" @click="activerSubvention(subvention)"
            :class="{ 'disabled': !canActivateSubvention(subvention) }">
            <div class="subvention-info">
              <span class="subvention-id">#{{ subvention.id }}</span>
              <span class="subvention-name">{{ subvention.nom }}</span>
              <div class="subvention-details">
                <span class="subvention-quantity">{{ formatNumber(subvention.quantite) }}$</span>
                <span class="subvention-duration">Durée: {{ subvention.duree }} mois</span>
              </div>
            </div>
            <button class="activate-btn" :disabled="!canActivateSubvention(subvention)"
              :title="!canActivateSubvention(subvention) ? 'Ressources insuffisantes' : 'Activer la subvention'">
              Activer
            </button>
          </div>
        </div>
        <div v-else class="no-subventions">
          Aucune subvention disponible
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SubventionsView',
  data() {
    return {
      subventionsActives: [],
      subventionsDisponibles: [],
      socket: null,
      updateInterval: null,
      gameData: {
        argent: 0,
        date: null,
        pointRecherche: 0,
        pointConstruction: 0,
        pointIngenieur: 0
      }
    };
  },
  methods: {
    formatNumber(number) {
      return new Intl.NumberFormat().format(number);
    },
    
    formatTimeRemaining(dateFin) {
      if (!dateFin) return '';
      
      const endDate = new Date(dateFin);
      const now = new Date();
      const diff = endDate - now;
      
      if (diff <= 0) return 'Expiré';
      
      const days = Math.floor(diff / (1000 * 60 * 60 * 24));
      const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
      const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
      
      return `${days}j ${hours}h ${minutes}m`;
    },

    calculateProgressBar(subvention) {
      if (!subvention.dateFin) return 0;
      
      const endDate = new Date(subvention.dateFin);
      const now = new Date();
      const startDate = new Date(endDate - (subvention.duree * 30 * 24 * 60 * 60 * 1000));
      
      const totalDuration = endDate - startDate;
      const elapsed = now - startDate;
      
      const progress = Math.max(0, Math.min(100, (1 - (elapsed / totalDuration)) * 100));
      return progress;
    },

    activerSubvention(subvention) {
      if (!this.canActivateSubvention(subvention)) return;

      const message = {
        type: "activateSubvention",
        subventionId: String(subvention.id),
        subventionNom: subvention.nom
      };

      console.log("Message envoyé au WebSocket:", JSON.stringify(message));
      this.socket.send(JSON.stringify(message));
    },

    canActivateSubvention(subvention) {
      // Logique de vérification des ressources ici
      return true;
    },

    updateSubventionsState() {
      this.socket.send(JSON.stringify({
        type: "getSubventionsState"
      }));
    },

    handleWebSocketMessage(event) {
      try {
        const data = JSON.parse(event.data);

        switch (data.action) {
          case "getSubventionsState":
            this.subventionsActives = data.subventionsActives;
            this.subventionsDisponibles = data.subventionsDisponibles || [];
            break;

          case "updateGameData":
            this.gameData = { ...this.gameData, ...data.gameData };
            break;

          case "subventionActivated":
            this.handleSubventionActivation(data.subvention);
            this.updateSubventionsState();
            break;
        }
      } catch (error) {
        console.error("Erreur lors du traitement des données WebSocket:", error);
      }
    },

    handleSubventionActivation(subvention) {
      console.log(`Subvention ${subvention.nom} activée avec succès`);
    }
  },

  mounted() {
    this.socket = new WebSocket("ws://localhost:3232");
    this.socket.onmessage = this.handleWebSocketMessage;

    this.socket.onopen = () => {
      this.updateSubventionsState();
      this.updateInterval = setInterval(() => {
        this.updateSubventionsState();
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
.subventions-page {
  padding: 20px;
}

.subventions-container {
  background-color: rgba(0, 0, 0, 0.6);
  border-radius: 8px;
  padding: 16px;
  max-width: 800px;
  margin: 0 auto;
}

.subventions-section {
  margin-bottom: 24px;
}

.subventions-section:last-child {
  margin-bottom: 0;
}

.subventions-title {
  font-size: 1.2rem;
  margin: 0 0 16px 0;
}

.subventions-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.subvention-item {
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 6px;
  padding: 12px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.subvention-id {
  background-color: rgba(255, 255, 255, 0.2);
  color: white;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 0.8rem;
  margin-right: 8px;
}

.subvention-name {
  font-weight: bold;
  font-size: 0.9rem;
}

.subvention-details {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-top: 8px;
}

.subvention-quantity {
  color: #4caf50;
  font-weight: bold;
}

.duration-bar-container {
  flex-grow: 1;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 4px;
  height: 20px;
  overflow: hidden;
}

.duration-bar {
  background-color: #2196f3;
  height: 100%;
  display: flex;
  align-items: center;
  padding: 0 8px;
  transition: width 0.3s ease;
  min-width: fit-content;
}

.duration-text {
  color: white;
  font-size: 0.8rem;
  white-space: nowrap;
}

.subvention-duration {
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.9rem;
}

.activate-btn {
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 8px 16px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.2s;
}

.activate-btn:hover:not(:disabled) {
  background-color: #45a049;
}

.activate-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.no-subventions {
  color: rgba(255, 255, 255, 0.6);
  text-align: center;
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.1);
  border-radius: 6px;
}

.available {
  cursor: pointer;
  transition: background-color 0.2s;
}

.available:hover:not(.disabled) {
  background-color: rgba(255, 255, 255, 0.2);
}

.disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

@media (max-width: 768px) {
  .subventions-container {
    max-width: 100%;
  }

  .subvention-details {
    flex-direction: column;
    align-items: flex-start;
  }

  .duration-bar-container {
    width: 100%;
  }
}
</style>