import React, { useContext, useEffect, useState } from "react";
import { Button, Form, Modal, Alert } from "react-bootstrap";
import Apis, { endpoints } from "../configs/Apis";
import { DeviceContext, MyUserContext } from "../configs/MyContexts";
import { Calendar, momentLocalizer } from "react-big-calendar";
import moment from "moment";
import "react-big-calendar/lib/css/react-big-calendar.css";

const localizer = momentLocalizer(moment);

const MaintenanceSchedule = () => {
  const [schedules, setSchedules] = useState([]);
  const { devices } = useContext(DeviceContext);
  const [frequencies, setFrequencies] = useState([]);
  const [types, setTypes] = useState([]);
  const [show, setShow] = useState(false);
  const [selectedEvent, setSelectedEvent] = useState(null);

  // Thêm state lưu ngày được chọn khi click vào lịch (slot)
  const [selectedDate, setSelectedDate] = useState(null);

  const [newSchedule, setNewSchedule] = useState({
    deviceId: "",
    frequencyId: "",
    typeId: "",
    date: "",
  });
  const user = useContext(MyUserContext);

  const [notifications, setNotifications] = useState({
    overdue: [],
    upcoming: [],
  });

  useEffect(() => {
    fetchSchedules();
    fetchFrequencies();
    fetchTypes();
  }, []);

  const checkNotifications = (schedules) => {
    const today = new Date();
    const upcomingLimit = new Date();
    upcomingLimit.setDate(today.getDate() + 7);

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
      const res = await Apis.get(endpoints.maintenances);
      let filtered = res.data;

      if (user.role !== "ROLE_ADMIN") {
        filtered = filtered.filter(
          (s) => s.deviceId?.baseId.id === user.baseId.id
        );
      }

      setSchedules(filtered);
      checkNotifications(filtered);
    } catch (err) {
      console.error("Lỗi khi tải lịch bảo trì:", err);
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

  // Hàm thêm lịch bảo trì
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
      const res = await Apis.post(endpoints.maintenances, newSchedule);
      const updated = [...schedules, res.data];
      setSchedules(updated);
      checkNotifications(updated);
      setShow(false);
      setNewSchedule({ deviceId: "", frequencyId: "", typeId: "", date: "" });
      setSelectedDate(null);
    } catch (err) {
      alert("Lỗi khi thêm lịch bảo trì!");
      console.error(err);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Bạn có chắc muốn xoá lịch bảo trì này?")) return;

    try {
      await Apis.delete(`${endpoints.maintenances}/${id}`);
      const updated = schedules.filter((s) => s.id !== id);
      setSchedules(updated);
      checkNotifications(updated);
    } catch (err) {
      alert("Lỗi khi xoá lịch bảo trì!");
      console.error(err);
    }
  };

  const events = schedules.map((s) => ({
    id: s.id,
    title: `${s.deviceId?.name || "Thiết bị"} - ${s.typeId?.type || "Bảo trì"}`,
    start: new Date(s.date),
    end: new Date(s.date),
    allDay: true,
    schedule: s,
  }));

  // Khi click vào ô ngày trên lịch, hiện popup thêm lịch với ngày được chọn
  const handleSelectSlot = (slotInfo) => {
    const date = moment(slotInfo.start).format("YYYY-MM-DD");
    setSelectedDate(date);
    setNewSchedule({
      deviceId: "",
      frequencyId: "",
      typeId: "",
      date: date,
    });
    setShow(true);
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2 style={{ color: "#1976d2" }}>Lịch Bảo Trì</h2>

      {notifications.overdue.length > 0 && (
        <Alert variant="danger">
          <strong>⚠️ Cảnh báo:</strong> Có {notifications.overdue.length} thiết
          bị quá hạn bảo trì:
          <ul className="mt-2">
            {notifications.overdue.map((s) => (
              <li key={s.id}>
                <strong>{s.deviceId?.name}</strong> – ngày bảo trì:{" "}
                {new Date(s.date).toLocaleDateString("vi-VN")}
              </li>
            ))}
          </ul>
        </Alert>
      )}

      {notifications.upcoming.length > 0 && (
        <Alert variant="warning">
          <strong>⏰ Nhắc nhở:</strong> Có {notifications.upcoming.length} thiết
          bị sắp đến hạn bảo trì:
          <ul className="mt-2">
            {notifications.upcoming.map((s) => (
              <li key={s.id}>
                <strong>{s.deviceId?.name}</strong> – ngày bảo trì:{" "}
                {new Date(s.date).toLocaleDateString("vi-VN")}
              </li>
            ))}
          </ul>
        </Alert>
      )}

      <Button variant="success" className="mb-3" onClick={() => {
        setSelectedDate(null);
        setNewSchedule({ deviceId: "", frequencyId: "", typeId: "", date: "" });
        setShow(true);
      }}>
        + Thêm Lịch Bảo Trì
      </Button>

      <Calendar
        localizer={localizer}
        events={events}
        startAccessor="start"
        endAccessor="end"
        style={{ height: 600 }}
        onSelectEvent={(event) => setSelectedEvent(event)}
        selectable
        onSelectSlot={handleSelectSlot}  // Bắt sự kiện click vào ô ngày
      />

      {/* Modal chi tiết sự kiện */}
      <Modal
        style={{ marginTop: "70px" }}
        show={selectedEvent !== null}
        onHide={() => setSelectedEvent(null)}
      >
        <Modal.Header closeButton>
          <Modal.Title>Chi Tiết Bảo Trì</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {selectedEvent && (
            <>
              <p>
                <strong>Thiết bị:</strong> {selectedEvent.schedule.deviceId?.name}
              </p>
              <p>
                <strong>Loại bảo trì:</strong> {selectedEvent.schedule.typeId?.type}
              </p>
              <p>
                <strong>Tần suất:</strong> {selectedEvent.schedule.frequencyId?.frequency}
              </p>
              <p>
                <strong>Ngày dự kiến:</strong>{" "}
                {new Date(selectedEvent.schedule.date).toLocaleDateString("vi-VN")}
              </p>
            </>
          )}
        </Modal.Body>
        <Modal.Footer>
          <Button
            variant="danger"
            onClick={() => {
              handleDelete(selectedEvent.schedule.id);
              setSelectedEvent(null);
            }}
          >
            Xoá
          </Button>
          <Button variant="secondary" onClick={() => setSelectedEvent(null)}>
            Đóng
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Modal Thêm lịch */}
      <Modal
        style={{ marginTop: "50px" }}
        show={show}
        onHide={() => setShow(false)}
      >
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
                  setNewSchedule({
                    ...newSchedule,
                    frequencyId: e.target.value,
                  })
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
