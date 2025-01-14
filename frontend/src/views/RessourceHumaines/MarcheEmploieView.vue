<template>
    <div class="employe-container">
      <h1>Liste des Employés</h1>
  
      <!-- Indicateur de chargement -->
      <div v-if="isLoading">
        <p>Chargement des employés...</p>
      </div>
  
      <!-- Grille d'employés -->
      <div v-else class="employe-grid">
        <div 
          v-for="(personne, index) in marcheEmploie" 
          :key="personne.prenom + personne.nom" 
          class="employe-details"
          :style="{ marginRight: (index + 1) % 5 === 0 ? '0' : '16px' }"
          @click="proposerEmbauche(personne)"
          role="button"
          tabindex="0"
          aria-label="Cliquez pour embaucher {{ personne.prenom }} {{ personne.nom }}"
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
  export default {
    name: "EmployeList",
    data() {
      return {
        salaireTotal: 0,
        marcheEmploie: [], // Liste des employés récupérés ou simulés
        socket: null,
        employeSelectionne: null, // Employé en cours de sélection
        isLoading: true, // Indicateur de chargement
      };
    },
    mounted() {
      this.socket = new WebSocket("ws://localhost:3232");
  
      // Gestion des messages WebSocket
      this.socket.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
  
          if (data.marcheEmploie) {
            this.marcheEmploie = data.marcheEmploie;
            this.isLoading = false; // Fin du chargement
          }
  
          // Réponse d'embauche
          if (data.action === "embaucheSuccess") {
            alert(`L'employé ${data.prenom} ${data.nom} a été embauché avec succès !`);
          } else if (data.error) {
            alert(`Erreur : ${data.error}`);
          }
        } catch (error) {
          console.error("Erreur lors du traitement des données WebSocket :", error);
          alert("Une erreur s'est produite lors de la communication avec le serveur.");
        }
      };
  
      // Gestion de la fermeture de la socket
      this.socket.onclose = () => {
        console.log("WebSocket fermé.");
      };
  
      // Gestion des erreurs WebSocket
      this.socket.onerror = (error) => {
        console.error("Erreur WebSocket :", error);
        alert("Impossible de se connecter au serveur.");
      };
    },
    beforeUnmount() {
      if (this.socket) {
        this.socket.close();
      }
    },
    methods: {
      proposerEmbauche(personne) {
        this.employeSelectionne = personne;
      },
      confirmerEmbauche() {
        if (this.socket) {
          this.socket.send(
            JSON.stringify({
              action: "embaucherEmploye",
              employe: this.employeSelectionne,
            })
          );
        }
        this.employeSelectionne = null; // Réinitialise la sélection
      },
      annulerEmbauche() {
        this.employeSelectionne = null;
      },
    },
  };
  </script>
  
  <style scoped>
  .employe-container {
    padding: 20px;
    font-family: Arial, sans-serif;
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
  