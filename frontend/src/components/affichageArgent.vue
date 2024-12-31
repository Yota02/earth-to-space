<template>
    <div class="mb-8">
      <p class="text-2xl font-bold">Argent: {{ gameData.argent }}</p>
    </div>
  </template>
  
<script>
export default {
  name: "affichageArgent",
  data() {
    return {
      gameData: {
        argent: 0,
        pointsRecherche: 0,
        recherches: []
      },
      socket: null
    };
  },
  mounted() {
    this.socket = new WebSocket("ws://localhost:3232");

    this.socket.onmessage = (event) => {
      try {
        const data = JSON.parse(event.data);
        console.log("Données reçues :", data);

        this.gameData = {
          ...this.gameData,
          argent: data.argent,
        };
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
  }
};
</script>
