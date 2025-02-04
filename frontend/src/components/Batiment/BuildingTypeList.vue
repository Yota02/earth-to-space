<template>
  <section class="building-list">
    <h1 class="title">Marché des Bâtiments</h1>

    <!-- Liste des bâtiments -->
    <section class="building-grid">
      <BuildingCard 
        v-for="building in filteredBuildings" 
        :key="building.nom" 
        :building="building"
        :can-afford="canAfford(building)" 
        @purchase="attemptPurchase(building)" 
      />
    </section>

    <!-- Message si aucun bâtiment disponible -->
    <section v-if="filteredBuildings.length === 0" class="no-buildings">
      Aucun bâtiment disponible {{ selectedType ? `de type ${selectedType}` : '' }}.
    </section>
  </section>
</template>

<script>
import BuildingCard from './BuildingCard.vue';

export default {
  name: 'BuildingTypeList',
  components: {
    BuildingCard
  },
  props: {
    buildings: {
      type: Array,
      required: true
    },
    money: {
      type: Number,
      required: true
    },
    types: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      selectedType: ''
    };
  },
  computed: {
    filteredBuildings() {
      if (!this.selectedType) return this.buildings;
      return this.buildings.filter(b => b.type === this.selectedType);
    }
  },
  methods: {
    canAfford(building) {
      return this.money >= building.cout;
    },
    attemptPurchase(building) {
      if (!this.canAfford(building)) {
        console.warn(`❌ [BuildingTypeList] Fonds insuffisants pour acheter ${building.nom}`);
        return;
      }

      this.$emit('purchase', building);
    },
    onTypeChange() {
      console.log(`🔄 [BuildingTypeList] Changement de type: ${this.selectedType}`);
      this.$emit('type-change', this.selectedType);
    }
  },
  emits: ['purchase', 'type-change']
};
</script>

<style scoped>
.building-list {
  width: 100%;
  background: white;
  border-radius: 0.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 1rem;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.title {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
}

.filter-section {
  display: flex;
  justify-content: flex-start;
  margin-bottom: 1rem;
  gap: 1rem;
}

.building-grid {
  display: grid;
  gap: 1rem;
  grid-template-columns: repeat(2, 1fr);
}

.no-buildings {
  color: #6b7280;
  text-align: center;
  padding: 1rem;
}
</style>