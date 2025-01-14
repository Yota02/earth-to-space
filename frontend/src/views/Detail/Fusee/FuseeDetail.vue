<template>
  <div class="fusee-container">
    <h1>Liste des Fusées</h1>
    <div v-for="fusee in fusees" :key="fusee.nom" class="fusee-details">
      <p><strong>Nom :</strong> {{ fusee.nom }}</p>
      <p><strong>Taille :</strong> {{ fusee.taille }}</p>
      <p><strong>Diamètre :</strong> {{ fusee.diametre }}</p>
      <p><strong>Poids Total :</strong> {{ fusee.poidsTotal }}</p>
      <p><strong>Altitude Max :</strong> {{ fusee.altitudeMax }} m</p>
      <p><strong>Booster-Principal : </strong> <router-link to="/booster-details" @click="closeMenu">  {{ fusee.boosterPrincipal }} </router-link></p>

      <p>
        <strong>Système de Sécurité :</strong>
        <span
          class="status-circle"
          :class="{ green: fusee.systemeSecurite, red: !fusee.systemeSecurite }"
        ></span>
      </p>
      <p><strong>État :</strong> {{ fusee.etat }}</p>
      <div>
        <strong>Charges Utiles :</strong>
        <ul>
          <li v-for="charge in fusee.poidChargeUtiles" :key="charge.nom">
            {{ charge.nom }} ({{ charge.poids }} kg)
          </li>
        </ul>
      </div>
      <hr />
    </div>
  </div>
</template>

<script>
export default {
  name: "FuseeList",
  data() {
    return {
      fusees: [], // Liste des fusées
      socket: null, // WebSocket
    };
  },
  mounted() {
    this.socket = new WebSocket("ws://localhost:3232");

    this.socket.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);

        if (data.fusees) {
          this.fusees = data.fusees;
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
    if (this.socket) {
      this.socket.close();
    }
  },
};
</script>

<style scoped>
.fusee-container {
  max-width: 800px;
  margin: auto;
  padding: 16px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.fusee-details {
  margin-bottom: 16px;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  background-color: #fff;
}

.fusee-details p {
  margin: 8px 0;
}

.status-circle {
  display: inline-block;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  margin-left: 8px;
}

.status-circle.green {
  background-color: #4caf50;
}

.status-circle.red {
  background-color: #f44336;
}

ul {
  padding-left: 20px;
}

ul li {
  margin: 4px 0;
}
</style>
