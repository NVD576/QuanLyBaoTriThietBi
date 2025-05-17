import React, { useEffect, useReducer, useState } from "react";
import { Routes, Route, useNavigate } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import DeviceManagement from "./components/DeviceManagement";
import MaintenanceSchedule from "./components/MaintenanceSchedule";
import IncidentManagement from "./components/IncidentManagement";
import RepairHistory from "./components/RepairHistory";
import Header from "./components/layout/Header";
import Footer from "./components/layout/Footer";
import { Container } from "react-bootstrap";
import "bootstrap-icons/font/bootstrap-icons.css";
import "bootstrap/dist/css/bootstrap.min.css";
import { DeviceProvider, MyDispatchContext, MyUserContext } from "./configs/MyContexts";
import MyUserReducer from "./reducers/MyUserReducer";
import Login from "./components/Login";
import Register from "./components/Register";
import Device from "./components/Device";
import Profile from "./components/Profile";
import Forum from "./components/Forum";

const toggleButtonStyle = {
  position: "fixed",
  top: "70px", // Đặt vị trí nút dưới header
  left: "210px", // Di chuyển nút sang bên trái
  zIndex: 1100,
  backgroundColor: "#1976d2",
  color: "white",
  border: "none",
  padding: "8px 12px",
  borderRadius: "0 4px 4px 0",
  cursor: "pointer",
  transition: "left 0.3s ease",
};

const containerStyle = {
  display: "flex",
  flexDirection: "column",
  minHeight: "100vh",
};

const mainContentStyle = {
  flex: 1,
  padding: "5px",
  marginTop: "10px", // Để tránh che khuất bởi header
};

const headerWrapperStyle = (sidebarOpen) => ({
  position: "fixed",
  top: 0,
  left: sidebarOpen ? "200px" : "0",
  width: sidebarOpen ? "calc(100% - 200px)" : "100%",
  transition: "left 0.3s ease, width 0.3s ease",
  zIndex: 1200,
});

const App = () => {
  const [user, dispatch] = useReducer(MyUserReducer, null);
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const protectedPaths = [
      "/",
      "/devices",
      "/maintenances",
      "/incidents",
      "/repair-history",
      "/profile",
      "/forum",
    ];
    const currentPath = window.location.pathname;
    if (!user && protectedPaths.includes(currentPath)) {
      navigate("/login");
    }
  }, [user, navigate]);
  const toggleSidebar = () => setSidebarOpen(!sidebarOpen);

  return (
    <>
      <MyUserContext.Provider value={user}>
        <MyDispatchContext.Provider value={dispatch}>
          <DeviceProvider>
          <div style={containerStyle}>
            <Header />

            <div>
              {<Sidebar sidebarOpen={sidebarOpen} />}
              <div
                style={{ display: "flex", flexDirection: "column", flex: 1 }}
              >
                {user && (
                  <div style={headerWrapperStyle(sidebarOpen)}>
                    <button
                      onClick={toggleSidebar}
                      style={{
                        ...toggleButtonStyle,
                        left: sidebarOpen ? "200px" : "10px",
                        top: "60px",
                      }}
                      aria-label="Toggle sidebar"
                    >
                      {sidebarOpen ? "‹" : "›"}
                    </button>
                  </div>
                )}

                <main style={mainContentStyle}>
                  <Container>
                    <Routes>
                      <Route path="/login" element={<Login />} />
                      <Route path="/register" element={<Register />} />
                      {user && (
                        <>
                          <Route path="/devices" element={<DeviceManagement />} />
                          <Route path="/device/:id" element={<Device/>} />
                          <Route path="/maintenance" element={<MaintenanceSchedule />} />
                          <Route path="/incidents" element={<IncidentManagement />} />
                          <Route path="/repair-history" element={<RepairHistory />} />
                          <Route path="/profile" element={<Profile />} />
                          <Route path="/forum" element={<Forum />} />
                        </>
                      )}
                    </Routes>
                  </Container>
                </main>

                {/* Footer luôn ở cuối vì là phần dưới của flex column */}
                <Footer />
              </div>
            </div>
          </div>
          </DeviceProvider>

        </MyDispatchContext.Provider>
      </MyUserContext.Provider>
    </>
  );
};

export default App;
