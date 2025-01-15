<template>
  <div class="employe-container">
    <h1>Liste de vos Employés</h1>
    <p v-if="salaireTotal !== null" class="salaire-total">
      Coût salarial total: {{ salaireTotal }}€
    </p>

    <!-- Indicateur de chargement -->
    <div v-if="isLoading" class="loading">
      <p>Chargement des employés...</p>
    </div>

    <!-- Liste des employés -->
    <div v-else>
      <div 
        v-for="categorie in employes" 
        :key="categorie.type" 
        class="categorie-section"
      >
        <h2>{{ categorie.type }}</h2>
        <div class="employe-grid">
          <div 
            v-for="personne in categorie.personnes" 
            :key="personne.cleprimaire" 
            class="employe-details"
            @click="proposerLicenciement(personne)"
            role="button"
            tabindex="0"
            :aria-label="'Cliquez pour licencier ' + personne.prenom + ' ' + personne.nom"
          >
            <p><strong>Prénom :</strong> {{ personne.prenom }}</p>
            <p><strong>Nom :</strong> {{ personne.nom }}</p>
            <p><strong>Âge :</strong> {{ personne.age }} ans</p>
            <p><strong>Salaire :</strong> {{ formatSalaire(personne.salaire) }}€</p>
            <p><strong>Sexe :</strong> 
              <span v-if="personne.sexe === 'Homme'">🚹</span>
              <span v-if="personne.sexe === 'Femme'">🚺</span>
            </p>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de confirmation de licenciement -->
    <div v-if="employeSelectionne" class="modal">
      <div class="modal-content">
        <h3>Confirmation de licenciement</h3>
        <p>
          Êtes-vous sûr de vouloir licencier {{ employeSelectionne.prenom }} {{ employeSelectionne.nom }} ?
        </p>
        <div class="modal-buttons">
          <button class="confirm-btn" @click="confirmerLicenciement">Confirmer le licenciement</button>
          <button class="cancel-btn" @click="annulerLicenciement">Annuler</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, onUnmounted } from 'vue';

export default {
  name: "EmployeList",
  setup() {
    const employes = ref([]);
    const websocket = ref(null);
    const employeSelectionne = ref(null);
    const isLoading = ref(true);
    const connectionStatus = ref('connecting');
    const salaireTotal = ref(null);

    const formatSalaire = (salaire) => {
      return new Intl.NumberFormat('fr-FR').format(salaire);
    };

    const proposerLicenciement = (personne) => {
      employeSelectionne.value = personne;
    };

    const confirmerLicenciement = () => {
      if (websocket.value && websocket.value.readyState === WebSocket.OPEN) {
        websocket.value.send(
          JSON.stringify({
            action: "licencierEmploye",
            employe: {
              cleprimaire: employeSelectionne.value.cleprimaire
            }
          })
        );
      }
      employeSelectionne.value = null;
    };

    const annulerLicenciement = () => {
      employeSelectionne.value = null;
    };

    const handleWebSocketMessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        
        if (data.action === "employesState") {
          employes.value = data.employes;
          salaireTotal.value = data.salaireTotal;
          isLoading.value = false;
        } else if (data.action === "personneLicencier") {
          console.log(`L'employé ${data.nom} a été licencié.`);
          websocket.value.send(JSON.stringify({ action: "getEmployesState" }));
        } else if (data.error) {
          console.error(`Erreur : ${data.error}`);
        }
      } catch (error) {
        console.error("Erreur lors du traitement des données WebSocket :", error);
      }
    };

    onMounted(() => {
      websocket.value = new WebSocket("ws://localhost:8080");

      websocket.value.onopen = () => {
        connectionStatus.value = "connected";
        websocket.value.send(JSON.stringify({ action: "getEmployesState" }));
      };

      websocket.value.onmessage = handleWebSocketMessage;

      websocket.value.onerror = (error) => {
        console.error("Erreur WebSocket :", error);
        connectionStatus.value = "error";
        isLoading.value = false;
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
      employes,
      employeSelectionne,
      isLoading,
      connectionStatus,
      salaireTotal,
      proposerLicenciement,
      confirmerLicenciement,
      annulerLicenciement,
      formatSalaire
    };
  }
};
</script>

<style scoped>
.employe-container {
  padding: 20px;
  font-family: Arial, sans-serif;
}

.salaire-total {
  background-color: #f0f8ff;
  padding: 10px;
  border-radius: 4px;
  margin-bottom: 20px;
  font-weight: bold;
}

.loading {
  text-align: center;
  padding: 20px;
  color: #666;
}

.categorie-section {
  margin-bottom: 24px;
}

.categorie-section h2 {
  margin-bottom: 16px;
  color: #333;
  padding-bottom: 8px;
  border-bottom: 2px solid #eee;
}

.employe-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 16px;
  padding: 8px;
}

.employe-details {
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 16px;
  transition: all 0.2s ease-in-out;
}

.employe-details:hover {
  background-color: #ffe6e6;
  border-color: #ff9999;
  cursor: pointer;
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.employe-details p {
  margin: 8px 0;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 24px;
  border-radius: 8px;
  max-width: 400px;
  width: 90%;
  text-align: center;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}

.modal-buttons {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 24px;
}

button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.2s;
}

.confirm-btn {
  background-color: #dc3545;
  color: white;
}

.confirm-btn:hover {
  background-color: #c82333;
}

.cancel-btn {
  background-color: #6c757d;
  color: white;
}

.cancel-btn:hover {
  background-color: #5a6268;
}
</style>