import React, { useState, useEffect, useContext } from "react";
import Apis, { authApis, endpoints } from "../configs/Apis";
import { useNavigate, useSearchParams } from "react-router-dom";
import styles from "./DeviceManagement.module.css"; // Import CSS Module
import { Button, Modal } from "react-bootstrap";
import { DeviceContext } from "../configs/MyContexts";
import {
  FaEdit,
  FaPlus,
  FaTrash,
  FaEye,
  FaSyncAlt,
  FaBuilding, // For base
  FaLayerGroup, // For category
  FaRegCalendarAlt, // For date
  FaIndustry, // For manufacturer
  FaTag, // For status
} from "react-icons/fa"; // Import more icons

const DeviceManagement = () => {
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

      if (user && user.role !== "ROLE_ADMIN") {
        url += `&baseId=${user.baseId.id}`;
      }

      if (kw) url += `&kw=${kw}`;
      if (cateId) url += `&cateId=${cateId}`;
      if (baseId) url += `&baseId=${baseId}`;

      let res = await Apis.get(url);

      if (res.data.length === 0 && page === 1) {
        setDevices([]);
        setPage(0);
      } else if (res.data.length === 0 && page > 1) {
        setPage(0);
      } else {
        if (page === 1) {
          setDevices(res.data);
        } else {
          setDevices((prevDevices) => {
            const newDevices = res.data.filter(
              (newD) =>
                !prevDevices.some((existingD) => existingD.id === newD.id)
            );
            return [...prevDevices, ...newDevices];
          });
        }
      }
    } catch (err) {
      console.error("Lỗi khi tải thiết bị:", err);
      alert("Lỗi khi tải thiết bị.");
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
    if (page > 0) loadDevices();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page, q, user]);

  useEffect(() => {
    loadCategories();
    loadStatuses();
    loadBases();
  }, []);

  useEffect(() => {
    setPage(1);
    setDevices([]);
    // eslint-disable-next-line react-hooks/exhaustive-deps
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
      file: null,
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
    if (newDevice.file) {
      formData.append("file", newDevice.file);
    }
    formData.append("baseId.id", newDevice.baseId);
    formData.append("categoryId.id", newDevice.categoryId);
    formData.append("statusId.id", newDevice.statusId);
    console.log("new device:", newDevice);
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
    } finally {
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
      id: device.id,
      name: device.name,
      manufacturer: device.manufacturer,
      date: formattedDate,
      file: null,
      image: device.image,
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
        alert("Thiết bị đã được xóa!");
      } catch (err) {
        console.error("Lỗi khi xóa thiết bị:", err);
        alert("Lỗi khi xóa thiết bị.");
      }
    }
  };

  if (!user) {
    return <p>Vui lòng đăng nhập để xem trang này.</p>;
  }

  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>Quản Lý Thiết Bị</h2>
      {user.role === "ROLE_ADMIN" && (
        <button
          onClick={() => {
            resetForm();
            setShowForm(true);
          }}
          className={styles.addButton}
        >
          <FaPlus /> Thêm thiết bị mới
        </button>
      )}

      <Modal
        show={showForm}
        onHide={() => {
          setShowForm(false);
          resetForm();
        }}
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
          <div className={styles.formContainer}>
            <div className={styles.formGrid}>
              {/* Left Column */}
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
                  {(imagePreview || newDevice.image) && (
                    <img
                      src={imagePreview || newDevice.image}
                      alt="Preview"
                      className={styles.formImagePreview}
                    />
                  )}
                </div>
              </div>

              {/* Right Column */}
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
                    disabled={user.role !== "ROLE_ADMIN"}
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
                    Đang xử lý <FaSyncAlt className={styles.spinner} />
                  </>
                ) : editingDevice ? (
                  "Cập nhật"
                ) : (
                  "Thêm thiết bị"
                )}
              </button>
              {user.role === "ROLE_ADMIN" && ( // Only show reset for ADMIN when adding
                <button
                  type="button"
                  onClick={resetForm}
                  className={styles.formResetButton}
                >
                  Xóa trắng
                </button>
              )}
              <button
                type="button"
                onClick={() => {
                  setShowForm(false);
                  resetForm();
                }}
                className={styles.formResetButton}
              >
                Hủy
              </button>
            </div>
          </div>
        </Modal.Body>
      </Modal>

      {/* <h3 className={styles.deviceListTitle}>Danh sách thiết bị</h3> */}
      <div className={styles.deviceCardsContainer}>
        {devices.length > 0 ? (
          devices.map((d) => (
            <div key={d.id} className={styles.deviceCard}>
              <div className={styles.deviceCardImageContainer}>
                {d.image ? (
                  <img
                    src={d.image}
                    alt={d.name}
                    className={styles.deviceCardImage}
                  />
                ) : (
                  <p>Không có hình ảnh</p>
                )}
              </div>
              <div className={styles.deviceCardInfo}>
                <h4 className={styles.deviceCardName}>{d.name}</h4>
                <p className={styles.deviceCardDetail}>
                  <FaIndustry /> <strong>NSX:</strong> {d.manufacturer}
                </p>
                <p className={styles.deviceCardDetail}>
                  <FaRegCalendarAlt /> <strong>Ngày mua:</strong>{" "}
                  {d.date
                    ? new Date(Number(d.date)).toLocaleDateString("vi-VN")
                    : ""}
                </p>
                <p className={styles.deviceCardDetail}>
                  <FaBuilding /> <strong>Cơ sở:</strong> {d.baseId?.name || ""}
                </p>
                <p className={styles.deviceCardDetail}>
                  <FaLayerGroup /> <strong>Danh mục:</strong>{" "}
                  {d.categoryId?.name || ""}
                </p>
                <p className={styles.deviceCardDetail}>
                  <FaTag /> <strong>Trạng thái:</strong>{" "}
                  {d.statusId?.name || ""}
                </p>
                <div className={styles.deviceCardActions}>
                  <button
                    onClick={() => nav(`/device/${d.id}`)}
                    className={`${styles.viewButton}`}
                  >
                    <FaEye /> Xem
                  </button>
                  <button
                    onClick={() => startEditing(d)}
                    className={`${styles.editButton}`}
                  >
                    <FaEdit /> Sửa
                  </button>
                  {user.role === "ROLE_ADMIN" && (
                    <button
                      onClick={() => deleteDevice(d.id)}
                      className={`${styles.deleteButton}`}
                    >
                      <FaTrash /> Xoá
                    </button>
                  )}
                </div>
              </div>
            </div>
          ))
        ) : (
          <p className="text-center" style={{ gridColumn: "1 / -1" }}>
            Không có thiết bị nào.
          </p>
        )}
      </div>
      {page > 0 && (
        <div className="text-center my-3">
          <Button onClick={loadMore}>Tải thêm</Button>
        </div>
      )}
    </div>
  );
};

export default DeviceManagement;
