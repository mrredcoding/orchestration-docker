const API_BASE_URL = "http://localhost:5000";

export async function fetchWithAuth(url, options = {}) {
    const token = localStorage.getItem("authToken");

    const headers = {
        ...options.headers,
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
    };

    const response = await fetch(`${API_BASE_URL}${url}`, {
        ...options,
        headers,
    });

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "An error occurred");
    }

    return response.json();
}
export async function customFetch(url, options = {}) {

    const headers = {
        ...options.headers,
        "Content-Type": "application/json",
    };

    const response = await fetch(`${API_BASE_URL}${url}`, {
        ...options,
        headers,
    });

    if (!response.ok) {
        const error = await response.json();
        console.log(error);
        throw new Error(error.message);

    }

    return response.json();
}
