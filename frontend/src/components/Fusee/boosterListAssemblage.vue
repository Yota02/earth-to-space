<template>
    <div class="fusee-display-container">
      <h3 class="fusee-display-title">Fusées actives</h3>
      <div v-if="boosters.length > 0" class="fusee-grid">
        <div v-for="(booster, index) in boosters" :key="index" class="fusee-item">
          <img src="../../assets/img/icone/fusee.png" alt="Fusée" class="fusee-image">
          <div class="fusee-name">{{ booster.nom || booster.name }}</div>
        </div>
      </div>
      <div v-else class="fusee-empty">
        <p>Aucune fusée active pour le moment</p>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'FuseeDisplay',
    props: {
      boosters: {
        type: Array,
        required: true,
        default: () => []
      }
    }
  }
  </script>
  
  <style>
  .fusee-display-container {
    background-color: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    padding: 15px;
    margin-top: 20px;
  }
  
  .fusee-display-title {
    font-size: 1.2rem;
    margin-bottom: 15px;
    color: #333;
  }
  
  .fusee-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 10px;
  }
  
  .fusee-empty {
    padding: 20px;
    text-align: center;
    color: #666;
  }
  
  .fusee-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 10px;
    border-radius: 5px;
    transition: background-color 0.2s;
  }
  
  .fusee-item:hover {
    background-color: #f0f0f0;
  }
  
  .fusee-image {
    width: 40px;
    height: auto;
    margin-bottom: 5px;
  }
  
  .fusee-name {
    font-size: 0.8rem;
    text-align: center;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: 100%;
  }
  
  @media (max-width: 768px) {
    .fusee-grid {
      grid-template-columns: repeat(3, 1fr);
    }
  }
  
  @media (max-width: 480px) {
    .fusee-grid {
      grid-template-columns: repeat(2, 1fr);
    }
  }
  </style>