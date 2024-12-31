<template>
  <div id="app">
    <h1>Test</h1>
    <nav>
      <ul>
        <li><router-link to="/">Home</router-link></li>
        <li><router-link to="/about">About</router-link></li>
        <li><router-link to="/researche">Researche</router-link></li>
      </ul>
    </nav>

    <router-view /> 
  </div>
</template>

<script setup>
</script>

<style>
/* Vous pouvez ajouter du CSS ici */
</style>
