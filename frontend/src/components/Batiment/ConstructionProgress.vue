<template>
    <div class="relative pt-1">
      <div class="flex mb-2 items-center justify-between">
        <div>
          <span class="text-xs font-semibold inline-block py-1 px-2 rounded-full text-blue-600">
            {{ etat }}%
          </span>
        </div>
        <div class="text-xs text-gray-500">
          {{ tempsRestant }}
        </div>
      </div>
      <div class="overflow-hidden h-2 mb-4 text-xs flex rounded bg-blue-100">
        <div 
          class="shadow-none flex flex-col text-center whitespace-nowrap text-white justify-center bg-blue-500"
          :style="{ width: etat + '%' }"
        ></div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: 'ConstructionProgress',
    props: {
      etat: {
        type: Number,
        required: true
      },
      tempsRestant: {
        type: String,
        required: true
      }
    }
  }
  </script>