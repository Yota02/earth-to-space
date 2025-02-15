<template>
    <div class="formulaire-creation">
        <h2>Créer un nouveau lanceur</h2>
        <form @submit.prevent="submitForm">
            <div class="form-group">
                <label for="nom">Nom du lanceur:</label>
                <input type="text" id="nom" v-model="lanceur.nom" required>
            </div>

            <div class="form-group">
                <label for="poids">Poids (tonnes):</label>
                <input type="number" id="poids" v-model="lanceur.poids" required>
            </div>

            <div class="form-group">
                <label for="taille">Taille (mètres):</label>
                <input type="number" id="taille" v-model="lanceur.taille" required>
            </div>

            <div class="form-group">
                <label for="puissance">Puissance (kN):</label>
                <input type="number" id="puissance" v-model="lanceur.puissance" required>
            </div>

            <h3>Charge utile maximale</h3>
            <div class="form-group">
                <label for="orbiteBasse">Orbite basse (kg):</label>
                <input type="number" id="orbiteBasse" v-model="lanceur.chargeUtile.orbiteBasse">
            </div>

            <div class="form-group">
                <label for="geostationnaire">Orbite géostationnaire (kg):</label>
                <input type="number" id="geostationnaire" v-model="lanceur.chargeUtile.geostationnaire">
            </div>

            <div class="form-group">
                <label for="lune">Vers la Lune (kg):</label>
                <input type="number" id="lune" v-model="lanceur.chargeUtile.lune">
            </div>

            <div class="form-group">
                <label for="autre">Autre destination (kg):</label>
                <input type="number" id="autre" v-model="lanceur.chargeUtile.autre">
            </div>

            <button type="submit">Créer le lanceur</button>
        </form>
    </div>
</template>

<script>
export default {
    name: 'FormulaireCreation',
    data() {
        return {
            lanceur: {
                nom: '',
                poids: null,
                taille: null,
                puissance: null,
                chargeUtile: {
                    orbiteBasse: null,
                    geostationnaire: null,
                    lune: null,
                    autre: null
                }
            }
        }
    },
    methods: {
        submitForm() {
            // Ici, vous pouvez ajouter la logique pour envoyer les données au backend
            console.log('Données du lanceur:', this.lanceur)
            // Réinitialiser le formulaire après soumission
            this.resetForm()
        },
        resetForm() {
            this.lanceur = {
                nom: '',
                poids: null,
                taille: null,
                puissance: null,
                chargeUtile: {
                    orbiteBasse: null,
                    geostationnaire: null,
                    lune: null,
                    autre: null
                }
            }
        }
    }
}
</script>

<style scoped>
.formulaire-creation {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
}

.form-group {
    margin-bottom: 15px;
}

label {
    display: block;
    margin-bottom: 5px;
}

input {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
}

button {
    background-color: #4CAF50;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

button:hover {
    background-color: #45a049;
}

h2, h3 {
    color: #333;
}
</style>