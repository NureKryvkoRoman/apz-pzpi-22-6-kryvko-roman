import React, { useState } from 'react';
import { useNavigate } from 'react-router';

const Register = () => {
  const [formData, setFormData] = useState({ email: '', login: '', password: '' });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = e =>
    setFormData({ ...formData, [e.target.name]: e.target.value });

  const handleSubmit = async e => {
    e.preventDefault();
    setError('');

    try {
      const res = await fetch('http://localhost:8080/api/auth/signup', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      });

      const data = await res.json();

      if (!res.ok) throw new Error(data.message || 'Registration failed');

      navigate('/auth/login');
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="min-h-screen flex justify-center items-center bg-gray-100 p-4">
      <form
        onSubmit={handleSubmit}
        className="bg-white p-6 rounded shadow-md w-full max-w-sm"
      >
        <h2 className="text-2xl font-bold mb-4">Register</h2>
        {error && <p className="text-red-500 mb-2">{error}</p>}
        <input
          type="email"
          name="email"
          placeholder="Email"
          required
          className="input mb-2"
          value={formData.email}
          onChange={handleChange}
        />
        <input
          type="text"
          name="login"
          placeholder="Username"
          required
          className="input mb-2"
          value={formData.login}
          onChange={handleChange}
        />
        <input
          type="password"
          name="password"
          placeholder="Password"
          required
          className="input mb-4"
          value={formData.password}
          onChange={handleChange}
        />
        <button type="submit" className="btn-primary w-full">
          Register
        </button>
      </form>
    </div>
  );
};

export default Register;
