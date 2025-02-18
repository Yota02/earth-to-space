<template>
  <section class="building-list">
    <h1 class="title">Marché des Bâtiments</h1>

    <!-- Supprimer cette section qui est en double -->
    <!-- <BuildingCard 
    v-for="building in getBuildingsByType(type)"
    :key="building.nom"
    :building="building"
    :can-afford="canAfford(building)"
    @click="$emit('select-building', building)"
    @purchase="attemptPurchase(building)"
    /> -->

    <!-- Onglets des types de bâtiments -->
    <div class="tabs">
      <button 
        v-for="type in types" 
        :key="type"
        @click="selectedType = type"
        :class="['tab-button', { active: selectedType === type }]"
      >
        {{ type }}
      </button>
      <button 
        @click="selectedType = ''"
        :class="['tab-button', { active: selectedType === '' }]"
      >
        Tous
      </button>
    </div>

    <!-- Liste des bâtiments par type -->
    <div v-if="!selectedType" class="building-sections">
      <div v-for="type in types" :key="type" class="building-section">
        <h2 class="section-title">{{ type }}</h2>
        <div class="building-grid">
          <BuildingCard 
            v-for="building in getBuildingsByType(type)"
            :key="building.nom"
            :building="building"
            :can-afford="canAfford(building)"
            @click="$emit('select-building', building)"
            @purchase="attemptPurchase(building)"
          />
        </div>
      </div>
    </div>

    <!-- Liste filtrée pour un type spécifique -->
    <div v-else class="building-section">
      <div class="building-grid">
        <BuildingCard 
          v-for="building in filteredBuildings"
          :key="building.nom"
          :building="building"
          :can-afford="canAfford(building)"
          @click="$emit('select-building', building)"
          @purchase="attemptPurchase(building)"
        />
      </div>
    </div>

    <!-- Message si aucun bâtiment disponible -->
    <section v-if="filteredBuildings.length === 0" class="no-buildings">
      Aucun bâtiment disponible {{ selectedType ? `de type ${selectedType}` : '' }}.
    </section>
  </section>
</template>

<style scoped>
.building-list {
  width: 100%;
  background: white;
  border-radius: 0.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  padding: 1.5rem;
}

.title {
  font-size: 1.75rem;
  font-weight: bold;
  margin-bottom: 1.5rem;
  color: #2d3748;
  text-align: center;
}

.tabs {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
  flex-wrap: wrap;
  justify-content: center;
}

.tab-button {
  padding: 0.5rem 1rem;
  border-radius: 0.5rem;
  background-color: #f3f4f6;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
}

.tab-button.active {
  background-color: #3b82f6;
  color: white;
}

.tab-button:hover {
  background-color: #60a5fa;
  color: white;
}

.building-sections {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.building-section {
  background-color: #f8fafc;
  border-radius: 0.5rem;
  padding: 1rem;
}

.section-title {
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: 1rem;
  color: #4b5563;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #e5e7eb;
}

.building-grid {
  display: grid;
  gap: 1rem;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
}

.no-buildings {
  color: #6b7280;
  text-align: center;
  padding: 2rem;
  background-color: #f3f4f6;
  border-radius: 0.5rem;
  margin-top: 1rem;
}
</style>

<script>
import BuildingCard from './BuildingCard.vue';

export default {
  emits: ['purchase', 'type-change', 'select-building'],
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
    getBuildingsByType(type) {
      return this.buildings.filter(b => b.type === type);
    },
    canAfford(building) {
      return this.money >= building.cout;
    },
    attemptPurchase(building) {
      if (!this.canAfford(building)) {
        console.warn(`❌ [BuildingTypeList] Fonds insuffisants pour acheter ${building.nom}`);
        return;
      }
      this.$emit('purchase', building);
    }
  },
  emits: ['purchase', 'type-change']
};
</script>