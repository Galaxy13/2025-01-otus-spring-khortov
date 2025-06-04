<template>
  <div class="toast" :class="type">
    {{ message }}
    <button @click="$emit('close')" class="close">&times;</button>
  </div>
</template>

<script setup>
defineProps({
  message: String,
  type: {
    type: String,
    default: 'error',
    validator: (value) => ['error', 'success', 'warning'].includes(value)
  }
});

defineEmits(['close']);
</script>

<style scoped>
.toast {
  position: fixed;
  bottom: 20px;
  right: 20px;
  padding: 15px 20px;
  border-radius: 5px;
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-width: 250px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  z-index: 9999;
}

.toast.error {
  background-color: #ff4444;
}

.toast.success {
  background-color: #00C851;
}

.toast.warning {
  background-color: #ffbb33;
}

.close {
  background: none;
  border: none;
  color: white;
  font-size: 1.2rem;
  cursor: pointer;
  margin-left: 10px;
}
</style>