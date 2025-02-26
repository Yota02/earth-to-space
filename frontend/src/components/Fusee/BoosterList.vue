  <!-- BoostersList.vue -->
  <template>
    <div class="boosters-list">
      <h2>Liste des Boosters</h2>
      
      <div v-if="!boosters || boosters.length === 0" class="no-boosters">
        Aucun booster disponible.
      </div>
      
      <div v-else class="boosters-grid">
        <div 
          v-for="(booster, index) in boosters" 
          :key="index" 
          class="booster-card"
          @click="selectBooster(booster)"
          :class="{ 'selected': selectedBoosterId === getBoosterId(booster) }"
        >
          <div class="booster-card-header">
            <h3>{{ booster.nom }}</h3>
            <div :class="['status-indicator', getStatusClass(booster)]"></div>
          </div>
          
          <div class="booster-card-content">
            <div class="booster-specs">
              <div class="spec-item">
                <span class="spec-label">Taille:</span>
                <span class="spec-value">{{ booster.taille }} m</span>
              </div>
              <div class="spec-item">
                <span class="spec-label">Poids:</span>
                <span class="spec-value">{{ booster.poids }} kg</span>
              </div>
              <div class="spec-item">
                <span class="spec-label">Type:</span>
                <span class="spec-value">{{ booster.estPrototype ? 'Prototype' : 'Standard' }}</span>
              </div>
              <div class="spec-item">
                <span class="spec-label">État:</span>
                <span class="spec-value">{{ formatStatut(booster.etat) }}</span>
              </div>
            </div>
            
            <div class="booster-mini-features">
              <span class="mini-feature" v-if="booster.estPrototype" title="Prototype">📝</span>
              <span class="mini-feature" v-if="booster.estReetulisable" title="Réutilisable">♻️</span>
              <span class="mini-feature" v-if="booster.aSystemeAutoDestruction" title="Auto-destruction">💥</span>
              <span class="mini-feature" v-if="booster.necessiteMaintenance" title="Maintenance requise">🔧</span>
            </div>
          </div>
          
          <div class="booster-card-footer">
            <button @click.stop="selectBooster(booster)" class="view-details-btn">
              Voir détails
            </button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'BoostersList',
    props: {
      boosters: {
        type: Array,
        default: () => []
      },
      selectedBoosterId: {
        type: [String, Number],
        default: null
      }
    },
    methods: {
      selectBooster(booster) {
        this.$emit('select-booster', booster);
      },
      getBoosterId(booster) {
        // ID unique pour chaque booster - utilise le nom comme ID par défaut
        return booster.id || booster.nom;
      },
      getStatusClass(booster) {
        const etat = typeof booster.etat === 'number' 
          ? booster.etat 
          : booster.etat?.toLowerCase() || '';
        
        // Si etat est un nombre
        if (typeof etat === 'number') {
          switch(etat) {
            case 1: return 'status-operational';
            case 2: return 'status-maintenance';
            case 3: return 'status-construction';
            case 4: return 'status-destroyed';
            default: return 'status-unknown';
          }
        }
        
        // Si etat est une chaîne
        if (typeof etat === 'string') {
          if (etat.includes('opérationnel') || etat.includes('operationnel')) {
            return 'status-operational';
          } else if (etat.includes('maintenance')) {
            return 'status-maintenance';
          } else if (etat.includes('construction')) {
            return 'status-construction';
          } else if (etat.includes('détruit') || etat.includes('detruit')) {
            return 'status-destroyed';
          }
        }
        
        return 'status-unknown';
      },
      formatStatut(etat) {
        if (typeof etat === 'number') {
          switch(etat) {
            case 1: return 'Opérationnel';
            case 2: return 'En maintenance';
            case 3: return 'En construction';
            case 4: return 'Détruit';
            default: return 'État inconnu';
          }
        }
        return etat || 'État inconnu';
      }
    }
  };
  </script>
  
  <style scoped>
.boosters-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 15px;
}

/* Amélioration du comportement responsive */
@media (max-width: 1024px) {
  .boosters-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  }
}

@media (max-width: 768px) {
  .boosters-grid {
    grid-template-columns: 1fr;
  }

  .booster-card {
    width: 100%;
  }
}

/* Ajustements supplémentaires pour mobile */
@media (max-width: 480px) {
  .booster-card-header h3 {
    font-size: 1rem;
  }

  .spec-item {
    font-size: 0.8rem;
  }

  .view-details-btn {
    font-size: 0.8rem;
    padding: 6px 12px;
  }
}



  .boosters-list {
    padding: 20px;
  }
  
  .boosters-list h2 {
    margin-top: 0;
    margin-bottom: 20px;
    font-size: 1.8rem;
    color: #333;
  }
  
  .no-boosters {
    padding: 20px;
    text-align: center;
    background-color: #f5f5f5;
    border-radius: 8px;
    color: #666;
  }
  

  .booster-card {
    border: 1px solid #ddd;
    border-radius: 8px;
    overflow: hidden;
    background-color: white;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: transform 0.2s, box-shadow 0.2s;
    cursor: pointer;
  }
  
  .booster-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  }
  
  .booster-card.selected {
    border: 2px solid #2196f3;
    box-shadow: 0 4px 8px rgba(33, 150, 243, 0.3);
  }
  
  .booster-card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px;
    background-color: #f5f5f5;
    border-bottom: 1px solid #eee;
  }
  
  .booster-card-header h3 {
    margin: 0;
    font-size: 1.2rem;
  }
  
  .status-indicator {
    width: 12px;
    height: 12px;
    border-radius: 50%;
  }
  
  .status-operational {
    background-color: #4caf50;
  }
  
  .status-maintenance {
    background-color: #ff9800;
  }
  
  .status-construction {
    background-color: #2196f3;
  }
  
  .status-destroyed {
    background-color: #f44336;
  }
  
  .status-unknown {
    background-color: #9e9e9e;
  }
  
  .booster-card-content {
    padding: 15px;
  }
  
  .booster-specs {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 10px;
    margin-bottom: 10px;
  }
  
  .spec-item {
    display: flex;
    flex-direction: column;
    font-size: 0.9rem;
  }
  
  .spec-label {
    color: #666;
    font-size: 0.8rem;
  }
  
  .spec-value {
    font-weight: bold;
  }
  
  .booster-mini-features {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    margin-top: 10px;
    padding-top: 10px;
    border-top: 1px dashed #eee;
  }
  
  .mini-feature {
    font-size: 1.2rem;
    cursor: help;
  }
  
  .booster-card-footer {
    padding: 15px;
    border-top: 1px solid #eee;
    text-align: center;
  }
  
  .view-details-btn {
    background-color: #2196f3;
    color: white;
    border: none;
    border-radius: 4px;
    padding: 8px 16px;
    font-size: 0.9rem;
    cursor: pointer;
    transition: background-color 0.2s;
  }
  
  .view-details-btn:hover {
    background-color: #1976d2;
  }
  </style>