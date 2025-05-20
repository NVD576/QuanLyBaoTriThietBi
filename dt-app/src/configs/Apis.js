import axios from "axios";
import cookie from "react-cookies";

const BASE_URL = "http://localhost:8080/QLTB/api/";

export const endpoints = {
  "register": "/accounts",
  "login": "/login",
  "profile": "/secure/profile",
  "profile-update": "/account/edit",
  "categories": "/categories",
  "devices": "/devices",
  "device": "/device",
  "device-add": "/device/add",
  "statuses": "/statuses",  
  "bases": "/base",
  "maintenance": "/maintenance",
  "maintenances": "/maintenances",
  "maintenance-add": "/maintenances/add",
  "maintenance-confirm": (id) => `/maintenance/${id}/confirm`,
  "types": "/maintenanceTypes",
  "issues": "/issues",
  "issue": "/issue",
  "issue-add": "/issue/add",
  "issue-confirm": (id) => `/issue/${id}/confirm`,
  "repairs": "/repairs",
  "repair": "/repair",
  "repairTypes": "/repairTypes",
  "frequencies": "/frequencies",
  "levels": "/levels",
};

export const authApis = () => {
  const token = cookie.load("token");
  return axios.create({
    baseURL: BASE_URL,
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });
};

export default axios.create({
  baseURL: BASE_URL,
});
