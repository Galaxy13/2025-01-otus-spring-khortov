const BASE_URL = '/api/v1/'

const handleResponse = async (response) => {
    if (!response.ok) throw new Error(await response.text());
    return response.json();
};

export default {
    async getGenres() {
        const genres = await fetch(`${BASE_URL}genre`);
        return handleResponse(genres);
    },

    async getGenreById(id) {
        const genre = await fetch(`${BASE_URL}genre/${id}`);
        return handleResponse(genre);
    },

    async createGenre(genre) {
        const response = await fetch(`${BASE_URL}genre`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(genre),
        });
        return handleResponse(response);
    },

    async updateGenre(id, genre) {
        const response = await fetch(`${BASE_URL}genre/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(genre),
        });
        return handleResponse(response);
    }
}