<template>
  <div class="author-view">
    <div class="header">
      <h1>Авторы</h1>
    </div>
    <button class="action-btn" @click="openCreateModal">➕ Новый автор</button>

    <AuthorTable
        :authors="authors"
        @edit="openEditModal"
    />

    <AuthorModal
        v-if="showEditModal"
        :author="selectedAuthor"
        @close="closeModal"
        @save="saveAuthor"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import authorApi from '@/api/author.js';
import AuthorTable from '@/components/author/AuthorTable.vue';
import AuthorModal from '@/components/author/AuthorModal.vue';
import { useToastStore } from '@/components/store/toast.js';

const authors = ref([]);
const selectedAuthor = ref(null);
const showEditModal = ref(false);

const loadData = async () => {
  authors.value = await authorApi.getAuthors();
};

const toast = useToastStore();

onMounted(loadData);

const openCreateModal = () => {
  selectedAuthor.value = { firstName: '', lastName: '' };
  showEditModal.value = true;
};

const openEditModal = (author) => {
  selectedAuthor.value = { ...author };
  showEditModal.value = true;
};

const closeModal = () => showEditModal.value = false;

const saveAuthor = async (author) => {
  const authorDto = {
    id: author?.id || 0,
    firstName: author.firstName,
    lastName: author.lastName,
  }
  try {
    if (author.id !== 0) {
      await authorApi.updateAuthor(authorDto);
    } else {
      await authorApi.createAuthor(authorDto);
    }
    toast.showToast('Автор успешно сохранён', 'success');
    await loadData();
    closeModal();
  } catch (error) {
    if (error.status === 403) {
      toast.showToast('Недостаточно прав для выполнения операции.', 'error');
    } else {
      const errorMessage = error?.message ||
          'Произошла ошибка при сохранении.';
      toast.showToast(errorMessage, 'error');
    }
  }
};
</script>

<style scoped>
.author-view {
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