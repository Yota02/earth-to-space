<template>
    <div class="app-container">
        <header class="app-header">
            <h1>Centre de Recherche Spatiale</h1>
           
        </header>
        
        <main class="research-management">
            <div class="research-sidebar">
                <nav class="research-categories">
                    <button 
                        v-for="categorie in categories" 
                        :key="categorie"
                        @click="selectedCategory = categorie"
                        :class="{ 'active': selectedCategory === categorie }"
                    >
                        {{ categorie }}
                    </button>
                </nav>
            </div>
            
            <div class="research-content">
                <RecherchesList 
                    :recherches="filteredRecherches"
                    @select-recherche="selectRecherche"
                />
                
                <RechercheDetail 
                    :selectedRecherche="selectedRecherche"
                    @start-recherche="demarerRecherche"
                />
            </div>
        </main>
    </div>
</template>

<script>
import RecherchesList from './RecherchesList.vue';
import RechercheDetail from './RechercheDetail.vue';

export default {
    components: {
        RecherchesList,
        RechercheDetail
    },
    data() {
        return {
            ws: null,
            connectionStatus: 'connecting',
            recherches: [],
            selectedRecherche: null,
            selectedCategory: null,
            categories: [
                'PROPULSION', 'NAVIGATION', 'ENERGIE', 'COMMUNICATIONS', 
                'EXPLORATION', 'TRANSPORT_HABITE', 'COLONISATION', 
                'BATIMENTS', 'ROBOTIQUE', 'INTELLIGENCE_ARTIFICIELLE', 
                'EXOBIOLOGIE'
            ]
        };
    },
    computed: {
        getConnectionStatusLabel() {
            const statusLabels = {
                'connected': 'Connecté',
                'connecting': 'Connexion en cours...',
                'error': 'Erreur de connexion'
            };
            return statusLabels[this.connectionStatus];
        },
        filteredRecherches() {
            if (!this.selectedCategory) return this.recherches;
            return this.recherches.filter(r => r.categorie === this.selectedCategory);
        }
    },
    methods: {
        initWebSocket() {
            this.ws = new WebSocket('ws://localhost:3232');

            this.ws.onopen = () => {
                this.connectionStatus = 'connected';
                this.requestRecherches();
                this.heartbeat = setInterval(() => this.requestRecherches(), 5000);
            };

            this.ws.onclose = () => {
                this.connectionStatus = 'error';
                clearInterval(this.heartbeat);
                setTimeout(this.initWebSocket, 5000);
            };

            this.ws.onmessage = (event) => {
                try {
                    const data = JSON.parse(event.data);
                    if (data.action === "recherchesState") {
                        this.recherches = data.recherches;
                    }
                } catch (error) {
                    console.error('Erreur de réception:', error);
                }
            };
        },
        requestRecherches() {
            if (this.ws && this.ws.readyState === WebSocket.OPEN) {
                this.ws.send(JSON.stringify({ action: 'getRecherches' }));
            }
        },
        selectRecherche(recherche) {
            this.selectedRecherche = recherche;
        },
        demarerRecherche(recherche) {
            if (this.ws && this.ws.readyState === WebSocket.OPEN) {
                this.ws.send(JSON.stringify({ 
                    action: 'demarerRecherche', 
                    rechercheId: recherche.id 
                }));
            }
        }
    },
    mounted() {
        this.initWebSocket();
    },
    beforeUnmount() {
        if (this.ws) {
            this.ws.close();
        }
        if (this.heartbeat) {
            clearInterval(this.heartbeat);
        }
    }
};
</script>

<style scoped>
.app-container {
    display: flex;
    flex-direction: column;
    height: 100vh;
    background-color: #f0f2f5;
}

.app-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 20px;
    background-color: #2c3e50;
    color: white;
}

.connection-status {
    padding: 5px 10px;
    border-radius: 5px;
}

.status-connected {
    background-color: #27ae60;
}

.status-connecting {
    background-color: #f39c12;
}

.status-error {
    background-color: #e74c3c;
}

.research-management {
    display: flex;
    flex-grow: 1;
    overflow: hidden;
}

.research-sidebar {
    width: 200px;
    background-color: #34495e;
    padding: 20px 0;
}

.research-categories {
    display: flex;
    flex-direction: column;
}

.research-categories button {
    padding: 10px 15px;
    text-align: left;
    background: none;
    border: none;
    color: white;
    cursor: pointer;
    transition: background-color 0.3s;
}

.research-categories button:hover,
.research-categories button.active {
    background-color: #2c3e50;
}

.research-content {
    display: flex;
    flex-grow: 1;
    overflow: hidden;
}
</style>