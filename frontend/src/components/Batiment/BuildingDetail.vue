<template>
  <div class="building-detail-container" v-if="selectedBuilding">
    <div class="building-detail">
      <div class="building-image-container">
        <img :src="getBuildingImage(selectedBuilding.type)" :alt="selectedBuilding.nom" class="building-image">
      </div>

      <div class="building-info">
        <h2>{{ selectedBuilding.nom }}</h2>
        <div class="building-metadata">
          <p><strong>Type : </strong>{{ selectedBuilding.type }}</p>
          <p><strong>Superficie : </strong>{{ selectedBuilding.superficie }} m²</p>
          <p><strong>Coût : </strong>{{ selectedBuilding.cout }} €</p>
        </div>

        <div class="building-description">
          <h3>Description</h3>
          <p>{{ selectedBuilding.description || 'Aucune description disponible.' }}</p>
        </div>

        <div class="building-effects" v-if="selectedBuilding.effets">
          <h3>Effets</h3>
          <div class="effect-item" v-for="(effet, index) in selectedBuilding.effets" :key="index">
            {{ effet }}
          </div>
        </div>

        <div class="building-actions">
          <button @click="handleBuyBatiment" :disabled="!canAfford" class="purchase-button">
            {{ canAfford ? 'Commencer la construction' : 'Fonds insuffisants' }}
          </button>
        </div>
      </div>
    </div>
  </div>
  <div v-else class="no-selection">
    <p>Sélectionnez un bâtiment pour voir les détails</p>
  </div>
</template>

<script>
export default {
  name: 'BuildingDetail',
  props: {
    selectedBuilding: {
      type: Object,
      default: null
    },
    canAfford: {
      type: Boolean,
      required: true
    }
  },
  methods: {
    getBuildingImage(type) {
      const images = {
        'USINE': 'src/assets/img/batiments/usine.jpg',
        'HANGAR': 'src/assets/img/batiments/hangar.jpg',
        'LABORATOIRE': 'src/assets/img/batiments/laboratoire.jpg',
        // Ajoutez d'autres types de bâtiments ici
        'DEFAULT': 'src/assets/img/batiments/default.jpg'
      }
      return images[type] || images.DEFAULT
    },
    handleBuyBatiment() {
      if (!this.canAfford || !this.selectedBuilding) return;

      const batimentData = {
        action: 'buyBatiment',
        batiment: {
          nom: this.selectedBuilding.nom,
          type: this.selectedBuilding.type,
          superficie: this.selectedBuilding.superficie,
          cout: this.selectedBuilding.cout
        }
      };

      this.$emit('purchase', batimentData);
    }
  }
}
</script>

<style scoped>
.building-detail-container {
  background-color: #f4f4f4;
  border-radius: 10px;
  padding: 20px;
  width: 400px;
  max-height: 80vh;
  overflow-y: auto;
}

.building-image-container {
  margin-bottom: 15px;
}

.building-image {
  width: 100%;
  height: 250px;
  object-fit: cover;
  border-radius: 10px;
}

.building-info {
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.building-info h2 {
  font-size: 1.5rem;
  color: #333;
  margin-bottom: 10px;
}

.building-metadata p {
  font-size: 0.95rem;
  color: #666;
  margin-bottom: 5px;
}

.building-description h3,
.building-effects h3 {
  font-size: 1.2rem;
  color: #444;
  margin-top: 15px;
  border-bottom: 2px solid #ddd;
  padding-bottom: 5px;
}

.building-actions {
  margin-top: 20px;
}

.purchase-button {
  width: 100%;
  padding: 10px;
  border: none;
  border-radius: 5px;
  background-color: #4CAF50;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

.purchase-button:hover:not(:disabled) {
  background-color: #45a049;
}

.purchase-button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.no-selection {
  color: #666;
  text-align: center;
  padding: 20px;
}

.effect-item {
  background-color: #f8f9fa;
  padding: 8px;
  margin: 5px 0;
  border-radius: 4px;
  font-size: 0.9rem;
}
</style>