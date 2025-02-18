<template>
    <div class="entreprise-info">
      <div class="info-block">
        <h3>{{ entreprise.nom }}</h3>
        <div class="details">
          <p><span>Pays:</span> {{ entreprise.pays }}</p>
          <p><span>Capitalisation:</span> {{ formatNumber(entreprise.capitalisationBoursiere) }} €</p>
          <p><span>Cours Action:</span> {{ formatNumber(entreprise.coursAction) }} €</p>
          <p><span>Dividende/Action:</span> {{ formatNumber(entreprise.dividendeParAction) }} €</p>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'EntrepriseInfo',
    data() {
      return {
        entreprise: {
          nom: '',
          pays: '',
          capitalisationBoursiere: 0,
          coursAction: 0,
          dividendeParAction: 0
        },
        socket: null
      }
    },
    mounted() {
      this.connectWebSocket();
    },
    methods: {
      connectWebSocket() {
        this.socket = new WebSocket('ws://localhost:3232');
        
        this.socket.onopen = () => {
          this.requestEntrepriseData();
        };
        
        this.socket.onmessage = (event) => {
          const data = JSON.parse(event.data);
          if (data.action === 'entrepriseData') {
            this.entreprise = data.entreprise;
          }
        };
      },
      requestEntrepriseData() {
        if (this.socket.readyState === WebSocket.OPEN) {
          this.socket.send(JSON.stringify({
            action: 'getEntrepriseData'
          }));
        }
      },
      formatNumber(number) {
        return new Intl.NumberFormat('fr-FR').format(number);
      }
    }
  }
  </script>
  
  <style scoped>
  .entreprise-info {
    position: fixed;
    top: 10%;
    right: 20px;
    background: rgba(26, 27, 58, 0.95);
    border-radius: 8px;
    padding: 15px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
    color: white;
    z-index: 1000;
    min-width: 250px;
    border: 1px solid rgba(88, 103, 221, 0.2);
  }
  
  .info-block {
    text-align: left;
  }
  
  .info-block h3 {
    margin: 0 0 10px 0;
    font-size: 1.2em;
    color: #58dddd;
  }
  
  .details p {
    margin: 5px 0;
    font-size: 0.9em;
  }
  
  .details span {
    color: #8888dd;
    margin-right: 5px;
  }
  </style>