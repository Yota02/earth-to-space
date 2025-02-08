<template>
    <div class="subventions-container">
      <!-- Subventions actives -->
      <div class="subventions-section">
        <h3 class="subventions-title">Subventions actives</h3>
        <div class="subventions-list" v-if="subventionsActives.length > 0">
          <div v-for="(subvention, index) in subventionsActives" 
               :key="subvention.id" 
               class="subvention-item">
            <div class="subvention-info">
              <span class="subvention-id">#{{ subvention.id }}</span>
              <span class="subvention-name">{{ subvention.nom }}</span>
              <div class="subvention-details">
                <span class="subvention-quantity">{{ subvention.quantite }}$</span>
                <div class="duration-bar-container">
                  <div class="duration-bar" 
                       :style="{ width: calculateDurationWidth(subvention.duree) + '%' }">
                    <span class="duration-text">{{ subvention.duree }} jours</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div v-else class="no-subventions">
          Aucune subvention active
        </div>
      </div>
  
      <!-- Subventions disponibles -->
      <div class="subventions-section">
        <h3 class="subventions-title">Subventions disponibles</h3>
        <div class="subventions-list" v-if="subventionsDisponibles.length > 0">
          <div v-for="(subvention, index) in subventionsDisponibles" 
               :key="subvention.id" 
               class="subvention-item available"
               @click="activerSubvention(subvention)"
               :class="{ 'disabled': !canActivateSubvention(subvention) }">
            <div class="subvention-info">
              <span class="subvention-id">#{{ subvention.id }}</span>
              <span class="subvention-name">{{ subvention.nom }}</span>
              <div class="subvention-details">
                <span class="subvention-quantity">{{ subvention.quantite }}$</span>
                <span class="subvention-duration">Durée: {{ subvention.duree }} jours</span>
              </div>
            </div>
            <button class="activate-btn" 
                    :disabled="!canActivateSubvention(subvention)"
                    :title="!canActivateSubvention(subvention) ? 'Ressources insuffisantes' : 'Activer la subvention'">
              Activer
            </button>
          </div>
        </div>
        <div v-else class="no-subventions">
          Aucune subvention disponible
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'Subventions',
    data() {
      return {
        subventionsActives: [],
        subventionsDisponibles: [],
        socket: null,
        gameData: {
          argent: 0,
          pointRecherche: 0,
          pointConstruction: 0,
          pointIngenieur: 0
        }
      };
    },
    methods: {
      // Calcule la largeur de la barre de progression en fonction de la durée
      calculateDurationWidth(duration) {
        // Supposons qu'une subvention peut durer jusqu'à 365 jours maximum
        return (duration / 365) * 100;
      },
      
      // Tente d'activer une subvention
      activerSubvention(subvention) {
        // Vérifie si la subvention peut être activée
        if (!this.canActivateSubvention(subvention)) return;
        
        // Envoi de la demande d'activation via WebSocket
        this.socket.send(JSON.stringify({
          type: "activateSubvention",
          subventionId: String(subvention.id),
          subventionNom: subvention.nom
        }));
      },
      
      // Vérifie si une subvention peut être activée
      canActivateSubvention(subvention) {
        // Logique de vérification des ressources
        // Exemple: vérifier si le joueur a assez d'argent
        return this.gameData.argent >= subvention.quantite;
      },
      
      // Gère les messages entrants du WebSocket
      handleWebSocketMessage(event) {
        try {
          const data = JSON.parse(event.data);
          
          switch(data.type) {
            case "getSubventionsState":
              // Mise à jour des listes de subventions
              this.subventionsActives = data.subventionsActives || [];
              this.subventionsDisponibles = data.subventionsDisponibles || [];
              break;
            
            case "updateGameData":
              // Mise à jour des données du jeu
              this.gameData = { ...this.gameData, ...data.gameData };
              break;
            
            case "subventionActivated":
              // Gestion de l'activation réussie d'une subvention
              this.handleSubventionActivation(data.subvention);
              break;
          }
        } catch (error) {
          console.error("Erreur lors du traitement des données WebSocket:", error);
        }
      },
      
      // Gère l'activation d'une subvention
      handleSubventionActivation(subvention) {
        // Logique pour gérer l'activation (peut afficher un message, etc.)
        console.log(`Subvention ${subvention.nom} activée avec succès`);
        
        // Optionnel: mettre à jour les listes de subventions
        // Déplacer la subvention de disponibles à actives
      }
    },
    
    // Configuration du WebSocket à la création du composant
    mounted() {
      // Établit la connexion WebSocket
      this.socket = new WebSocket("ws://localhost:3232");
      
      // Gestionnaire des messages entrants
      this.socket.onmessage = this.handleWebSocketMessage;
      
      // À l'ouverture de la connexion, demande l'état initial
      this.socket.onopen = () => {
        this.socket.send(JSON.stringify({
          type: "getSubventionsState"
        }));
      };
      
      // Gestion des erreurs de connexion
      this.socket.onerror = (error) => {
        console.error("Erreur de connexion WebSocket:", error);
      };
    },
    
    // Fermeture propre du WebSocket
    beforeUnmount() {
      if (this.socket) {
        this.socket.close();
      }
    }
  };
  </script>
  
  <style scoped>
  .subventions-container {
    background-color: rgba(0, 0, 0, 0.6);
    border-radius: 8px;
    padding: 16px;
    max-width: 400px;
    color: white;
  }
  
  .subventions-section {
    margin-bottom: 24px;
  }
  
  .subventions-section:last-child {
    margin-bottom: 0;
  }
  
  .subventions-title {
    font-size: 1.2rem;
    margin: 0 0 16px 0;
  }
  
  .subventions-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }
  
  .subvention-item {
    background-color: rgba(255, 255, 255, 0.1);
    border-radius: 6px;
    padding: 12px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
  .subvention-id {
    background-color: rgba(255, 255, 255, 0.2);
    color: white;
    padding: 2px 6px;
    border-radius: 4px;
    font-size: 0.8rem;
    margin-right: 8px;
  }
  
  .subvention-name {
    font-weight: bold;
    font-size: 0.9rem;
  }
  
  .subvention-details {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-top: 8px;
  }
  
  .subvention-quantity {
    color: #4caf50;
    font-weight: bold;
  }
  
  .duration-bar-container {
    flex-grow: 1;
    background-color: rgba(255, 255, 255, 0.1);
    border-radius: 4px;
    height: 20px;
    overflow: hidden;
  }
  
  .duration-bar {
    background-color: #2196f3;
    height: 100%;
    display: flex;
    align-items: center;
    padding: 0 8px;
    transition: width 0.3s ease;
  }
  
  .subvention-duration {
    color: rgba(255, 255, 255, 0.8);
    font-size: 0.9rem;
  }
  
  .activate-btn {
    background-color: #4caf50;
    color: white;
    border: none;
    border-radius: 4px;
    padding: 8px 16px;
    cursor: pointer;
    font-weight: bold;
    transition: background-color 0.2s;
  }
  
  .activate-btn:hover:not(:disabled) {
    background-color: #45a049;
  }
  
  .activate-btn:disabled {
    background-color: #cccccc;
    cursor: not-allowed;
  }
  
  .no-subventions {
    color: rgba(255, 255, 255, 0.6);
    text-align: center;
    padding: 20px;
  }
  
  .available {
    cursor: pointer;
    transition: background-color 0.2s;
  }
  
  .available:hover:not(.disabled) {
    background-color: rgba(255, 255, 255, 0.2);
  }
  
  .disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
  
  @media (max-width: 768px) {
    .subventions-container {
      max-width: 100%;
    }
  }
</style>