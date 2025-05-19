// AppContext.js
import React, { createContext, useState, useContext } from "react";

const AppContext = createContext();

export const AppProvider = ({ children }) => {
  const [language, setLanguage] = useState("vi");
  const [darkMode, setDarkMode] = useState(false);

  const toggleLanguage = () => setLanguage((prev) => (prev === "vi" ? "en" : "vi"));
  const toggleDarkMode = () => setDarkMode((prev) => !prev);

  return (
    <AppContext.Provider value={{ language, toggleLanguage, darkMode, toggleDarkMode }}>
      {children}
    </AppContext.Provider>
  );
};

export const useAppContext = () => useContext(AppContext);
