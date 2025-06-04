import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useToastStore = defineStore('toast', () => {
    const show = ref(false);
    const message = ref('');
    const type = ref('info');

    function showToast(msg, toastType = 'info') {
        message.value = msg;
        type.value = toastType;
        show.value = true;

        setTimeout(() => {
            show.value = false;
        }, 3000);
    }

    return { show, message, type, showToast };
});
