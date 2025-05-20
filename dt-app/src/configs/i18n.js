// i18n.js
import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import LanguageDetector from "i18next-browser-languagedetector";

const resources = {
  en: {
    translation: {

      //sidebar
      DeviceManagement: "Device Management",
      MaintenanceSchedule: "Maintenance Schedule",
      IncidentManagement: "Incident Management",
      RepairHistory: "Repair History",
      Forum: "Forum",
      Home: "Home",
      Settings: "Settings",
      Profile: "Profile",
      CostAnalytics: "CostAnalytics",

      //header
      category: "Categories",
      search_placeholder: "Search...",
      search: "Search",
      register: "Register",
      login: "Login",
      logout: "Logout",
      greeting: "Hello {{username}}!",
      brand: "OU's Equipment Maintenance",
      //settings
      settings: "Settings",
      current_language: "Current language",
      display_mode: "Display mode",
      switch_to_light: "Switch to Light Mode",
      switch_to_dark: "Switch to Dark Mode",
      Language: "Language",
      DarkMode: "Dark Mode",
      LightMode: "Light Mode",
      SwitchtoVietnamese: "Switch to Vietnamese",
    },
  },
  vi: {
    translation: {

      //sidebar
      DeviceManagement: "Quản Lý Thiết Bị",
      MaintenanceSchedule: "Lịch Bảo Trì",
      IncidentManagement: "Quản Lý Sự Cố",
      RepairHistory: "Lịch Sử Sửa Chữa",
      Forum: "Diễn Đàn",
      Home: "Trang Chủ",
      Settings: "Cài Đặt",
      Profile: "Hồ Sơ",
      CostAnalytics: "Phân Tích Chi Phí", 
      //header

      category: "Danh mục",
      search_placeholder: "Tìm kiếm...",
      search: "Tìm",
      register: "Đăng ký",
      login: "Đăng nhập",
      logout: "Đăng xuất",
      greeting: "Chào {{username}}!",
      brand: "OU's Quản lý bảo trì thiết bị",
      //settings
      settings: "Cài Đặt",
      current_language: "Ngôn ngữ hiện tại",
      display_mode: "Chế độ hiển thị",
      switch_to_light: "Chuyển sang chế độ Sáng",
      switch_to_dark: "Chuyển sang chế độ Tối",
      Language: "Ngôn ngữ",
      DarkMode: "Chế độ Tối",
      LightMode: "Chế độ Sáng",
      SwitchtoVietnamese: "Chuyển sang Tiếng Việt",
    },
  },
};

i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    resources,
    fallbackLng: "vi",
    interpolation: {
      escapeValue: false,
    },
  });

export default i18n;
