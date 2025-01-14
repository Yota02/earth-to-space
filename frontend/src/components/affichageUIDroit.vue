<template>
  <div class="game-ui-container">
    <!-- Section de la date -->
    <div class="date-container">
      <div class="date-display">
        <p class="value">{{ formattedDate }}</p>
      </div>
    
        <div class="progress-container">
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: yearProgress + '%' }"></div>
          </div>
        </div>
     </div>
    

    <div class="stats-container">
      <div class="info-block">
        <div class="info-line">
          <p class="value">{{ gameData.argent }}</p>
          <img src="../assets/img/icone/dollar.png" alt="dollars" class="argent-icon" />
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
          argent: data.argent,
          date: data.date,
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
.game-ui-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  max-width: 400px;
  margin: auto;
  padding: 16px;
}

.date-container {
  display: flex;
  padding: 5px;
  max-width: 380px;
  flex-direction: column;
  gap: 0;
  width: 100%;
  background-color: white;
  border-radius: 6px;
}

.date-display {
  text-align: center;
  margin-bottom: 0;
}

.progress-container {
  width: 100%;
  text-align: center;
  margin-top: 0; 
}

.progress-bar {
  width: 100%;
  height: 12px;
  background-color: #e0e0e0;
  border-radius: 6px;
  overflow: hidden;
  position: relative;
  margin-bottom: 5px;
}

.progress-fill {
  height: 100%;
  background-color: #4caf50;
  transition: width 0.3s ease;
}

.stats-container {
  margin-top: 5px;
  width: 100%;
  height: auto;
  max-width: 380px;
  background-color: white;
  border-radius: 6px;
  padding: 5px;
}

.info-block {
  text-align: center;
  margin-bottom: 16px;
}

.info-line {
  display: flex;
  align-items: center;
  justify-content: center;
}

.value {
  font-size: 1.5rem;
  font-weight: bold;
  color: #333;
}

.argent-icon {
  width: 24px;
  height: auto;
  margin-left: 8px;
}

@media (max-width: 600px) {
  .game-ui-container {
    max-width: 90%;
    padding: 16px;
  }
}
</style>