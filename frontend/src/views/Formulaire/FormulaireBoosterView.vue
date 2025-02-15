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
        <input type="number" id="poids" v-model.number="lanceur.poids" @input="recalculer">
      </div>

      <h3>Configuration des moteurs</h3>
      <div class="moteur-config">
        <div class="form-group">
          <label for="typeMoteur">Type de moteur:</label>
          <select id="typeMoteur" v-model.number="lanceur.moteurType" @change="recalculer">
            <option value="">Sélectionnez un moteur</option>
            <option v-for="moteur in typesMoteursDisponibles" :key="moteur.id" :value="moteur.id">
              {{ moteur.nom }} - ISP: {{ moteur.isp }}s - Poussée: {{ moteur.poussee }}kN
            </option>
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
        <select id="ergol" v-model.number="lanceur.ergolType" @change="recalculer">
          <option value="">Sélectionnez un ergol</option>
          <option v-for="ergol in ergolsDisponibles" :key="ergol.id" :value="ergol.id">
            {{ ergol.nom }} - Densité: {{ ergol.densite }}kg/m³
          </option>
        </select>
      </div>

      <div class="form-group">
        <label for="capaciteReservoir">Capacité des réservoirs (m³):</label>
        <input type="number" id="capaciteReservoir" v-model.number="lanceur.capaciteReservoir" @input="recalculer">
      </div>

      <div class="debug-info">
        <pre>{{ debugInfo }}</pre>
      </div>

      <div class="performances-panel">
        <h3>Performances calculées</h3>
        <div class="performance-item"><label>Poussée totale:</label> <span>{{ formatNumber(performances.pousseeTotale) }} kN</span></div>
        <div class="performance-item"><label>Delta-V:</label> <span>{{ formatNumber(performances.deltaV) }} m/s</span></div>
        <div class="performance-item"><label>Masse totale de carburant:</label> <span>{{ formatNumber(performances.masseCarburant) }} kg</span></div>
        <div class="performance-item"><label>Charge utile en LEO:</label> <span>{{ formatNumber(performances.chargeLEO) }} tonnes</span></div>
        <div class="performance-item"><label>Charge utile en GEO:</label> <span>{{ formatNumber(performances.chargeGEO) }} tonnes</span></div>
        <div class="performance-item"><label>Charge utile en LUNE:</label> <span>{{ formatNumber(performances.chargeLUNE) }} tonnes</span></div>
      </div>

      <button type="submit">Créer le lanceur</button>
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
        poids: 0, // en tonnes
        moteurType: null,
        nombreMoteurs: 1,
        ergolType: null,
        capaciteReservoir: 0, // en m³
        chargeUtile: 0 // en tonnes
      },
      typesMoteursDisponibles: [
        { id: 1, nom: 'Merlin 1D', isp: 311, poussee: 845 },
        { id: 2, nom: 'Raptor', isp: 350, poussee: 2200 },
        { id: 3, nom: 'BE-4', isp: 340, poussee: 2400 },
        { id: 4, nom: 'RD-180', isp: 338, poussee: 3830 }
      ],
      ergolsDisponibles: [], // Sera rempli dynamiquement via WebSocket
      performances: {
        pousseeTotale: 0,
        deltaV: 0,
        masseCarburant: 0,
        chargeLEO: 0,
        chargeGEO: 0,
        chargeLUNE: 0
      },
      debugInfo: 'Aucun calcul effectué'
    };
  },
  methods: {
    initWebSocket() {
      this.ws = new WebSocket('ws://localhost:3232');

      this.ws.onopen = () => {
        this.connectionStatus = 'connected';
        this.requestCarburants();
      };

      this.ws.onclose = () => {
        this.connectionStatus = 'error';
      };

      this.ws.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data);
          
          if (data.action === "carburantsState" && Array.isArray(data.carburants)) {
            // Transformer les carburants reçus en format compatible
            this.ergolsDisponibles = data.carburants.map((carburant, index) => ({
              id: index + 1,
              nom: carburant.nom,
              densite: this.getDensiteForCarburant(carburant.nom)
            }));
          }
        } catch (error) {
          console.error('Erreur lors du parsing du message:', error);
        }
      };
    },

    requestCarburants() {
      if (this.ws && this.ws.readyState === WebSocket.OPEN) {
        this.ws.send(JSON.stringify({
          action: 'getCarburants'
        }));
      }
    },

    // Fonction utilitaire pour obtenir la densité en fonction du type de carburant
    getDensiteForCarburant(nom) {
      const densites = {
        "OXYGEN": 1141, // LOX
        "KEROSENE": 820, // RP-1
        "HYDROGENE": 71, // LH2
        "METHANES": 422, // LCH4
        "HELIUM": 125,
        "AZOTE": 807,
        "ALCOOL": 789, // Ethanol
        "BIODIESEL": 880,
        // Valeurs par défaut pour les autres types
        "IONIQUE": 1000,
        "NUCLEAIRE": 1000,
        "PROPULSION_ELECTRIQUE": 1000,
        "GAZ_NATUREL": 422
      };
      return densites[nom] || 1000; // Valeur par défaut si non trouvé
    },

    recalculer() {
      console.log('Recalcul déclenché');
      const moteur = this.typesMoteursDisponibles.find(m => m.id === this.lanceur.moteurType);
      const ergol = this.ergolsDisponibles.find(e => e.id === this.lanceur.ergolType);

      if (!moteur || !ergol || !this.lanceur.poids || !this.lanceur.capaciteReservoir) {
        this.debugInfo = 'Données manquantes';
        return;
      }

      // Calculs des performances inchangés
      this.performances.pousseeTotale = moteur.poussee * this.lanceur.nombreMoteurs;
      this.performances.masseCarburant = this.lanceur.capaciteReservoir * ergol.densite;

      const poidsKg = this.lanceur.poids * 1000;
      const masseTotaleInitiale = poidsKg + this.performances.masseCarburant + (this.lanceur.chargeUtile * 1000);
      const masseTotaleFinale = poidsKg + (this.lanceur.chargeUtile * 1000);
      const massRatio = masseTotaleInitiale / masseTotaleFinale;

      const g0 = 9.81;
      const pertes = 1800;
      this.performances.deltaV = (moteur.isp * g0 * Math.log(massRatio)) - pertes;

      this.performances.chargeLEO = this.chargeMax(9300);
      this.performances.chargeGEO = this.chargeMax(12000);
      this.performances.chargeLUNE = this.chargeMax(15000);

      this.debugInfo = `Poussée totale: ${this.performances.pousseeTotale} kN\n`
                       + `Delta-V: ${this.performances.deltaV.toFixed(2)} m/s\n`
                       + `Masse de carburant: ${this.performances.masseCarburant} kg\n`
                       + `Charge utile LEO: ${this.performances.chargeLEO.toFixed(2)} tonnes\n`
                       + `Charge utile GEO: ${this.performances.chargeGEO.toFixed(2)} tonnes\n`
                       + `Charge utile LUNE: ${this.performances.chargeLUNE.toFixed(2)} tonnes\n`;

      console.log(this.debugInfo);
    },

    chargeMax(deltaV_requis) {
      if (this.performances.deltaV < deltaV_requis) return 0;
      const coeff = Math.exp((this.performances.deltaV - deltaV_requis) / (this.typesMoteursDisponibles[0].isp * 9.81));
      const chargeMaxEnKg = (this.lanceur.poids * 1000 + this.performances.masseCarburant) * coeff;
      return chargeMaxEnKg / 1000;
    },

    formatNumber(number) {
      return Number(number).toLocaleString('fr-FR', { maximumFractionDigits: 2 });
    },

    submitForm() {
      console.log('Données du lanceur:', this.lanceur);
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
h1, h2, h3, h4 {
  color: white;
}

.main{
  margin-top: 20px;
  height: 100%;
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

input, select {
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