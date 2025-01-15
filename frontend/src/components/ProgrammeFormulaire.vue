<template>
  <div class="max-w-2xl mx-auto p-6">

    <div class="mb-8 p-6 bg-white rounded-lg shadow-lg border border-gray-200">
      <h2 class="text-2xl font-bold text-gray-800 mb-4">Programmes</h2>
      <p class="text-gray-700 mb-4">
        {{ programmes.length > 0
          ? "Cliquez sur un programme pour voir ses détails."
          : "Aucun programme en cours. Veuillez en créer un." }}
      </p>
      <div class="programmes-list">
        <div
          v-for="programme in programmes"
          :key="programme.nom"
          class="programme-item"
        >
          <button
            @click="navigateToProgramme(programme)"
            class="programme-button"
          >
            <div class="programme-name">
              <strong>{{ programme.nom }}</strong>
            </div>
            <div class="programme-details">
              <div class="programme-info">
                <span class="info-label">Objectif:</span> 
                {{ programme.objectif }}
              </div>
              <div class="programme-info">
                <span class="info-label">Budget:</span> 
                {{ programme.budget.toLocaleString() }} €
              </div>
              <div class="programme-info">
                <span class="info-label">Durée:</span> 
                {{ programme.dureePrevu }} mois
              </div>
            </div>
          </button>
        </div>
      </div>
    </div>

    <!-- Formulaire -->
    <form @submit.prevent="handleSubmit" class="space-y-6 bg-white shadow-lg rounded-lg p-6 border border-gray-200">
      <h2 class="text-2xl font-bold text-gray-800 mb-6">Nouveau Programme</h2>
      
      <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <!-- Nom du programme -->
        <div>
          <label for="nom" class="block text-sm font-medium text-gray-700 mb-2">
            Nom du programme
          </label>
          <input
            type="text"
            id="nom"
            v-model="formData.nom"
            required
            class="w-full px-4 py-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all"
            placeholder="Entrez le nom du programme"
          />
        </div>

        <!-- Objectif -->
        <div>
          <label for="objectif" class="block text-sm font-medium text-gray-700 mb-2">
            Objectif
          </label>
          <input
            type="text"
            id="objectif"
            v-model="formData.objectif"
            required
            class="w-full px-4 py-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all"
            placeholder="Définissez l'objectif"
          />
        </div>

        <!-- Budget -->
        <div>
          <label for="budget" class="block text-sm font-medium text-gray-700 mb-2">
            Budget (€)
          </label>
          <input
            type="number"
            id="budget"
            v-model="formData.budget"
            required
            min="0"
            step="1000"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all"
            placeholder="Entrez le budget"
          />
        </div>

        <!-- Durée prévue -->
        <div>
          <label for="dureePrevu" class="block text-sm font-medium text-gray-700 mb-2">
            Durée prévue (mois)
          </label>
          <input
            type="number"
            id="dureePrevu"
            v-model="formData.dureePrevu"
            required
            min="1"
            max="120"
            class="w-full px-4 py-3 border border-gray-300 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all"
            placeholder="Entrez la durée"
          />
        </div>
      </div>

      <button
        type="submit"
        class="w-full bg-blue-600 text-white py-3 px-6 rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-all font-medium text-lg mt-6"
      >
        Créer le programme
      </button>
    </form>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from "vue";
import { useRouter } from "vue-router";

export default {
  name: "ProgrammeForm",
  setup() {
    const router = useRouter();
    const programmes = ref([]);
    const formData = ref({
      nom: "",
      objectif: "",
      budget: 0,
      dureePrevu: 1
    });
    const websocket = ref(null);
    const connectionStatus = ref("connecting");

    onMounted(() => {
      websocket.value = new WebSocket("ws://localhost:3232");

      websocket.value.onopen = () => {
        connectionStatus.value = "connected";
        websocket.value.send(JSON.stringify({ action: "getProgrammeState" })); 
      };

      websocket.value.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          if (data.action === "programmeState") {
            if (data.programme) {
              programmes.value = [data.programme];
            }
          } else if (data.action === "programmeCree") {
            websocket.value.send(JSON.stringify({ action: "getProgrammeState" }));
            formData.value = {
              nom: "",
              objectif: "",
              budget: 0,
              dureePrevu: 1
            };
          }
        } catch (error) {
          console.error("Error processing WebSocket data:", error);
        }
      };

      websocket.value.onerror = (error) => {
        console.error("WebSocket error:", error);
        connectionStatus.value = "error";
      };

      websocket.value.onclose = () => {
        connectionStatus.value = "disconnected";
      };
    });

    onUnmounted(() => {
      if (websocket.value) {
        websocket.value.close();
      }
    });

    const handleSubmit = () => {
      if (websocket.value && websocket.value.readyState === WebSocket.OPEN) {
        websocket.value.send(
          JSON.stringify({
            action: "creerUnProgramme",
            ...formData.value,
            budget: Number(formData.value.budget),
            dureePrevu: Number(formData.value.dureePrevu)
          })
        );
      } else {
        console.error("WebSocket not connected");
      }
    };

    const navigateToProgramme = (programme) => {
      router.push({
        name: "ProgrammeDetails",
        query: {
          nom: programme.nom,
          objectif: programme.objectif,
          budget: programme.budget,
          dureePrevu: programme.dureePrevu
        }
      });
    };

    return {
      programmes,
      formData,
      connectionStatus,
      handleSubmit,
      navigateToProgramme
    };
  }
};
</script>

<style scoped>
/* Ajout de la transition pour les champs de formulaire */
input {
  transition: all 0.3s ease-in-out;
}

input:focus {
  border-color: #3b82f6;
  box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.3);
}

/* Disposition et style du formulaire */
.grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 1.5rem;
}

@media (min-width: 768px) {
  .grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

button {
  transition: all 0.3s ease;
}

/* Général */
label {
  font-size: 0.875rem;
  font-weight: 500;
  color: #4b5563;
}

input {
  font-size: 1rem;
  padding: 12px;
  border-radius: 8px;
  border: 1px solid #e5e7eb;
}

input:focus {
  border-color: #3b82f6;
  outline: none;
}

.programmes-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.programme-item {
  background-color: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: all 0.2s ease;
}

.programme-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.programme-button {
  width: 100%;
  text-align: left;
  padding: 0;
}

.programme-name {
  font-size: 1.25rem;
  font-weight: 600;
  color: #1e40af;
  margin-bottom: 8px;
}

.programme-details {
  display: grid;
  gap: 8px;
}

.programme-info {
  color: #4b5563;
  font-size: 0.975rem;
}

.info-label {
  font-weight: 500;
  color: #374151;
}

.programme-item:focus-within {
  outline: none;
  ring: 2px solid #3b82f6;
}
</style>