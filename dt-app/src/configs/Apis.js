import axios from "axios";
import cookie from "react-cookies";

const BASE_URL = "http://localhost:8080/QLTB/api/";

export const endpoints = {
  "categories": "/categories",
  "devices": "/devices",
  "device-add": "/device/add",
  "device": "/device",
  "register": "/accounts",
  "login": "/login",
  "profile": "/secure/profile",
  "statuses": "/statuses",  
  "bases": "/base",
  "maintenances": "/maintenances",
  "maintenance-add": "/maintenances/add",
  "issues": "/issues",
  "issue": "/issue",
  "issue-add": "/issue/add",
  "issue-confirm": (id) => `/issue/${id}/confirm`,
  "repairs": "/repairs",
  "repair": "/repair",
  "repairTypes": "/repairTypes",
  "frequencies": "/frequencies",
  "types": "/maintenanceTypes",
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
