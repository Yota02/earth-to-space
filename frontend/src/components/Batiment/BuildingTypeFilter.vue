<template>
    <div class="mb-4">
      <select 
        v-model="selectedType" 
        class="w-full p-2 border rounded-lg"
        @change="$emit('type-change', selectedType)"
      >
        <option value="">Tous les types</option>
        <option v-for="type in types" :key="type" :value="type">
          {{ type }}
        </option>
      </select>
    </div>
  </template>
  
  <script>
  export default {
    name: 'BuildingTypeFilter',
    props: {
      types: {
        type: Array,
        required: true
      }
    },
    data() {
      return {
        selectedType: ''
      }
    },
    emits: ['type-change']
  }
  </script>