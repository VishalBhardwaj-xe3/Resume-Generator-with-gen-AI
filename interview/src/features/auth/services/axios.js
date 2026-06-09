import axios from "axios";

const api = axios.create({
  baseURL: "https://fulfilling-delight-production-05c0.up.railway.app/",
});

// automatically add token to every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
