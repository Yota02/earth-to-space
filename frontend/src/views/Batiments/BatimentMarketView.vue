<!-- src/views/BuildingMarket.vue -->
<template>
  <div class="p-4 flex gap-4">
    <!-- Marché des bâtiments -->
    <BuildingTypeList
      :buildings="batimentsDisponibles"
      :money="gameState.argent"
      :types="availableTypes"
      @purchase="acheterBatiment"
      @type-change="handleTypeChange"
    />
    
    <!-- Liste des constructions en cours -->
    <BuildingConstruction
      :buildings="batimentsEnConstruction"
    />
  </div>
</template>

<script>
import BuildingTypeList from '../../components/Batiment/BuildingTypeList.vue'
import BuildingConstruction from '../../components/Batiment/BuildingConstruction.vue'

export default {
  name: 'BuildingMarket',
  components: {
    BuildingTypeList,
    BuildingConstruction
  },
  data() {
    return {
      socket: null,
      gameState: {
        argent: 0,
        date: null
      },
      batimentsDisponibles: [],
      batimentsEnConstruction: [],
      currentType: ''
    }
  },
  computed: {
    availableTypes() {
      return [...new Set(this.batimentsDisponibles.map(b => b.type))].sort()
    }
  },
  methods: {
    initWebSocket() {
      this.socket = new WebSocket('ws://localhost:3232')

      this.socket.onopen = () => {
        console.log('Connecté au serveur')
        this.getBatimentsState()
      }

      this.socket.onmessage = (event) => {
        try {
          const data = JSON.parse(event.data)
          
          if (data.argent !== undefined) {
            this.gameState.argent = data.argent
          }
          if (data.date !== undefined) {
            this.gameState.date = data.date
          }
          
          if (data.action === "batimentsState") {
            this.batimentsDisponibles = data.batimentsDisponibles || []
            this.batimentsEnConstruction = data.batimentsEnConstruction || []
          }
        } catch (error) {
          console.error('Erreur lors du parsing:', error)
        }
      }
    },
    
    getBatimentsState() {
      if (this.socket && this.socket.readyState === WebSocket.OPEN) {
        const message = {
          action: "getBatiment"
        }
        this.socket.send(JSON.stringify(message))
      }
    },
    
    handleTypeChange(newType) {
      this.currentType = newType
    },
    
    acheterBatiment(building) {
      if (this.gameState.argent < building.cout) return
      
      const message = {
        action: "buyBatiment",
        name: building.nom
      }
      
      this.socket.send(JSON.stringify(message))
    }
  },
  mounted() {
    this.initWebSocket()
    
    setInterval(() => {
      this.getBatimentsState()
    }, 5000)
  },
  beforeUnmount() {
    if (this.socket) {
      this.socket.close()
    }
  }
}
</script>