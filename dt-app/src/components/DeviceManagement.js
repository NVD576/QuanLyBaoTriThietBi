import React, { useState, useEffect, useContext } from "react";
import Apis, { authApis, endpoints } from "../configs/Apis";
import { useNavigate, useSearchParams } from "react-router-dom";
import styles from "./DeviceManagement.module.css"; // Import CSS Module
import { Button } from "react-bootstrap";
import {  DeviceContext } from "../configs/MyContexts";
import { FaEdit, FaTrash, FaPlus } from "react-icons/fa";
import { Modal } from "react-bootstrap";

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
    file: null,
    baseId: "",
    categoryId: "",
    statusId: 1,
  });
  const [editingDevice, setEditingDevice] = useState(null);
  const [imagePreview, setImagePreview] = useState("");

  const [showForm, setShowForm] = useState(false);
  const nav = useNavigate();
  const [page, setPage] = useState(1);
  const [user] = useState(() => {
  const savedUser = localStorage.getItem("user");
  return savedUser ? JSON.parse(savedUser) : null;
}); 
  const [isLoading, setIsLoading] = useState(false);
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

      let res = await Apis.get(url);

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
      const res = await Apis.get(endpoints.categories);
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
    if (  page > 0) loadDevices();
  }, [ page, q]);

  useEffect(() => { 
      loadCategories();
      loadStatuses();
      loadBases();
    
  }, []);

  useEffect(() => {
    setPage(1);
    setDevices([]);
  }, [q]);
  const handleChange = (e) => {
    const { name, value, files } = e.target;
    if (name === "image") {
      const file = files[0];
      setNewDevice({ ...newDevice, file: file });
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
      statusId: 1,
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
      formData.append("file", newDevice.file);
    }
    formData.append("image", newDevice.image);
    formData.append("baseId.id", newDevice.baseId);
    formData.append("categoryId.id", newDevice.categoryId);
    formData.append("statusId.id", newDevice.statusId);
    console.log("Form data:", formData);
    try {
      setIsLoading(true);
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
    }finally {
      setIsLoading(false);
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
      imaage: device.file,
      image: device.image,
      baseId: device.baseId?.id || "",
      categoryId: device.categoryId?.id || "",
      statusId: device.statusId?.id || "",
    });
    console.log("Editing device:", device);
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


  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>Quản Lý Thiết Bị</h2>
      {user.role === "ROLE_ADMIN" && (
        <button
          onClick={() => {
            resetForm();
            setShowForm(!showForm);
          }}
          className={styles.addButton}
        >
          {editingDevice ? "Cập nhật thiết bị" : "Thêm thiết bị"}
        </button>
      )}
      <Modal
        show={showForm}
        onHide={() => setShowForm(false)}
        centered
        size="lg"
      >
        <Modal.Header closeButton>
          <Modal.Title>
            <h3 className={styles.formTitle}>
              {editingDevice ? "Cập nhật thiết bị" : "Thêm thiết bị mới"}
            </h3>
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {showForm && (
            <div className={styles.formContainer}>
              <div className={styles.formGrid}>
                {/* Cột trái */}
                <div className={styles.formColumn}>
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
                </div>

                {/* Cột phải */}

                <div className={styles.formColumn}>
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
                </div>
              </div>

              <div className={styles.formButtonContainer}>
                <button
                  type="button"
                  onClick={handleSubmit}
                  className={styles.formSubmitButton}
                  disabled={isLoading}
                >
                  {isLoading ? (
                    <>
                      Đang xử lý <span className={styles.spinner}></span>
                    </>
                  ) : editingDevice ? (
                    "Cập nhật"
                  ) : (
                    "Thêm thiết bị"
                  )}
                </button>
                {/* <button
                  type="button"
                  onClick={resetForm}
                  className={styles.formResetButton}
                >
                  Xóa trắng
                </button> */}
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
        </Modal.Body>
      </Modal>

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
                  <FaPlus /> Xem
                </button>
                <button
                  onClick={() => startEditing(d)}
                  className={`${styles.editButton}`}
                >
                  <FaEdit /> Sửa
                </button>
                {/* <button
                  onClick={() => deleteDevice(d.id)}
                  className={`${styles.deleteButton}`}
                >
                  <FaTrash /> Xoá
                </button> */}
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


export default DeviceManagement;
