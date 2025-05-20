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
import {
  DeviceProvider,
  MyDispatchContext,
  MyUserContext,
} from "./configs/MyContexts";
import MyUserReducer from "./reducers/MyUserReducer";
import Login from "./components/Login";
import Register from "./components/Register";
import Device from "./components/Device";
import Profile from "./components/Profile";
import Forum from "./components/Forum";
import Settings from "./components/Settings";
import { AppProvider } from "./configs/AppContext";
import "./configs/i18n";
import CostAnalyticsAndReportingPage from "./components/CostAnalyticsAndReportingPage";

const toggleButtonStyle = {
  position: "fixed",
  top: "70px",
  left: "210px",
  zIndex: 1100,
  backgroundColor: "#1976d2",
  color: "white",
  border: "none",
  padding: "8px 12px",
  borderRadius: "0 4px 4px 0",
  cursor: "pointer",
  transition: "left 0.3s ease",
};

const containerStyle = (sidebarOpen) => ({
  display: "flex",
  flexDirection: "column",
  minHeight: "100vh",
  transition: "margin-left 0.3s ease",
});

const mainContentStyle = {
  flex: 1,
  padding: "5px",
  marginTop: "70px",
};

const headerWrapperStyle = (sidebarOpen) => ({
  position: "fixed",
  top: 0,
  transition: "left 0.3s ease, width 0.3s ease",
  zIndex: 1200,
});

const App = () => {
  const [user, dispatch] = useReducer(MyUserReducer, null);
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const savedUser = localStorage.getItem("user");
    if (savedUser) {
      const parsedUser = JSON.parse(savedUser);
      dispatch({
        type: "login",
        payload: parsedUser,
      });
      setSidebarOpen(true);
    } else {
      setSidebarOpen(false);
    }
    setLoading(false);
  }, []);

  useEffect(() => {
    if (user) {
      setSidebarOpen(true);
    } else {
      setSidebarOpen(false);
    }
  }, [user]);

  useEffect(() => {
    const protectedPaths = [
      "/devices",
      "/maintenance",
      "/incidents",
      "/repair-history",
      "/cost-analytics",
      "/profile",
      "/forum",
      "/settings",
    ];
    const currentPath = window.location.pathname;

    if (!loading && !user && protectedPaths.includes(currentPath)) {
      navigate("/login");
    }
  }, [user, navigate, loading]);

  const toggleSidebar = () => setSidebarOpen(!sidebarOpen);

  const calculatedMarginLeft = user && sidebarOpen ? "200px" : "0";
  const calculatedHeaderWidth = user && sidebarOpen ? "calc(100% - 200px)" : "100%";
  const calculatedHeaderLeft = user && sidebarOpen ? "200px" : "0";
  const calculatedToggleButtonLeft = user && sidebarOpen ? "200px" : "10px";

  return (
    <>
      <AppProvider>
        <MyUserContext.Provider value={user}>
          <MyDispatchContext.Provider value={dispatch}>
            <DeviceProvider>
              <div style={{...containerStyle(sidebarOpen), marginLeft: calculatedMarginLeft }}>
                {user && <Sidebar sidebarOpen={sidebarOpen} />}

                <div
                  style={{
                    display: "flex",
                    flexDirection: "column",
                    flex: 1,
                  }}
                >
                  <div style={{...headerWrapperStyle(sidebarOpen), width: calculatedHeaderWidth, left: calculatedHeaderLeft }}>
                    <Header />
                    {user && (
                      <button
                        onClick={toggleSidebar}
                        style={{
                          ...toggleButtonStyle,
                          left: calculatedToggleButtonLeft,
                          top: "60px",
                        }}
                        aria-label="Toggle sidebar"
                      >
                        {sidebarOpen ? "‹" : "›"}
                      </button>
                    )}
                  </div>

                  <main style={mainContentStyle}>
                    <Container>
                      <Routes>
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />
                        {user && (
                          <>
                            <Route
                              path="/devices"
                              element={<DeviceManagement />}
                            />
                            <Route path="/device/:id" element={<Device />} />
                            <Route
                              path="/maintenance"
                              element={<MaintenanceSchedule />}
                            />
                            <Route
                              path="/incidents"
                              element={<IncidentManagement />}
                            />
                            <Route
                              path="/repair-history"
                              element={<RepairHistory />}
                            />
                            <Route path="/cost-analytics" element={<CostAnalyticsAndReportingPage />} />
                            <Route path="/profile" element={<Profile />} />
                            <Route path="/forum" element={<Forum />} />
                            <Route path="/settings" element={<Settings />} />
                            <Route path="/" element={<DeviceManagement />} />
                          </>
                        )}
                        {!user && <Route path="/" element={<Login />} />}
                      </Routes>
                    </Container>
                  </main>

                  <Footer />
                </div>
              </div>
            </DeviceProvider>
          </MyDispatchContext.Provider>
        </MyUserContext.Provider>
      </AppProvider>
    </>
  );
};

export default App;
