<template>
  <div class="container">
    <div class="mission-steps-slider">
      <MissionStepsSlider />
    </div>

    <div class="spacer"></div>

    <div class="system-metrics">
      <FlightMetrics />
    </div>
  </div>
</template>

<script>
import MissionStepsSlider from '../components/Fusee/MissionStepsSlider.vue'
import FlightMetrics from '../components/Fusee/FlightMetrics.vue'

export default {
  components: {
    MissionStepsSlider,
    FlightMetrics
  }
}
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  height: 100vh;  /* Hauteur de la fenêtre de l'écran */
}

/* MissionStepsSlider en haut */
.mission-steps-slider {
  background-color: #333;
  color: white;
  padding: 10px;
  width: 100%;
  text-align: center; /* Centrer le contenu du slider */
  position: relative;
}

/* Espace vide entre le slider et le system metrics (permet de pousser le system metrics vers le bas) */
.spacer {
  flex-grow: 1;
}

/* FlightMetrics centré en bas */
.system-metrics {
  display: flex;
  justify-content: center;  /* Centrer horizontalement */
  align-items: center;      /* Centrer verticalement dans l'espace disponible */
  padding: 20px;
  background-color: #f4f4f4;
  width: 100%;
}
</style>
