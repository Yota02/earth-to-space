<template>
  <div id="app">
    <!-- Navigation -->
    <nav class="navbar">
      <div class="navbar-container">
        <!-- Affichage component intégré dans la navbar -->
        <div class="navbar-left">
          <AffichageArgent />
        </div>
        
        <button class="navbar-toggle" @click="toggleMenu">
          ☰
        </button>
        <ul :class="['navbar-menu', { 'navbar-menu-active': menuOpen }]">
          <li><router-link to="/" @click="closeMenu">Home</router-link></li>
          <li><router-link to="/shop" @click="closeMenu">Shop</router-link></li>
          <li><router-link to="/lancement" @click="closeMenu">Lancement</router-link></li>
          <li><router-link to="/programme" @click="closeMenu">Programme</router-link></li>
          <li><router-link to="/researche" @click="closeMenu"> 
            <img src="./assets/img/icone/recherche.png" alt="" class="market-icon"> 
            </router-link>
          </li>
          <li><router-link to="/Ressource-Humaines" @click="closeMenu"> 
            <img src="./assets/img/icone/ressource_humaine.png" alt="" class="market-icon"> 
            </router-link>
          </li>
          <li><router-link to="/lanceur" @click="closeMenu">Lanceur</router-link></li>
          <li><router-link to="/politique" @click="closeMenu">Politique</router-link></li>
          <li><router-link to="/ergol" @click="closeMenu">Ergol</router-link></li>
          <li><router-link to="/marketBatiment" @click="closeMenu"> 
            <img src="./assets/img/icone/construction.png" alt="" class="market-icon"> 
            </router-link>
          </li>
        </ul>
      </div>
    </nav>

    <!-- Main content -->
    <div class="main-content">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import AffichageArgent from "./components/affichageUIDroit.vue";

const menuOpen = ref(false);

const toggleMenu = () => {
  menuOpen.value = !menuOpen.value;
};

const closeMenu = () => {
  menuOpen.value = false;
};
</script>

<style>
.main-content {
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  flex-grow: 1;
  min-height: calc(100vh - 60px);
  box-sizing: border-box;
}

.market-icon {
  width: 24px;
  height: auto;
}

/* Navbar styles */
.navbar {
  position: sticky;
  top: 0;
  background-color: #007BFF;
  color: #fff;
  padding: 10px 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.navbar-container {
  display: flex;
  justify-content: space-between;  /* Changed to space-between */
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

.navbar-left {
  display: flex;
  align-items: center;
}

.navbar-toggle {
  display: none;
  background: none;
  border: none;
  font-size: 1.5rem;
  color: #fff;
  cursor: pointer;
}

/* Navbar menu */
.navbar-menu {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  gap: 15px;
}

.navbar-menu li a {
  text-decoration: none;
  color: #fff;
  font-weight: bold;
  transition: color 0.3s ease-in-out;
}

.navbar-menu li a:hover {
  color: #FFD700;
}

/* Responsive styles */
@media (max-width: 768px) {
  .navbar-container {
    flex-wrap: wrap;
  }

  .navbar-toggle {
    display: block;
    order: 2;
  }

  .navbar-left {
    order: 1;
  }

  .navbar-menu {
    order: 3;
    position: absolute;
    top: 100%;
    left: 0;
    background-color: #007BFF;
    width: 100%;
    flex-direction: column;
    align-items: center;
    display: none;
    gap: 10px;
    padding: 20px 0;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  }

  .navbar-menu-active {
    display: flex;
  }
}
</style>
