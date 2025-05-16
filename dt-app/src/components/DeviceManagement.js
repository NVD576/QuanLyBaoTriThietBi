import React, { useState, useEffect } from "react";
import  { authApis, endpoints } from "../configs/Apis";
import cookie from "react-cookies";
import axios from "axios";
import { useSearchParams } from "react-router-dom";
const DeviceManagement = () => {
  const [devices, setDevices] = useState([]);
  const [categories, setCategories] = useState([]);
  const [statuses, setStatuses] = useState([]);
  const [bases, setBases] = useState([]);
  const [q] = useSearchParams();
  const [newDevice, setNewDevice] = useState({
    name: "",
    manufacturer: "",
    date: "",
    image: null,
    baseId: "",
    categoryId: "",
    statusId: "",
  });
  const [editingDevice, setEditingDevice] = useState(null);
  const [imagePreview, setImagePreview] = useState("");
  const [isAuthenticated, setIsAuthenticated] = useState(
    !!cookie.load("token")
  );


   const fetchDevices = async () => {
    try {
      let url = `${endpoints.devices}`;
      let cateId = q.get("cateId");
      if (cateId) {
        url = `${url}?cateId=${cateId}`;
      }
      let res = await authApis().get(url);

      setDevices(res.data);
    } catch (err) {
      alert("Lỗi khi tải thiết bị");
      console.error(err);
    }
  };

  useEffect(() => {
    if (isAuthenticated) {
      fetchDevices();
      fetchCategories();
      fetchStatuses();
      fetchBases();
    }
  }, [isAuthenticated,q]);

 

  const fetchCategories = async () => {
    try {
      const res = await authApis().get(endpoints.categories);
      setCategories(res.data);
    } catch (err) {
      console.error("Lỗi khi tải danh mục:", err);
    }
  };
  const fetchStatuses = async () => {
    try {
      const res = await authApis().get(endpoints.statuses);
      setStatuses(res.data);
    } catch (err) {
      console.error("Lỗi khi tải trạng thái:", err);
    }
  };
  const fetchBases = async () => {
    try {
      const res = await authApis().get(endpoints.bases);
      setBases(res.data);
      console.log(res.data);
    } catch (err) {
      console.error("Lỗi khi tải cơ sở:", err);
    }
  };

  const handleChange = (e) => {
    const { name, value, files } = e.target;
    if (name === "image") {
      const file = files[0];
      setNewDevice({ ...newDevice, image: file });
      setImagePreview(URL.createObjectURL(file));
    } else {
      setNewDevice({ ...newDevice, [name]: value });
    }
  };

  const resetForm = () => {
    setNewDevice({
      name: "",
      manufacturer: "",
      date: "",
      image: null,
      baseId: "",
      categoryId: "",
      statusId: "",
    });
    setImagePreview("");
    setEditingDevice(null);
  };

  const handleSubmit = async () => {
    if (!newDevice.name) {
      alert("Vui lòng nhập tên thiết bị!");
      return;
    }

    const formData = new FormData();
    formData.append("name", newDevice.name);
    formData.append("manufacturer", newDevice.manufacturer);
    formData.append("date", newDevice.date);
    if (newDevice.image) formData.append("image", newDevice.image);
    formData.append("baseId", newDevice.baseId);
    formData.append("categoryId", newDevice.categoryId);
    formData.append("statusId", newDevice.statusId);

    try {
      if (editingDevice) {
        await authApis().put(
          `${endpoints.devices}/${editingDevice.id}`,
          formData
        );
      } else {
        await authApis().post(endpoints.devices, formData);
      }
      fetchDevices();
      resetForm();
    } catch (err) {
      console.error(err);
      alert("Lỗi khi lưu thiết bị");
    }
  };

  const startEditing = (device) => {
    setEditingDevice(device);
    // Chuyển đổi timestamp sang yyyy-MM-dd
    const formattedDate = device.date
      ? new Date(Number(device.date)).toISOString().split("T")[0]
      : "";

    setNewDevice({
      name: device.name,
      manufacturer: device.manufacturer,
      date: formattedDate,
      image: null,
      baseId: device.baseId?.id || "",
      categoryId: device.categoryId?.id || "",
      statusId: device.statusId?.id || "",
    });
    setImagePreview(device.image || "");
  };

  const deleteDevice = async (id) => {
    if (window.confirm("Bạn có chắc muốn xóa không?")) {
      try {
        await authApis().delete(`${endpoints.devices}/${id}`);
        fetchDevices();
      } catch (err) {
        console.error(err);
        alert("Lỗi khi xóa thiết bị");
      }
    }
  };

  if (!isAuthenticated)
    return <Login setIsAuthenticated={setIsAuthenticated} />;

  return (
    <div style={styles.container}>
      <h2>Quản Lý Thiết Bị</h2>
      <div>
        <input
          type="text"
          name="name"
          placeholder="Tên thiết bị"
          value={newDevice.name}
          onChange={handleChange}
          style={styles.input}
        />
        <input
          type="text"
          name="manufacturer"
          placeholder="Nhà sản xuất"
          value={newDevice.manufacturer}
          onChange={handleChange}
          style={styles.input}
        />
        <input
          type="date"
          name="date"
          value={newDevice.date}
          onChange={handleChange}
          style={styles.input}
        />
        <input
          type="file"
          name="image"
          onChange={handleChange}
          style={styles.input}
        />
        {imagePreview && (
          <img src={imagePreview} alt="Preview" style={styles.image} />
        )}

        <select
          name="baseId"
          value={newDevice.baseId}
          onChange={handleChange}
          style={styles.input}
        >
          <option value="">-- Chọn cơ sở --</option>
          {bases.map((c) => (
            <option key={c.id} value={c.id}>
              {c.name}
            </option>
          ))}
        </select>
        <select
          name="categoryId"
          value={newDevice.categoryId}
          onChange={handleChange}
          style={styles.input}
        >
          <option value="">-- Chọn danh mục --</option>
          {categories.map((c) => (
            <option key={c.id} value={c.id}>
              {c.name}
            </option>
          ))}
        </select>

        <select
          name="statusId"
          value={newDevice.statusId}
          onChange={handleChange}
          style={styles.input}
        >
          <option value="">-- Chọn trạng thái --</option>
          {statuses.map((c) => (
            <option key={c.id} value={c.id}>
              {c.name}
            </option>
          ))}
        </select>

        <div style={{ display: "flex", gap: "10px" }}>
          <button onClick={handleSubmit} style={{ ...styles.button, flex: 1 }}>
            {editingDevice ? "Cập nhật" : "Thêm"} thiết bị
          </button>
          {editingDevice && (
            <button
              onClick={resetForm}
              style={{ ...styles.button, backgroundColor: "#9e9e9e", flex: 1 }}
            >
              Xóa trắng
            </button>
          )}
        </div>
      </div>

      <hr />
      <h3>Danh sách thiết bị</h3>
      <table style={styles.table}>
        <thead>
          <tr>
            <th style={styles.cell}>Tên</th>
            <th style={styles.cell}>NSX</th>
            <th style={styles.cell}>Ngày mua</th>
            <th style={styles.cell}>Hình ảnh</th>
            <th style={styles.cell}>Cơ sở</th>
            <th style={styles.cell}>Danh mục</th>
            <th style={styles.cell}>Trạng thái</th>
            <th style={styles.cell}>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {devices.map((d) => (
            <tr key={d.id}>
              <td style={styles.cell}>{d.name}</td>
              <td style={styles.cell}>{d.manufacturer}</td>
              <td style={styles.cell}>
                {d.date
                  ? new Date(Number(d.date)).toLocaleDateString("vi-VN")
                  : ""}
              </td>
              <td style={styles.cell}>
                {d.image ? (
                  <img src={d.image} alt="img" style={styles.image} />
                ) : (
                  "Không có"
                )}
              </td>
              <td style={styles.cell}>{d.baseId?.name || ""}</td>
              <td style={styles.cell}>{d.categoryId?.name || ""}</td>
              <td style={styles.cell}>{d.statusId?.name || ""}</td>
              <td style={styles.cell}>
                <button
                  onClick={() => startEditing(d)}
                  style={{ ...styles.button, backgroundColor: "#4caf50" }}
                >
                  Sửa
                </button>
                <button
                  onClick={() => deleteDevice(d.id)}
                  style={{ ...styles.button, backgroundColor: "#f44336" }}
                >
                  Xóa
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

const Login = ({ setIsAuthenticated }) => {
  const [credentials, setCredentials] = useState({
    username: "",
    password: "",
  });

  const handleChange = (e) => {
    setCredentials({ ...credentials, [e.target.name]: e.target.value });
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post(endpoints.login, credentials);
      cookie.save("token", res.data, { path: "/" });
      setIsAuthenticated(true);
    } catch (err) {
      alert("Sai tài khoản hoặc mật khẩu");
    }
  };

  return (
    <div style={styles.container}>
      <h2>Đăng nhập</h2>
      <form onSubmit={handleLogin}>
        <input
          type="text"
          name="username"
          placeholder="Tên đăng nhập"
          value={credentials.username}
          onChange={handleChange}
          style={styles.input}
        />
        <input
          type="password"
          name="password"
          placeholder="Mật khẩu"
          value={credentials.password}
          onChange={handleChange}
          style={styles.input}
        />
        <button type="submit" style={styles.button}>
          Đăng nhập
        </button>
      </form>
    </div>
  );
};

const styles = {
  container: {
    maxWidth: "100%",
    padding: "20px",
    margin: "20px auto",
    backgroundColor: "#fff",
    borderRadius: "8px",
    boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
  },
  input: {
    width: "100%",
    marginBottom: "10px",
    padding: "8px",
    borderRadius: "4px",
    border: "1px solid #ccc",
  },
  button: {
    width: "100%",
    padding: "10px",
    backgroundColor: "#1976d2",
    color: "#fff",
    border: "none",
    borderRadius: "5px",
    marginBottom: "10px",
    fontWeight: "bold",
    cursor: "pointer",
  },
  table: {
    width: "100%",
    borderCollapse: "collapse",
    marginTop: "20px",
  },
  cell: {
    border: "1px solid #ddd",
    padding: "8px",
    textAlign: "left",
  },
  image: {
    maxWidth: "100px",
    maxHeight: "100px",
    objectFit: "cover",
  },
};

export default DeviceManagement;
