<template>
  <div class="max-w-2xl mx-auto p-6">

    <div class="mb-8 p-4 bg-gray-50 rounded-lg">
      <p class="text-gray-700">
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
            <div class="programme-objectif">
              Objectif: {{ programme.objectif }}
            </div>
          </button>
        </div>
      </div>
    </div>

    <form @submit.prevent="handleSubmit" class="space-y-4 bg-white shadow-md rounded-lg p-6">
      <div>
        <label for="objectif" class="block text-sm font-medium text-gray-700 mb-1">
          Nom
        </label>
        <input
          type="text"
          id="objectif"
          v-model="formData.objectif"
          required
          class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
        />
      </div>

      <div>
        <label for="nom" class="block text-sm font-medium text-gray-700 mb-1">
          Objectif
        </label>
        <input
          type="text"
          id="nom"
          v-model="formData.nom"
          required
          class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
        />
      </div>

      <button
        type="submit"
        class="w-full bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 transition-colors"
      >
        Créer
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
      objectif: ""
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
            nom: formData.value.nom,
            objectif: formData.value.objectif
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
          objectif: programme.objectif
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
.programmes-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.programme-item {
  background-color: #f9fafb;
  border-radius: 8px;
  padding: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: background-color 0.3s;
}

.programme-item:hover {
  background-color: #f0f9ff;
}

.programme-button {
  width: 100%;
  text-align: left;
  padding: 10px;
  display: block;
}

.programme-name {
  font-size: 1.125rem;
  font-weight: 600;
  color: #1e3a8a;
}

.programme-objectif {
  font-size: 1rem;
  color: #4b5563;
}

.programme-item:focus-within {
  outline: none;
  border: 2px solid #3b82f6;
}
</style>
