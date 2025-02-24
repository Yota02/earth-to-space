<template>
    <div class="booster-display">
      <div class="booster-header">
        <h2>{{ booster.nom || 'Booster sans nom' }}</h2>
        <div class="booster-status" :class="statusClass">
          {{ booster.etat || 'Non défini' }}
        </div>
      </div>
  
      <div class="booster-content">
        <div class="booster-specs">
          <h3>Spécifications</h3>
          <div class="specs-grid">
            <div class="spec-item">
              <span class="spec-label">Taille</span>
              <span class="spec-value">{{ booster.taille }} m</span>
            </div>
            <div class="spec-item">
              <span class="spec-label">Diamètre</span>
              <span class="spec-value">{{ booster.diametre }} m</span>
            </div>
            <div class="spec-item">
              <span class="spec-label">Poids à vide</span>
              <span class="spec-value">{{ booster.poidsAVide }} kg</span>
            </div>
            <div class="spec-item">
              <span class="spec-label">Poids actuel</span>
              <span class="spec-value">{{ booster.poids }} kg</span>
            </div>
            <div class="spec-item">
              <span class="spec-label">Vitesse max</span>
              <span class="spec-value">{{ booster.vitesseMax }} km/h</span>
            </div>
            <div class="spec-item">
              <span class="spec-label">Altitude max</span>
              <span class="spec-value">{{ booster.altitudeMax }} km</span>
            </div>
            <div class="spec-item">
              <span class="spec-label">Vitesse actuelle</span>
              <span class="spec-value">{{ booster.vitesse }} km/h</span>
            </div>
          </div>
        </div>
  
        <div class="booster-features">
          <h3>Caractéristiques</h3>
          <div class="features-list">
            <div class="feature-item" :class="{ active: booster.estPrototype }">
              <span class="feature-icon">📝</span>
              <span class="feature-label">Prototype</span>
            </div>
            <div class="feature-item" :class="{ active: booster.estReetulisable }">
              <span class="feature-icon">♻️</span>
              <span class="feature-label">Réutilisable</span>
            </div>
            <div class="feature-item" :class="{ active: booster.aSystemeAutoDestruction }">
              <span class="feature-icon">💥</span>
              <span class="feature-label">Auto-destruction</span>
            </div>
            <div class="feature-item" :class="{ active: booster.necessiteMaintenance }">
              <span class="feature-icon">🔧</span>
              <span class="feature-label">Maintenance requise</span>
            </div>
          </div>
        </div>
  
        <div class="booster-engine" v-if="booster.moteur">
          <h3>Moteur</h3>
          <div class="engine-info">
            <div class="engine-name">{{ booster.moteur.nom }}</div>
            <div class="engine-weight">{{ booster.moteur.poids }} kg</div>
            <div class="engine-quantity">{{ booster.nombreMoteurs }} unités</div>
          </div>
        </div>
  
        <div class="booster-tanks" v-if="booster.reservoirs && booster.reservoirs.length > 0">
          <h3>Réservoirs ({{ booster.reservoirs.length }})</h3>
          <div class="tanks-list">
            <div v-for="(reservoir, index) in booster.reservoirs" :key="index" class="tank-item">
              <div class="tank-name">{{ reservoir.nom }}</div>
              <div class="tank-weight">
                <span class="weight-current">{{ reservoir.poids }}</span> / 
                <span class="weight-empty">{{ reservoir.poidsAVide }}</span> kg
              </div>
            </div>
          </div>
        </div>
  
        <div class="launch-history" v-if="booster.historiquesLancement && booster.historiquesLancement.length > 0">
          <h3>Historique des lancements</h3>
          <ul class="history-list">
            <li v-for="(lancement, index) in booster.historiquesLancement" :key="index">
              {{ lancement }}
            </li>
          </ul>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'BoosterDisplay',
    props: {
      booster: {
        type: Object,
        default: () => ({
          nom: '',
          taille: 0,
          diametre: 0,
          poidsAVide: 0,
          poids: 0,
          altitudeMax: 0,
          vitesseMax: 0,
          vitesse: 0,
          etat: 'Inconnu',
          necessiteMaintenance: false,
          estPrototype: false,
          estReetulisable: false,
          aSystemeAutoDestruction: false,
          moteur: null,
          nombreMoteurs: 0,
          reservoirs: [],
          historiquesLancement: []
        })
      }
    },
    computed: {
      statusClass() {
        const etat = this.booster.etat?.toLowerCase() || '';
        if (etat.includes('opérationnel') || etat.includes('operationnel')) {
          return 'status-operational';
        } else if (etat.includes('maintenance')) {
          return 'status-maintenance';
        } else if (etat.includes('construction')) {
          return 'status-construction';
        } else if (etat.includes('détruit') || etat.includes('detruit')) {
          return 'status-destroyed';
        }
        return 'status-unknown';
      }
    }
  };
  </script>
  
  <style scoped>
  .booster-display {
    background-color: #1a1a2e;
    color: #e6e6e6;
    border-radius: 8px;
    overflow: hidden;
    max-width: 800px;
    margin: 0 auto;
    font-family: 'Arial', sans-serif;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.3);
  }
  
  .booster-header {
    background-color: #16213e;
    padding: 15px 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #2c2c44;
  }
  
  .booster-header h2 {
    margin: 0;
    font-size: 1.6rem;
    color: #fff;
  }
  
  .booster-status {
    padding: 5px 10px;
    border-radius: 4px;
    font-weight: bold;
    font-size: 0.8rem;
    text-transform: uppercase;
    letter-spacing: 0.5px;
  }
  
  .status-operational {
    background-color: #25b350;
    color: #fff;
  }
  
  .status-maintenance {
    background-color: #daa520;
    color: #fff;
  }
  
  .status-construction {
    background-color: #3498db;
    color: #fff;
  }
  
  .status-destroyed {
    background-color: #e74c3c;
    color: #fff;
  }
  
  .status-unknown {
    background-color: #7f8c8d;
    color: #fff;
  }
  
  .booster-content {
    padding: 20px;
    display: grid;
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  @media (min-width: 768px) {
    .booster-content {
      grid-template-columns: 1fr 1fr;
    }
    
    .booster-specs, .booster-tanks {
      grid-column: span 2;
    }
  }
  
  h3 {
    margin-top: 0;
    margin-bottom: 15px;
    color: #4ca1ff;
    font-size: 1.2rem;
    border-bottom: 1px solid #2c2c44;
    padding-bottom: 8px;
  }
  
  .specs-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 15px;
  }
  
  .spec-item {
    background-color: #232342;
    padding: 10px;
    border-radius: 4px;
    display: flex;
    flex-direction: column;
  }
  
  .spec-label {
    font-size: 0.8rem;
    color: #a0a0c8;
    margin-bottom: 4px;
  }
  
  .spec-value {
    font-size: 1.1rem;
    font-weight: bold;
  }
  
  .features-list {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 10px;
  }
  
  .feature-item {
    display: flex;
    align-items: center;
    padding: 10px;
    background-color: #232342;
    border-radius: 4px;
    opacity: 0.5;
  }
  
  .feature-item.active {
    opacity: 1;
    background-color: #2c3c55;
  }
  
  .feature-icon {
    margin-right: 10px;
    font-size: 1.2rem;
  }
  
  .engine-info {
    background-color: #232342;
    padding: 15px;
    border-radius: 4px;
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 10px;
  }
  
  .engine-name {
    grid-column: span 2;
    font-weight: bold;
    font-size: 1.1rem;
    color: #fff;
    margin-bottom: 5px;
  }
  
  .tanks-list {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 10px;
  }
  
  .tank-item {
    background-color: #232342;
    padding: 10px;
    border-radius: 4px;
  }
  
  .tank-name {
    font-weight: bold;
    margin-bottom: 5px;
  }
  
  .tank-weight {
    font-size: 0.9rem;
    color: #a0a0c8;
  }
  
  .weight-current {
    color: #4ca1ff;
  }
  
  .history-list {
    background-color: #232342;
    border-radius: 4px;
    margin: 0;
    padding: 10px 10px 10px 30px;
    list-style-type: disc;
  }
  
  .history-list li {
    padding: 5px 0;
    border-bottom: 1px solid #2c2c44;
  }
  
  .history-list li:last-child {
    border-bottom: none;
  }
  </style>