<template>
  <div class="modal">
    <h2>{{ localGenre.id ? 'Редактировать жанр' : 'Добавить жанр' }}</h2>
    <form @submit.prevent="save">
      <div class="form-group">
        <label>Название:</label>
        <input v-model="localGenre.name" required />
      </div>

      <div class="button-group">
        <button type="submit">💾 Сохранить</button>
        <button type="button" @click="$emit('close')">🚫 Отмена</button>
      </div>
    </form>
    <button
        class="close-button"
        @click="$emit('close')"
        aria-label="Close"
    >
      &times;
    </button>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';

const props = defineProps({
  genre: Object
});

const emit = defineEmits(['close', 'save']);

const localGenre = ref({
  id: null,
  name: ''
});

watch(() => props.genre, (newGenre) => {
  localGenre.value = {
    id: newGenre?.id || null,
    name: newGenre?.name || ''
  };
}, { immediate: true });

const save = () => {
  emit('save', {
    id: localGenre.value.id || 0,
    name: localGenre.value.name
  });
};
</script>

<style scoped>
.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: black;
  padding: 20px;
  border: 1px solid #ccc;
  z-index: 1000;
  width: 300px;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
}

.form-group label {
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.button-group {
  display: flex;
  gap: 10px;
  margin-top: 15px;
}

.button-group button {
  padding: 8px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.close-button {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 2rem;
  cursor: pointer;
  color: #666;
  padding: 0 8px;
  line-height: 1;
}

.close-button:hover {
  background-color: black;
  color: red;
}
</style>