<template>
  <div>
    <h1>Книги</h1>
    <button class="action-btn" @click="openCreateModal">➕ Новая книга</button>

    <BookTable
        :books="books"
        @edit="openEditModal"
        @delete="deleteBook"
        @view-comments="openCommentsModal"
    />

    <BookModalModal
        v-if="showEditModal"
        :book="selectedBook"
        :authors="authors"
        :genres="genres"
        @close="closeModal"
        @save="saveBook"
    />

    <CommentModal
        v-if="showCommentsModal"
        :book-id="selectedBookId"
        @close="closeCommentsModal"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import bookApi from '@/api/book.js';
import authorApi from '@/api/author.js';
import genreApi from '@/api/genre.js';
import BookTable from '@/components/book/BookTable.vue';
import BookModalModal from '@/components/book/BookModal.vue';
import CommentModal from '@/components/book/CommentModal.vue';

const books = ref([]);
const authors = ref([]);
const genres = ref([]);
const selectedBook = ref(null);
const selectedBookId = ref(null);
const showEditModal = ref(false);
const showCommentsModal = ref(false);

const loadData = async () => {
  books.value = await bookApi.getBooks();
  authors.value = await authorApi.getAuthors();
  genres.value = await genreApi.getGenres();
};

onMounted(loadData);

const openCreateModal = () => {
  selectedBook.value = { title: '', authorId: null, genres: [] };
  showEditModal.value = true;
};

const openEditModal = (book) => {
  selectedBook.value = {
    id: book.id,
    title: book.title,
    authorId: book.author?.id || null ,
    genreIds: book.genres?.map(g => g.id) || []
  };
  showEditModal.value = true;
};

const openCommentsModal = (bookId) => {
  selectedBookId.value = bookId;
  showCommentsModal.value = true;
};

const closeModal = () => showEditModal.value = false;
const closeCommentsModal = () => showCommentsModal.value = false;

const saveBook = async (book) => {
  if (book.id) {
    await bookApi.updateBook(book.id, book);
  } else {
    await bookApi.createBook(book);
  }
  await loadData();
  closeModal();
};

const deleteBook = async (id) => {
  if (confirm(`Удалить книгу #${id}?`)) {
    await bookApi.deleteBook(id);
    await loadData();
  }
};
</script>

<style scoped>
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

<style>
::selection {
  background: #cceeff;
  color: #000;
}
</style>
