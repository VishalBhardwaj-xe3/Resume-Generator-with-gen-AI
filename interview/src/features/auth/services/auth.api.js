import api from "./axios";

export const register = async ({username, email, password}) => {
  try {
    const response = await api.post("/api/auth/register", {
      username,
      email,
      password,
    });
      return response.data;
  } catch (error) {
    console.log(error);
  }
  
};

export const login = async ({email, password}) => {
  try {
    const response = await api.post("/api/auth/login", {
      email,
      password,
    });
      localStorage.setItem("token", response.data.token);
       return response.data;
  } catch (error) {
    console.log(error);
  }
 
};

export const logout = async () => {
  try {
    const response = await api.post("/api/auth/logout");
      localStorage.removeItem("token");
        return response.data;
  } catch (error) {
    console.log(err);
    }
  
};

export const getMe = async () => {
  try {
      const response = await api.get("/api/auth/me");
       return response.data;
  } catch (error) {
    console.log(err);
  }
 
};