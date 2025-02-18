<template>
  <div class="construction-list">
    <div class="construction-header">
      <h2>Constructions en cours</h2>
      <span class="construction-count">{{ buildings.length }}</span>
    </div>

    <div class="construction-items">
      <div v-for="building in buildings" 
           :key="building.nom" 
           class="construction-item">
        <div class="construction-info">
          <h3>{{ building.nom }}</h3>
          <p class="construction-type">{{ building.type }}</p>
          <div class="progress-bar">
            <div class="progress-fill" :style="{ width: `${building.progression}%` }"></div>
          </div>
          <p class="construction-time">{{ getTempsRestant(building) }}</p>
        </div>
      </div>

      <div v-if="buildings.length === 0" class="empty-state">
        Aucune construction en cours
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BuildingConstruction',
  props: {
    buildings: {
      type: Array,
      required: true
    }
  },
  methods: {
    getTempsRestant(building) {
      const progression = building.progression || 0;
      const tempsTotal = building.tempsConstruction;
      const tempsRestant = tempsTotal * (100 - progression) / 100;
      return `Temps restant: ${Math.ceil(tempsRestant)} mois`;
    }
  }
}
</script>

<style scoped>
.construction-list {
  height: 100%;
  padding: 0.75rem;
  background-color: white;
  border-radius: 0.5rem;
  display: flex;
  flex-direction: column;
  min-height: 0; /* Important pour le scroll */
}

.construction-header {
  flex-shrink: 0; /* Empêche le header de rétrécir */
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #e5e7eb;
}

.construction-header h2 {
  font-size: 1.25rem;
  font-weight: 600;
  color: #374151;
}

.construction-count {
  background-color: #3b82f6;
  color: white;
  padding: 0.25rem 0.75rem;
  border-radius: 9999px;
  font-size: 0.875rem;
  font-weight: 500;
}

.construction-items {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
  overflow-y: auto;
  flex: 1; /* Prend tout l'espace restant */
  min-height: 0; /* Important pour le scroll */
}

.construction-item {
  background-color: #f8fafc;
  border-radius: 0.5rem;
  padding: 0.75rem;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  flex-shrink: 0; /* Empêche les items de rétrécir */
}
.construction-info h3 {
  font-size: 1rem;
  font-weight: 600;
  color: #1f2937;
  margin-bottom: 0.25rem;
}

.construction-type {
  font-size: 0.875rem;
  color: #6b7280;
  margin-bottom: 0.75rem;
}

.progress-bar {
  width: 100%;
  height: 0.5rem;
  background-color: #e5e7eb;
  border-radius: 9999px;
  overflow: hidden;
  margin-bottom: 0.5rem;
}

.progress-fill {
  height: 100%;
  background-color: #3b82f6;
  transition: width 0.3s ease;
}

.construction-time {
  font-size: 0.875rem;
  color: #4b5563;
  text-align: right;
}

.empty-state {
  text-align: center;
  padding: 2rem;
  color: #6b7280;
  font-size: 0.875rem;
}
</style>