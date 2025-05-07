<template>
  <div class="author-table-container">
    <table class="author-table">
      <thead>
      <tr>
        <th>#</th>
        <th>Имя</th>
        <th>Фамилия</th>
        <th>Действия</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="(author, index) in authors" :key="author.id" tabindex="0">
        <td class="index-column">{{ index + 1 }}</td>
        <td class="first-name-column">{{ author.firstName }}</td>
        <td class="last-name-column">{{ author.lastName }}</td>
        <td class="actions-column">
          <button class="action-btn edit-btn" @click="$emit('edit', author)">✏️ Редактировать</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
defineProps({
  authors: Array
});

defineEmits(['add', 'edit']);
</script>

<style scoped>
.author-table-container {
  margin: 2rem 0;
}

.author-table {
  width: 100%;
  border-collapse: collapse;
  font-family: system-ui, sans-serif;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.author-table th, .author-table td {
  padding: 12px 16px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.author-table thead th {
  background-color: black;
  font-weight: 600;
}

.author-table tbody tr {
  transition: background 0.2s ease;
}

.author-table tbody tr:hover {
  background-color: #f0f4f8;
  color: black;
}

.author-table tbody tr:active,
.author-table tbody tr:focus-within {
  background-color: green;
  color: white;
}

.index-column {
  width: 40px;
  color: #888;
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