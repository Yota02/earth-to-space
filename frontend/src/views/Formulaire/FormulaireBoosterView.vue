<template>
  <div class="booster-form-container">
    <form @submit.prevent="submitBooster" class="booster-form">
      <h2>Créer un Booster</h2>

      <!-- Basic Specifications -->
      <div class="grid-container grid grid-cols-2 gap-4 mb-6">
        <div>
          <label class="block mb-2">Nom du Booster</label>
          <input
              v-model="booster.nom"
              type="text"
              class="w-full p-2 border rounded"
              placeholder="Nom du booster"
              required
          >
        </div>
        <div>
          <label class="block mb-2">Taille (m)</label>
          <input
              v-model.number="booster.taille"
              type="number"
              step="0.1"
              class="w-full p-2 border rounded"
              placeholder="Taille en mètres"
              required
          >
        </div>
        <div>
          <label class="block mb-2">Diamètre (m)</label>
          <input
              v-model.number="booster.diametre"
              type="number"
              step="0.1"
              class="w-full p-2 border rounded"
              placeholder="Diamètre en mètres"
              required
          >
        </div>
        <div>
          <label class="block mb-2">Poids à Vide (kg)</label>
          <input
              v-model.number="booster.poidsAVide"
              type="number"
              step="0.1"
              class="w-full p-2 border rounded"
              placeholder="Poids à vide en kg"
              required
          >
        </div>
        <div>
          <label class="block mb-2">Altitude Maximale (m)</label>
          <input
              v-model.number="booster.altitudeMax"
              type="number"
              class="w-full p-2 border rounded"
              placeholder="Altitude maximale"
              required
          >
        </div>
        <div>
          <label class="block mb-2">Vitesse Maximale (m/s)</label>
          <input
              v-model.number="booster.vitesseMax"
              type="number"
              step="0.1"
              class="w-full p-2 border rounded"
              placeholder="Vitesse maximale"
              required
          >
        </div>
      </div>

      <!-- Special Specifications -->
      <div class="mb-6 grid grid-cols-3 gap-4">
        <div class="flex items-center">
          <input
              v-model="booster.estPrototype"
              type="checkbox"
              class="mr-2"
          >
          <label>Est Prototype</label>
        </div>
        <div class="flex items-center">
          <input
              v-model="booster.estReetulisable"
              type="checkbox"
              class="mr-2"
          >
          <label>Est Réutilisable</label>
        </div>
        <div class="flex items-center">
          <input
              v-model="booster.aSystèmeAutoDestruction"
              type="checkbox"
              class="mr-2"
          >
          <label>Système Auto-Destruction</label>
        </div>
      </div>

      

      <div class="mb-6">
        <h3>Moteurs</h3>
        <div class="grid grid-cols-3 gap-4 mb-4">
          <select 
            v-model="moteurSelectionne" 
            class="p-2 border rounded"
          >
            <option 
              v-for="motorDisponible in moteursDebloquer" 
              :key="motorDisponible.nom"
              :value="motorDisponible.nom"
            >
              {{ motorDisponible.nom }}
            </option>
          </select>
          
          <input 
            v-model.number="nombreMoteurs" 
            type="number" 
            min="1" 
            placeholder="Nombre de moteurs"
            class="p-2 border rounded"
          >
        </div>

        <div 
          v-for="(moteur, index) in moteurs" 
          :key="index" 
          class="grid grid-cols-4 gap-4 mb-4"
        >
          <input
              :value="moteur.nom"
              type="text"
              placeholder="Nom du moteur"
              class="p-2 border rounded"
              readonly
          >
          <input
              :value="moteur.poids"
              type="number"
              placeholder="Poids"
              class="p-2 border rounded"
              readonly
          >
          <input
              :value="moteur.pousse"
              type="number"
              placeholder="Poussée"
              class="p-2 border rounded"
              readonly
          >
          <input
              :value="moteur.consommationCarburant"
              type="number"
              placeholder="Consommation"
              class="p-2 border rounded"
          >
        </div>
      </div>

      <!-- Reservoirs Section -->
      <div class="mb-6">
        <h3>Réservoirs</h3>
        <div
            v-for="(reservoir, index) in reservoirs"
            :key="index"
            class="grid grid-cols-3 gap-4 mb-4"
        >
          <input
              v-model="reservoir.nom"
              type="text"
              placeholder="Nom du réservoir"
              class="p-2 border rounded"
          >
          <div class="flex items-center">
            <input
                v-model.number="reservoir.poidsAVide"
                type="number"
                step="0.1"
                placeholder="Poids à vide"
                class="p-2 border rounded mr-2"
            >
            <button
                type="button"
                @click="removeReservoir(index)"
                class="btn-remove bg-red-500 text-white p-2 rounded"
            >
              Supprimer
            </button>
          </div>
        </div>
        <button
            type="button"
            @click="addReservoir"
            class="btn-add bg-green-500 text-white p-2 rounded"
        >
          Ajouter un Réservoir
        </button>
      </div>

      <button
          type="submit"
          class="w-full bg-blue-500 text-white p-3 rounded text-lg font-bold"
      >
        Créer le Booster
      </button>
    </form>
  </div>
</template>

<style>
/* Professional Modern Theme */
.booster-form-container {
  background: linear-gradient(135deg, #f4f7f9 0%, #e9ecef 100%);
  font-family: 'Roboto', 'Arial', sans-serif;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
}

.booster-form {
  width: 100%;
  max-width: 900px;
  background-color: white;
  border-radius: 16px;
  box-shadow: 
    0 15px 35px rgba(50, 50, 93, 0.1), 
    0 5px 15px rgba(0, 0, 0, 0.07);
  padding: 3rem;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.booster-form:hover {
  box-shadow: 
    0 20px 40px rgba(50, 50, 93, 0.15), 
    0 8px 20px rgba(0, 0, 0, 0.1);
}

/* Typography */
.booster-form h2 {
  color: #2c3e50;
  text-align: center;
  margin-bottom: 2rem;
  font-weight: 500;
  position: relative;
}

.booster-form h3 {
  color: #34495e;
  margin-bottom: 1rem;
  font-weight: 500;
  border-left: 4px solid #4a90e2;
  padding-left: 10px;
}

/* Input Styles */
.booster-form input[type="text"],
.booster-form input[type="number"] {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #d1d8e0;
  border-radius: 8px;
  transition: all 0.3s ease;
  background-color: #f9fafc;
}

.booster-form input[type="text"]:focus,
.booster-form input[type="number"]:focus {
  border-color: #4a90e2;
  box-shadow: 0 0 0 3px rgba(74, 144, 226, 0.1);
  outline: none;
}

/* Checkbox Styles */
.booster-form input[type="checkbox"] {
  width: 20px;
  height: 20px;
  margin-right: 10px;
  accent-color: #4a90e2;
}

/* Button Styles */
.btn-add, .btn-remove {
  padding: 10px 15px;
  border-radius: 6px;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
}

.btn-add {
  background-color: #50c878;
  color: white;
}

.btn-add:hover {
  background-color: #3cb371;
}

.btn-remove {
  background-color: #e74c3c;
  color: white;
}

.btn-remove:hover {
  background-color: #c0392b;
}

.booster-form button[type="submit"] {
  background: black;
  color: white;
  padding: 15px;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 1px;
  transition: transform 0.3s ease;
}

.booster-form button[type="submit"]:hover {
  transform: translateY(-3px);
  box-shadow: 0 7px 14px rgba(50, 50, 93, 0.1), 0 3px 6px rgba(0, 0, 0, 0.08);
}

/* Responsive Design */
@media (max-width: 768px) {
  .booster-form {
    padding: 1.5rem;
  }

  .booster-form .grid-container {
    grid-template-columns: 1fr;
  }
}

/* Subtle Animations */
@keyframes fadeInUp {
  from { 
    opacity: 0; 
    transform: translateY(20px); 
  }
  to { 
    opacity: 1; 
    transform: translateY(0); 
  }
}

.booster-form > div {
  animation: fadeInUp 0.5s ease backwards;
}
</style>

<script setup>
import { ref, watch } from 'vue'

const booster = ref({
  nom: '',
  taille: null,
  diametre: null,
  poidsAVide: null,
  altitudeMax: null,
  vitesseMax: null,
  estPrototype: false,
  estReetulisable: false,
  aSystèmeAutoDestruction: false
})

const moteursDebloquer = ref([
  { 
    nom: 'Merlin 1D', 
    poids: 630, 
    pousse: 845, 
    consommationCarburant: 275 
  },
  { 
    nom: 'Raptor', 
    poids: 1000, 
    pousse: 1600, 
    consommationCarburant: 350 
  },
  { 
    nom: 'RD-180', 
    poids: 5400, 
    pousse: 4152, 
    consommationCarburant: 450 
  }
])

const moteurSelectionne = ref('')
const nombreMoteurs = ref(1)
const moteurs = ref([])

watch([moteurSelectionne, nombreMoteurs], () => {
  if (moteurSelectionne.value && nombreMoteurs.value > 0) {
    const moteurType = moteursDebloquer.value.find(m => m.nom === moteurSelectionne.value)
    
    moteurs.value = Array(nombreMoteurs.value).fill().map(() => ({
      nom: moteurSelectionne.value,
      poids: moteurType.poids,
      pousse: moteurType.pousse,
      consommationCarburant: moteurType.consommationCarburant
    }))
  }
})

const submitBooster = () => {
  const boosterData = {
    moteurs: moteurs.value
  }

  console.log('Booster Created:', boosterData)
}
</script>
