<template>
    <div class="boosters-app">
      <div class="connection-status" :class="{ 'connected': isConnected }">
        {{ isConnected ? 'Connecté au serveur' : 'Déconnecté' }}
      </div>
      
      <div class="app-container">
        <div class="boosters-list-container">
          <BoostersList 
            :boosters="boosters" 
            :selectedBoosterId="selectedBooster ? (selectedBooster.id || selectedBooster.nom) : null"
            @select-booster="selectBooster" 
          />
        </div>
        
        <div class="booster-detail-container" v-if="selectedBooster">
          <BoosterDetail :booster="selectedBooster" />
        </div>
        
        <div class="no-booster-selected" v-else>
          <p>Sélectionnez un booster pour voir ses détails</p>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import BoostersList from '../../components/Fusee/BoosterList.vue';
  import BoosterDetail from '../../components/Fusee/BoosterDetail.vue';
  
  export default {
    name: 'BoostersApp',
    components: {
      BoostersList,
      BoosterDetail
    },
    data() {
      return {
        websocket: null,
        isConnected: false,
        boosters: [],
        selectedBooster: null,
        pollingInterval: null
      };
    },
    mounted() {
      this.connectWebSocket();
    },
    beforeUnmount() {
      this.disconnectWebSocket();
    },
    methods: {
      selectBooster(booster) {
        this.selectedBooster = booster;
      },
      connectWebSocket() {
        try {
          this.websocket = new WebSocket('ws://localhost:3232');
          
          this.websocket.onopen = () => {
            console.log('Connexion WebSocket établie');
            this.isConnected = true;
            
            // Demander les données des boosters
            this.websocket.send(JSON.stringify({ action: 'getBoostersState' }));
            
            // Demander périodiquement les mises à jour
            this.pollingInterval = setInterval(() => {
              if (this.isConnected) {
                this.websocket.send(JSON.stringify({ action: 'getBoostersState' }));
              }
            }, 5000);
          };
          
          this.websocket.onmessage = (event) => {
            try {
              console.log('Message reçu:', event.data);
              const data = JSON.parse(event.data);
              
              // Vérifier si nous avons reçu les boosters
              if (data.boosters && Array.isArray(data.boosters)) {
                this.boosters = data.boosters;
                console.log('Données de boosters mises à jour:', this.boosters);
                
                // Si un booster est sélectionné, mettre à jour ses données
                if (this.selectedBooster) {
                  const updatedBooster = this.boosters.find(b => 
                    (b.id && b.id === this.selectedBooster.id) || 
                    (b.nom === this.selectedBooster.nom)
                  );
                  if (updatedBooster) {
                    this.selectedBooster = updatedBooster;
                  }
                }
                
                // Sélectionner automatiquement le premier booster si aucun n'est sélectionné
                if (!this.selectedBooster && this.boosters.length > 0) {
                  this.selectedBooster = this.boosters[0];
                }
              }
            } catch (error) {
              console.error('Erreur lors du traitement des données WebSocket:', error);
            }
          };
          
          this.websocket.onerror = (error) => {
            console.error('Erreur WebSocket:', error);
            this.isConnected = false;
          };
          
          this.websocket.onclose = () => {
            console.log('Connexion WebSocket fermée');
            this.isConnected = false;
            
            // Annuler le polling en cas de déconnexion
            if (this.pollingInterval) {
              clearInterval(this.pollingInterval);
            }
            
            // Tentative de reconnexion après 5 secondes
            setTimeout(() => {
              if (!this.isConnected) {
                this.connectWebSocket();
              }
            }, 5000);
          };
        } catch (error) {
          console.error('Erreur lors de la création de la connexion WebSocket:', error);
        }
      },
      disconnectWebSocket() {
        if (this.pollingInterval) {
          clearInterval(this.pollingInterval);
        }
        
        if (this.websocket) {
          this.websocket.close();
          this.websocket = null;
        }
        this.isConnected = false;
      }
    }
  };
  </script>
  
  <style>
  .boosters-app {
    font-family: Arial, sans-serif;
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
  }
  
  .connection-status {
    position: fixed;
    top: 10px;
    right: 10px;
    padding: 8px 16px;
    border-radius: 4px;
    font-size: 0.8rem;
    background-color: #f44336;
    color: white;
    z-index: 1000;
  }
  
  .connection-status.connected {
    background-color: #4caf50;
  }
  
  .app-container {
    display: grid;
    grid-template-columns: 1fr 2fr;
    gap: 20px;
    margin-top: 40px;
  }
  
  .boosters-list-container {
    background-color: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    overflow: hidden;
  }
  
  .booster-detail-container {
    background-color: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    overflow: hidden;
  }
  
  .no-booster-selected {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    background-color: #f5f5f5;
    border-radius: 8px;
    color: #666;
    font-size: 1.2rem;
    padding: 40px;
  }
  
  @media (max-width: 768px) {
    .app-container {
      grid-template-columns: 1fr;
    }
  }
  </style>