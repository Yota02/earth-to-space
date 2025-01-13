<template>
  <div class="max-w-3xl mx-auto p-8">
    <h1 class="text-3xl font-semibold mb-8 text-center text-gray-800">Détails du Programme</h1>

    <!-- Section Programme -->
    <div class="bg-white p-6 rounded-lg shadow-lg">
      <div class="mb-4 flex items-center">
        <span class="font-medium text-lg text-gray-900 w-32">Nom :</span>
        <span class="text-gray-700">{{ programme.nom }}</span>
      </div>

      <div class="mb-4 flex items-center">
        <span class="font-medium text-lg text-gray-900 w-32">Objectif :</span>
        <span class="text-gray-700">{{ programme.objectif }}</span>
      </div>

      <div class="mb-4 flex items-center">
        <span class="font-medium text-lg text-gray-900 w-32">Budget :</span>
        <span class="text-gray-700">{{ programme.budget !== null ? programme.budget : 'Budget non spécifié' }}</span>
      </div>

      <div class="mb-6 flex items-center">
        <span class="font-medium text-lg text-gray-900 w-32">Durée prévue :</span>
        <span class="text-gray-700">{{ programme.dureePrevu !== null ? programme.dureePrevu + ' mois' : 'Durée non spécifiée' }}</span>
      </div>
    </div>

    <!-- Lien Retour -->
    <div class="mt-8 text-center">
      <router-link to="/programme" class="text-blue-600 hover:underline text-lg">
        Retour à la liste des programmes
      </router-link>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from "vue";
import { useRoute } from "vue-router";

export default {
  name: "ProgrammeDetails",
  setup() {
    const route = useRoute();
    const programme = ref({
      nom: "",
      objectif: "",
      budget: null,
      dureePrevu: null
    });

    onMounted(() => {
      const nom = route.query.nom || "Nom non spécifié";
      const objectif = route.query.objectif || "Objectif non spécifié";

      const budget = route.query.budget ? parseFloat(route.query.budget) : null;
      const dureePrevu = route.query.dureePrevu ? parseInt(route.query.dureePrevu) : null;

      programme.value = { nom, objectif, budget, dureePrevu };
    });

    return { programme };
  }
};
</script>

<style scoped>
/* Style des titres et des sections */
.text-gray-700 {
  font-size: 1.125rem;
}

.text-gray-900 {
  font-size: 1.125rem;
}

.bg-white {
  background-color: #ffffff;
}

/* Ombre pour les cartes */
.shadow-lg {
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

/* Espacement et marges */
.mb-4 {
  margin-bottom: 1rem;
}

.mb-6 {
  margin-bottom: 1.5rem;
}

.mb-8 {
  margin-bottom: 2rem;
}

/* Liens */
.text-blue-600 {
  color: #2563eb;
}

/* Retour */
.text-lg {
  font-size: 1.125rem;
}
</style>
