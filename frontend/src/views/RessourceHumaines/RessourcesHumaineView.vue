<template>
  <div class="rh-container">
    <div class="header">
      <h1>Gestion des Ressources Humaines</h1>
      
      <router-link to="/MarcheEmploie" class="market-link">
        Marché de l'Emploi
      </router-link>
    </div>

    <div v-if="!isConnected" class="connection-status error">
      Connexion au serveur perdue. Tentative de reconnexion...
    </div>

    <div v-if="salaireTotal !== null" class="total-salary">
      Coût salarial total: {{ formatSalaire(salaireTotal) }}€
    </div>

    <div v-if="isLoading" class="loading">
      <p>Chargement des employés...</p>
    </div>

    <div v-else-if="employes.length > 0" class="employees-columns">
      <div v-for="categorie in employes" :key="categorie.type" class="category-column">
        <div class="category-header">
          <div class="category-title">
            <h2>{{ categorie.type }}</h2>
            <div class="category-icon">
              <img 
                :src="getCategoryIcon(categorie.type)" 
                :alt="'Icône ' + categorie.type"
                class="type-icon"
              />
            </div>
          </div>
        </div>
        <div class="employees-list">
          <div
            v-for="personne in categorie.personnes"
            :key="personne.cleprimaire"
            class="employee-card"
            @click="proposerLicenciement(personne)"
            role="button"
            tabindex="0"
            :aria-label="'Cliquer pour gérer ' + personne.prenom + ' ' + personne.nom"
          >
            <div class="employee-info">
              <div class="employee-header">
                <span class="employee-name">{{ personne.prenom }} {{ personne.nom }}</span>
                <span class="employee-salary">{{ formatSalaire(personne.salaire) }}€</span>
              </div>
              <div class="employee-details">
                <span>{{ personne.age }} ans</span>
                <span class="gender-icon">
                  <span v-if="personne.sexe === 'Homme'">🚹</span>
                  <span v-else>🚺</span>
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else class="no-data">
      Aucun employé trouvé
    </div>

    <div v-if="employeSelectionne" class="modal-overlay" @click.self="annulerLicenciement">
      <div class="modal-content">
        <h3>Confirmation de licenciement</h3>
        <p>
          Êtes-vous sûr de vouloir licencier {{ employeSelectionne.prenom }} {{ employeSelectionne.nom }} ?
        </p>
        <div class="modal-buttons">
          <button 
            class="confirm-btn" 
            @click="confirmerLicenciement" 
            :disabled="!isConnected"
          >
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
  name: 'RessourcesHumaines',
  
  data() {
    return {
      employes: [], // Contiendra les catégories d'employés
      salaireTotal: null,
      isConnected: true,
      isLoading: true,
      employeSelectionne: null,
      socket: null,
      typeIcons: {
        'astronaute': 'src/assets/img/icone/rh/astronaute.png',
        'ouvrier': 'src/assets/img/icone/rh/ouvrier.png',
        'ingenieur': 'src/assets/img/icone/rh/ingenieur.png',
        'scientifique': 'src/assets/img/icone/rh/scientifique.png',
      }
    }
  },

  methods: {
    formatSalaire(montant) {
      return new Intl.NumberFormat('fr-FR', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      }).format(montant)
    },

    getCategoryIcon(type) {
      const typeLowerCase = type.toLowerCase()
      return this.typeIcons[typeLowerCase] || '/icons/default.svg'
    },

    initWebSocket() {
      this.socket = new WebSocket('ws://localhost:3232/')

      this.socket.onopen = () => {
        this.isConnected = true
        this.requestEmployesData()
      }

      this.socket.onclose = () => {
        this.isConnected = false
        setTimeout(() => this.initWebSocket(), 5000)
      }

      this.socket.onerror = () => {
        this.isConnected = false
      }

      this.socket.onmessage = (event) => {
        const data = JSON.parse(event.data)
        if (data.action === "employesState") {
          this.employes = data.employes
          this.salaireTotal = data.salaireTotal
          this.isLoading = false
        }
      }
    },

    requestEmployesData() {
      if (this.socket && this.socket.readyState === WebSocket.OPEN) {
        this.socket.send(JSON.stringify({ action: "getEmployesState" }))
      }
    },

    proposerLicenciement(employe) {
      this.employeSelectionne = employe
    },

    confirmerLicenciement() {
      if (!this.employeSelectionne || !this.isConnected) return

      this.socket.send(JSON.stringify({
        action: "licencierEmploye",
        employe: {
          cleprimaire: this.employeSelectionne.cleprimaire
        }
      }))

      this.employeSelectionne = null
    },

    annulerLicenciement() {
      this.employeSelectionne = null
    }
  },

  created() {
    this.initWebSocket()
  },

  beforeDestroy() {
    if (this.socket) {
      this.socket.close()
    }
  }
}
</script>

<style scoped>
.rh-container {
  padding: 2rem;
  max-width: 1400px;
  margin: 0 auto;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.market-link {
  padding: 0.75rem 1.5rem;
  background-color: #4a90e2;
  color: white;
  border-radius: 0.5rem;
  text-decoration: none;
  transition: background-color 0.2s;
}

.market-link:hover {
  background-color: #357abd;
}

.connection-status {
  padding: 1rem;
  margin-bottom: 1rem;
  border-radius: 0.5rem;
}

.connection-status.error {
  background-color: #ffe6e6;
  color: #cc0000;
  border: 1px solid #ffcccc;
}

.total-salary {
  background-color: #f0f8ff;
  padding: 1rem;
  border-radius: 0.5rem;
  margin-bottom: 2rem;
  font-weight: bold;
  font-size: 1.1rem;
}

.loading, .no-data {
  text-align: center;
  padding: 2rem;
  color: #666;
}

.employees-columns {
  display: flex;
  justify-content: space-between;
  gap: 2rem;
  flex-wrap: wrap;
}

.category-column {
  width: 30%;
  background: #ffffff;
  border-radius: 0.5rem;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.category-header {
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #eee;
}

.category-title {
  position: relative;
  padding-right: 50px;
}

.category-title h2 {
  margin: 0;
  color: #333;
}

.category-icon {
  position: absolute;
  top: 0;
  right: 0;
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.type-icon {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.employees-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.employee-card {
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 0.5rem;
  padding: 1rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.employee-card:hover {
  background-color: #ffe6e6;
  border-color: #ff9999;
  transform: translateY(-2px);
}

.employee-info {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.employee-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.employee-name {
  font-weight: bold;
}

.employee-salary {
  color: #4a90e2;
  font-weight: bold;
}

.employee-details {
  display: flex;
  gap: 1rem;
  color: #666;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 0.5rem;
  max-width: 500px;
  width: 90%;
}

.modal-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 2rem;
}

.confirm-btn, .cancel-btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 0.25rem;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.2s;
}

.confirm-btn {
  background-color: #dc3545;
  color: white;
}

.confirm-btn:hover:not(:disabled) {
  background-color: #c82333;
}

.confirm-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.cancel-btn {
  background-color: #6c757d;
  color: white;
}

.cancel-btn:hover {
  background-color: #5a6268;
}

@media (max-width: 768px) {
  .rh-container {
    padding: 1rem;
  }
  
  .employees-columns {
    flex-direction: column;
    align-items: center;
  }

  .category-column {
    width: 100%;
    margin-bottom: 1.5rem;
  }
}
</style>