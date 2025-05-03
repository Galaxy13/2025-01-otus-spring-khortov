const BASE_URL = '/api/v1/'

const handleResponse = async (response) => {
    if (!response.ok) throw new Error(await response.text());
    return response.json();
};

export default {
    async getAuthors() {
        const response = await fetch(`${BASE_URL}author`);
        return handleResponse(response);
    },

    async getAuthorById(id) {
        const response = await fetch(`${BASE_URL}author/${id}`);
        return handleResponse(response);
    },

    async updateAuthor(id, author) {
        const response = await fetch(`${BASE_URL}author/${id}`, {
            method: 'PUT',
            headers: { 'content-type': 'application/json' },
            body: JSON.stringify(author),
        });
        return handleResponse(response);
    },

    async createAuthor(author) {
        const response = await fetch(`${BASE_URL}author`, {
            method: 'POST',
            headers: { 'content-type': 'application/json' },
            body: JSON.stringify(author),
        });
        return handleResponse(response);
    }
};