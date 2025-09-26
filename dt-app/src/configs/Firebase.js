// src/configs/Firebase.js
import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";
import { getAuth, GoogleAuthProvider, signInWithPopup } from "firebase/auth";

const firebaseConfig = {
  apiKey: "",
  authDomain: "",
  projectId: "",
  // các config khác
};

// Khởi tạo app
const app = initializeApp(firebaseConfig);

// Khởi tạo Firestore (db) **phải ở dưới khởi tạo app**
const db = getFirestore(app);

// Khởi tạo auth và provider
const auth = getAuth(app);
const googleProvider = new GoogleAuthProvider();

// Xuất các biến ra ngoài
export { db, auth, googleProvider, signInWithPopup };
