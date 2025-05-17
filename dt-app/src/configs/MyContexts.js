import { useState } from "react";
import { createContext } from "react";

export const MyUserContext = createContext();
export const MyDispatchContext = createContext();
export const DeviceContext = createContext();
export const DeviceProvider = ({ children }) => {
  const [devices, setDevices] = useState([]);

  return (
    <DeviceContext.Provider value={{ devices, setDevices }}>
      {children}
    </DeviceContext.Provider>
  );
};