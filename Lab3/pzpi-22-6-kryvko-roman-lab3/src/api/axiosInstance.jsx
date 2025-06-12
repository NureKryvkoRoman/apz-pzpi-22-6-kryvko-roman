import axios from 'axios';
import { useAuth } from '../context/AuthContext';

const baseURL = 'http://localhost:8080';

let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
  failedQueue.forEach(prom => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });
  failedQueue = [];
};

export const createAxiosInstance = (auth, setAuth) => {
  const instance = axios.create({ baseURL });

  instance.interceptors.request.use(
    config => {
      if (auth?.accessToken) {
        config.headers['Authorization'] = `Bearer ${auth.accessToken}`;
      }
      return config;
    },
    error => Promise.reject(error)
  );

  instance.interceptors.response.use(
    res => res,
    async error => {
      const originalRequest = error.config;
      if (
        error.response?.status === 401 &&
        !originalRequest._retry &&
        localStorage.getItem('user')
      ) {
        originalRequest._retry = true;

        if (isRefreshing) {
          return new Promise((resolve, reject) => {
            failedQueue.push({ resolve, reject });
          })
            .then(token => {
              originalRequest.headers['Authorization'] = 'Bearer ' + token;
              return axios(originalRequest);
            })
            .catch(err => Promise.reject(err));
        }

        isRefreshing = true;

        try {
          const stored = JSON.parse(localStorage.getItem('user'));
          const response = await axios.post(`${baseURL}/refresh`, {
            refreshToken: stored.refreshToken,
          });

          const newAccessToken = response.data.accessToken;

          setAuth(prev => ({ ...prev, accessToken: newAccessToken }));

          isRefreshing = false;
          processQueue(null, newAccessToken);

          originalRequest.headers['Authorization'] = 'Bearer ' + newAccessToken;
          return axios(originalRequest);
        } catch (err) {
          isRefreshing = false;
          processQueue(err, null);
          return Promise.reject(err);
        }
      }

      return Promise.reject(error);
    }
  );

  return instance;
};
