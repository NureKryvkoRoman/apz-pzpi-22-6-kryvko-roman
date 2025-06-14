import { AuthProvider } from './context/AuthContext.jsx'
import Login from './pages/Login.jsx'
import Register from './pages/Register.jsx'
import Dashboard from './pages/Dashboard.jsx'
import GreenhouseOverview from './pages/GreenhouseOverview.jsx'
import GreenhouseList from './pages/GreenhouseList.jsx'
import UserProfile from './pages/UserProfile.jsx'
import NotFound from './pages/NotFound.jsx'
import './App.css'
import { BrowserRouter, Route, Routes } from 'react-router'
import { ToastContainer } from 'react-toastify'
import Header from './components/Header.jsx'

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Header />
        <Routes>
          <Route path="login" element={<Login />} />
          <Route path="register" element={<Register />} />

          <Route path="/" element={<GreenhouseList />} />
          <Route path="greenhouses/:greenhouseId" element={<GreenhouseOverview />} />
          <Route path="/dashboard/:greenhouseId" element={<Dashboard />} />
          <Route path="profile" element={<UserProfile />} />

          <Route path="*" element={<NotFound />} />
        </Routes>
        <ToastContainer />
      </BrowserRouter>
    </AuthProvider >
  )
}

export default App
