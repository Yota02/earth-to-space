<template>
    <div class="mission-slider">
      <div class="slider-container">
        <transition-group name="slide">
          <h3 
            v-for="(step, index) in steps" 
            :key="step"
            v-show="currentStep === index"
            class="step-text"
          >
            {{ step }}
          </h3>
        </transition-group>
      </div>
      <div class="progress-bar"></div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'MissionStepsSlider',
    data() {
      return {
        currentStep: 0,
        steps: [
          'Préparation du lancement T-00:05:16',
          'Vérification des systèmes T-00:04:00',
          'Initialisation moteurs T-00:03:00',
          'Séquence finale T-00:01:00',
          'Décollage T-00:00:00'
        ]
      }
    },
    mounted() {
      this.startSlideshow()
    },
    beforeUnmount() {
      clearInterval(this.intervalId)
    },
    methods: {
      startSlideshow() {
        this.intervalId = setInterval(() => {
          this.currentStep = (this.currentStep + 1) % this.steps.length
        }, 50000)
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