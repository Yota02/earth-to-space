<template>
    <div class="employe-container">
      <h1>Liste des Employés</h1>
      <h1> Salaire a payer : {{ salaireTotal }}</h1>
      
      <div class="employe-grid">
        <div 
          v-for="(employe, index) in employes" 
          :key="employe.prenom + employe.nom" 
          class="employe-details"
          :style="{ marginRight: (index + 1) % 5 === 0 ? '0' : '16px' }"  
        >

          <p><strong>Prénom :</strong> {{ employe.prenom }}</p>
          <p><strong>Nom :</strong> {{ employe.nom }}</p>
          <p><strong>Âge :</strong> {{ employe.age }} ans</p>
          <p><strong>Salaire :</strong> {{ employe.salaire }}€</p>
          <p><strong>Sexe :</strong> 
            <span v-if="employe.sexe === 'Homme'">🚹</span>
            <span v-if="employe.sexe === 'Femme'">🚺</span>
          </p>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: "EmployeList",
    data() {
      return {
        salaireTotal: 0,
        employes: [], // Liste des employés
        socket: null, // WebSocket
      };
    },
    mounted() {
      this.socket = new WebSocket("ws://localhost:3232");
  
      this.socket.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
  
          if (data.employes) {
            this.salaireTotal = data.salaireTotal;
            this.employes = data.employes;
          }
        } catch (error) {
          console.error("Erreur lors du traitement des données WebSocket :", error);
        }
      };
  
      this.socket.onclose = () => {
        console.log("WebSocket fermé.");
      };
    },
    beforeUnmount() {
      // Fermer la connexion WebSocket lorsque le composant est démonté
      if (this.socket) {
        this.socket.close();
      }
    },
  };
  </script>
  
  <style scoped>
  .employe-container {
    max-width: 800px;
    margin: auto;
    padding: 16px;
    background-color: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  }
  
  .employe-grid {
    display: grid;
    grid-template-columns: repeat(5, 1fr); /* 5 colonnes de même largeur */
    gap: 16px;
    flex-wrap: wrap;
  }
  
  .employe-details {
    padding: 12px;
    border: 1px solid #ddd;
    border-radius: 8px;
    background-color: #fff;
  }
  
  .employe-details p {
    margin: 8px 0;
  }
  </style>
  