<template>
  <div id="app">
      <create-entreprise-form 
          v-if="!gameStarted" 
          :debugMode="debugMode"
          @entreprise-created="startGame"
      />
  </div>
</template>

<script>
import CreateEntrepriseForm from '../../components/MarcheFinancier/EntrepriseForm.vue'

export default {
  name: 'App',
  components: {
      CreateEntrepriseForm
  },
  data() {
      return {
          gameStarted: false,
          socket: null,
          debugMode: false
      }
  },
  mounted() {
        this.socket = new WebSocket("ws://localhost:3232");

        this.socket.onopen = () => {
            console.log("WebSocket connecté - EntrepriseForm");
            this.checkDebugMode();
        };

        this.socket.onmessage = (event) => {
            console.log("Message WebSocket reçu:", event.data);
            const data = JSON.parse(event.data);
            
            switch(data.action) {
                case "debugModeState":
                    this.debugMode = data.debugMode;
                    if (this.debugMode) {
                        this.gameStarted = true; // Démarrer directement en mode debug
                    }
                    break;
                case "entrepriseCreated":
                    if (data.success) {
                        this.gameStarted = true;
                    }
                    break;
            }
        };
    },
  methods: {
      checkDebugMode() {
          // Envoyer une requête pour vérifier le mode debug
          if (this.socket && this.socket.readyState === WebSocket.OPEN) {
              this.socket.send(JSON.stringify({
                  action: 'checkDebugMode'
              }));
          }
      },
      startGame(entreprise) {
          if (this.socket && this.socket.readyState === WebSocket.OPEN) {
              this.socket.send(JSON.stringify({
                  action: 'createEntreprise',
                  nom: entreprise.nom,
                  pays: entreprise.pays
              }));
          } else {
              console.error('La connexion WebSocket n\'est pas établie');
          }
      }
  }
}
</script>