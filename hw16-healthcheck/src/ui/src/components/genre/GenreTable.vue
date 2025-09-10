<template>
  <div class="genre-table-container">
    <table class="genre-table">
      <thead>
      <tr>
        <th>#</th>
        <th>Наименования</th>
        <th>Действия</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(genre, index) in genres" :key="genre.id" tabindex="0">
        <td class="index-column">{{ index + 1 }}</td>
        <td class="name-column">{{ genre.name }}</td>
        <td class="actions-column">
          <button class="action-btn edit-btn" @click="$emit('edit', genre)">✏️ Редактировать</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
defineProps({
  genres: Array
});

defineEmits(['add', 'edit']);
</script>

<style scoped>
.genre-table-container {
  margin: 2rem 0;
}

.genre-table {
  width: 100%;
  border-collapse: collapse;
  font-family: system-ui, sans-serif;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.genre-table th, .genre-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.genre-table thead th {
  background-color: black;
  font-weight: 600;
}

.genre-table tbody tr {
  transition: background 0.2s ease;
}

.genre-table tbody tr:hover {
  background-color: #f0f4f8;
  color: black;
}

.genre-table tbody tr:active,
.genre-table tbody tr:focus-within {
  background-color: green;
  color: white;
}

.index-column {
  width: 40px;
  color: #888;
}

.name-column {
  min-width: 150px;
}

.actions-column {
  white-space: nowrap;
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

.action-btn:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

.action-btn:active {
  transform: translateY(0);
}
</style>