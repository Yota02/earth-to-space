<template>
  <div id="app">
    <nav>
      <ul>
        <li><router-link to="/">Home</router-link></li>
        <li><router-link to="/shop">Shop</router-link></li>
        <li><router-link to="/programme">Programme</router-link></li>
        <li><router-link to="/researche">Researche</router-link></li>
        <li><router-link to="/LanceurView"> Lanceur </router-link></li>
      </ul>
    </nav>

    <router-view /> 
  </div>
</template>

<script setup>
</script>

<style>
#app {
  font-family: Arial, sans-serif;
  text-align: center;
  margin: 0;
  padding: 0;
  background-color: #f9f9f9;
}

h1 {
  font-size: 2.5rem;
  color: #333;
  margin-top: 20px;
}

nav {
  margin-top: 20px;
  background-color: #007BFF;
  padding: 10px 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

nav ul {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  justify-content: center;
}

nav ul li {
  margin: 0 15px;
}

nav ul li a {
  text-decoration: none;
  color: #fff;
  font-weight: bold;
  transition: color 0.3s;
}

nav ul li a:hover {
  color: #FFD700;
}

router-view {
  margin: 20px auto;
  padding: 20px;
  max-width: 800px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

</style>
