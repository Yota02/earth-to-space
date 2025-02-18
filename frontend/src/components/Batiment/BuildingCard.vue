<template>
  <button 
    class="card"
    @click="handleClick"
    :disabled="!canAfford"
    :class="{'disabled': !canAfford}"
  >
    <h3 class="title">{{ building.nom }}</h3>
    <p class="cost">Coût: {{ building.cout }} €</p>
    <p>Capacité: {{ building.capacite }}</p>
    <p>Type: {{ building.type }}</p>
    <p>Superficie: {{ building.superficie }} m²</p>
    <p>Temps de construction: {{ building.tempsConstruction }} mois</p>
  </button>
</template>

<script>
export default {
  name: 'BuildingCard',
  props: {
    building: {
      type: Object,
      required: true
    },
    canAfford: {
      type: Boolean,
      required: true
    }
  },
  emits: ['purchase', 'click'],
  methods: {
    handleClick() {
      this.$emit('click');
    },
    handlePurchase() {
      if (!this.canAfford) {
        console.warn(`❌ [BuildingCard] Achat impossible, fonds insuffisants pour: ${this.building.nom}`);
        return;
      }
      this.$emit('purchase', this.building);
    }
  }
}
</script>

<style scoped>
.card {
  width: 100%;
  padding: 1rem;
  border: 1px solid #ddd;
  border-radius: 0.5rem;
  background: white;
  text-align: left;
  cursor: pointer;
  transition: background 0.2s ease;
}

.card:not(.disabled):hover {
  background-color: #f3f4f6;
}

.card:active {
  background-color: #e5e7eb;
}

.card.disabled {
  background-color: #d1d5db;
  cursor: not-allowed;
}
</style>
