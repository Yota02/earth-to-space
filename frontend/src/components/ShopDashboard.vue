<template>
    <div class="p-6 max-w-7xl mx-auto">
      <!-- Error State -->
      <div v-if="connectionStatus === 'error'" class="p-6 text-center text-red-600">
        Error connecting to server. Please refresh the page.
      </div>
  
      <!-- Loading State -->
      <div v-else-if="connectionStatus === 'connecting'" class="p-6 text-center">
        Connecting to server...
      </div>
  
      <!-- Main Content -->
      <template v-else>
        <div class="mb-8">
          <p class="text-2xl font-bold">Argent: {{ gameData.argent }}</p>
        </div>
  
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div
            v-for="object in gameData.objectsAchetables"
            :key="object.nom"
            class="p-4 border rounded-lg shadow-sm hover:shadow-md transition-shadow"
          >
            <h4 class="font-semibold text-lg mb-2">{{ object.nom }}</h4>
            <div class="space-y-1 text-sm text-gray-600">
              <p>Prix: {{ object.prix }}</p>
              <p v-if="object.type === 'carburant'">
                Quantité en stock: {{ object.quantiteStock?.toFixed(2) || 0 }}
              </p>
            </div>
            <div class="flex gap-2 mt-3">
              <button
                @click="handlePurchase(object.nom)"
                class="flex-1 px-4 py-2 bg-blue-600 text-white hover:bg-blue-700 rounded-md transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                :disabled="gameData.argent < object.prix"
              >
                Acheter
              </button>
              <button
                @click="handleSell(object.nom)"
                class="flex-1 px-4 py-2 bg-red-600 text-white hover:bg-red-700 rounded-md transition-colors"
              >
                Vendre
              </button>
            </div>
          </div>
        </div>
      </template>
    </div>
  </template>
  
  <script>
  export default {
    name: 'ShopDashboard',
    data() {
      return {
        gameData: {
          argent: 0,
          objectsAchetables: []
        },
        websocket: null,
        connectionStatus: 'connecting'
      }
    },
    methods: {
      sendAction(action, name) {
        if (this.websocket && this.websocket.readyState === WebSocket.OPEN) {
          this.websocket.send(JSON.stringify({ action, name }))
        } else {
          console.error("WebSocket is not connected")
        }
      },
      handlePurchase(objectName) {
        this.sendAction("buyObject", objectName)
      },
      handleSell(objectName) {
        this.sendAction("sellObject", objectName)
      },
      initializeWebSocket() {
        this.websocket = new WebSocket("ws://localhost:3232")
  
        this.websocket.onopen = () => {
          this.connectionStatus = 'connected'
          console.log("WebSocket connection established")
        }
  
        this.websocket.onmessage = (event) => {
          try {
            const data = JSON.parse(event.data)
            console.log("Received data:", data)
  
            if (data.error) {
              console.error("Server error:", data.error)
              return
            }
  
            if (!data.objectsAchetables && !Array.isArray(data.objectsAchetables)) {
              console.warn("Missing or invalid objectsAchetables in received data")
              return
            }
  
            this.gameData = {
              ...this.gameData,
              argent: data.argent || this.gameData.argent,
              objectsAchetables: data.objectsAchetables || this.gameData.objectsAchetables
            }
          } catch (error) {
            console.error("Error processing WebSocket data:", error)
          }
        }
  
        this.websocket.onerror = (error) => {
          console.error("WebSocket error:", error)
          this.connectionStatus = 'error'
        }
  
        this.websocket.onclose = () => {
          console.log("WebSocket connection closed")
          this.connectionStatus = 'disconnected'
        }
      }
    },
    mounted() {
      this.initializeWebSocket()
    },
    beforeUnmount() {
      if (this.websocket) {
        this.websocket.close()
      }
    }
  }
  </script>