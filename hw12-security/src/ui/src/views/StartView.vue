<template>
  <div class="start-view">
    <div class="auth-status" v-if="isAuthenticated">
      <div class="user-info">
        <span>👤 {{ username }}</span>
        <button @click="logout">Logout</button>
      </div>
    </div>

    <h1>Хранилище книг</h1>

    <div class="view-tabs">
      <button
          v-for="tab in tabs"
          :key="tab.id"
          @click="handleTabChange(tab)"
          :class="{ active: activeTab === tab.id }"
      >
        {{ tab.label }}
      </button>
    </div>

    <div v-if="showAuthRequiredModal" class="modal-overlay" @click.self="showAuthRequiredModal = false">
      <div class="modal-content">
        <h2>Требуется авторизация</h2>
        <p>Вам необходимо авторизоваться для просмотра данной секции.</p>
        <button @click="showAuthRequiredModal = false">Отмена</button>
        <button @click="showLoginModal = true; showAuthRequiredModal = false" class="primary">Войти</button>
      </div>
    </div>

    <div class="view-container">
      <BookView v-if="activeTab === 'books'" />
      <AuthorView v-if="activeTab === 'authors'" />
      <GenreView v-if="activeTab === 'genres'" />
    </div>

    <Toast
        v-if="toast.show"
        :message="toast.message"
        :type="toast.type"
        @close="toast.show = false"
    />

    <div v-if="showLoginModal" class="modal-overlay" @click.self="showLoginModal = false">
      <div class="modal-content">
        <h2>Login</h2>
        <form @submit.prevent="login">
          <div class="form-group">
            <label for="username">Имя пользователя:</label>
            <input id="username" v-model="loginForm.username" type="text" required>
          </div>
          <div class="form-group">
            <label for="password">Пароль:</label>
            <input id="password" v-model="loginForm.password" type="password" required>
          </div>
          <div class="form-group">
            <label>
              <input type="checkbox" v-model="loginForm.rememberMe"> Запомнить меня
            </label>
          </div>
          <button type="submit" class="login-button">Войти</button>
          <button type="button" @click="showLoginModal = false" class="login-cancel-button">Отмена</button>
          <div v-if="loginError" class="error-message">{{ loginError }}</div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted, } from 'vue';
import { useToastStore } from '@/components/store/toast.js';
import BookView from './BookView.vue';
import AuthorView from './AuthorView.vue';
import GenreView from './GenreView.vue';
import Toast from '../components/ui/Toast.vue';

const tabs = [
  { id: 'books', label: '📚 Книги', requiresAuth: false },
  { id: 'authors', label: '✍️ Авторы', requiresAuth: true },
  { id: 'genres', label: '🏷️ Жанры', requiresAuth: true }
];

const activeTab = ref('books');
const showLoginModal = ref(false);
const showAuthRequiredModal = ref(false);
const isAuthenticated = ref(false);
const username = ref('');
const loginError = ref('');

const loginForm = ref({
  username: '',
  password: '',
  rememberMe: false
});

onMounted(async () => {
  try {
    const response = await fetch('/api/v1/user/current', {
      credentials: 'include'
    });

    if (response.ok) {
      isAuthenticated.value = true;
      const userResponse = await fetch('/api/v1/user/current',
          { credentials: 'include' });
      const userData = await userResponse.json();
      username.value = userData.username;
    } else {
      isAuthenticated.value = false;
    }
  } catch (error) {
    isAuthenticated.value = false;
    console.error('Auth check failed:', error);
  }
});

const toast = useToastStore();

const login = async () => {
  loginError.value = '';

  const formData = new URLSearchParams();
  formData.append('username', loginForm.value.username);
  formData.append('password', loginForm.value.password);
  if (loginForm.value.rememberMe) {
    formData.append('remember-me', 'true');
  }

  try {
    const response = await fetch('/login', {
      method: 'POST',
      body: formData,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      credentials: 'include'
    });

    if (response.ok) {
      isAuthenticated.value = true;
      username.value = loginForm.value.username;
      showLoginModal.value = false;
      loginForm.value = { username: '', password: '', rememberMe: false };
    } else {
      const error = await response.json();
      const errMsg = error.error;
      loginError.value = errMsg || 'Login failed';
    }
  } catch (error) {
    loginError.value = 'Network error during login';
    console.error('Login failed:', error);
  }
};

const logout = async () => {
  try {
    const response = await fetch('/logout', {
      method: 'POST',
      credentials: 'include'
    });

    if (response.redirected) {
      window.location.href = response.url
    }
    else if (response.ok) {
      isAuthenticated.value = false;
      username.value = '';
      window.location.href = '/login'
    }
  } catch (error) {
    console.error('Logout failed:', error);
  }
};

const handleTabChange = (tab) => {
  if (tab.requiresAuth && !isAuthenticated.value) {
    showAuthRequiredModal.value = true;
  } else {
    activeTab.value = tab.id;
  }
};
</script>

<style scoped>
.start-view {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  position: relative;
}

.auth-status {
  position: absolute;
  top: 1rem;
  right: 1rem;
}

.auth-status button, .user-info button {
  background: none;
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  transition: all 0.3s;
}

.auth-status button:hover, .user-info button:hover {
  background: #f0f0f0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
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

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(5px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  max-width: 400px;
  width: 100%;
}

.modal-content h2 {
  margin-top: 0;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
}

.form-group input {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.modal-content button {
  margin-right: 1rem;
  padding: 0.5rem 1rem;
  cursor: pointer;
}

.modal-overlay {
  background: black;
  color: black;
}

.modal-overlay button {
  padding: 0.75rem 1.5rem;
  min-width: 120px;
  text-align: center;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: all 0.3s;
  background: #e0e0e0;
}

.modal-overlay button:hover {
  background: #d5d5d5;
}

.modal-overlay button.primary {
  background: #4a6baf;
  color: white;
  font-weight: bold;
}

.error-message {
  color: red;
  margin-top: 1rem;
}
</style>