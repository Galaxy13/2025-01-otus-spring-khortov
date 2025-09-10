<template>
  <div class="book-table-container">
    <table class="book-table">
      <thead>
      <tr>
        <th>#</th>
        <th>Наименование</th>
        <th>Автор</th>
        <th>Жанры</th>
        <th>Действия</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(book, index) in books" :key="book.id" tabindex="0">
        <td class="index-column">{{ index + 1 }}</td>
        <td class="title-column">{{ book.title }}</td>
        <td class="author-column">{{ book.author.firstName }} {{ book.author.lastName }}</td>
        <td class="genres-column">
            <span v-for="(genre, gIndex) in book.genres" :key="genre.id">
              {{ genre.name }}<span v-if="gIndex < book.genres.length - 1">, </span>
            </span>
        </td>
        <td class="actions-column">
          <button class="action-btn edit-btn" @click="$emit('edit', book)">✏️ Редактировать</button>
          <button class="action-btn delete-btn" @click="$emit('delete', book.id)">🗑️ Удалить</button>
          <button class="action-btn comments-btn" @click="$emit('view-comments', book.id)">💬 Комментарии</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
defineProps({
  books: Array
});
</script>

<style scoped>
.book-table {
  width: 100%;
  border-collapse: collapse;
  font-family: system-ui, sans-serif;
  margin: 1rem 0;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.book-table th, .book-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.book-table thead th {
  background-color: black;
  font-weight: 600;
}

.book-table tbody tr {
  transition: background 0.2s ease;
}

.book-table tbody tr:hover {
  background-color: #f0f4f8;
  color: black;
}

.book-table tbody tr:active,
.book-table tbody tr:focus-within {
  background-color: green;
  color: white;
}

.index-column {
  width: 40px;
  color: #888;
}

.author-column,
.actions-column {
  white-space: nowrap;
}

.genres-column {
  min-width: 150px;
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

.edit-btn {
  background-color: #e3f2fd;
  color: #1976d2;
}

.delete-btn {
  background-color: #ffebee;
  color: #d32f2f;
}

.comments-btn {
  background-color: #e8f5e9;
  color: #388e3c;
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

