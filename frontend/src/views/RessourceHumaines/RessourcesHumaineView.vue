<template>
  <div class="employe-container">
    <h1>Liste des Employés</h1>
    <h1>Salaire à payer : {{ salaireTotal }}</h1>
    <router-link to="/MarcheEmploie" @click="closeMenu">Marche de l'employé</router-link>

    <div class="employe-column">
      <div
        v-for="employe in employes"
        :key="employe.cleprimaire"
        class="employe-details"
      >
        <p><strong>Prénom :</strong> {{ employe.prenom }}</p>
        <p><strong>Nom :</strong> {{ employe.nom }}</p>
        <p><strong>Âge :</strong> {{ employe.age }} ans</p>
        <p><strong>Salaire :</strong> {{ employe.salaire }}€</p>
        <p>
          <strong>Sexe :</strong>
          <span v-if="employe.sexe === 'Homme'">🚹</span>
          <span v-if="employe.sexe === 'Femme'">🚺</span>
        </p>
        <p><strong>Type :</strong> {{ employe.type }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from "vue";

export default {
  name: "EmployeList",
  setup() {
    const salaireTotal = ref(0);
    const employes = ref([]);
    const websocket = ref(null);
    const connectionStatus = ref("connecting");

    const closeMenu = () => {
      console.log("Fermeture du menu.");
    };

    const fetchEmployeData = () => {
      if (websocket.value && connectionStatus.value === "connected") {
        websocket.value.send(JSON.stringify({ action: "getEmployesState" }));
      }
    };

    onMounted(() => {
      websocket.value = new WebSocket("ws://localhost:3232");

      websocket.value.onopen = () => {
        connectionStatus.value = "connected";
        fetchEmployeData();
      };

      websocket.value.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          if (data.action === "employesState") {
            salaireTotal.value = data.salaireTotal || 0;
            employes.value = data.employes.flatMap((categorie) => categorie.personnes) || [];
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
      salaireTotal,
      employes,
      closeMenu,
    };
  },
};
</script>

<style scoped>
.employe-container {
  max-width: 1200px;
  margin: auto;
  padding: 16px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.employe-column {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.employe-details {
  width: 300px;
  padding: 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fff;
  transition: transform 0.2s, box-shadow 0.2s;
}

.employe-details:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
}

.employe-details p {
  margin: 8px 0;
  line-height: 1.4;
}

router-link {
  display: inline-block;
  margin: 16px 0;
  padding: 8px 16px;
  background-color: #3498db;
  color: white;
  text-decoration: none;
  border-radius: 4px;
  transition: background-color 0.2s;
}

router-link:hover {
  background-color: #2980b9;
}
</style>
