<template>
    <div class="booster-detail">
        <div class="booster-header">
            <h2>{{ booster.nom }}</h2>
            <div :class="['status-badge', statusClass]">
                {{ statutFormate }}
            </div>
        </div>

        <div class="booster-specs">
            <div class="specs-section">
                <h3>Caractéristiques générales</h3>
                <div class="specs-grid">
                    <div class="spec-item">
                        <span class="spec-label">Taille:</span>
                        <span class="spec-value">{{ booster.taille }} m</span>
                    </div>
                    <div class="spec-item">
                        <span class="spec-label">Diamètre:</span>
                        <span class="spec-value">{{ booster.diametre }} m</span>
                    </div>
                    <div class="spec-item">
                        <span class="spec-label">Poids à vide:</span>
                        <span class="spec-value">{{ booster.poidsAVide }} kg</span>
                    </div>
                    <div class="spec-item">
                        <span class="spec-label">Poids total:</span>
                        <span class="spec-value">{{ booster.poids }} kg</span>
                    </div>
                </div>
            </div>

            <div class="specs-section">
                <h3>Performances</h3>
                <div class="specs-grid">
                    <div class="spec-item">
                        <span class="spec-label">Altitude max:</span>
                        <span class="spec-value">{{ formatAltitude(booster.altitudeMax) }}</span>
                    </div>
                    <div class="spec-item">
                        <span class="spec-label">Vitesse max:</span>
                        <span class="spec-value">{{ formatVitesse(booster.vitesseMax) }}</span>
                    </div>
                </div>
            </div>

            <div class="specs-section">
                <h3>Propulsion</h3>
                <div class="specs-grid">
                    <div class="spec-item">
                        <span class="spec-label">Moteur:</span>
                        <span class="spec-value">{{ booster.moteur?.nom || 'Non spécifié' }}</span>
                    </div>
                    <div class="spec-item">
                        <span class="spec-label">Nombre de moteurs:</span>
                        <span class="spec-value">{{ booster.nombreMoteurs }}</span>
                    </div>
                    <div class="spec-item">
                        <span class="spec-label">Poids du moteur:</span>
                        <span class="spec-value">{{ booster.moteur?.poids || 0 }} kg</span>
                    </div>
                </div>
            </div>

            <div class="specs-section" v-if="booster.reservoirs && booster.reservoirs.length > 0">
                <h3>Réservoirs ({{ booster.reservoirs.length }})</h3>
                <div class="reservoirs-list">
                    <div class="reservoir-item" v-for="(reservoir, index) in booster.reservoirs" :key="index">
                        <h4>{{ reservoir.nom }}</h4>
                        <div class="specs-grid">
                            <div class="spec-item">
                                <span class="spec-label">Poids à vide:</span>
                                <span class="spec-value">{{ reservoir.poidsAVide }} kg</span>
                            </div>
                            <div class="spec-item">
                                <span class="spec-label">Poids total:</span>
                                <span class="spec-value">{{ reservoir.poids }} kg</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="specs-section">
                <h3>Coût</h3>
                <ul v-if="booster.cout && Object.keys(booster.cout).length > 0" class="cost-list">
                    <li v-for="(quantite, piece) in booster.cout" :key="piece">
                        <strong>{{ piece }}:</strong> {{ quantite }}
                    </li>
                </ul>
                <p v-else>Aucune donnée de coût disponible.</p>
            </div>

            <div class="specs-section">
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
                </div>
            </div>

            <div class="specs-section" v-if="booster.historiquesLancement && booster.historiquesLancement.length > 0">
                <h3>Historique des lancements</h3>
                <ul class="launch-history">
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
    name: 'BoosterDetail',
    props: {
        booster: {
            type: Object,
            required: true
        }
    },
    computed: {
        statusClass() {
            const etat = typeof this.booster.etat === 'number'
                ? this.booster.etat
                : this.booster.etat?.toLowerCase() || '';

            if (typeof etat === 'number') {
                switch (etat) {
                    case 1: return 'status-operational';
                    case 2: return 'status-maintenance';
                    case 3: return 'status-construction';
                    case 4: return 'status-destroyed';
                    default: return 'status-unknown';
                }
            }

            if (typeof etat === 'string') {
                if (etat.includes('opérationnel')) return 'status-operational';
                if (etat.includes('maintenance')) return 'status-maintenance';
                if (etat.includes('construction')) return 'status-construction';
                if (etat.includes('détruit')) return 'status-destroyed';
            }

            return 'status-unknown';
        },
        statutFormate() {
            const etat = this.booster.etat;
            if (typeof etat === 'number') {
                switch (etat) {
                    case 1: return 'Opérationnel';
                    case 2: return 'En maintenance';
                    case 3: return 'En construction';
                    case 4: return 'Détruit';
                    default: return 'État inconnu';
                }
            }
            return etat || 'État inconnu';
        }
    },
    methods: {
        formatAltitude(altitude) {
            if (altitude >= 1000000) return `${(altitude / 1000000).toFixed(2)} million m`;
            if (altitude >= 1000) return `${(altitude / 1000).toFixed(2)} km`;
            return `${altitude.toFixed(2)} m`;
        },
        formatVitesse(vitesse) {
            if (!vitesse || isNaN(vitesse)) return 'Donnée indisponible';
            if (vitesse >= 1000) return `${(vitesse / 1000).toFixed(2)} km/s`;
            return `${vitesse.toFixed(2)} m/s`;
        }
    }
};
</script>

<style scoped>
.cost-list {
    list-style-type: none;
    padding: 0;
}

.cost-list li {
    padding: 6px 0;
    border-bottom: 1px solid #eee;
}

.cost-list li:last-child {
    border-bottom: none;
}

.booster-detail {
    background-color: #f5f5f5;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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

.status-badge {
    padding: 6px 12px;
    border-radius: 20px;
    font-weight: bold;
    font-size: 0.85rem;
}

.status-operational {
    background-color: #4caf50;
    color: white;
}

.status-maintenance {
    background-color: #ff9800;
    color: white;
}

.status-construction {
    background-color: #2196f3;
    color: white;
}

.status-destroyed {
    background-color: #f44336;
    color: white;
}

.status-unknown {
    background-color: #9e9e9e;
    color: white;
}

.specs-section {
    margin-bottom: 20px;
    border: 1px solid #ddd;
    border-radius: 6px;
    padding: 15px;
    background-color: white;
}

.specs-section h3 {
    margin-top: 0;
    margin-bottom: 15px;
    font-size: 1.2rem;
    color: #333;
    border-bottom: 1px solid #eee;
    padding-bottom: 8px;
}

.specs-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 15px;
}

.spec-item {
    display: flex;
    flex-direction: column;
}

.spec-label {
    font-weight: bold;
    color: #666;
    font-size: 0.9rem;
}

.spec-value {
    font-size: 1.1rem;
    margin-top: 4px;
}

.features-list {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 15px;
}

.feature-item {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 10px;
    border-radius: 6px;
    background-color: #f5f5f5;
    opacity: 0.6;
    transition: all 0.3s ease;
}

.feature-item.active {
    background-color: #00ff44;
    opacity: 1;
    box-shadow: 0 2px 4px rgba(33, 150, 243, 0.1);
}

.feature-icon {
    font-size: 1.5rem;
}

.feature-label {
    font-weight: bold;
    color: #555;
}

.reservoirs-list {
    display: flex;
    flex-direction: column;
    gap: 15px;
}

.reservoir-item {
    border: 1px solid #eee;
    border-radius: 4px;
    padding: 10px;
    background-color: #f9f9f9;
}

.reservoir-item h4 {
    margin-top: 0;
    margin-bottom: 10px;
    font-size: 1rem;
}

.launch-history {
    list-style-type: none;
    padding: 0;
    margin: 0;
}

.launch-history li {
    padding: 8px 0;
    border-bottom: 1px solid #eee;
}

.launch-history li:last-child {
    border-bottom: none;
}
</style>