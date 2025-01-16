<template>
  <div class="employe-container">
    <h1>Liste des Employés</h1>

    <router-link to="/MarcheEmploie">
      Marche Emploie
    </router-link>

    <!-- Status de connexion -->
    <div v-if="!isConnected" class="connection-status error">
      Connexion au serveur perdue. Tentative de reconnexion...
    </div>

    <p v-if="salaireTotal !== null" class="salaire-total">
      Coût salarial total: {{ formatSalaire(salaireTotal) }}€
    </p>

    <!-- Indicateur de chargement -->
    <div v-if="isLoading" class="loading">
      <p>Chargement des employés...</p>
    </div>

    <!-- Liste des employés -->
    <div v-else-if="employes.length > 0">
      <div v-for="categorie in employes" :key="categorie.type" class="categorie-section">
        <h2>{{ categorie.type }}</h2>
        <div class="employe-grid">
          <div v-for="personne in categorie.personnes" :key="personne.cleprimaire" class="employe-details"
            @click="proposerLicenciement(personne)" role="button" tabindex="0"
            :aria-label="'Cliquez pour licencier ' + personne.prenom + ' ' + personne.nom">
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

    <!-- Message si aucun employé -->
    <div v-else-if="!isLoading" class="no-data">
      Aucun employé trouvé
    </div>

    <!-- Modal de confirmation de licenciement -->
    <div v-if="employeSelectionne" class="modal" @click.self="annulerLicenciement">
      <div class="modal-content">
        <h3>Confirmation de licenciement</h3>
        <p>
          Êtes-vous sûr de vouloir licencier {{ employeSelectionne.prenom }} {{ employeSelectionne.nom }} ?
        </p>
        <div class="modal-buttons">
          <button class="confirm-btn" @click="confirmerLicenciement" :disabled="!isConnected">
            Confirmer le licenciement
          </button>
          <button class="cancel-btn" @click="annulerLicenciement">
            Annuler
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      employes: [], // Liste des employés par catégories
      isLoading: true, // Indicateur de chargement
      salaireTotal: null, // Total des salaires
      employeSelectionne: null, // Employé sélectionné pour licenciement
      isConnected: false, // Statut de connexion WebSocket
      socket: null, // Instance de WebSocket
      reconnexionInterval: null, // Intervalle pour les tentatives de reconnexion
    };
  },
  methods: {
    // Formatter les salaires
    formatSalaire(salaire) {
      return salaire.toLocaleString('fr-FR', { style: 'currency', currency: 'EUR' });
    },

    // Charger les données initiales
    chargerEmployes(donnees) {
      this.employes = donnees.categories || [];
      this.salaireTotal = donnees.salaireTotal || 0;
      this.isLoading = false;
    },

    // Gestion des messages WebSocket
    handleMessage(event) {
      const data = JSON.parse(event.data);

      if (data.type === 'EMPLOYES') {
        this.chargerEmployes(data.payload);
      } else if (data.type === 'LICENCIEMENT_CONFIRMATION') {
        this.employes = this.employes.map((categorie) => ({
          ...categorie,
          personnes: categorie.personnes.filter(
            (p) => p.cleprimaire !== data.payload.cleprimaire
          ),
        }));
        this.salaireTotal -= data.payload.salaire;
      }
    },

    connecterWebSocket() {
      this.socket = new WebSocket('ws://localhost:3232');

      this.socket.onopen = () => {
        console.log('Connexion WebSocket établie');
        this.isConnected = true;
        clearInterval(this.reconnexionInterval);
      };

      this.socket.onmessage = this.handleMessage;

      this.socket.onclose = () => {
        console.error('Connexion WebSocket fermée');
        this.isConnected = false;
        this.tenterReconnexion();
      };

      this.socket.onerror = (error) => {
        console.error('Erreur WebSocket', error);
      };
    },

    // Tentative de reconnexion automatique
    tenterReconnexion() {
      if (!this.reconnexionInterval) {
        this.reconnexionInterval = setInterval(() => {
          console.log('Tentative de reconnexion...');
          this.connecterWebSocket();
        }, 5000);
      }
    },

    // Proposer un licenciement
    proposerLicenciement(personne) {
      this.employeSelectionne = personne;
    },

    // Confirmer le licenciement
    confirmerLicenciement() {
      if (this.socket && this.isConnected) {
        this.socket.send(
          JSON.stringify({
            type: 'LICENCIEMENT',
            payload: this.employeSelectionne,
          })
        );
        this.employeSelectionne = null;
      }
    },

    // Annuler le licenciement
    annulerLicenciement() {
      this.employeSelectionne = null;
    },
  },
  mounted() {
    this.connecterWebSocket(); // Etablir la connexion au WebSocket dès le montage
  },
  beforeDestroy() {
    if (this.socket) {
      this.socket.close(); // Fermer la connexion WebSocket
    }
    clearInterval(this.reconnexionInterval);
  },
};
</script>

<style scoped>
.employe-container {
  padding: 20px;
  font-family: Arial, sans-serif;
}

.connection-status {
  padding: 10px;
  margin-bottom: 15px;
  border-radius: 4px;
}

.connection-status.error {
  background-color: #ffe6e6;
  color: #cc0000;
  border: 1px solid #ffcccc;
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

.no-data {
  text-align: center;
  padding: 20px;
  color: #666;
  font-style: italic;
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
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
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
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
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

button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.confirm-btn {
  background-color: #dc3545;
  color: white;
}

.confirm-btn:hover:not(:disabled) {
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