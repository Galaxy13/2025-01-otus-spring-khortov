<template>
  <div class="start-view">
    <h1>Хранилище книг</h1>

    <div class="view-tabs">
      <button
          v-for="tab in tabs"
          :key="tab.id"
          @click="activeTab = tab.id"
          :class="{ active: activeTab === tab.id }"
      >
        {{ tab.label }}
      </button>
    </div>

    <div class="view-container">
      <BookView v-if="activeTab === 'books'" />
      <AuthorView v-if="activeTab === 'authors'" />
      <GenreView v-if="activeTab === 'genres'" />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import BookView from './BookView.vue';
import AuthorView from './AuthorView.vue';
import GenreView from './GenreView.vue';

const tabs = [
  { id: 'books', label: '📚 Книги' },
  { id: 'authors', label: '✍️ Авторы' },
  { id: 'genres', label: '🏷️ Жанры' }
];

const activeTab = ref('books');
</script>

<style scoped>
.start-view {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.view-tabs {
  display: flex;
  gap: 1rem;
  margin: 2rem 0;
  justify-content: center;
  position: sticky;
  top: 0;
  padding: 1rem 0;
  z-index: 10;
}

.view-tabs button {
  padding: 0.75rem 1.5rem;
  min-width: 120px;
  text-align: center;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: all 0.3s;
}

.view-tabs button.active {
  background: #4a6baf;
  font-weight: bold;
}

.view-container {
  flex: 1;
  width: 100%;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 2rem;
}
</style>