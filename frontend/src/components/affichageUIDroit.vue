<template>
  <div class="game-ui-nav">
    <!-- Section de la date à gauche -->
    <div class="date-section">
      <p class="date-value">{{ formattedDate }}</p>
      <div class="progress-container">
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: yearProgress + '%' }"></div>
        </div>
      </div>
    </div>

    <!-- Section des stats à droite -->
    <div class="stats-section">
      <div class="stat-column">
        <div class="stat-item">
          <img src="../assets/img/icone/dollar.png" alt="dollars" class="icon" />
          <p class="stat-value">{{ gameData.argent }}</p>
        </div>
        <div class="stat-item">
          <img src="../assets/img/icone/recherche.png" alt="recherche" class="icon" />
          <p class="stat-value">{{ gameData.pointRecherche }}</p>
        </div>
      </div>
      <div class="stat-column">
        <div class="stat-item">
          <img src="../assets/img/icone/construction.png" alt="construction" class="icon" />
          <p class="stat-value">{{ gameData.pointConstruction }}</p>
        </div>
        <div class="stat-item">
          <img src="../assets/img/icone/ingenieur.png" alt="ingenieur" class="icon" />
          <p class="stat-value">{{ gameData.pointIngenieur }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "GameUI",
  data() {
    return {
      gameData: {
        argent: 0,
        date: null,
        pointRecherche: 0,
        pointConstruction: 0,
        pointIngenieur: 0
      },
      socket: null,
    };
  },
  computed: {
    formattedDate() {
      if (!this.gameData.date) return "Date non disponible";
      const date = new Date(this.gameData.date);
      return date.toLocaleDateString("fr-FR", {
        year: "numeric",
        month: "long",
        day: "numeric",
      });
    },
    yearProgress() {
      if (!this.gameData.date) return 0;
      const currentDate = new Date(this.gameData.date);
      const startOfYear = new Date(currentDate.getFullYear(), 0, 1);
      const endOfYear = new Date(currentDate.getFullYear(), 11, 31);
      const yearDuration = endOfYear - startOfYear;
      const elapsedTime = currentDate - startOfYear;
      return Math.floor((elapsedTime / yearDuration) * 100);
    },
  },
  mounted() {
    this.socket = new WebSocket("ws://localhost:3232");

    this.socket.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        this.gameData = {
          ...this.gameData,
          ...data
        };
      } catch (error) {
        console.error("Erreur lors du traitement des données WebSocket :", error);
      }
    };

    this.socket.onclose = () => {
      console.log("WebSocket fermé.");
    };
  },
  beforeUnmount() {
    if (this.socket) {
      this.socket.close();
    }
  },
};
</script>

<style scoped>
.game-ui-nav {
  display: flex;
  align-items: center;
  gap: 20px;
  height: 100%;
  padding: 0 10px;
}

/* Section de la date */
.date-section {
  display: flex;
  flex-direction: column;
  justify-content: center;
  gap: 4px;
  min-width: 180px;
}

.date-value {
  font-size: 0.9rem;
  font-weight: bold;
  color: white;
  margin: 0;
  white-space: nowrap;
}

.progress-container {
  width: 100%;
}

.progress-bar {
  width: 100%;
  height: 4px;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 2px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background-color: #4caf50;
  transition: width 0.3s ease;
}

/* Section des stats */
.stats-section {
  display: flex;
  gap: 20px;
}

.stat-column {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.icon {
  width: 16px;
  height: 16px;
  object-fit: contain;
}

.stat-value {
  font-size: 0.9rem;
  font-weight: bold;
  color: white;
  margin: 0;
}

@media (max-width: 768px) {
  .game-ui-nav {
    flex-direction: column;
    gap: 8px;
    padding: 8px;
  }

  .date-section {
    min-width: unset;
    width: 100%;
  }

  .stats-section {
    width: 100%;
    justify-content: space-between;
  }
}
</style>