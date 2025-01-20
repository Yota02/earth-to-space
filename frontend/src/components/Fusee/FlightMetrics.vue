<template>
  <div class="flight-display">
    <div class="metrics-icon">
      <div class="circles-grid">
        <div v-for="n in 20" :key="n" class="circle"></div>
      </div>
    </div>

    <div class="metrics-container">
      <div class="metrics">
        <div class="metric">
          <span class="label">SPEED</span>
          <span class="value">{{ missionData.fusee?.vitesse || 0 }} KM/H</span> <!-- Affichage de la vitesse -->
        </div>
        <div class="metric">
          <span class="label">ALTITUDE</span>
          <span class="value">{{ missionData.fusee?.altitude || 0 }} KM</span> <!-- Affichage de l'altitude -->
        </div>
        <div class="metric">
          <span class="label">STATUS</span>
          <span class="value">{{ missionData.statutMission || 'N/A' }}</span> <!-- Affichage du statut -->
        </div>
      </div>
    </div>

    <div class="mission-timer">
      <span>T+{{ missionTime }}</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FlightMetrics',
  data() {
    return {
      missionData: {},  // Données de la mission
      websocket: null,
      missionTime: '00:00:00',  // Timer de mission
      timer: null
    }
  },
  mounted() {
    this.connectWebSocket();
    this.startTimer();
  },
  beforeUnmount() {
    if (this.websocket) {
      this.websocket.close();
    }
    if (this.timer) {
      clearInterval(this.timer);
    }
  },
  methods: {
    connectWebSocket() {
      this.websocket = new WebSocket('ws://localhost:3232');

      this.websocket.onopen = () => {
        this.websocketInterval = setInterval(() => {
          this.websocket.send(JSON.stringify({ action: 'getMissionState' }));
        }, 500);
      };

      this.websocket.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          console.log(data); // Pour déboguer et voir les données reçues
          if (data.action === 'missionsState' && data.missions.length > 0) {
            const mission = data.missions[0];  // Récupérer la première mission
            this.missionData = {
              ...this.missionData,
              ...mission,  // Mise à jour avec les données de la mission
              fusee: {
                vitesse: mission.fusee?.vitesse || 0,   // Récupération de la vitesse
                altitude: mission.fusee?.altitude || 0   // Récupération de l'altitude
              }
            };
          }
        } catch (error) {
          console.error('Error processing WebSocket data:', error);
        }
      };


      this.websocket.onerror = (error) => {
        console.error('WebSocket error:', error);
      };
    },
    startTimer() {
      let seconds = 0;
      this.timer = setInterval(() => {
        seconds++;
        const hours = Math.floor(seconds / 3600);
        const minutes = Math.floor((seconds % 3600) / 60);
        const remainingSeconds = seconds % 60;
        this.missionTime = `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(remainingSeconds).padStart(2, '0')}`;
      }, 1000);
    }
  }
}
</script>


<style scoped>
.flight-display {
  background-color: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 20px;
  font-family: monospace;
}

.metrics-icon {
  width: 60px;
  height: 60px;
}

.circles-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2px;
}

.circle {
  width: 8px;
  height: 8px;
  background-color: white;
  border-radius: 50%;
}

.metrics-container {
  flex-grow: 1;
}

.metrics {
  display: flex;
  gap: 30px;
  margin-bottom: 10px;
}

.metric {
  display: flex;
  flex-direction: column;
}

.label {
  font-size: 12px;
  color: #888;
}

.value {
  font-size: 16px;
  font-weight: bold;
}

.progress-container {
  width: 100%;
}

.progress-bar {
  height: 4px;
  background-color: #333;
  position: relative;
  margin-bottom: 5px;
}

.progress-fill {
  height: 100%;
  background-color: rgb(255, 0, 0);
  position: absolute;
  left: 0;
  top: 0;
}

.progress-cursor {
  width: 12px;
  height: 12px;
  background-color: rgb(255, 0, 0);
  border-radius: 50%;
  position: absolute;
  top: 50%;
  transform: translate(-50%, -50%);
}

.time-info {
  font-size: 12px;
  color: #888;
}

.mission-timer {
  font-size: 20px;
  font-weight: bold;
  min-width: 150px;
  text-align: right;
}
</style>