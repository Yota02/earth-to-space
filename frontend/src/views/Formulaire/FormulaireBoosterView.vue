<template>
  <div class="main">
    <div class="formulaire-creation">
      <h2>Créer un nouveau lanceur</h2>
      <form @submit.prevent="submitForm">
        <div class="form-group">
          <label for="nom">Nom du lanceur:</label>
          <input type="text" id="nom" v-model="lanceur.nom">
        </div>

        <div class="form-group">
          <label for="poids">Poids à vide (tonnes):</label>
          <input type="number" id="poids" v-model.number="lanceur.poidsAVide" @input="recalculer">
        </div>

        <h3>Configuration des moteurs</h3>
        <div class="moteur-config">
          <div class="form-group">
            <label for="typeMoteur">Type de moteur:</label>
            <select id="typeMoteur" v-model="lanceur.moteurNom" @change="recalculer">
              <option value="">Sélectionnez un moteur</option>
              <option value="Chimiques">Moteur Chimique - Poussée: 2279kN - Propergol: Méthane</option>
              <option value="Electriques">Moteur Électrique - Poussée: 0.5kN - Propulsion électrique</option>
              <option value="Nucleaires">Moteur Nucléaire - Poussée: 333.6kN - Propergol: Nucléaire</option>
              <option value="Ioniques">Moteur Ionique - Poussée: 0.236kN - Propergol: Ionique</option>
              <option value="Solides">Moteur Solide - Poussée: 1688.4kN - Propergol: Oxygène</option>
            </select>
          </div>

          <div class="form-group">
            <label for="nombreMoteurs">Nombre de moteurs:</label>
            <input type="number" id="nombreMoteurs" v-model.number="lanceur.nombreMoteurs" min="1" @input="recalculer">
          </div>
        </div>

        <h3>Configuration des réservoirs</h3>
        <div class="form-group">
          <label for="ergol">Type d'ergol:</label>
          <select id="ergol" v-model="lanceur.ergolType" @change="recalculer">
            <option value="">Sélectionnez un ergol</option>
            <option v-for="ergol in ergolsDisponibles" :key="ergol.nom" :value="ergol.nom">
              {{ ergol.nom }} - Densité: {{ ergol.densite }}kg/m³
            </option>
          </select>
        </div>

        <div class="form-group">
          <label for="capaciteReservoir">Capacité des réservoirs (m³):</label>
          <input type="number" id="capaciteReservoir" v-model.number="lanceur.capaciteReservoir" @input="recalculer">
        </div>

        <div class="form-group">
          <label for="taille">Taille (m):</label>
          <input type="number" id="taille" v-model.number="lanceur.taille">
        </div>

        <div class="form-group">
          <label for="diametre">Diamètre (m):</label>
          <input type="number" id="diametre" v-model.number="lanceur.diametre">
        </div>

        <div class="specifications">
          <label>
            <input type="checkbox" v-model="lanceur.estPrototype">
            Prototype
          </label>
          <label>
            <input type="checkbox" v-model="lanceur.estReetulisable">
            Réutilisable
          </label>
          <label>
            <input type="checkbox" v-model="lanceur.aSystèmeAutoDestruction">
            Système d'auto-destruction
          </label>
        </div>

        <div class="performances-panel">
          <h3>Performances calculées</h3>
          <div class="performance-item">
            <label>Poussée totale:</label>
            <span>{{ formatNumber(performances.pousseeTotale) }} kN</span>
          </div>
          <div class="performance-item">
            <label>Delta-V:</label>
            <span>{{ formatNumber(performances.deltaV) }} m/s</span>
          </div>
          <div class="performance-item">
            <label>Masse de carburant:</label>
            <span>{{ formatNumber(performances.masseCarburant) }} kg</span>
          </div>
          <div class="performance-item">
            <label>Vitesse max:</label>
            <span>{{ formatNumber(performances.vitesseMax) }} m/s</span>
          </div>
          <div class="performance-item">
            <label>Altitude max:</label>
            <span>{{ formatNumber(performances.altitudeMax) }} km</span>
          </div>
        </div>

        <button type="submit" :disabled="!isFormValid">Créer le lanceur</button>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name: 'FormulaireCreation',
  data() {
    return {
      ws: null,
      connectionStatus: 'connecting',
      lanceur: {
        nom: '',
        taille: 0,
        diametre: 0,
        poidsAVide: 0,
        moteurNom: '',
        nombreMoteurs: 1,
        ergolType: '',
        capaciteReservoir: 0,
        estPrototype: false,
        estReetulisable: false,
        aSystèmeAutoDestruction: false,
        etat: 1,
        necessiteMaintenance: false
      },
      moteurSpecs: {
        'Chimiques': { pousse: 2279.0, isp: 380, consommation: 473.0 },
        'Electriques': { pousse: 0.5, isp: 2000, consommation: 0.1 },
        'Nucleaires': { pousse: 333.6, isp: 800, consommation: 8.5 },
        'Ioniques': { pousse: 0.236, isp: 3000, consommation: 0.005 },
        'Solides': { pousse: 1688.4, isp: 270, consommation: 2721.0 }
      },
      ergolsDisponibles: [
        { nom: "OXYGEN", densite: 1141 },
        { nom: "KEROSENE", densite: 820 },
        { nom: "HYDROGENE", densite: 71 },
        { nom: "METHANES", densite: 422 },
        { nom: "HELIUM", densite: 125 },
        { nom: "AZOTE", densite: 807 },
        { nom: "ALCOOL", densite: 789 },
        { nom: "BIODIESEL", densite: 880 },
        { nom: "IONIQUE", densite: 1000 },
        { nom: "NUCLEAIRE", densite: 1000 },
        { nom: "PROPULSION_ELECTRIQUE", densite: 1000 },
        { nom: "GAZ_NATUREL", densite: 422 }
      ],
      performances: {
        pousseeTotale: 0,
        deltaV: 0,
        masseCarburant: 0,
        vitesseMax: 0,
        altitudeMax: 0
      }
    }
  },

  computed: {
    isFormValid() {
      return this.lanceur.nom &&
             this.lanceur.taille > 0 &&
             this.lanceur.diametre > 0 &&
             this.lanceur.poidsAVide > 0 &&
             this.lanceur.moteurNom &&
             this.lanceur.nombreMoteurs > 0 &&
             this.lanceur.ergolType &&
             this.lanceur.capaciteReservoir > 0;
    }
  },

  methods: {
    initWebSocket() {
      this.ws = new WebSocket('ws://localhost:3232');

      this.ws.onopen = () => {
        this.connectionStatus = 'connected';
      };

      this.ws.onclose = () => {
        this.connectionStatus = 'disconnected';
      };

      this.ws.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          if (data.action === "boosterCreated") {
            console.log("Booster créé avec succès:", data.booster);
            this.resetForm();
          }
        } catch (error) {
          console.error('Erreur lors du parsing du message:', error);
        }
      };
    },

    recalculer() {
      if (!this.lanceur.moteurNom || !this.lanceur.ergolType) {
        return;
      }

      const moteur = this.moteurSpecs[this.lanceur.moteurNom];
      const ergol = this.ergolsDisponibles.find(e => e.nom === this.lanceur.ergolType);

      // Calcul de la poussée totale
      this.performances.pousseeTotale = moteur.pousse * this.lanceur.nombreMoteurs;

      // Calcul de la masse de carburant
      this.performances.masseCarburant = this.lanceur.capaciteReservoir * ergol.densite;

      // Calcul du ratio de masse
      const masseTotaleInitiale = (this.lanceur.poidsAVide * 1000) + this.performances.masseCarburant;
      const masseTotaleFinale = this.lanceur.poidsAVide * 1000;
      const massRatio = masseTotaleInitiale / masseTotaleFinale;

      // Calcul du Delta-V (équation de Tsiolkovsky)
      const g0 = 9.81;
      const pertes = 1500; // Pertes diverses (traînée, gravité, etc.)
      this.performances.deltaV = (moteur.isp * g0 * Math.log(massRatio)) - pertes;

      // Estimation de la vitesse max et altitude max
      this.performances.vitesseMax = this.performances.deltaV * 0.7;
      this.performances.altitudeMax = (this.performances.deltaV * this.performances.deltaV) / (2 * g0) / 1000;
    },

    createBooster() {
      const moteur = {
        nom: this.lanceur.moteurNom,
        poids: this.moteurSpecs[this.lanceur.moteurNom].pousse / 100,
        pousse: this.moteurSpecs[this.lanceur.moteurNom].pousse,
        consommationCarburant: this.moteurSpecs[this.lanceur.moteurNom].consommation
      };

      const reservoir = {
        nom: `Reservoir ${this.lanceur.ergolType}`,
        poidsAvide: this.lanceur.capaciteReservoir * 0.1,
        poids: this.lanceur.capaciteReservoir * this.ergolsDisponibles.find(e => e.nom === this.lanceur.ergolType).densite,
        capacite: this.lanceur.capaciteReservoir,
        type: this.lanceur.ergolType
      };

      return {
        nom: this.lanceur.nom,
        taille: this.lanceur.taille,
        diametre: this.lanceur.diametre,
        poidsAVide: this.lanceur.poidsAVide,
        altitudeMax: this.performances.altitudeMax * 1000,
        VitesseMax: this.performances.vitesseMax,
        moteur: [moteur], // On envoie un tableau avec un seul moteur
        nombreMoteurs: this.lanceur.nombreMoteurs,
        reservoirs: [reservoir],
        estPrototype: this.lanceur.estPrototype,
        estReetulisable: this.lanceur.estReetulisable,
        aSystèmeAutoDestruction: this.lanceur.aSystèmeAutoDestruction,
        etat: this.lanceur.etat,
        necessiteMaintenance: this.lanceur.necessiteMaintenance,
        historiquesLancement: []
      };
    },

    async submitForm() {
      try {
        const boosterData = this.createBooster();
        
        if (this.ws && this.ws.readyState === WebSocket.OPEN) {
          this.ws.send(JSON.stringify({
            action: 'createBooster',
            booster: boosterData
          }));
        }
      } catch (error) {
        console.error('Erreur lors de la création du booster:', error);
      }
    },

    resetForm() {
      this.lanceur = {
        nom: '',
        taille: 0,
        diametre: 0,
        poidsAVide: 0,
        moteurNom: '',
        nombreMoteurs: 1,
        ergolType: '',
        capaciteReservoir: 0,
        estPrototype: false,
        estReetulisable: false,
        aSystèmeAutoDestruction: false,
        etat: 1,
        necessiteMaintenance: false
      };
      this.performances = {
        pousseeTotale: 0,
        deltaV: 0,
        masseCarburant: 0,
        vitesseMax: 0,
        altitudeMax: 0
      };
    },

    formatNumber(number) {
      return Number(number).toLocaleString('fr-FR', { maximumFractionDigits: 2 });
    }
  },

  mounted() {
    this.initWebSocket();
  },

  beforeUnmount() {
    if (this.ws) {
      this.ws.close();
    }
  }
};
</script>

<style scoped>
h1,
h2,
h3,
h4,
label {
  color: black;
}

.main {
  margin-top: 20px;
  height: 100%;
  background-color: white;
}

.formulaire-creation {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

input,
select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.performances-panel {
  background-color: #f5f5f5;
  padding: 20px;
  border-radius: 8px;
  margin: 20px 0;
}

.performance-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  padding: 5px 0;
  border-bottom: 1px solid #ddd;
}

.debug-info {
  font-family: monospace;
  white-space: pre-wrap;
  font-size: 12px;
}

button {
  background-color: #4CAF50;
  color: white;
  padding: 12px 24px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
  margin-top: 20px;
}

button:hover {
  background-color: #45a049;
}
</style>