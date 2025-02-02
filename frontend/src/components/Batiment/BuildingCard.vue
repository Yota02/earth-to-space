<template>
    <div class="card">
      <h3 class="title">{{ building.nom }}</h3>
      <p class="cost">Coût: {{ building.cout }} €</p>
      <p>Capacité: {{ building.capacite }}</p>
      
      <div class="details">
        <h3 class="title">{{ building.nom }}</h3>
        <p>Type: {{ building.type }}</p>
        <p>Superficie: {{ building.superficie }} m²</p>
        <p>Temps de construction: {{ building.tempsConstruction }} mois</p>
        <p class="cost">Coût: {{ building.cout }} €</p>
        <button 
          @click="$emit('purchase', building)"
          :disabled="!canAfford"
          class="purchase-button"
          :class="{'disabled': !canAfford}"
        >
          Acheter et Construire
        </button>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'BuildingCard',
    props: {
      building: {
        type: Object,
        required: true
      },
      canAfford: {
        type: Boolean,
        required: true
      }
    },
    emits: ['purchase']
  }
  </script>
  
  <style scoped>
  .card {
    padding: 1rem;
    border: 1px solid #ddd;
    border-radius: 0.5rem;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
    position: relative;
    overflow: hidden;
  }
  
  .card:hover {
    transform: scale(1.05);
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
  }
  
  .title {
    font-weight: bold;
    font-size: 1.125rem;
    margin-bottom: 0.5rem;
  }
  
  .cost {
    font-weight: 600;
  }
  
  .details {
    position: absolute;
    inset: 0;
    background: white;
    padding: 1rem;
    border-radius: 0.5rem;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
    opacity: 0;
    transition: opacity 0.5s ease-out, transform 0.5s ease-out;
    transform: translateY(10px);
  }
  
  .card:hover .details {
    opacity: 1;
    transform: translateY(0);
  }
  
  .purchase-button {
    margin-top: 1rem;
    width: 100%;
    padding: 0.5rem;
    border-radius: 0.5rem;
    color: white;
    transition: transform 0.2s ease;
  }
  
  .purchase-button:not(.disabled) {
    background-color: #3b82f6;
  }
  
  .purchase-button:not(.disabled):hover {
    background-color: #2563eb;
    transform: scale(1.05);
  }
  
  .disabled {
    background-color: #9ca3af;
  }
  </style>
  