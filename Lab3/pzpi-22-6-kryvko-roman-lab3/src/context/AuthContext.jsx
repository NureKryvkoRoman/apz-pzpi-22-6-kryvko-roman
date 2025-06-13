import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState({
    id: null,
    email: null,
    username: null,
    accessToken: null, // stored in memory
  });

  useEffect(() => {
    // Rehydrate from localStorage on first load
    const stored = JSON.parse(localStorage.getItem('user'));
    if (stored?.refreshToken) {
      setAuth(prev => ({
        ...prev,
        ...stored,
        accessToken: null, // will be refreshed
      }));
    }
  }, []);

  return (
    <AuthContext.Provider value={{ auth, setAuth }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
