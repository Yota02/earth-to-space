<template>
    <div class="systeme-container" @mousedown="startDrag" @mousemove="onDrag" @mouseup="stopDrag"
        @mouseleave="stopDrag">
        <h1 v-if="currentZoom === 1" class="titre">Système Solaire</h1>

        <!-- Modal pour l'affichage des planètes et satellites -->
        <div v-if="selectedObject" class="planet-modal" @click="closeModal">
            <div class="planet-modal-content" @click.stop>
                <div class="planet-large" :style="{
                    backgroundColor: selectedObject.color,
                    boxShadow: `0 0 50px ${selectedObject.color}`,
                    width: selectedObject.modalSize + 'px',
                    height: selectedObject.modalSize + 'px'
                }">
                </div>
                <h2>{{ selectedObject.name }}</h2>
                <p>{{ selectedObject.description }}</p>

                <!-- Affichage des satellites pour une planète -->
                <div class="satellites-container" v-if="selectedObject.satellites && selectedObject.satellites.length">
                    <h3>Satellites :</h3>
                    <div class="satellites-list">
                        <div v-for="satellite in selectedObject.satellites" :key="satellite.name" class="satellite-item"
                            @click="showSatelliteDetails(satellite)">
                            <div class="satellite-circle"
                                :style="{ width: `${satellite.size * 2}px`, height: `${satellite.size * 2}px` }">
                            </div>
                            <span>{{ satellite.name }}</span>
                        </div>
                    </div>
                </div>

                <!-- Navigation pour revenir à la planète parente -->
                <div v-if="selectedObject.parentPlanet" class="parent-planet-nav">
                    <button class="back-button" @click="showPlanetDetails(selectedObject.parentPlanet)">
                        Retour à {{ selectedObject.parentPlanet.name }}
                    </button>
                </div>

                <button class="close-button" @click="closeModal">Fermer</button>
            </div>
        </div>

        <div class="solar-system" ref="solarSystem">
            <div class="zoom-container" :style="zoomStyle">
                <div class="sun" @click="resetZoom"></div>
                <div v-for="(planet, index) in planets" :key="planet.name" class="orbit"
                    :style="{ width: `${planet.distance}px` }">
                    <div class="planet" :style="{
                        backgroundColor: planet.color,
                        width: `${planet.size}px`,
                        height: `${planet.size}px`
                    }" @click="showPlanetDetails(planet)">
                        <div class="planet-name">{{ planet.name }}</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'SystemeSolaire',
    data() {
        return {
            currentZoom: 1,
            zoomPosition: { x: 0, y: 0 },
            isDragging: false,
            startX: 0,
            selectedObject: null,
            planets: [
                {
                    name: 'Mercure',
                    distance: 150,
                    size: 30,
                    modalSize: 300,
                    color: '#A0522D',
                    satellites: [],
                    description: 'Mercure est la planète la plus proche du Soleil.'
                },
                {
                    name: 'Venus',
                    distance: 250,
                    size: 45,
                    modalSize: 300,
                    color: '#DEB887',
                    satellites: [],
                    description: 'Vénus est souvent appelée la sœur jumelle de la Terre.'
                },
                {
                    name: 'Terre',
                    distance: 350,
                    size: 48,
                    modalSize: 300,
                    color: '#4169E1',
                    satellites: [
                        {
                            name: 'Lune',
                            size: 15,
                            modalSize: 200,
                            color: '#D3D3D3',
                            description: 'La Lune est le seul satellite naturel de la Terre. Elle est le cinquième plus grand satellite du système solaire.'
                        }
                    ],
                    description: 'Notre planète abrite la vie et possède un unique satellite naturel, la Lune.'
                },
                {
                    name: 'Mars',
                    distance: 450,
                    size: 40,
                    modalSize: 300,
                    color: '#CD5C5C',
                    satellites: [
                        {
                            name: 'Phobos',
                            size: 10,
                            modalSize: 150,
                            color: '#8B4513',
                            description: ''
                        },
                        {
                            name: 'Deimos',
                            size: 8,
                            modalSize: 120,
                            color: '#A0522D',
                            description: ''
                        }
                    ],
                    description: ''
                },
                {
                    name: 'Jupiter',
                    distance: 600,
                    size: 120,
                    modalSize: 400,
                    color: '#DAA520',
                    satellites: [
                        {
                            name: 'Io',
                            size: 20,
                            modalSize: 180,
                            color: '#FFD700',
                            description: ''
                        },
                        {
                            name: 'Europe',
                            size: 18,
                            modalSize: 170,
                            color: '#F0F8FF',
                            description: ''
                        },
                        {
                            name: 'Ganymède',
                            size: 25,
                            modalSize: 190,
                            color: '#B8860B',
                            description: ''
                        },
                        {
                            name: 'Callisto',
                            size: 22,
                            modalSize: 185,
                            color: '#808080',
                            description: ''
                        }
                    ],
                    description: ''
                },
                {
                    name: 'Saturne',
                    distance: 800,
                    size: 100,
                    modalSize: 380,
                    color: '#F4A460',
                    satellites: [
                        {
                            name: 'Titan',
                            size: 22,
                            modalSize: 200,
                            color: '#FFA500',
                            description: ''
                        },
                        {
                            name: 'Encelade',
                            size: 15,
                            modalSize: 160,
                            color: '#F0FFFF',
                            description: ''
                        },
                        {
                            name: 'Mimas',
                            size: 12,
                            modalSize: 150,
                            color: '#DCDCDC',
                            description: ''
                        }
                    ],
                    description: ''
                },
                {
                    name: 'Uranus',
                    distance: 1000,
                    size: 75,
                    modalSize: 340,
                    color: '#87CEEB',
                    satellites: [
                        {
                            name: 'Miranda',
                            size: 12,
                            modalSize: 150,
                            color: '#E0FFFF',
                            description: ''
                        },
                        {
                            name: 'Titania',
                            size: 16,
                            modalSize: 170,
                            color: '#F5F5F5',
                            description: ''
                        },
                        {
                            name: 'Obéron',
                            size: 15,
                            modalSize: 165,
                            color: '#D3D3D3',
                            description: ''
                        }
                    ],
                    description: ''
                },
                {
                    name: 'Neptune',
                    distance: 1200,
                    size: 72,
                    modalSize: 330,
                    color: '#1E90FF',
                    satellites: [
                        {
                            name: 'Triton',
                            size: 18,
                            modalSize: 180,
                            color: '#E6E6FA',
                            description: ''
                        },
                        {
                            name: 'Néréide',
                            size: 10,
                            modalSize: 140,
                            color: '#B0C4DE',
                            description: ''
                        }
                    ],
                    description: ''
                }
            ]
        }
    },
    methods: {
        showPlanetDetails(planet) {
            this.selectedObject = planet;
        },
        showSatelliteDetails(satellite) {
            const parentPlanet = this.selectedObject;
            satellite.parentPlanet = parentPlanet;
            this.selectedObject = satellite;
        },
        closeModal() {
            this.selectedObject = null;
        },
        resetZoom() {
            this.currentZoom = 1;
            this.zoomPosition = { x: 0, y: 0 };
            this.selectedObject = null;
        }
    }
}
</script>

<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

.systeme-container {
    width: 100%;
    min-height: 100vh;
    background-color: #000;
    color: white;
    padding: 20px;
    overflow: hidden;
    cursor: grab;
    position: relative;
}

.systeme-container:active {
    cursor: grabbing;
}

.titre {
    color: white;
    text-align: center;
    margin-bottom: 20px;
    z-index: 100;
    position: relative;
    font-size: 2.5em;
    text-shadow: 0 0 10px rgba(255, 255, 255, 0.5);
}

.solar-system {
    position: relative;
    width: 100%;
    height: calc(100vh - 100px);
    display: flex;
    align-items: center;
    justify-content: left;
    overflow: hidden;
}

.zoom-container {
    position: relative;
    transition: all 0.8s ease-out;
    transform-origin: left center;
    will-change: transform;
}

.sun {
    position: absolute;
    width: 120px;
    height: 120px;
    background-color: #FFD700;
    border-radius: 50%;
    left: 0;
    top: 50%;
    transform: translate(-50%, -50%);
    box-shadow: 0 0 60px #FFA500;
    cursor: pointer;
    z-index: 2;
    transition: box-shadow 0.3s ease;
}

.sun:hover {
    box-shadow: 0 0 80px #FFA500;
}

.orbit {
    position: absolute;
    height: 2px;
    z-index: -100;
    background-color: rgba(255, 255, 255, 0.1);
    top: 50%;
    left: 60px;
    transform-origin: left center;
}

.planet {
    position: absolute;
    border-radius: 50%;
    right: -5px;
    top: 50%;
    transform: translateY(-50%);
    cursor: pointer;
    transition: all 0.3s ease;
    z-index: 100;
}

.planet:hover {
    box-shadow: 0 0 30px rgba(255, 255, 255, 0.5);
    transform: translateY(-50%) scale(1.1);
}

.planet-name {
    position: absolute;
    top: -30px;
    left: 50%;
    transform: translateX(-50%);
    color: white;
    font-size: 16px;
    white-space: nowrap;
    text-shadow: 0 0 10px rgba(0, 0, 0, 0.8);
    opacity: 0.8;
    transition: opacity 0.3s ease;
}

.planet:hover .planet-name {
    opacity: 1;
}

/* Modal Styles */
.planet-modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.9);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
    opacity: 0;
    animation: fadeIn 0.3s ease forwards;
}

@keyframes fadeIn {
    from {
        opacity: 0;
    }

    to {
        opacity: 1;
    }
}

.planet-modal-content {
    background-color: rgba(20, 20, 40, 0.95);
    border-radius: 20px;
    padding: 40px;
    max-width: 800px;
    width: 90%;
    text-align: center;
    position: relative;
    transform: scale(0.9);
    opacity: 0;
    animation: modalIn 0.3s ease forwards 0.2s;
}

@keyframes modalIn {
    from {
        transform: scale(0.9);
        opacity: 0;
    }

    to {
        transform: scale(1);
        opacity: 1;
    }
}

.planet-large {
    width: 300px;
    height: 300px;
    border-radius: 50%;
    margin: 0 auto 30px;
    transition: all 0.3s ease;
    animation: rotate 60s linear infinite;
}

@keyframes rotate {
    from {
        transform: rotate(0deg);
    }

    to {
        transform: rotate(360deg);
    }
}

.planet-modal h2 {
    font-size: 2.5em;
    margin-bottom: 20px;
    color: white;
    text-shadow: 0 0 10px rgba(255, 255, 255, 0.3);
}

.planet-modal p {
    font-size: 1.2em;
    line-height: 1.6;
    color: #ccc;
    margin-bottom: 30px;
    max-width: 600px;
    margin-left: auto;
    margin-right: auto;
}

.satellites-container {
    margin-top: 30px;
    padding-top: 20px;
    border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.satellites-container h3 {
    color: #fff;
    font-size: 1.5em;
    margin-bottom: 20px;
    opacity: 0.9;
}

.satellites-list {
    display: flex;
    justify-content: center;
    gap: 30px;
    flex-wrap: wrap;
    margin-top: 20px;
}

.satellite-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
    transition: transform 0.3s ease;
}

.satellite-item:hover {
    transform: scale(1.1);
}

.satellite-circle {
    background-color: #888;
    border-radius: 50%;
    box-shadow: 0 0 15px rgba(255, 255, 255, 0.2);
    transition: box-shadow 0.3s ease;
}

.satellite-item:hover .satellite-circle {
    box-shadow: 0 0 20px rgba(255, 255, 255, 0.4);
}

.satellite-item span {
    color: #fff;
    font-size: 0.9em;
    opacity: 0.8;
}

.close-button {
    position: absolute;
    top: 20px;
    right: 20px;
    background: none;
    border: 2px solid white;
    color: white;
    padding: 8px 16px;
    border-radius: 20px;
    cursor: pointer;
    transition: all 0.3s ease;
    font-size: 1em;
    text-transform: uppercase;
    letter-spacing: 1px;
}

.close-button:hover {
    background-color: white;
    color: black;
    transform: scale(1.05);
}

/* Responsive Design */
@media (max-width: 768px) {
    .planet-large {
        width: 200px;
        height: 200px;
    }

    .planet-modal-content {
        padding: 20px;
    }

    .planet-modal h2 {
        font-size: 2em;
    }

    .planet-modal p {
        font-size: 1em;
    }

    .satellites-list {
        gap: 15px;
    }

    .close-button {
        top: 10px;
        right: 10px;
        padding: 6px 12px;
        font-size: 0.9em;
    }
}

@media (max-width: 480px) {
    .titre {
        font-size: 2em;
    }

    .planet-large {
        width: 150px;
        height: 150px;
    }

    .planet-modal h2 {
        font-size: 1.5em;
    }

    .satellites-container h3 {
        font-size: 1.2em;
    }
}
</style>