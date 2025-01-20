<template>
  <div class="mission-slider">
    <div class="slider-container">
      <transition-group name="slide">
        <h3 
          v-for="(step, index) in currentSteps" 
          :key="index" 
          v-show="currentStep === index"
          class="step-text"
        >
          {{ step }}
        </h3>
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
      currentStep: 0,
      currentSteps: [], // Tableau pour stocker les étapes de mission
      progress: 0,
      intervalId: null
    }
  },
  mounted() {
    this.connectWebSocket();
  },
  beforeUnmount() {
    if (this.websocket) {
      this.websocket.close();
    }
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  },
  methods: {
    connectWebSocket() {
      this.websocket = new WebSocket('ws://localhost:3232');

      this.websocket.onopen = () => {
        this.websocket.send(JSON.stringify({ action: 'getMissionSteps' })); // Demander les étapes de mission
      };

      this.websocket.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          if (data.action === 'missionsState') {  // Vérifier que l'action est "missionsState"
            const mission = data.missions[0];  // Prendre la première mission dans le tableau (vous pouvez ajuster selon vos besoins)
            this.currentSteps = mission.etapeMission || []; // Assurez-vous que etapeMission existe
            this.startStepProgression();
          }
        } catch (error) {
          console.error('Error processing WebSocket data:', error);
        }
      };
    },
    startStepProgression() {
      const stepDuration = 50000; // Durée de chaque étape
      const updateInterval = 100; // Intervalle de mise à jour
      let elapsed = 0;

      this.intervalId = setInterval(() => {
        elapsed += updateInterval;
        this.progress = (elapsed % stepDuration) / stepDuration * 100;
        
        if (elapsed % stepDuration === 0) {
          this.currentStep = (this.currentStep + 1) % this.currentSteps.length;
        }
      }, updateInterval);
    }
  }
}
</script>

  
  <style scoped>
  .mission-slider {
    position: relative;
    width: 100%;
    height: 64px;
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
  
  .step-text {
    position: absolute;
    width: 100%;
    text-align: center;
    color: white;
    font-family: monospace;
    font-size: 1.5rem;
    margin: 0;
  }
  
  .progress-bar {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 4px;
    background-color: #3b82f6;
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