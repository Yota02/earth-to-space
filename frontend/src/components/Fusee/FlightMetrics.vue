<template>
    <div class="flight-display">
      <!-- Partie gauche avec l'icône circulaire -->
      <div class="metrics-icon">
        <div class="circles-grid">
          <div v-for="n in 20" :key="n" class="circle"></div>
        </div>
      </div>
      
      <!-- Métriques centrales -->
      <div class="metrics-container">
        <div class="metrics">
          <div class="metric">
            <span class="label">SPEED</span>
            <span class="value">{{ speed }} KM/H</span>
          </div>
          <div class="metric">
            <span class="label">ALTITUDE</span>
            <span class="value">{{ altitude }} KM</span>
          </div>
        </div>
      </div>
  
      <!-- Timer à droite -->
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
        speed: 1785,
        altitude: 17,
        progress: 30,
        currentTime: '14:42',
        totalTime: '17:08',
        missionTime: '00:01:27'
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