<template>
  <div class="create-entreprise" v-if="!debugMode">
      <h2>Créer votre entreprise</h2>
      <form @submit.prevent="createEntreprise">
          <div class="form-group">
              <label for="nom">Nom de l'entreprise</label>
              <input type="text" id="nom" v-model="entreprise.nom" required>
          </div>
          <div class="form-group">
              <label for="pays">Pays</label>
              <select id="pays" v-model="entreprise.pays" required>
                  <option value="USA">États-Unis</option>
                  <option value="EU">Europe</option>
                  <option value="CN">Chine</option>
              </select>
          </div>
          <button type="submit">Créer</button>
      </form>
  </div>
</template>

<script>
export default {
  name: 'CreateEntrepriseForm',
  props: {
      debugMode: {
          type: Boolean,
          default: false
      }
  },
  data() {
      return {
          entreprise: {
              nom: '',
              pays: '',
          }
      }
  },
  methods: {
      createEntreprise() {
          if (!this.debugMode) {
              this.$emit('entreprise-created', this.entreprise);
          }
      }
  }
}
</script>

<style scoped>
.create-entreprise {
  max-width: 500px;
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

input, select {
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
</style>