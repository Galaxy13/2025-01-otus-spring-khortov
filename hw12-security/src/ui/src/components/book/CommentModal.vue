<template>
  <div class="modal">
    <h2>Комментарии для книги #{{ bookId }}</h2>

    <div v-if="loading">Загрузка...</div>

    <div v-else-if="comments.length === 0">Комментариев нет</div>

    <ul v-else>
      <li v-for="comment in comments" :key="comment.id">
        <input
            v-if="comment.editAllowed"
            v-model="comment.text"
            @keyup.enter="saveComment(comment)"
        />
        <span v-else>{{ comment.text }}</span>
        <button
            v-if="comment.editAllowed"
            @click="saveComment(comment)"
        >
          💾
        </button>
      </li>
    </ul>

    <div class="new-comment">
      <input
          v-model="newCommentText"
          placeholder="Введите новый комментарий"
          @keyup.enter="addComment"
      />
      <button @click="addComment" class="save-button">Добавить</button>
    </div>

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
import {useToastStore} from "@/components/store/toast.js";

const props = defineProps({
  bookId: {
    type: Number,
    required: true
  }
});

defineEmits(['close']);
const comments = ref([]);
const loading = ref(false);
const newCommentText = ref('');

const toast = useToastStore();

const fetchComments = async () => {
  try {
    loading.value = true;
    const response = await commentApi.getCommentsByBookId(props.bookId);
    comments.value = Array.isArray(response) ? response : [];
    for (const comment of response) {
      console.log(comment);
    }
  } catch (error) {
    console.error('Error loading comments:', error);
    comments.value = [];
  } finally {
    loading.value = false;
  }
};

watch(() => props.bookId, fetchComments, {immediate: true});

const saveComment = async (comment) => {
  try {
    await commentApi.updateComment({
      id: comment.id,
      text: comment.text,
      bookId: props.bookId,
      editAllowed: false
    });
    toast.showToast('Комментарий успешно сохранён', 'success');
  } catch (error) {
    if (error?.response === 403) {
      toast.showToast('Недостаточно прав для выполнения этой операции', 'error');
    } else {
      const errorMessage = error?.message || 'Произошла ошибка при сохранении комментария';
      toast.showToast(errorMessage, 'error');
    }
  }
};

const addComment = async () => {
  if (!newCommentText.value.trim()) return;

  try {
    const newComment = await commentApi.createComment({
      id: 0,
      text: newCommentText.value,
      bookId: props.bookId
    });

    comments.value = [...comments.value, newComment];
    newCommentText.value = '';
  } catch (error) {
    console.error('Error adding comment:', error);
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
  margin-bottom: 1rem;
}

li {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
  align-items: center;
}

.new-comment {
  display: flex;
  gap: 0.5rem;
  margin-top: 1rem;
}

.new-comment input {
  flex-grow: 1;
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

.save-button {
  padding: 6px 10px;
  margin-right: 6px;
  font-size: 0.85rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  background: #4a6baf;
  color: #fff;
}
</style>