import React, { useState } from "react";
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
  const { t } = useTranslation();
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
    { to: "/devices", icon: <FaTools />, title: t("DeviceManagement") },
    { to: "/maintenance", icon: <FaCalendarCheck />, title: t("MaintenanceSchedule") },
    { to: "/incidents", icon: <FaExclamationTriangle />, title: t("IncidentManagement") },
    { to: "/repair-history", icon: <FaHistory />, title: t("RepairHistory") },
    { to: "/forum", icon: <FaComments />, title: t("Forum") },
    { to: "/profile", icon: <FaUser />, title: t("Profile") },
    { to: "/settings", icon: <FaCog />, title: t("Settings") },
  ];

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
          onClick={() => setCurrentTitle(link.title)}
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
