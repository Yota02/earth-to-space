<template>
    <div class="objectives-page">
      <h1>Objectifs</h1>
      <div class="objectives-container">
        <!-- Active Objectives -->
        <div class="objectives-section">
          <h3 class="objectives-title">Objectifs en cours</h3>
          <div class="objectives-list">
            <div v-for="objective in activeObjectives" :key="objective.titre" class="objective-item">
              <div class="objective-header">
                <span class="objective-title">{{ objective.titre }}</span>
                <span class="objective-owner" :class="objective.proprietaire.toLowerCase()">
                  {{ objective.proprietaire }}
                </span>
              </div>
              
              <div class="objective-body">
                <p class="objective-description">{{ objective.description }}</p>
                
                <div class="objective-details">
                  <div class="progress-container">
                    <div class="progress-bar" :style="{ width: `${objective.progression}%` }">
                      <span class="progress-text">{{ objective.progression }}%</span>
                    </div>
                  </div>
                  
                  <div class="objective-stats">
                    <div class="difficulty">
                      Difficulté:
                      <span class="stars">
                        <span v-for="n in objective.difficulte" :key="n">⭐</span>
                      </span>
                    </div>
                    <div class="target-date">
                      Date cible: {{ formatDate(objective.dateCible) }}
                    </div>
                  </div>
                  
                  <div class="effects-grid">
                    <div class="effect-item" v-if="objective.effet">
                      <span class="effect-label">Effet:</span>
                      <span class="effect-value">{{ objective.effet }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
  
        <!-- Completed Objectives -->
        <div class="objectives-section">
          <h3 class="objectives-title">Objectifs terminés</h3>
          <div class="objectives-list">
            <div v-for="objective in completedObjectives" :key="objective.titre" class="objective-item completed">
              <div class="objective-header">
                <span class="objective-title">{{ objective.titre }}</span>
                <span class="objective-status">✓ Terminé</span>
              </div>
              <div class="objective-body">
                <p class="objective-description">{{ objective.description }}</p>
                <div class="completion-date">
                  Terminé le: {{ formatDate(objective.dateCible) }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'ObjectifsView',
    data() {
      return {
        objectives: [],
        socket: null,
        updateInterval: null
      };
    },
    computed: {
      activeObjectives() {
        return this.objectives.filter(obj => !obj.terminee);
      },
      completedObjectives() {
        return this.objectives.filter(obj => obj.terminee);
      }
    },
    methods: {
      formatDate(dateString) {
        const date = new Date(dateString);
        return new Intl.DateTimeFormat('fr-FR', {
          year: 'numeric',
          month: 'long',
          day: 'numeric'
        }).format(date);
      },
  
      updateObjectivesState() {
        this.socket.send(JSON.stringify({
          type: "getObjectifsState"
        }));
      },
  
      handleWebSocketMessage(event) {
        try {
          const data = JSON.parse(event.data);
          if (data.action === "getObjectifsState") {
            this.objectives = data.objectifs;
          }
        } catch (error) {
          console.error("Erreur lors du traitement des données WebSocket:", error);
        }
      }
    },
    mounted() {
      this.socket = new WebSocket("ws://localhost:3232");
      this.socket.onmessage = this.handleWebSocketMessage;
  
      this.socket.onopen = () => {
        this.updateObjectivesState();
        this.updateInterval = setInterval(() => {
          this.updateObjectivesState();
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
  .objectives-page {
    padding: 2rem;
  }
  
  .objectives-container {
    display: flex;
    flex-direction: column;
    gap: 2rem;
  }
  
  .objectives-section {
    background: white;
    border-radius: 8px;
    padding: 1.5rem;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  }
  
  .objectives-title {
    font-size: 1.5rem;
    margin-bottom: 1.5rem;
    color: #2c3e50;
  }
  
  .objective-item {
    background: #f8f9fa;
    border-radius: 6px;
    padding: 1.5rem;
    margin-bottom: 1rem;
  }
  
  .objective-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1rem;
  }
  
  .objective-title {
    font-size: 1.2rem;
    font-weight: bold;
    color: #2c3e50;
  }
  
  .objective-owner {
    padding: 0.3rem 0.8rem;
    border-radius: 15px;
    font-size: 0.9rem;
  }
  
  .nasa { background: #1e88e5; color: white; }
  .esa { background: #43a047; color: white; }
  .spacex { background: #d81b60; color: white; }
  .cnsa { background: #f4511e; color: white; }
  
  .objective-description {
    color: #546e7a;
    margin-bottom: 1rem;
    line-height: 1.5;
  }
  
  .progress-container {
    background: #e9ecef;
    border-radius: 4px;
    height: 20px;
    margin-bottom: 1rem;
    overflow: hidden;
  }
  
  .progress-bar {
    background: #4caf50;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: width 0.3s ease;
  }
  
  .progress-text {
    color: white;
    font-size: 0.8rem;
    font-weight: bold;
  }
  
  .objective-stats {
    display: flex;
    justify-content: space-between;
    margin-bottom: 1rem;
  }
  
  .difficulty, .target-date {
    font-size: 0.9rem;
    color: #546e7a;
  }
  
  .stars {
    color: #ffd700;
  }
  
  .effects-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 1rem;
    padding: 1rem;
    background: #f1f3f5;
    border-radius: 4px;
  }
  
  .effect-item {
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .effect-label {
    font-weight: bold;
    color: #2c3e50;
  }
  
  .effect-value {
    color: #546e7a;
  }
  
  .completed {
    opacity: 0.8;
  }
  
  .objective-status {
    color: #4caf50;
    font-weight: bold;
  }
  
  .completion-date {
    color: #78909c;
    font-size: 0.9rem;
    font-style: italic;
  }
  </style>