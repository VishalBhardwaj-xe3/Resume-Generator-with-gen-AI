import React from 'react'
import { useNavigate, Link } from 'react-router';
import { register } from '../services/auth.api';

const Register = () => {

    const navigate = useNavigate()

     const handleSubmit = async (e) => {
         e.preventDefault();
         
          try {
        const data = await register(username, email, password);
        console.log("Registeres", data);
        
    } catch (error) {
        console.log("Error", error.response.data.message);
        
    }
    };
    
   

  return (
    <main>
      <div className="form-container">
        <h1>Register</h1>

        <form onSubmit={handleSubmit}>
          <div className="input-group">
            <label htmlFor="username">Username</label>
            <input
              type="text"
              id="username"
              name="username"
              placeholder="Enter username"
            />
          </div>
          <div className="input-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              placeholder="Enter email address"
            />
          </div>
          <div className="input-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              name="password"
              placeholder="Enter password"
            />
          </div>
          <button className="button primary-button">Register</button>
              </form>
              
              <p>Already have an account? <Link to={"/login"}>Login</Link></p>
      </div>
    </main>
  );
}

export default Register