<template>
  <div class="flight-display">
    <div class="metrics-icon">
      <div class="circles-grid">
        <div v-for="n in missionData.fusee?.boosterPrincipal?.nombreMoteurs || 0" :key="n" class="circle"></div>
      </div>
    </div>

    <div class="metrics-container">
      <div class="metrics">
        <div class="metric">
          <span class="label">SPEED</span>
          <span class="value">{{ Math.floor(missionData.fusee?.vitesse || 0) }} KM/H</span>
        </div>
        <div class="metric">
          <span class="label">ALTITUDE</span>
          <span class="value">{{ Math.floor(missionData.fusee?.altitude || 0) }} KM</span>
        </div>
        <div class="metric">
          <span class="label">STATUS</span>
          <span class="value">{{ missionData.statutMission || 'N/A' }}</span>
        </div>
      </div>
    </div>

    <div class="mission-timer">
      <span>{{ timerDisplay }}</span>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FlightMetrics',
  data() {
    return {
      missionData: {
        fusee: {
          vitesse: 0,
          altitude: 0,
          boosterPrincipal: null 
        },
        statutMission: 'N/A',
        dateHeureLancement: null
      },
      now: null,
      websocket: null,
    };
  },
  computed: {
  timerDisplay() {
    if (!this.missionData.dateHeureLancement || !this.now) return 'T+00:00:00';

    const now = new Date(this.now); 
    const missionDate = new Date(this.missionData.dateHeureLancement); 

    // Calcul de la différence en millisecondes
    const timeDiff = missionDate - now;

    // Calcul de l'heure, minute, et seconde de la différence
    const absoluteDiff = Math.abs(timeDiff);
    const hours = Math.floor(absoluteDiff / (1000 * 60 * 60));
    const minutes = Math.floor((absoluteDiff % (1000 * 60 * 60)) / (1000 * 60));
    const seconds = Math.floor((absoluteDiff % (1000 * 60)) / 1000);

    // Formatage du temps
    const timeString = `${String(hours).padStart(2, '0')}:${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;

    // Affichage du temps avant ou après la mission
    if (timeDiff > 0) {
      return `T-${timeString}`;
    }

    return `T+${timeString}`;
  }
}
,
  mounted() {
    this.connectWebSocket();
    setInterval(() => {
      this.$forceUpdate();
    }, 1000);
  },
  beforeUnmount() {
    if (this.websocket) {
      this.websocket.close();
    }
  },
  methods: {
    connectWebSocket() {
      this.websocket = new WebSocket('ws://localhost:3232');

      this.websocket.onopen = () => {
        setInterval(() => {
          this.websocket.send(JSON.stringify({ action: 'getMissionState' }));
        }, 100);
      };

      this.websocket.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          if (data.action === 'missionsState' && data.missions.length > 0) {
            const mission = data.missions[0];
            this.missionData = {
              ...this.missionData,
              fusee: {
                vitesse: mission.fusee?.vitesse || 0,
                altitude: mission.fusee?.altitude || 0,
                boosterPrincipal: mission.fusee?.boosterPrincipal || null // Récupérer le booster
              },
              statutMission: mission.statutMission || 'N/A',
              dateHeureLancement: mission.dateHeureLancement
            };
          }

          // Mettre à jour la date actuelle
          if (data.date) {
            this.now = data.date;
          }
        } catch (error) {
          console.error('Error processing WebSocket data:', error);
        }
      };

      this.websocket.onerror = (error) => {
        console.error('WebSocket error:', error);
      };
    }
  }
};
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

.mission-timer {
  font-size: 20px;
  font-weight: bold;
  min-width: 150px;
  text-align: right;
}
</style>
