<template>
    <div class="max-w-2xl mx-auto p-6">
      <h1 class="text-2xl font-bold mb-6 text-center">
        Créer un nouveau programme
      </h1>
  
      <div class="mb-8 p-4 bg-gray-50 rounded-lg">
        <p class="text-gray-700">
          {{ programmeStatus
            ? `Programme en cours: ${programmeStatus.nom} - Objectif: ${programmeStatus.objectif}`
            : "Aucun programme en cours. Veuillez en créer un." }}
        </p>
      </div>
  
      <form @submit.prevent="handleSubmit" class="space-y-4 bg-white shadow-md rounded-lg p-6">
        <div>
          <label for="nom" class="block text-sm font-medium text-gray-700 mb-1">
            Nom
          </label>
          <input
            type="text"
            id="nom"
            v-model="formData.nom"
            required
            class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
          />
        </div>
  
        <div>
          <label for="objectif" class="block text-sm font-medium text-gray-700 mb-1">
            Objectif
          </label>
          <input
            type="text"
            id="objectif"
            v-model="formData.objectif"
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
  
  export default {
    name: "ProgrammeForm",
    setup() {
      const programmeStatus = ref(null);
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
              programmeStatus.value = data.programme;
            } else if (data.action === "programmeCree") {
              window.location.href = "programme.html";
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
  
      return {
        programmeStatus,
        formData,
        connectionStatus,
        handleSubmit
      };
    }
  };
  </script>
  
  <style>
  /* Ajoutez ici vos styles personnalisés */
  </style>