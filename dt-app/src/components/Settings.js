import React from "react";
import { useTranslation } from "react-i18next";
import { useAppContext } from "../configs/AppContext"; // vẫn dùng cho darkMode
import "./Settings.css"; // Thêm CSS cho toggle switch

const Settings = () => {
  const { darkMode, toggleDarkMode } = useAppContext();
  const { i18n, t } = useTranslation();

  const toggleLanguage = () => {
    const newLang = i18n.language === "vi" ? "en" : "vi";
    i18n.changeLanguage(newLang);
  };

  const containerStyle = {
    padding: "20px",
    backgroundColor: darkMode ? "#121212" : "#f5f5f5",
    color: darkMode ? "#ffffff" : "#000000",
    minHeight: "100vh",
    transition: "background-color 0.3s, color 0.3s",
  };

  return (
    <div style={containerStyle}>
      <h2>{t("settings")}</h2>

      <div style={{ marginBottom: "20px" }}>
        <label style={{ display: "block", marginBottom: "8px" }}>
          {t("current_language")}: {i18n.language === "vi" ? "Tiếng Việt" : "English"}
        </label>
        <label className="switch">
          <input
            type="checkbox"
            checked={i18n.language === "en"}
            onChange={toggleLanguage}
          />
          <span className="slider round"></span>
        </label>
      </div>

      <div>
        <label style={{ display: "block", marginBottom: "8px" }}>
          {t("display_mode")}: {darkMode ? t("DarkMode") : t("LightMode")}
        </label>
        <label className="switch">
          <input
            type="checkbox"
            checked={darkMode}
            onChange={toggleDarkMode}
          />
          <span className="slider round"></span>
        </label>
      </div>
    </div>
  );
};

export default Settings;
