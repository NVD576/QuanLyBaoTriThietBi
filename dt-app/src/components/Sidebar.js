import React, { useEffect, useState } from "react";
import { Link, useLocation } from "react-router-dom";
import { useTranslation } from "react-i18next";
import {
  FaTools,
  FaCalendarCheck,
  FaExclamationTriangle,
  FaHistory,
  FaComments,
  FaUser,
  FaCog,
} from "react-icons/fa";

const Sidebar = ({ sidebarOpen }) => {
  const { t, i18n } = useTranslation();
  const location = useLocation();
  const [currentTitle, setCurrentTitle] = useState(t("DeviceManagement"));

  const sidebarStyle = {
    position: "fixed",
    top: 0,
    left: sidebarOpen ? 0 : "-200px",
    width: "200px",
    height: "100vh",
    backgroundColor: "#ffffff",
    borderRight: "1px solid #ddd",
    paddingTop: "20px",
    boxSizing: "border-box",
    transition: "left 0.3s ease",
    overflowY: "auto",
    zIndex: 1000,
    boxShadow: "2px 0 5px rgba(0,0,0,0.1)",
  };

  const navLinkStyle = (path) => ({
    display: "flex",
    alignItems: "center",
    padding: "10px 15px",
    textDecoration: "none",
    color: location.pathname === path ? "#1976d2" : "#333",
    backgroundColor: location.pathname === path ? "#e3f2fd" : "transparent",
    fontWeight: "bold",
    borderRadius: "4px",
    marginBottom: "8px",
    cursor: "pointer",
    transition: "all 0.3s ease",
    boxShadow: location.pathname === path ? "0 2px 4px rgba(0,0,0,0.1)" : "none",
  });

  const iconStyle = {
    marginRight: "10px",
  };

  const handleHover = (e) => {
    e.currentTarget.style.backgroundColor = "#f1f1f1";
    e.currentTarget.style.transform = "translateY(-2px)";
    e.currentTarget.style.boxShadow = "0 4px 8px rgba(0,0,0,0.1)";
  };

  const handleLeave = (e, path) => {
    if (location.pathname !== path) {
      e.currentTarget.style.backgroundColor = "transparent";
    }
    e.currentTarget.style.transform = "translateY(0)";
    e.currentTarget.style.boxShadow = "none";
  };

  const links = [
    { to: "/devices", icon: <FaTools />, title: t("DeviceManagement"), key: "DeviceManagement" },
    { to: "/maintenance", icon: <FaCalendarCheck />, title: t("MaintenanceSchedule"), key: "MaintenanceSchedule" },
    { to: "/incidents", icon: <FaExclamationTriangle />, title: t("IncidentManagement"), key: "IncidentManagement" },
    { to: "/repair-history", icon: <FaHistory />, title: t("RepairHistory"), key: "RepairHistory" },
    { to: "/forum", icon: <FaComments />, title: t("Forum"), key: "Forum" },
    { to: "/profile", icon: <FaUser />, title: t("Profile"), key: "Profile" },
    { to: "/settings", icon: <FaCog />, title: t("Settings"), key: "Settings" },
  ];

  // Update currentTitle when language or pathname changes
  useEffect(() => {
    const currentLink = links.find((link) => location.pathname === link.to) || links[0];
    setCurrentTitle(t(currentLink.key));
  }, [location.pathname, i18n.language, t]);

  return (
    <div style={sidebarStyle}>
      <h2
        style={{
          padding: "10px 15px",
          color: "#1976d2",
          textAlign: "center",
          height: "50px",
          fontSize: "16px",
          lineHeight: "20px",
          overflow: "hidden",
          wordBreak: "break-word",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        {currentTitle}
      </h2>

      {links.map((link) => (
        <Link
          key={link.to}
          to={link.to}
          style={navLinkStyle(link.to)}
          onClick={() => setCurrentTitle(t(link.key))}
          onMouseEnter={handleHover}
          onMouseLeave={(e) => handleLeave(e, link.to)}
        >
          <span style={iconStyle}>{link.icon}</span>
          {link.title}
        </Link>
      ))}
    </div>
  );
};

export default Sidebar;