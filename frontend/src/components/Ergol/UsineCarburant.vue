<template>
  <h2>Usine</h2>
  <div class="container">
    <div class="">
      <div v-if="connectionStatus === 'error'" class="error-message">
        Erreur de connexion au serveur. Veuillez rafraîchir la page.
      </div>

      <div v-else-if="connectionStatus === 'connecting'" class="connecting-message">
        Connexion au serveur...
      </div>

      <template v-else>
        <div class="usine-list">
          <div v-if="usinesCarburant.length > 0" class="usine-list-horizontal">
            <div v-for="usine in usinesCarburant" :key="usine.nom" class="usine-item">
              <div class="usine-image-container">
                <img 
                  src="../../assets/img/icone/usinecarburant.png" 
                  class="usine-image"
                  alt="Usine de carburant"
                />
                <div 
                  class="status-indicator"
                  :style="{ backgroundColor: getUsineColor(usine) }"
                ></div>
              </div>
            </div>
          </div>
          <div v-else class="no-usine-message">Aucune usine de carburant disponible.</div>
        </div>
      </template>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      ws: null,
      connectionStatus: 'connecting',
      usinesCarburant: []
    };
  },

  methods: {
    initWebSocket() {
      this.ws = new WebSocket('ws://localhost:3232');

      this.ws.onopen = () => {
        this.connectionStatus = 'connected';
        this.requestUsinesCarburant();
        setInterval(() => this.requestUsinesCarburant(), 1000);
      };

      this.ws.onclose = () => {
        this.connectionStatus = 'error';
      };

      this.ws.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          if (data.action === "usineCarburantsState") {
            this.usinesCarburant = data.usineCarburants;
          }
        } catch (error) {
          console.error('Erreur de réception:', error);
        }
      };
    },

    requestUsinesCarburant() {
      if (this.ws && this.ws.readyState === WebSocket.OPEN) {
        this.ws.send(JSON.stringify({ action: 'getUsineCarburants' }));
      }
    },

    getUsineColor(usine) {
      return usine.operationnel ? 'green' : usine.progression < 100 ? 'orange' : 'red';
    }
  },

  mounted() {
    this.initWebSocket();
  }
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 100%;
  height: 100%; 
  min-width: 0;
  max-width: 100px;
  margin: 0;
  margin-left: 5%;
  padding: 0.15rem; 
  box-sizing: border-box;
  background-color: transparent;
  border-radius: 8px;
  background-color: gray;
}

.usine-list {
  width: 100%;
  height: 100%;
  max-height: 300px;
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.usine-list-horizontal {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(25px, 1fr));
  gap: 0.15rem;
  width: 100%;
  padding: 0.15rem;
  box-sizing: border-box;
}

.usine-item {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0.1rem;
}

.usine-image-container {
  width: 25px;
  height: 25px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.usine-image {
  width: 20px;
  height: 20px;
  object-fit: contain;
}

.usine-image:hover {
  width: 23px;
  height: 23px;
  transition: 0.2s;
}

.status-indicator {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

/* Messages d'erreur et de connexion */
.error-message, 
.connecting-message,
.no-usine-message {
  text-align: center;
  color: white;
  font-size: 12px;
  padding: 0.25rem;
}
</style>