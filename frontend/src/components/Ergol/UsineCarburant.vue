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
                  :style="{ filter: `drop-shadow(0 0 10px ${getUsineColor(usine)})` }"
                  alt="Usine de carburant"
                />
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
// Le script reste identique
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
  background-color: gray;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.usine-list {
  width: 100%;
  height: 100%;
  max-height: 300px;
  display: flex;
  align-items: center; /* Centre verticalement */
  gap: 0.25rem;
}

.usine-list-horizontal {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(25px, 1fr)); /* Réduit encore la taille minimale */
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
}

.usine-image {
  width: 20px;
  height: 20px;
  object-fit: contain;
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