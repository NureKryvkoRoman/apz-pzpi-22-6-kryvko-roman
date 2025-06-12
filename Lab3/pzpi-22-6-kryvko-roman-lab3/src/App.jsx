import { AuthProvider, useAuth } from './context/AuthContext.jsx'
import Login from './pages/Login.jsx'
import Register from './pages/Register.jsx'
// import DashboardLayout from './pages/DashboardLayout.jsx'
// import GreenhouseOverview from './pages/GreenhouseOverview.jsx'
// import GreenhouseList from './pages/GreenhouseList.jsx'
// import GreenhouseStatistics from './pages/GreenhouseStatistics.jsx'
// import GreenhouseSensors from './pages/GreenhouseSensors.jsx'
// import Notifications from './pages/Notifications.jsx'
// import UserProfile from './pages/UserProfile.jsx'
import NotFound from './pages/NotFound.jsx'
import './App.css'
import { BrowserRouter, Route, Routes } from 'react-router'

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="login" element={<Login />} />
          <Route path="register" element={<Register />} />

          {/* <Route path="/dashboard" element={<DashboardLayout />}> */}
          {/*   <Route path="greenhouses"> */}
          {/*     <Route index element={<GreenhouseList />} /> */}
          {/*     <Route path=":greenhouseId"> */}
          {/*       <Route path="overview" element={<GreenhouseOverview />} /> */}
          {/*       <Route path="sensors" element={<GreenhouseSensors />} /> */}
          {/*       <Route path="automation" element={<GreenhouseAutomation />} /> */}
          {/*       <Route path="statistics" element={<GreenhouseStatistics />} /> */}
          {/*     </Route> */}
          {/*   </Route> */}
          {/**/}
          {/*   <Route path="notifications" element={<Notifications />} /> */}
          {/*   <Route path="profile" element={<UserProfile />} /> */}
          {/* </Route> */}

          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider >
  )
}

export default App
