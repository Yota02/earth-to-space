<template>
  <div class="employe-container">
    <h1>Liste des Employés</h1>

    <!-- Indicateur de chargement -->
    <div v-if="isLoading">
      <p>Chargement des employés...</p>
    </div>

    <!-- Catégories et employés -->
    <div v-else>
      <div 
        v-for="categorie in marcheEmploie" 
        :key="categorie.type" 
        class="categorie-section"
      >
        <h2>{{ categorie.type }}</h2>
        <div class="employe-grid">
          <div 
            v-for="(personne, index) in categorie.personnes" 
            :key="personne.cleprimaire" 
            class="employe-details"
            :style="{ marginRight: (index + 1) % 5 === 0 ? '0' : '16px' }"
            @click="proposerEmbauche(personne)"
            role="button"
            tabindex="0"
            :aria-label="'Cliquez pour embaucher ' + personne.prenom + ' ' + personne.nom"
          >
            <p><strong>Prénom :</strong> {{ personne.prenom }}</p>
            <p><strong>Nom :</strong> {{ personne.nom }}</p>
            <p><strong>Âge :</strong> {{ personne.age }} ans</p>
            <p><strong>Salaire :</strong> {{ personne.salaire }}€</p>
            <p><strong>Sexe :</strong> 
              <span v-if="personne.sexe === 'Homme'">🚹</span>
              <span v-if="personne.sexe === 'Femme'">🚺</span>
            </p>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de confirmation -->
    <div v-if="employeSelectionne" class="modal">
      <p>
        Voulez-vous embaucher {{ employeSelectionne.prenom }} {{ employeSelectionne.nom }} ?
      </p>
      <button @click="confirmerEmbauche">Oui</button>
      <button @click="annulerEmbauche">Non</button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue';

export default {
  name: "EmployeList",
  setup() {
    const salaireTotal = ref(0);
    const marcheEmploie = ref([]);
    const websocket = ref(null);
    const employeSelectionne = ref(null);
    const isLoading = ref(true);
    const connectionStatus = ref('connecting');

    const proposerEmbauche = (personne) => {
      employeSelectionne.value = personne;
    };

    const confirmerEmbauche = () => {
      if (websocket.value && websocket.value.readyState === WebSocket.OPEN) {
        websocket.value.send(
          JSON.stringify({
            action: "embaucherEmploye",
            employe: employeSelectionne.value,
          })
        );
      }
      employeSelectionne.value = null;
    };

    const annulerEmbauche = () => {
      employeSelectionne.value = null;
    };

    onMounted(() => {
      websocket.value = new WebSocket("ws://localhost:3232");

      websocket.value.onopen = () => {
        connectionStatus.value = "connected";
        websocket.value.send(JSON.stringify({ action: "getMarcheEmploisState" }));
      };

      websocket.value.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          
          if (data.action === "marcheEmploisState") {
            marcheEmploie.value = data.marcheEmploie;
            isLoading.value = false;
          } else if (data.action === "embaucheSuccess") {
            alert(`L'employé ${data.prenom} ${data.nom} a été embauché avec succès !`);
            // Demander une mise à jour de l'état
            websocket.value.send(JSON.stringify({ action: "getEmployesState" }));
          } else if (data.error) {
            alert(`Erreur : ${data.error}`);
          }
        } catch (error) {
          console.error("Erreur lors du traitement des données WebSocket :", error);
          alert("Une erreur s'est produite lors de la communication avec le serveur.");
        }
      };

      websocket.value.onerror = (error) => {
        console.error("Erreur WebSocket :", error);
        connectionStatus.value = "error";
        alert("Impossible de se connecter au serveur.");
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
      marcheEmploie,
      employeSelectionne,
      isLoading,
      connectionStatus,
      proposerEmbauche,
      confirmerEmbauche,
      annulerEmbauche
    };
  }
};
</script>

<style scoped>
.employe-container {
  padding: 20px;
  font-family: Arial, sans-serif;
}

.categorie-section {
  margin-bottom: 24px;
}

.categorie-section h2 {
  margin-bottom: 16px;
}

.employe-grid {
  display: flex;
  flex-wrap: wrap;
}

.employe-details {
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
  transition: background-color 0.2s ease-in-out;
}

.employe-details:hover {
  background-color: #e6f7ff;
  cursor: pointer;
}

.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  padding: 20px;
  box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  z-index: 1000;
}

button {
  margin: 5px;
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

button:last-child {
  background-color: #dc3545;
}

button:last-child:hover {
  background-color: #c82333;
}
</style>