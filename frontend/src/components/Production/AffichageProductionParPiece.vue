<template>
  <div class="production-par-piece">
    <h3>Stock total de pièces</h3>
    <div class="stock-total" v-if="totalStock">
      <div class="stockage-info">
        <div>Capacité totale: {{ totalStock.capaciteTotal }}</div>
        <div>Stockage total actuel: {{ totalStock.stockageTotal }}</div>
      </div>
      <div class="pieces-list">
        <div v-for="(quantite, piece) in totalStock.pieces" :key="piece" class="piece-item">
          <img :src="getPieceImage(piece)" :alt="formatPieceType(piece)" class="piece-image">
          <div class="piece-type">{{ formatPieceType(piece) }}</div>
          <div class="piece-quantity">{{ quantite }} unités</div>
        </div>
      </div>
    </div>
    <div v-else class="no-data">
      Aucune donnée de stockage disponible
    </div>
  </div>
</template>

<script>
export default {
  name: 'AffichageProductionParPiece',
  data() {
    return {
      pieces: [],
      socket: null,
      totalStock: null
    }
  },
  methods: {
    formatPieceType(type) {
      const typeMap = {
        'MOTEUR': 'Moteurs',
        'RESERVOIR': 'Réservoirs',
        'COQUE': 'Coques'
      }
      return typeMap[type] || type
    },
    getPieceImage(type) {
      const imageMap = {
        'MOTEUR': 'src/assets/img/icone/pieceFusee/tuyere.png',
        'RESERVOIR': 'src/assets/img/icone/pieceFusee/reservoir.png',
        'COQUE': 'src/assets/img/icone/pieceFusee/coque.png',
        'PANNEAUX_SOLAIRES': 'src/assets/img/icone/pieceFusee/panneau_solaire.png',
        'REACTEUR': 'src/assets/img/icone/pieceFusee/reacteur.png',
        'CAPTEUR': 'src/assets/img/icone/pieceFusee/capteur.png',
        'HABITAT': 'src/assets/img/icone/pieceFusee/habitat.png',
        'MODULES_VAISSEAU': 'src/assets/img/icone/pieceFusee/station.png'
      }
      return imageMap[type] || '/images/pieces/default.png'
    },
    connectWebSocket() {
      this.socket = new WebSocket('ws://localhost:3232')
      this.socket.onmessage = (event) => this.handleWebSocketMessage(event)
      this.socket.onopen = () => this.updateProductionState()
    },
    updateProductionState() {
      if (this.socket && this.socket.readyState === WebSocket.OPEN) {
        this.socket.send(JSON.stringify({
          action: "getProductionParPiece"
        }))
      }
    },
    handleWebSocketMessage(event) {
      try {
        const data = JSON.parse(event.data)
        if (data.action === "productionParPieceState") {
          const pieces = data.pieces || []
          this.calculerTotalStock(pieces)
        }
      } catch (error) {
        console.error("Erreur lors du traitement des données WebSocket:", error)
      }
    },
    calculerTotalStock(pieces) {
      const total = {
        capaciteTotal: 0,
        stockageTotal: 0,
        pieces: {}
      }

      pieces.forEach(batiment => {
        if (batiment.operationnel) {
          total.capaciteTotal += batiment.capaciteStockage
          total.stockageTotal += batiment.stockageActuel
          
          if (batiment.stockage) {
            Object.entries(batiment.stockage).forEach(([piece, quantite]) => {
              total.pieces[piece] = (total.pieces[piece] || 0) + quantite
            })
          }
        }
      })

      this.totalStock = total
    }
  },
  mounted() {
    this.connectWebSocket()
  },
  beforeUnmount() {
    if (this.socket) {
      this.socket.close()
    }
  }
}
</script>

<style scoped>
.piece-image {
  width: 64px;
  height: 64px;
  object-fit: contain;
  margin-bottom: 10px;
}

.piece-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: #333333;
  border-radius: 6px;
  padding: 15px;
  text-align: center;
  transition: transform 0.2s;
}

.piece-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.production-par-piece {
  background-color: #1a1a1a;
  border-radius: 8px;
  padding: 20px;
  margin: 15px;
}

.stock-total {
  background-color: #2a2a2a;
  border-radius: 6px;
  padding: 15px;
}

.stockage-info {
  color: #4a9eff;
  margin: 10px 0;
  font-size: 1.1em;
}

.pieces-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 10px;
  margin-top: 15px;
}

@media (max-width: 1024px) {
  .pieces-list {
    grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  }
}

.piece-item {
  background-color: #333333;
  border-radius: 6px;
  padding: 15px;
  text-align: center;
}

.piece-type {
  font-weight: bold;
  color: #4a9eff;
  margin-bottom: 8px;
}

.piece-quantity {
  color: #ffffff;
}

.no-data {
  text-align: center;
  color: #666;
  margin-top: 20px;
}

h3 {
  color: #ffffff;
  margin-bottom: 15px;
}
</style>