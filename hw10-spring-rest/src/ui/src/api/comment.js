const BASE_URL = '/api/v1/'

const handleResponse = async (response) => {
    if (!response.ok) throw new Error(await response.text());
    return response.json();
};

export default {
    async getCommentsByBookId(id) {
        const response = await fetch(`${BASE_URL}comment?book_id=${id}`)
        return handleResponse(response);
    },

    async getCommentById(id) {
        const response = await fetch(`${BASE_URL}comment/${id}`,)
        return handleResponse(response);
    },

    async updateComment(comment) {
        const response = await fetch(`${BASE_URL}comment/${comment.id}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(comment),
        });
        return handleResponse(response);
    },

    async createComment(comment) {
        const response = await fetch(`${BASE_URL}comment`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(comment),
        });
        return handleResponse(response);
    }
}