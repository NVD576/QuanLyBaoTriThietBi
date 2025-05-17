import React, { useState, useEffect, useContext } from "react";
import { authApis, endpoints } from "../configs/Apis";
import cookie from "react-cookies";
import axios from "axios";
import { useNavigate, useSearchParams } from "react-router-dom";
import styles from "./DeviceManagement.module.css"; // Import CSS Module
import { Button } from "react-bootstrap";
import { MyUserContext,DeviceContext  } from "../configs/MyContexts";

const DeviceManagement = () => {
  // const [devices, setDevices] = useState([]);
  const { devices, setDevices } = useContext(DeviceContext);
  const [categories, setCategories] = useState([]);
  const [statuses, setStatuses] = useState([]);
  const [bases, setBases] = useState([]);
  const [q] = useSearchParams();
  const [newDevice, setNewDevice] = useState({
    id: "",
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
  const [showForm, setShowForm] = useState(false);
  const nav = useNavigate();
  const [page, setPage] = useState(1);
  const user = useContext(MyUserContext);

  const loadDevices = async () => {
  try {
    let url = `${endpoints.devices}?page=${page}`;
    let kw = q.get("kw");
    let cateId = q.get("cateId");
    let baseId = q.get("baseId");

    // Nếu không phải ROLE_ADMIN thì thêm baseId của user vào URL
    if (user.role !== "ROLE_ADMIN") {
      url += `&baseId=${user.baseId.id}`;
    }

    // Nếu có thêm filter từ URL
    if (kw) url += `&kw=${kw}`;
    if (cateId) url += `&cateId=${cateId}`;
    if (baseId) url += `&baseId=${baseId}`; // Dòng này sẽ ghi đè baseId nếu có trong query string

    let res = await authApis().get(url);

    if (res.data.length === 0) {
      setPage(0);
    } else {
      if (page === 1) {
        setDevices(res.data);
      } else {
        setDevices((prevDevices) => [...prevDevices, ...res.data]);
      }
    }
  } catch (err) {
    alert("Lỗi khi tải thiết bị");
    console.error(err);
  }
};

  const loadMore = () => {
    if (page > 0) setPage((prevPage) => prevPage + 1);
  };
  const loadCategories = async () => {
    try {
      const res = await authApis().get(endpoints.categories);
      setCategories(res.data);
    } catch (err) {
      console.error("Lỗi khi tải loại thiết bị:", err);
    }
  };

  const loadStatuses = async () => {
    try {
      const res = await authApis().get(endpoints.statuses);
      setStatuses(res.data);
    } catch (err) {
      console.error("Lỗi khi tải trạng thái:", err);
    }
  };

  const loadBases = async () => {
    try {
      const res = await authApis().get(endpoints.bases);
      setBases(res.data);
    } catch (err) {
      console.error("Lỗi khi tải cơ sở:", err);
    }
  };

  useEffect(() => {
    if (isAuthenticated) {
      if (page > 0) loadDevices();
      loadCategories();
      loadStatuses();
      loadBases();
    }
  }, [isAuthenticated, q, page]);

  useEffect(() => {
    setPage(1);
    setDevices([]);
  }, [q]);
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
      id: "",
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
    if (!newDevice.baseId || !newDevice.categoryId || !newDevice.statusId) {
      alert("Vui lòng chọn đầy đủ cơ sở, loại thiết bị và trạng thái!");
      return;
    }
    const formData = new FormData();
    formData.append("id", newDevice.id);
    formData.append("name", newDevice.name);
    formData.append("manufacturer", newDevice.manufacturer);
    formData.append("date", newDevice.date); // YYYY-MM-DD
    if (newDevice.image) {
      formData.append("image", newDevice.image);
    }
    formData.append("baseId", newDevice.baseId);
    formData.append("categoryId", newDevice.categoryId);
    formData.append("statusId", newDevice.statusId);

    try {
      await authApis().post(endpoints["device-add"], formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      alert("Thiết bị đã được thêm!");
      loadDevices();
      resetForm();
      setShowForm(false); // Ẩn form sau khi thêm thành công
    } catch (err) {
      console.error(err);
      alert("Lỗi khi thêm thiết bị.");
    }
  };

  const startEditing = (device) => {
    setEditingDevice(device);
    setShowForm(true);
    const formattedDate = device.date
      ? new Date(Number(device.date)).toISOString().split("T")[0]
      : "";

    setNewDevice({
      id: device.id, // Thêm id vào form chỉnh sửa
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
        loadDevices();
      } catch (err) {
        console.error(err);
        alert("Lỗi khi xóa thiết bị");
      }
    }
  };

  if (!isAuthenticated)
    return <Login setIsAuthenticated={setIsAuthenticated} />;

  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>Quản Lý Thiết Bị</h2>
      <button
        onClick={() => {
          resetForm();
          setShowForm(!showForm);
        }}
        className={styles.addButton}
      >
        {editingDevice ? "Cập nhật thiết bị" : "Thêm thiết bị"}
      </button>

      {showForm && (
        <div className={styles.formContainer}>
          <h3 className={styles.formTitle}>
            {editingDevice ? "Cập nhật thiết bị" : "Thêm thiết bị mới"}
          </h3>
          <div className={styles.formGroup}>
            <label htmlFor="name" className={styles.formLabel}>
              Tên thiết bị:
            </label>
            <input
              type="text"
              id="name"
              name="name"
              placeholder="Tên thiết bị"
              value={newDevice.name}
              onChange={handleChange}
              className={styles.formInput}
            />
          </div>
          <div className={styles.formGroup}>
            <label htmlFor="manufacturer" className={styles.formLabel}>
              Nhà sản xuất:
            </label>
            <input
              type="text"
              id="manufacturer"
              name="manufacturer"
              placeholder="Nhà sản xuất"
              value={newDevice.manufacturer}
              onChange={handleChange}
              className={styles.formInput}
            />
          </div>
          <div className={styles.formGroup}>
            <label htmlFor="date" className={styles.formLabel}>
              Ngày mua:
            </label>
            <input
              type="date"
              id="date"
              name="date"
              value={newDevice.date}
              onChange={handleChange}
              className={styles.formInput}
            />
          </div>
          <div className={styles.formGroup}>
            <label htmlFor="image" className={styles.formLabel}>
              Hình ảnh:
            </label>
            <input
              type="file"
              id="image"
              name="image"
              onChange={handleChange}
              className={styles.formInput}
            />
            {imagePreview && (
              <img
                src={imagePreview}
                alt="Preview"
                className={styles.formImagePreview}
              />
            )}
          </div>
          <div className={styles.formGroup}>
            <label htmlFor="baseId" className={styles.formLabel}>
              Cơ sở:
            </label>
            <select
              id="baseId"
              name="baseId"
              value={newDevice.baseId}
              onChange={handleChange}
              className={styles.formSelect}
            >
              <option value="">-- Chọn cơ sở --</option>
              {bases.map((c) => (
                <option key={c.id} value={c.id}>
                  {c.name}
                </option>
              ))}
            </select>
          </div>
          <div className={styles.formGroup}>
            <label htmlFor="categoryId" className={styles.formLabel}>
              Loại thiết bị:
            </label>
            <select
              id="categoryId"
              name="categoryId"
              value={newDevice.categoryId}
              onChange={handleChange}
              className={styles.formSelect}
            >
              <option value="">-- Chọn loại thiết bị --</option>
              {categories.map((c) => (
                <option key={c.id} value={c.id}>
                  {c.name}
                </option>
              ))}
            </select>
          </div>
          <div className={styles.formGroup}>
            <label htmlFor="statusId" className={styles.formLabel}>
              Trạng thái:
            </label>
            <select
              id="statusId"
              name="statusId"
              value={newDevice.statusId}
              onChange={handleChange}
              className={styles.formSelect}
            >
              <option value="">-- Chọn trạng thái --</option>
              {statuses.map((c) => (
                <option key={c.id} value={c.id}>
                  {c.name}
                </option>
              ))}
            </select>
          </div>
          <div className={styles.formButtonContainer}>
            <button
              type="button"
              onClick={handleSubmit}
              className={styles.formSubmitButton}
            >
              {editingDevice ? "Cập nhật" : "Thêm"} thiết bị
            </button>
            <button
              type="button"
              onClick={resetForm}
              className={styles.formResetButton}
            >
              Xóa trắng
            </button>
            <button
              type="button"
              onClick={() => setShowForm(false)}
              className={styles.formResetButton}
            >
              Hủy
            </button>
          </div>
        </div>
      )}

      <h3 className={styles.deviceListTitle}>Danh sách thiết bị</h3>
      <table className={styles.deviceTable}>
        <thead>
          <tr>
            <th>Tên</th>
            <th>NSX</th>
            <th>Ngày mua</th>
            <th>Hình ảnh</th>
            <th>Cơ sở</th>
            <th>Danh mục</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
        </thead>
        <tbody>
          {devices.map((d) => (
            <tr key={d.id}>
              <td>{d.name}</td>
              <td>{d.manufacturer}</td>
              <td>
                {d.date
                  ? new Date(Number(d.date)).toLocaleDateString("vi-VN")
                  : ""}
              </td>
              <td>
                {d.image ? (
                  <img src={d.image} alt="img" className={styles.deviceImage} />
                ) : (
                  "Không có"
                )}
              </td>
              <td>{d.baseId?.name || ""}</td>
              <td>{d.categoryId?.name || ""}</td>
              <td>{d.statusId?.name || ""}</td>
              <td className={styles.actionButtonContainer}>
                <button
                  onClick={() => nav(`/device/${d.id}`)}
                  className={`${styles.viewButton}`}
                >
                  Xem
                </button>
                <button
                  onClick={() => startEditing(d)}
                  className={`${styles.editButton}`}
                >
                  Sửa
                </button>
                <button
                  onClick={() => deleteDevice(d.id)}
                  className={`${styles.deleteButton}`}
                >
                  Xóa
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {page > 0 && (
        <div className="text-center my-3 ">
          <Button onClick={loadMore}>Tải thêm</Button>
        </div>
      )}
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
    <div className={styles.loginContainer}>
      <h2 className={styles.loginHeading}>Đăng nhập</h2>
      <form onSubmit={handleLogin} className={styles.loginForm}>
        <input
          type="text"
          name="username"
          placeholder="Tên đăng nhập"
          value={credentials.username}
          onChange={handleChange}
          className={styles.loginInput}
        />
        <input
          type="password"
          name="password"
          placeholder="Mật khẩu"
          value={credentials.password}
          onChange={handleChange}
          className={styles.loginInput}
        />
        <button type="submit" className={styles.loginButton}>
          Đăng nhập
        </button>
      </form>
    </div>
  );
};

export default DeviceManagement;
