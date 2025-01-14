<template>
  <div id="app">
    <!-- Display component -->
    <AffichageArgent class="affichage-argent" />

    <!-- Navigation -->
    <nav class="navbar">
      <div class="navbar-container">
        <button class="navbar-toggle" @click="toggleMenu">
          ☰
        </button>
        <ul :class="['navbar-menu', { 'navbar-menu-active': menuOpen }]">
          <li><router-link to="/" @click="closeMenu">Home</router-link></li>
          <li><router-link to="/shop" @click="closeMenu">Shop</router-link></li>
          <li><router-link to="/programme" @click="closeMenu">Programme</router-link></li>
          <li><router-link to="/researche" @click="closeMenu">Researche</router-link></li>
          <li><router-link to="/Ressource-Humaines" @click="closeMenu">Ressource Humaines</router-link></li>
          <li><router-link to="/lanceur" @click="closeMenu">Lanceur</router-link></li>
          <li><router-link to="/ergol" @click="closeMenu">Ergol</router-link></li>
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
  display: flex; /* Activer Flexbox */
  flex-direction: column; /* Permet d'empiler les enfants verticalement */
  justify-content: center; /* Centre verticalement */
  align-items: center; /* Centre horizontalement */
  flex-grow: 1; /* Permet à la section de prendre tout l'espace disponible */
  min-height: calc(100vh - 60px); /* Ajuster en fonction de la hauteur de la navbar */
  box-sizing: border-box;
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
  justify-content: flex-end;  /* Align navbar content to the right */
  align-items: center;
}

.navbar-brand {
  font-size: 1.5rem;
  font-weight: bold;
  color: #fff;
}

.navbar-brand a {
  text-decoration: none;
  color: inherit;
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
  .navbar-toggle {
    display: block;
  }

  .navbar-menu {
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

/* Affichage argent styles */
.affichage-argent {
  position: fixed; 
  top: 10px;   /* Place it slightly below the very top */
  left: 10px;  /* Align to the left */
  padding: 10px 20px;
  border-radius: 8px;
  background-color: #007BFF;
  color: white;
  z-index: 1100;  /* Ensures it stays above the navbar */
}
</style>
