const BASE_URL = '/flux/';

const handleResponse = async (response) => {
    if (!response.ok) throw new Error(await response.text());
    return response.json();
};

export default {
    async getBooks() {
        const response = await fetch(`${BASE_URL}book`);
        return handleResponse(response);
    },

    async getBook(id) {
        const response = await fetch(`${BASE_URL}book/${id}`);
        return handleResponse(response);
    },

    async updateBook(book) {
        const response = await fetch(`${BASE_URL}book/${book.id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(book),
        });
        return handleResponse(response);
    },

    async createBook(book) {
        const response = await fetch(`${BASE_URL}book`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(book),
        });
        return handleResponse(response);
    },

    async deleteBook(id) {
        const response = await fetch(`${BASE_URL}book/${id}`, {
            method: 'DELETE',
        });
        return handleResponse(response);
    },
};
