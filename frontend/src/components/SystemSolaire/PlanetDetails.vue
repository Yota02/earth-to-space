<template>
    <div v-if="planet" class="planet-details">
        <h2>{{ planet.name }}</h2>
        <p>{{ planet.description }}</p>
        <h3>Satellites</h3>
        <ul v-if="planet.satellites && planet.satellites.length">
            <li v-for="satellite in planet.satellites" :key="satellite.name">
                {{ satellite.name }} (Taille: {{ satellite.size }}px)
            </li>
        </ul>
        <p v-else>Aucun satellite</p>
        <button @click="$emit('close')">Fermer</button>
    </div>
    <p v-else>La planète n'est pas disponible.</p>
</template>

<script>
export default {
    name: 'PlanetDetails',
    props: {
        planet: Object
    },
    watch: {
        planet(newPlanet) {
            console.log('Planète reçue:', newPlanet);
        }
    }
}
</script>

<style>
.planet-details {
    position: fixed;
    top: 10%;
    right: 10px;
    background: rgba(0, 0, 0, 0.8);
    color: white;
    padding: 15px;
    border-radius: 8px;
    width: 250px;
    box-shadow: 0 0 10px rgba(255, 255, 255, 0.3);
}

.planet-details h2, .planet-details h3 {
    margin-bottom: 10px;
}

.planet-details ul {
    list-style: none;
    padding: 0;
}

.planet-details button {
    margin-top: 10px;
    padding: 5px 10px;
    background: red;
    color: white;
    border: none;
    cursor: pointer;
    border-radius: 5px;
}

.planet-details button:hover {
    background: darkred;
}
</style>
