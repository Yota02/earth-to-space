<template>
  <div id="app">
    <nav class="navbar">
      <div class="navbar-container">
        <div class="navbar-left">
          <AffichageArgent />
        </div>
        
        <button class="navbar-toggle" @click="toggleMenu" aria-label="Toggle menu">
          <span class="hamburger"></span>
        </button>

        <ul :class="['navbar-menu', { 'navbar-menu-active': menuOpen }]">
          <li>
            <router-link to="/" @click="closeMenu" class="nav-link">
              <span>Base</span>
            </router-link>
          </li>
          <li>
            <router-link to="/shop" @click="closeMenu" class="nav-link">
              <img src="./assets/img/icone/shop.png" alt="Shop" class="nav-icon">
              <span>Marché</span>
            </router-link>
          </li>
          <li>
            <router-link to="/marcheFinancier" @click="closeMenu" class="nav-link">
              <span>Marche Financier</span>
            </router-link>
          </li>
          <li>
            <router-link to="/lancement" @click="closeMenu" class="nav-link">
              <span>Lancement</span>
            </router-link>
          </li>
          <li>
            <router-link to="/programme" @click="closeMenu" class="nav-link">
              <span>Programme</span>
            </router-link>
          </li>
          <li>
            <router-link to="/researche" @click="closeMenu" class="nav-link">
              <img src="./assets/img/icone/recherche.png" alt="Recherche" class="nav-icon">
              <span>Recherche</span>
            </router-link>
          </li>
          <li>
            <router-link to="/Ressource-Humaines" @click="closeMenu" class="nav-link">
              <img src="./assets/img/icone/ressource_humaine.png" alt="RH" class="nav-icon">
              <span>RH</span>
            </router-link>
          </li>
          <li>
            <router-link to="/politique" @click="closeMenu" class="nav-link">
              <img src="./assets/img/icone/politique.png" alt="Politique" class="nav-icon">
              <span>Politique</span>
            </router-link>
          </li>
          <li>
            <router-link to="/ergol" @click="closeMenu" class="nav-link">
              <img src="./assets/img/icone/carburant.png" alt="Ergol" class="nav-icon">
              <span>Ergol</span>
            </router-link>
          </li>
          <li>
            <router-link to="/marketBatiment" @click="closeMenu" class="nav-link">
              <img src="./assets/img/icone/construction.png" alt="Bâtiments" class="nav-icon">
              <span>Bâtiments</span>
            </router-link>
          </li>
        </ul>
      </div>
    </nav>

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
  min-height: calc(100vh - 60px); /* Assure que la hauteur minimale est ajustée pour être en dessous de la navbar */
  background-color: #0a0b1e;
  box-sizing: border-box;
  padding-top: 60px; /* Ajoute de l'espace pour la navbar */
}

/* Navbar styles */
.navbar {
  position: sticky;
  top: 0;
  background: linear-gradient(to right, #1a1b3a, #2a2b5a);
  color: #fff;
  padding: 0.5rem 1rem;
  box-shadow: 0 2px 10px rgba(88, 103, 221, 0.15);
  backdrop-filter: blur(10px);
  z-index: 1000;
  border-bottom: 1px solid rgba(88, 103, 221, 0.2);
  height: 60px; /* Définit explicitement la hauteur de la navbar */
}

.navbar-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}

.navbar-left {
  display: flex;
  align-items: center;
}



/* Hamburger menu button */
.navbar-toggle {
  display: none;
  background: none;
  border: none;
  padding: 0.5rem;
  cursor: pointer;
  position: relative;
}

.hamburger {
  display: block;
  width: 24px;
  height: 2px;
  background: #fff;
  position: relative;
  transition: all 0.3s ease-in-out;
}

.hamburger::before,
.hamburger::after {
  content: '';
  position: absolute;
  width: 24px;
  height: 2px;
  background: #fff;
  transition: all 0.3s ease-in-out;
}

.hamburger::before {
  transform: translateY(-8px);
}

.hamburger::after {
  transform: translateY(8px);
}

/* Navigation menu */
.navbar-menu {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  gap: 1rem;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  text-decoration: none;
  color: #fff;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  transition: all 0.3s ease;
  font-weight: 500;
}

.nav-link:hover {
  background: rgba(88, 103, 221, 0.2);
  transform: translateY(-2px);
}

.nav-icon {
  width: 24px;
  height: 24px;
  filter: brightness(0) invert(1);
  transition: transform 0.3s ease;
}

.nav-link:hover .nav-icon {
  transform: scale(1.1);
}

/* Active link styles */
.router-link-active {
  background: rgba(88, 103, 221, 0.3);
  box-shadow: 0 2px 8px rgba(88, 103, 221, 0.2);
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
    right: 0;
    background: linear-gradient(to bottom, #1a1b3a, #2a2b5a);
    flex-direction: column;
    padding: 1rem;
    gap: 0.5rem;
    display: none;
    border-bottom-left-radius: 12px;
    border-bottom-right-radius: 12px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
  }

  .navbar-menu-active {
    display: flex;
  }

  .nav-link {
    padding: 0.75rem 1rem;
    justify-content: flex-start;
  }

  /* Animate hamburger to X */
  .navbar-menu-active + .navbar-toggle .hamburger {
    background: transparent;
  }

  .navbar-menu-active + .navbar-toggle .hamburger::before {
    transform: rotate(45deg);
  }

  .navbar-menu-active + .navbar-toggle .hamburger::after {
    transform: rotate(-45deg);
  }
}

</style>