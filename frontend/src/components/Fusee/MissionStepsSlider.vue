<template>
  <div class="mission-slider">
    <div class="slider-container">
      <transition-group name="slide">
        <div 
          v-for="(step, index) in sortedSteps" 
          :key="step.dateHeure"
          v-show="currentStepIndex === index"
          class="step-content"
        >
          <div class="step-time">{{ formatTime(step.dateHeure) }}</div>
          <div class="step-text">{{ step.description }}</div>
        </div>
      </transition-group>
    </div>
    <div class="progress-bar" :style="{ width: `${progress}%` }"></div>
  </div>
</template>

<script>
export default {
  name: 'MissionSteps',
  data() {
    return {
      websocket: null,
      currentStepIndex: 0,
      mission: null,
      gameDate: null,
      progress: 0,
      sortedSteps: []
    }
  },
  mounted() {
    this.connectWebSocket();
  },
  beforeDestroy() {
    if (this.websocket) {
      this.websocket.close();
    }
  },
  methods: {
    formatTime(date) {
      const d = new Date(date);
      return d.toLocaleTimeString('fr-FR', {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      });
    },
    connectWebSocket() {
      this.websocket = new WebSocket('ws://localhost:3232');

      this.websocket.onopen = () => {
        this.websocket.send(JSON.stringify({ action: 'getMissionSteps' }));
      };

      this.websocket.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          
          // Mise à jour de la date du jeu
          if (data.date) {
            this.gameDate = new Date(data.date);
            this.updateCurrentStep();
          }
          
          // Mise à jour des données de mission
          if (data.action === 'missionsState' && data.missions?.[0]) {
            this.mission = data.missions[0];
            if (this.mission.etapeMission) {
              this.sortedSteps = this.mission.etapeMission
                .map(step => ({
                  dateHeure: new Date(step.dateHeure),
                  description: step.description
                }))
                .sort((a, b) => a.dateHeure - b.dateHeure);
            }
          }
        } catch (error) {
          console.error('Error processing WebSocket data:', error);
        }
      };
    },
    updateCurrentStep() {
      if (!this.sortedSteps.length || !this.gameDate) return;

      // Trouver l'étape courante basée sur la date du jeu
      let newIndex = 0;
      for (let i = 0; i < this.sortedSteps.length; i++) {
        if (this.gameDate < this.sortedSteps[i].dateHeure) {
          break;
        }
        newIndex = i;
      }

      // Mettre à jour l'index de l'étape courante
      this.currentStepIndex = newIndex;

      // Calculer la progression jusqu'à la prochaine étape
      if (newIndex < this.sortedSteps.length - 1) {
        const currentStepTime = this.sortedSteps[newIndex].dateHeure.getTime();
        const nextStepTime = this.sortedSteps[newIndex + 1].dateHeure.getTime();
        const currentTime = this.gameDate.getTime();
        
        this.progress = ((currentTime - currentStepTime) / (nextStepTime - currentStepTime)) * 100;
      } else {
        this.progress = 100;
      }
    }
  }
}
</script>

<style scoped>
.mission-slider {
  position: relative;
  width: 100%;
  height: 80px; /* Augmenté pour accueillir le temps */
  background-color: black;
  overflow: hidden;
}

.slider-container {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.step-content {
  position: absolute;
  width: 100%;
  text-align: center;
  color: white;
  font-family: monospace;
}

.step-time {
  font-size: 1rem;
  color: #3b82f6;
  margin-bottom: 4px;
}

.step-text {
  font-size: 1.5rem;
  margin: 0;
}

.progress-bar {
  position: absolute;
  bottom: 0;
  left: 0;
  height: 4px;
  background-color: #3b82f6;
  transition: width 0.3s ease;
}

/* Animations */
.slide-enter-active,
.slide-leave-active {
  transition: transform 1s ease-in-out, opacity 1s ease-in-out;
}

.slide-enter-from {
  transform: translateX(100%);
  opacity: 0;
}

.slide-leave-to {
  transform: translateX(-100%);
  opacity: 0;
}
</style>