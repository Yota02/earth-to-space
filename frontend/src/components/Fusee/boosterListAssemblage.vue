<template>
  <div class="fusee-display-container">
    <h3 class="fusee-display-title">Fusées actives</h3>
    <div v-if="boosters.length > 0" class="fusee-grid">
      <div v-for="(booster, index) in boosters" :key="index" class="fusee-item">
        <div class="fusee-image-container">
          <img src="../../assets/img/icone/fusee.png" alt="Fusée" class="fusee-image">
          <!-- Pastille d'état -->
          <div class="fusee-status" :class="getEtatClass(booster.etat)"></div>
          <!-- Icône clé à molette si maintenance nécessaire -->
          <div v-if="booster.necessiteMaintenance" class="maintenance-icon">🔧</div>
        </div>
        <div class="fusee-name">{{ booster.nom || booster.name }}</div>
      </div>
    </div>
    <div v-else class="fusee-empty">
      <p>Aucune fusée active pour le moment</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FuseeDisplay',
  props: {
    boosters: {
      type: Array,
      required: true,
      default: () => []
    }
  },
  methods: {
    getEtatClass(etat) {
      switch (etat) {
        case 0: return 'etat-red'; // 🚨 Rouge
        case 1: return 'etat-orange'; // ⚠️ Orange
        case 2: return 'etat-green'; // ✅ Vert
        default: return 'etat-gray'; // 🟦 Si état inconnu
      }
    }
  }
}
</script>

<style>
.fusee-display-container {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 15px;
  margin-top: 20px;
}

.fusee-display-title {
  font-size: 1.2rem;
  margin-bottom: 15px;
  color: #333;
}

.fusee-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10px;
}

.fusee-empty {
  padding: 20px;
  text-align: center;
  color: #666;
}

.fusee-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
  border-radius: 5px;
  transition: background-color 0.2s;
  position: relative;
}

.fusee-item:hover {
  background-color: #f0f0f0;
}

.fusee-image-container {
  position: relative;
  display: inline-block;
}

.fusee-image {
  width: 40px;
  height: auto;
  margin-bottom: 5px;
}

/* Pastille d'état */
.fusee-status {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  position: absolute;
  top: -4px;
  right: -4px;
  border: 2px solid white; /* Contour propre */
}

.etat-red { background-color: red; }      /* 🚨 État 0 */
.etat-orange { background-color: orange; } /* ⚠️ État 1 */
.etat-green { background-color: green; }   /* ✅ État 2 */
.etat-gray { background-color: gray; }     /* 🟦 Inconnu */

/* Icône clé à molette */
.maintenance-icon {
  position: absolute;
  top: -4px;
  left: -4px;
  font-size: 14px;
  background-color: white;
  border-radius: 50%;
  padding: 2px;
}
</style>
