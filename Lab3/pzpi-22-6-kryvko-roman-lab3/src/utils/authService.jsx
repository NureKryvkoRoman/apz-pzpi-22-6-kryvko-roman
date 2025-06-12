export const loginUser = async (credentials, setAuth) => {
    const res = await fetch('https://your-api.com/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credentials),
    });

    const data = await res.json();

    if (res.ok) {
        const { id, email, username, accessToken, refreshToken } = data;

        // Store non-sensitive info and refreshToken
        localStorage.setItem(
            'user',
            JSON.stringify({ id, email, username, refreshToken })
        );

        // Keep access token in memory
        setAuth({ id, email, username, accessToken });
    } else {
        throw new Error(data.message || 'Login failed');
    }
};

export const logoutUser = (setAuth) => {
    localStorage.removeItem('user');
    setAuth({ id: null, email: null, username: null, accessToken: null });
};
