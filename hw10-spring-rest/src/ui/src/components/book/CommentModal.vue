<template>
  <div class="modal">
    <h2>Комментарии для книги #{{ bookId }}</h2>

    <div v-if="loading">Загрузка...</div>

    <div v-else-if="comments.length === 0">Комментриев нет</div>

    <ul v-else>
      <li v-for="comment in comments" :key="comment.id">
        <input v-model="comment.text" @keyup.enter="saveComment(comment)" />
        <button @click="saveComment(comment)">💾</button>
      </li>
    </ul>

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
import commentApi from '@/api/comment.js';

const props = defineProps({
  bookId: {
    type: Number,
    required: true
  }
});
defineEmits(['close']);
const comments = ref([]);
const loading = ref(false);

const fetchComments = async () => {
  try {
    loading.value = true;
    const response = await commentApi.getCommentsByBookId(props.bookId);
    comments.value = Array.isArray(response) ? response : [];
  } catch (error) {
    console.error('Error loading comments:', error);
    comments.value = [];
  } finally {
    loading.value = false;
  }
};

watch(() => props.bookId, fetchComments, { immediate: true });

const saveComment = async (comment) => {
  try {
    await commentApi.updateComment(
        { id: comment.id,
          text: comment.text,
          bookId: props.bookId });
  } catch (error) {
    console.error('Error saving comment:', error);
  }
};
</script>

<style scoped>
.modal {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: black;
  padding: 2rem;
  border: 1px solid #ccc;
  z-index: 1000;
  min-width: 400px;
}

ul {
  list-style: none;
  padding: 0;
}

li {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
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