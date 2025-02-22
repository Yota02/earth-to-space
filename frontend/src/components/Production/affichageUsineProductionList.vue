<template>
  <div class="production-container">
    <h2>Usines de Production</h2>

    <div class="usines-list">
      <div v-for="usine in usinesProduction" :key="usine.nom" class="usine-card">
        <h3>{{ usine.nom }}</h3>
        <div class="usine-details">
          <p><strong>Superficie:</strong> {{ usine.superficie }} m²</p>
          <p><strong>Temps de construction:</strong> {{ usine.tempsConstruction }} jours</p>
          <p><strong>Matériaux requis:</strong> {{ usine.materiauxEnEntree }}</p>
          
          <div class="production-section">
            <p><strong>Production:</strong></p>
            <select :value="usine.pieceProduite" @change="updateProduction($event, usine)">
              <option v-for="piece in piecesDisponibles" :key="piece" :value="piece">
                {{ piece }}
              </option>
            </select>
            <p class="selected-piece">Sélectionné : <strong>{{ usine.pieceProduite }}</strong></p>
          </div>
          
          <p><strong>Quantité produite:</strong> {{ usine.quantiteProduite }} unités</p>
        </div>
      </div>
    </div>

    <div v-if="usinesProduction.length === 0" class="no-data">
      Aucune usine de production disponible
    </div>
  </div>
</template>

<script>
export default {
  name: "ProductionView",
  data() {
    return {
      usinesProduction: [],
      socket: null,
      updateInterval: null,
      piecesDisponibles: []
    };
  },
  methods: {
    updateProduction(event, usine) {
      const nouvellePiece = event.target.value;
      usine.pieceProduite = nouvellePiece; // Mettre à jour localement pour un affichage instantané
      if (this.socket?.readyState === WebSocket.OPEN) {
        this.socket.send(
          JSON.stringify({
            action: "updatePieceProduction",
            usineName: usine.nom,
            nouvellePiece
          })
        );
      }
    },
    handleWebSocketMessage(event) {
      try {
        const data = JSON.parse(event.data);
        if (data.action === "productionState") {
          this.usinesProduction = data.production;
        } else if (data.action === "pieceFuseeList") {
          this.piecesDisponibles = data.pieces;
        }
      } catch (error) {
        console.error("Erreur WebSocket:", error);
      }
    },
    updateProductionState() {
      this.socket.send(JSON.stringify({ action: "getProduction" }));
    }
  },
  mounted() {
    this.socket = new WebSocket("ws://localhost:3232");
    this.socket.onmessage = this.handleWebSocketMessage;
    this.socket.onopen = () => {
      this.updateProductionState();
      this.socket.send(JSON.stringify({ action: "getPieceFuseeList" }));
      this.updateInterval = setInterval(this.updateProductionState, 30000);
    };
  },
  beforeUnmount() {
    if (this.socket) this.socket.close();
    if (this.updateInterval) clearInterval(this.updateInterval);
  }
};
</script>

<style scoped>
.production-container {
  max-width: 900px;
  margin: auto;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  color: #333;
}

.usines-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}

.usine-card {
  background: white;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  width: 280px;
  text-align: center;
  transition: transform 0.2s;
}

.usine-card:hover {
  transform: scale(1.05);
}

.usine-details {
  margin-top: 10px;
}

.production-section {
  margin: 10px 0;
}

.selected-piece {
  font-size: 14px;
  color: #555;
  margin-top: 5px;
}

p {
  margin: 5px 0;
  color: #555;
}

select {
  width: 100%;
  padding: 8px;
  margin-top: 5px;
  border-radius: 4px;
  border: 1px solid #ccc;
  background: #f8f9fa;
}

.no-data {
  text-align: center;
  font-size: 16px;
  color: #888;
}
</style>