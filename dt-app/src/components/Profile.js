import React, { useState, useEffect } from "react";
import { authApis, endpoints } from "../configs/Apis";
import { MyUserContext } from "../configs/MyContexts";
import { useContext } from "react";

const Profile = () => {
  const [profile, setProfile] = useState({
    username: "",
    name: "",
    email: "",
    phone: "",
    avatar: null,
  });
  const [avatarPreview, setAvatarPreview] = useState("");
  const [isLoading, setIsLoading] = useState(true);

   const user = useContext(MyUserContext);
  console.log("User context:", user);
  // Load profile info từ API
  const loadProfile = async () => {
    try {
      const res = await authApis().get(endpoints.profile); // giả sử có endpoint profile
      setProfile({
        role: res.data.role || "",
        name: res.data.name || "",
        email: res.data.email || "",
        avatar: null,
      });
      setAvatarPreview(res.data.avatar || "");
      setIsLoading(false);
      console.log("Profile loaded:", res.data);
    } catch (error) {
      console.error("Lỗi khi tải profile:", error);
      setIsLoading(false);
    }
  };

  useEffect(() => {
    loadProfile();
  }, []);

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    if (name === "avatar") {
      const file = files[0];
      setProfile({ ...profile, avatar: file });
      setAvatarPreview(URL.createObjectURL(file));
    } else {
      setProfile({ ...profile, [name]: value });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("name", profile.name);
    formData.append("email", profile.email);
    if (profile.avatar) {
      formData.append("avatar", profile.avatar);
    }

    try {
      await authApis().put(endpoints.profile, formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      alert("Cập nhật thông tin thành công!");
    } catch (error) {
      console.error("Lỗi khi cập nhật profile:", error);
      alert("Cập nhật thông tin thất bại.");
    }
  };

  if (isLoading) return <p>Đang tải thông tin...</p>;

  return (
    <div style={styles.container}>
      <h2>Thông tin cá nhân</h2>
      <form onSubmit={handleSubmit} style={styles.form}>
        <label>
          ROLE:
          <input
            type="text"
            name="username"
            value={profile.role}
            onChange={handleChange}
            disabled
            style={styles.input}
          />
        </label>

        <label>
          Họ và tên:
          <input
            type="text"
            name="fullName"
            value={profile.name}
            onChange={handleChange}
            style={styles.input}
          />
        </label>

        <label>
          Email:
          <input
            type="email"
            name="email"
            value={profile.email}
            onChange={handleChange}
            style={styles.input}
          />
        </label>



        <label>
          Ảnh đại diện:
          <input
            type="file"
            name="avatar"
            accept="image/*"
            onChange={handleChange}
            style={styles.input}
          />
        </label>
        {avatarPreview && (
          <img src={avatarPreview} alt="Avatar Preview" style={styles.avatar} />
        )}

        <button type="submit" style={styles.button}>
          Cập nhật thông tin
        </button>
      </form>
    </div>
  );
};

const styles = {
  container: {
    maxWidth: "500px",
    margin: "20px auto",
    padding: "20px",
    backgroundColor: "#fff",
    borderRadius: "8px",
    boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
  },
  form: {
    display: "flex",
    flexDirection: "column",
    gap: "15px",
  },
  input: {
    width: "100%",
    padding: "8px",
    marginTop: "5px",
    borderRadius: "4px",
    border: "1px solid #ccc",
  },
  avatar: {
    marginTop: "10px",
    width: "120px",
    height: "120px",
    objectFit: "cover",
    borderRadius: "50%",
    border: "1px solid #ddd",
  },
  button: {
    padding: "10px",
    backgroundColor: "#1976d2",
    color: "white",
    border: "none",
    borderRadius: "5px",
    cursor: "pointer",
    fontWeight: "bold",
  },
};

export default Profile;
