import React, { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import {
  FaTools,
  FaCalendarCheck,
  FaExclamationTriangle,
  FaHistory,
  FaComments,
} from "react-icons/fa";

const Sidebar = ({ sidebarOpen }) => {
  const [currentTitle, setCurrentTitle] = useState("Quản Lý Thiết Bị");
  const location = useLocation(); // lấy đường dẫn hiện tại

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
  });

  const iconStyle = {
    marginRight: "10px",
  };

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

      <Link
        to="/devices"
        style={navLinkStyle("/devices")}
        onClick={() => setCurrentTitle("Quản Lý Thiết Bị")}
        onMouseEnter={(e) =>
          (e.currentTarget.style.backgroundColor = "#f1f1f1")
        }
        onMouseLeave={(e) => {
          if (location.pathname !== "/devices")
            e.currentTarget.style.backgroundColor = "transparent";
        }}
      >
        <FaTools style={iconStyle} />
        Quản Lý Thiết Bị
      </Link>

      <Link
        to="/maintenance"
        style={navLinkStyle("/maintenance")}
        onClick={() => setCurrentTitle("Lịch Bảo Trì")}
        onMouseEnter={(e) =>
          (e.currentTarget.style.backgroundColor = "#f1f1f1")
        }
        onMouseLeave={(e) => {
          if (location.pathname !== "/maintenance")
            e.currentTarget.style.backgroundColor = "transparent";
        }}
      >
        <FaCalendarCheck style={iconStyle} />
        Lịch Bảo Trì
      </Link>

      <Link
        to="/incidents"
        style={navLinkStyle("/incidents")}
        onClick={() => setCurrentTitle("Quản Lý Sự Cố")}
        onMouseEnter={(e) =>
          (e.currentTarget.style.backgroundColor = "#f1f1f1")
        }
        onMouseLeave={(e) => {
          if (location.pathname !== "/incidents")
            e.currentTarget.style.backgroundColor = "transparent";
        }}
      >
        <FaExclamationTriangle style={iconStyle} />
        Quản Lý Sự Cố
      </Link>

      <Link
        to="/repair-history"
        style={navLinkStyle("/repair-history")}
        onClick={() => setCurrentTitle("Lịch Sử Sửa Chữa")}
        onMouseEnter={(e) =>
          (e.currentTarget.style.backgroundColor = "#f1f1f1")
        }
        onMouseLeave={(e) => {
          if (location.pathname !== "/repair-history")
            e.currentTarget.style.backgroundColor = "transparent";
        }}
      >
        <FaHistory style={iconStyle} />
        Lịch Sử Sửa Chữa
      </Link>

      <Link
        to="/forum"
        style={navLinkStyle("/forum")}
        onClick={() => setCurrentTitle("Forum")}
        onMouseEnter={(e) =>
          (e.currentTarget.style.backgroundColor = "#f1f1f1")
        }
        onMouseLeave={(e) => {
          if (location.pathname !== "/forum")
            e.currentTarget.style.backgroundColor = "transparent";
        }}
      >
        <FaComments style={iconStyle} />
        Forum 
      </Link>
    </div>
  );
};

export default Sidebar;
