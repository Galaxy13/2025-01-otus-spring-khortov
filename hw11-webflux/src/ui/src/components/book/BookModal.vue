<template>
  <div class="modal">
    <h2>{{ localBook.id ? 'Редактировать книгу' : 'Добавить книгу' }}</h2>
    <form @submit.prevent="save">
      <div class="form-group">
        <label>Название:</label>
        <input v-model="localBook.title" required />
      </div>

      <div class="form-group">
        <label>Автор:</label>
        <select v-model="localBook.authorId" required>
          <option
              v-for="author in authors"
              :key="author.id"
              :value="author.id"
          >
            {{ author.firstName }} {{ author.lastName }}
          </option>
        </select>
      </div>

      <div class="form-group">
        <label>Жанры:</label>
        <select
            v-model="localBook.genreIds"
            multiple
            required
        >
          <option
              v-for="genre in genres"
              :key="genre.id"
              :value="genre.id"
          >
            {{ genre.name }}
          </option>
        </select>
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
  book: Object,
  authors: Array,
  genres: Array
});

const emit = defineEmits(['close', 'save']);

const localBook = ref({
  id: null,
  title: '',
  authorId: null,
  genreIds: []
});

watch(() => props.book, (newBook) => {
  localBook.value = {
    id: newBook?.id || null,
    title: newBook?.title || '',
    authorId: newBook?.authorId || null,
    genreIds: newBook?.genreIds || []
  };
}, { immediate: true });

const save = () => {
  const bookDto = {
    id: localBook.value.id || 0,
    title: localBook.value.title,
    authorId: localBook.value.authorId,
    genreIds: [...localBook.value.genreIds]
  };

  emit('save', bookDto);
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

.form-group input,
.form-group select {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

select[multiple] {
  height: auto;
  min-height: 100px;
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