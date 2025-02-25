<template>
  <div class="booster-container">
    <h1>Liste des Boosters</h1>
    <div v-for="booster in boosters" :key="booster.nom" class="booster-details">
      <p><strong>Nom :</strong> {{ booster.nom }}</p>
      <p><strong>Taille :</strong> {{ booster.taille }}</p>
      <p><strong>Diamètre :</strong> {{ booster.diametre }}</p>
      <p><strong>Poids à Vide :</strong> {{ booster.poidsAVide }}</p>
      <p>
        <strong>Réutilisable :</strong>
        <span
          class="status-circle"
          :class="{ green: booster.estReetulisable, red: !booster.estReetulisable }"
        ></span>
      </p>
      <p>
        <strong>Prototype :</strong>
        <span
          class="status-circle"
          :class="{ green: booster.estPrototype, red: !booster.estPrototype }"
        ></span>
      </p>
      <p>
        <strong>Autodestruction :</strong>
        <span
          class="status-circle"
          :class="{ green: booster.aSystemeAutoDestruction, red: !booster.aSystemeAutoDestruction }"
        ></span>
      </p>
      <hr />
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from "vue";

export default {
  name: "BoosterList",
  setup() {
    const boosters = ref([]);
    const websocket = ref(null);
    const connectionStatus = ref("connecting");

    onMounted(() => {
      websocket.value = new WebSocket("ws://localhost:3232");

      websocket.value.onopen = () => {
        connectionStatus.value = "connected";
        websocket.value.send(JSON.stringify({ action: "getBoostersState" }));
      };

      websocket.value.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          if (data.action === "boostersState") {
            boosters.value = data.boosters || [];
          }
        } catch (error) {
          console.error("Erreur lors du traitement des données WebSocket :", error);
        }
      };

      websocket.value.onerror = (error) => {
        console.error("Erreur WebSocket :", error);
        connectionStatus.value = "error";
      };

      websocket.value.onclose = () => {
        connectionStatus.value = "disconnected";
        console.log("WebSocket fermé");
      };
    });

    onUnmounted(() => {
      if (websocket.value) {
        websocket.value.close();
      }
    });

    return {
      boosters,
      connectionStatus
    };
  }
};
</script>

<style scoped>
.booster-container {
  max-width: 800px;
  margin: auto;
  padding: 16px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.booster-details {
  margin-bottom: 16px;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fff;
}

.booster-details p {
  margin: 8px 0;
}

.status-circle {
  display: inline-block;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  margin-left: 8px;
}

.status-circle.green {
  background-color: #4caf50;
}

.status-circle.red {
  background-color: #f44336;
}
</style>