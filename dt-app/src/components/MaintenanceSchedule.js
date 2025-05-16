import React, { useEffect, useState } from "react";
import { Button, Form, Table, Modal, Alert } from "react-bootstrap";
import Apis, { endpoints } from "../configs/Apis";

const MaintenanceSchedule = () => {
  const [schedules, setSchedules] = useState([]);
  const [devices, setDevices] = useState([]);
  const [frequencies, setFrequencies] = useState([]);
  const [types, setTypes] = useState([]);

  const [show, setShow] = useState(false);
  const [newSchedule, setNewSchedule] = useState({
    deviceId: "",
    frequencyId: "",
    typeId: "",
    date: "",
  });

  // Thêm state thông báo
  const [notifications, setNotifications] = useState({
    overdue: [],
    upcoming: [],
  });

  useEffect(() => {
    fetchSchedules();
    fetchDevices();
    fetchFrequencies();
    fetchTypes();
  }, []);

  // Kiểm tra lịch để tạo thông báo
  const checkNotifications = (schedules) => {
    const today = new Date();
    const upcomingLimit = new Date();
    upcomingLimit.setDate(today.getDate() + 7); // 7 ngày tới

    const overdue = [];
    const upcoming = [];

    schedules.forEach((s) => {
      const scheduleDate = new Date(s.date);
      if (scheduleDate < today) {
        overdue.push(s);
      } else if (scheduleDate >= today && scheduleDate <= upcomingLimit) {
        upcoming.push(s);
      }
    });

    setNotifications({ overdue, upcoming });
  };

  const fetchSchedules = async () => {
    try {
      const res = await Apis.get(endpoints["maintenances"]);
      setSchedules(res.data);
      checkNotifications(res.data);
    } catch (err) {
      console.error("Lỗi khi tải lịch bảo trì:", err);
    }
  };

  const fetchDevices = async () => {
    try {
      const res = await Apis.get(endpoints.devices);
      setDevices(res.data);
    } catch (err) {
      alert("Lỗi khi tải thiết bị");
      console.error(err);
    }
  };

  const fetchFrequencies = async () => {
    try {
      const res = await Apis.get(endpoints.frequencies);
      setFrequencies(res.data);
    } catch (err) {
      console.error("Lỗi khi tải tần suất:", err);
    }
  };

  const fetchTypes = async () => {
    try {
      const res = await Apis.get(endpoints.types);
      setTypes(res.data);
    } catch (err) {
      console.error("Lỗi khi tải loại bảo trì:", err);
    }
  };

  const handleAdd = async () => {
    if (
      !newSchedule.deviceId ||
      !newSchedule.frequencyId ||
      !newSchedule.typeId ||
      !newSchedule.date
    ) {
      alert("Vui lòng điền đầy đủ thông tin.");
      return;
    }

    try {
      const res = await Apis.post(endpoints.maintenances, {
        deviceId: newSchedule.deviceId,
        frequencyId: newSchedule.frequencyId,
        typeId: newSchedule.typeId,
        date: newSchedule.date,
      });

      const updatedSchedules = [...schedules, res.data];
      setSchedules(updatedSchedules);
      checkNotifications(updatedSchedules);
      setShow(false);
      setNewSchedule({ deviceId: "", frequencyId: "", typeId: "", date: "" });
    } catch (err) {
      alert("Lỗi khi thêm lịch bảo trì!");
      console.error(err);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Bạn có chắc muốn xoá lịch bảo trì này?")) return;

    try {
      await Apis.delete(`${endpoints.maintenances}/${id}`);
      const updatedSchedules = schedules.filter((s) => s.id !== id);
      setSchedules(updatedSchedules);
      checkNotifications(updatedSchedules);
    } catch (err) {
      alert("Lỗi khi xoá lịch bảo trì!");
      console.error(err);
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2 style={{ color: "#1976d2" }}>Lịch Bảo Trì</h2>

      <p>
        <strong>Quản lý Lịch Bảo Trì:</strong> Tạo và quản lý lịch bảo trì định kỳ.
      </p>

      {/* Phần thông báo */}
      {notifications.overdue.length > 0 && (
        <Alert variant="danger">
          <strong>Cảnh báo!</strong> Có {notifications.overdue.length} lịch bảo trì đã quá hạn:
          <ul>
            {notifications.overdue.map((s) => (
              <li key={s.id}>
                Thiết bị: {s.deviceId?.name || s.deviceId}, ngày dự kiến:{" "}
                {new Date(s.date).toLocaleDateString("vi-VN")}
              </li>
            ))}
          </ul>
        </Alert>
      )}

      {notifications.upcoming.length > 0 && (
        <Alert variant="warning">
          <strong>Nhắc nhở:</strong> Có {notifications.upcoming.length} lịch bảo trì sắp tới trong 7 ngày:
          <ul>
            {notifications.upcoming.map((s) => (
              <li key={s.id}>
                Thiết bị: {s.deviceId?.name || s.deviceId}, ngày dự kiến:{" "}
                {new Date(s.date).toLocaleDateString("vi-VN")}
              </li>
            ))}
          </ul>
        </Alert>
      )}

      <Button variant="success" className="my-3" onClick={() => setShow(true)}>
        + Thêm Lịch Bảo Trì
      </Button>

      <Table striped bordered hover>
        <thead>
          <tr>
            <th>#</th>
            <th>Thiết Bị</th>
            <th>Tần Suất</th>
            <th>Loại Bảo Trì</th>
            <th>Ngày Dự Kiến</th>
            <th>Hành Động</th>
          </tr>
        </thead>
        <tbody>
          {schedules.map((s, index) => (
            <tr key={s.id}>
              <td>{index + 1}</td>
              <td>{s.deviceId?.name || s.deviceId}</td>
              <td>{s.frequencyId?.frequency || s.frequencyId}</td>
              <td>{s.typeId?.type || s.typeId}</td>
              <td>{new Date(s.date).toLocaleDateString("vi-VN")}</td>
              <td>
                <Button
                  variant="danger"
                  size="sm"
                  onClick={() => handleDelete(s.id)}
                >
                  Xoá
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      {/* Modal Thêm Lịch */}
      <Modal style={{ marginTop: "50px" }} show={show} onHide={() => setShow(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Thêm Lịch Bảo Trì</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Thiết Bị</Form.Label>
              <Form.Select
                value={newSchedule.deviceId}
                onChange={(e) =>
                  setNewSchedule({ ...newSchedule, deviceId: e.target.value })
                }
              >
                <option value="">-- Chọn thiết bị --</option>
                {devices.map((d) => (
                  <option key={d.id} value={d.id}>
                    {d.name}
                  </option>
                ))}
              </Form.Select>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Tần Suất</Form.Label>
              <Form.Select
                value={newSchedule.frequencyId}
                onChange={(e) =>
                  setNewSchedule({ ...newSchedule, frequencyId: e.target.value })
                }
              >
                <option value="">-- Chọn tần suất --</option>
                {frequencies.map((f) => (
                  <option key={f.id} value={f.id}>
                    {f.frequency}
                  </option>
                ))}
              </Form.Select>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Loại Bảo Trì</Form.Label>
              <Form.Select
                value={newSchedule.typeId}
                onChange={(e) =>
                  setNewSchedule({ ...newSchedule, typeId: e.target.value })
                }
              >
                <option value="">-- Chọn loại bảo trì --</option>
                {types.map((t) => (
                  <option key={t.id} value={t.id}>
                    {t.type}
                  </option>
                ))}
              </Form.Select>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Ngày Dự Kiến</Form.Label>
              <Form.Control
                type="date"
                value={newSchedule.date}
                onChange={(e) =>
                  setNewSchedule({ ...newSchedule, date: e.target.value })
                }
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShow(false)}>
            Huỷ
          </Button>
          <Button variant="primary" onClick={handleAdd}>
            Lưu
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default MaintenanceSchedule;
