<template>
    <div class="charge-utile-form">
      <div class="form-header">
        <h2>{{ formTitle }}</h2>
        <div class="form-status" :class="statusClass">
          {{ statusMessage }}
        </div>
      </div>
  
      <div class="form-content">
        <div class="form-section">
          <h3>Informations générales</h3>
          <div class="form-grid">
            <div class="form-item">
              <label for="nom">Nom de la charge utile</label>
              <input id="nom" v-model="chargeUtile.nom" type="text" placeholder="ex: Satellite GPS-III" required>
            </div>
            <div class="form-item">
              <label for="poids">Poids (kg)</label>
              <input id="poids" v-model.number="chargeUtile.poids" type="number" min="0" step="0.1" placeholder="ex: 1500" required>
            </div>
            <div class="form-item">
              <label for="volume">Volume (m³)</label>
              <input id="volume" v-model.number="chargeUtile.volume" type="number" min="0" step="0.1" placeholder="ex: 8.5" required>
            </div>
            <div class="form-item">
              <label for="cout">Coût (EUR)</label>
              <input id="cout" v-model.number="chargeUtile.cout" type="number" min="0" placeholder="ex: 250000" required>
            </div>
          </div>
        </div>
  
        <div class="form-section">
          <h3>Mission</h3>
          <div class="form-grid">
            <div class="form-item">
              <label for="dureeMission">Durée de la mission (jours)</label>
              <input id="dureeMission" v-model.number="chargeUtile.dureeMission" type="number" min="1" placeholder="ex: 365" required>
            </div>
            <div class="form-item">
              <label for="statut">Statut</label>
              <select id="statut" v-model.number="chargeUtile.statut" required>
                <option :value="1">En cours</option>
                <option :value="2">Terminé</option>
                <option :value="3">Annulé</option>
                <option :value="0">Inconnu</option>
              </select>
            </div>
            <div class="form-item">
              <label for="dateDeLancement">Date de lancement prévue</label>
              <input id="dateDeLancement" v-model="chargeUtile.dateDeLancement" type="datetime-local">
            </div>
          </div>
        </div>
  
        <div class="form-section">
          <h3>Destination</h3>
          <div class="form-grid">
            <div class="form-item">
              <label for="planete">Planète cible</label>
              <select id="planete" v-model="chargeUtile.planete">
                <option value="">-- Sélectionner --</option>
                <option v-for="(planete, index) in planetes" :key="index" :value="planete.value">
                  {{ planete.label }}
                </option>
              </select>
            </div>
            <div class="form-item">
              <label for="orbite">Orbite cible</label>
              <select id="orbite" v-model="chargeUtile.orbite">
                <option value="">-- Sélectionner --</option>
                <option v-for="(orbite, index) in orbites" :key="index" :value="orbite.value">
                  {{ orbite.label }}
                </option>
              </select>
            </div>
          </div>
        </div>
  
        <div class="form-section">
          <h3>Propriétaire</h3>
          <div class="form-grid">
            <div class="form-item">
              <label for="proprietaire">Type de propriétaire</label>
              <select id="proprietaire" v-model="chargeUtile.proprietaireType" required @change="handleProprietaireChange">
                <option value="">-- Sélectionner --</option>
                <option value="ENTREPRISE">Notre entreprise</option>
                <option value="CLIENT">Client externe</option>
              </select>
            </div>
            <div class="form-item" v-if="chargeUtile.proprietaireType === 'CLIENT'">
              <label for="clientName">Nom du client</label>
              <input id="clientName" v-model="chargeUtile.proprietaireNom" type="text" placeholder="ex: ESA">
            </div>
          </div>
        </div>
  
        <div class="form-actions">
          <button class="action-button reset" @click="resetForm">Réinitialiser</button>
          <button class="action-button submit" @click="submitForm" :disabled="!isFormValid">Créer la charge utile</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'ChargeUtileForm',
    data() {
      return {
        formTitle: "Création d'une nouvelle charge utile",
        statusMessage: "En attente de soumission",
        chargeUtile: {
          nom: "",
          poids: null,
          volume: null,
          cout: null,
          dureeMission: null,
          statut: 1,
          dateDeLancement: null,
          planete: "",
          orbite: "",
          proprietaireType: "",
          proprietaireNom: ""
        },
        websocket: null,
        isConnected: false,
        // Listes des planètes et orbites disponibles
        planetes: [
          { value: "TERRE", label: "Terre" },
          { value: "LUNE", label: "Lune" },
          { value: "MARS", label: "Mars" },
          { value: "VENUS", label: "Vénus" },
          { value: "JUPITER", label: "Jupiter" },
          { value: "SATURNE", label: "Saturne" }
        ],
        orbites: [
          { value: "LEO", label: "Orbite terrestre basse (LEO)" },
          { value: "MEO", label: "Orbite terrestre moyenne (MEO)" },
          { value: "GEO", label: "Orbite géostationnaire (GEO)" },
          { value: "LLO", label: "Orbite lunaire basse" },
          { value: "MO", label: "Orbite martienne" }
        ]
      };
    },
    computed: {
      // Déterminer la classe de statut pour le style
      statusClass() {
        if (this.statusMessage.includes('succès')) {
          return 'status-success';
        } else if (this.statusMessage.includes('erreur')) {
          return 'status-error';
        } else {
          return 'status-pending';
        }
      },
      // Vérifier si le formulaire est valide
      isFormValid() {
        // Vérification des champs obligatoires
        if (!this.chargeUtile.nom || 
            this.chargeUtile.poids === null || 
            this.chargeUtile.volume === null || 
            this.chargeUtile.cout === null || 
            this.chargeUtile.dureeMission === null || 
            !this.chargeUtile.proprietaireType) {
          return false;
        }
        
        // Si le propriétaire est un client, le nom du client est obligatoire
        if (this.chargeUtile.proprietaireType === 'CLIENT' && !this.chargeUtile.proprietaireNom) {
          return false;
        }
        
        return true;
      }
    },
    mounted() {
      this.connectWebSocket();
    },
    beforeUnmount() {
      this.disconnectWebSocket();
    },
    methods: {
      // Gérer le changement de type de propriétaire
      handleProprietaireChange() {
        if (this.chargeUtile.proprietaireType !== 'CLIENT') {
          this.chargeUtile.proprietaireNom = "";
        }
      },
      
      // Réinitialiser le formulaire
      resetForm() {
        this.chargeUtile = {
          nom: "",
          poids: null,
          volume: null,
          cout: null,
          dureeMission: null,
          statut: 1,
          dateDeLancement: null,
          planete: "",
          orbite: "",
          proprietaireType: "",
          proprietaireNom: ""
        };
        this.statusMessage = "En attente de soumission";
      },
      
      // Soumettre le formulaire
      submitForm() {
        if (!this.isFormValid) {
          this.statusMessage = "Erreur: Veuillez remplir tous les champs obligatoires";
          return;
        }
        
        // Préparer les données pour l'envoi
        const chargeUtileData = {
          action: "createChargeUtile",
          chargeUtile: {
            nom: this.chargeUtile.nom,
            poids: this.chargeUtile.poids,
            volume: this.chargeUtile.volume,
            cout: this.chargeUtile.cout,
            dureeMission: this.chargeUtile.dureeMission,
            statut: this.chargeUtile.statut,
            dateDeLancement: this.chargeUtile.dateDeLancement,
            planete: this.chargeUtile.planete,
            orbite: this.chargeUtile.orbite,
            proprietaire: {
              type: this.chargeUtile.proprietaireType,
              nom: this.chargeUtile.proprietaireNom
            }
          }
        };
        
        // Envoyer les données au serveur via WebSocket
        if (this.isConnected && this.websocket) {
          this.websocket.send(JSON.stringify(chargeUtileData));
          this.statusMessage = "Envoi en cours...";
        } else {
          this.statusMessage = "Erreur: Connexion WebSocket non disponible";
          console.error("WebSocket non connecté lors de la tentative d'envoi des données");
          // Tenter de se reconnecter
          this.connectWebSocket();
        }
      },
      
      // Connexion WebSocket
      connectWebSocket() {
        try {
          this.websocket = new WebSocket('ws://localhost:3232');
          
          this.websocket.onopen = () => {
            console.log('Connexion WebSocket établie');
            this.isConnected = true;
          };
          
          this.websocket.onmessage = (event) => {
            try {
              console.log('Message reçu:', event.data);
              const data = JSON.parse(event.data);
              
              // Traiter la réponse du serveur
              if (data.action === 'createChargeUtileResponse') {
                if (data.success) {
                  this.statusMessage = "Succès: Charge utile créée avec succès";
                  // Réinitialiser le formulaire après un délai
                  setTimeout(() => {
                    this.resetForm();
                  }, 3000);
                } else {
                  this.statusMessage = `Erreur: ${data.message || 'Échec de la création'}`;
                }
              }
            } catch (error) {
              console.error('Erreur lors du traitement des données WebSocket:', error);
              this.statusMessage = "Erreur: Impossible de traiter la réponse du serveur";
            }
          };
          
          this.websocket.onerror = (error) => {
            console.error('Erreur WebSocket:', error);
            this.isConnected = false;
            this.statusMessage = "Erreur: Problème de connexion au serveur";
          };
          
          this.websocket.onclose = () => {
            console.log('Connexion WebSocket fermée');
            this.isConnected = false;
            
            // Tentative de reconnexion après 5 secondes
            setTimeout(() => {
              if (!this.isConnected) {
                this.connectWebSocket();
              }
            }, 5000);
          };
        } catch (error) {
          console.error('Erreur lors de la création de la connexion WebSocket:', error);
          this.statusMessage = "Erreur: Impossible de se connecter au serveur";
        }
      },
      
      disconnectWebSocket() {
        if (this.websocket) {
          this.websocket.close();
          this.websocket = null;
        }
        this.isConnected = false;
      }
    }
  };
  </script>
  
  <style scoped>
  .charge-utile-form {
    width: 100%;
    max-width: 900px;
    margin: 0 auto;
    background-color: #ffffff;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    overflow: hidden;
  }
  
  .form-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 24px;
    background-color: #f8f8f8;
    border-bottom: 1px solid #e0e0e0;
  }
  
  .form-header h2 {
    margin: 0;
    font-size: 1.5rem;
    color: #333;
  }
  
  .form-status {
    padding: 6px 12px;
    border-radius: 4px;
    font-size: 0.875rem;
    font-weight: 500;
  }
  
  .status-success {
    background-color: #e6f7e9;
    color: #2e7d32;
  }
  
  .status-error {
    background-color: #fdecea;
    color: #d32f2f;
  }
  
  .status-pending {
    background-color: #e3f2fd;
    color: #1976d2;
  }
  
  .form-content {
    padding: 24px;
  }
  
  .form-section {
    margin-bottom: 24px;
  }
  
  .form-section h3 {
    margin: 0 0 16px 0;
    font-size: 1.25rem;
    color: #424242;
    border-bottom: 1px solid #e0e0e0;
    padding-bottom: 8px;
  }
  
  .form-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
    gap: 16px;
  }
  
  .form-item {
    margin-bottom: 16px;
  }
  
  .form-item label {
    display: block;
    margin-bottom: 8px;
    font-weight: 500;
    color: #616161;
  }
  
  .form-item input, 
  .form-item select {
    width: 100%;
    padding: 10px;
    border: 1px solid #bdbdbd;
    border-radius: 4px;
    font-size: 1rem;
    background-color: #fff;
  }
  
  .form-item input:focus, 
  .form-item select:focus {
    outline: none;
    border-color: #2196f3;
    box-shadow: 0 0 0 2px rgba(33, 150, 243, 0.2);
  }
  
  .form-actions {
    display: flex;
    justify-content: space-between;
    margin-top: 32px;
    padding-top: 16px;
    border-top: 1px solid #e0e0e0;
  }
  
  .action-button {
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    font-size: 1rem;
    font-weight: 500;
    cursor: pointer;
    transition: background-color 0.2s;
  }
  
  .action-button.reset {
    background-color: #f5f5f5;
    color: #616161;
  }
  
  .action-button.reset:hover {
    background-color: #e0e0e0;
  }
  
  .action-button.submit {
    background-color: #2196f3;
    color: white;
  }
  
  .action-button.submit:hover {
    background-color: #1976d2;
  }
  
  .action-button.submit:disabled {
    background-color: #bbdefb;
    cursor: not-allowed;
  }
  </style>