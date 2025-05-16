import axios from "axios";
import cookie from "react-cookies";

const BASE_URL = "http://localhost:8080/QLTB/api/";

export const endpoints = {
  categories: "/categories",
  devices: "/devices",
  register: "/accounts",
  login: "/login",
  profile: "/secure/profile",
  statuses: "/statuses",
  bases: "/base",
  maintenances: "/maintenances",
  incidents: "/incidents",
  frequencies: "/frequencies/",
  types: "/maintenanceTypes/",
};

export const authApis = () => {
  console.info(cookie.load("token"));
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
