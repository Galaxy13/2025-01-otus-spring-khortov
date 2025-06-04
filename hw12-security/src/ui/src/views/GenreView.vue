<template>
  <div class="genre-view">
    <div class="header">
      <h1>Жанры</h1>
    </div>
    <button class="action-btn" @click="openCreateModal">➕ Новый жанр</button>

    <GenreTable
        :genres="genres"
        @edit="openEditModal"
    />

    <GenreModal
        v-if="showEditModal"
        :genre="selectedGenre"
        @close="closeModal"
        @save="saveGenre"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import genreApi from '@/api/genre.js';
import GenreTable from '@/components/genre/GenreTable.vue';
import GenreModal from '@/components/genre/GenreModal.vue';
import {useToastStore} from "@/components/store/toast.js";

const genres = ref([]);
const selectedGenre = ref(null);
const showEditModal = ref(false);

const loadData = async () => {
  genres.value = await genreApi.getGenres();
};

const toast = useToastStore();

onMounted(loadData);

const openCreateModal = () => {
  selectedGenre.value = { name: '' };
  showEditModal.value = true;
};

const openEditModal = (genre) => {
  selectedGenre.value = { ...genre };
  showEditModal.value = true;
};

const closeModal = () => showEditModal.value = false;

const saveGenre = async (genre) => {
  const genreDto = {
    id: genre.id,
    name: genre.name,
  }
  try {
    if (genre.id !== 0) {
      await genreApi.updateGenre(genreDto);
    } else {
      await genreApi.createGenre(genreDto);
    }
    await loadData();
    toast.showToast('Жанр успешно сохранён', 'success');
    closeModal();
  } catch (error) {
    if (error.status === 403) {
      toast.showToast('Недостаточно прав для выполнения операции.', 'error');
    } else {
      const errorMessage = error?.message || 'Произошла ошибка при сохранении.'
      toast.showToast(errorMessage, 'error');
    }
  }
};
</script>

<style scoped>
.genre-view {
  padding: 2rem;
}

.action-btn {
  padding: 6px 10px;
  margin-right: 6px;
  font-size: 0.85rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
}

.action-btn {
  background-color: #e3f2fd;
  color: #1976d2;
}

.action-btn:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

.action-btn:active {
  transform: translateY(0);
}
</style>