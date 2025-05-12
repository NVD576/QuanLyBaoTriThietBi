import React, { useEffect, useState } from "react";
import { Button, Form, Table, Modal } from "react-bootstrap";
import axios from "axios";

const MaintenanceSchedule = () => {
  const [schedules, setSchedules] = useState([
    {
      id: 1,
      device: "Máy điều hòa",
      frequency: "Hàng tháng",
      type: "Vệ sinh",
      due: "2025-05-30",
    },
    {
      id: 2,
      device: "Máy in",
      frequency: "Hàng quý",
      type: "Bảo dưỡng",
      due: "2025-06-15",
    },
  ]);

  const [show, setShow] = useState(false);
  const [newSchedule, setNewSchedule] = useState({
    device: "",
    frequency: "",
    type: "",
    due: "",
  });
  useEffect(() => {
    const fetchSchedules = async () => {
      try {
        const res = await axios.get("http://localhost:8000/api/maintenance/");
        setSchedules(res.data);
      } catch (err) {
        console.error("Lỗi khi tải lịch bảo trì:", err);
      }
    };
    fetchSchedules();
  }, []);
  const handleAdd = () => {
    const nextId = schedules.length
      ? Math.max(...schedules.map((s) => s.id)) + 1
      : 1;
    setSchedules([...schedules, { id: nextId, ...newSchedule }]);
    setShow(false);
    setNewSchedule({ device: "", frequency: "", type: "", due: "" });
  };

  const handleDelete = (id) => {
    setSchedules(schedules.filter((s) => s.id !== id));
  };

  return (
    <div style={{ padding: "20px" }}>
      <h2 style={{ color: "#1976d2" }}>Lịch Bảo Trì</h2>

      <p>
        <strong>Quản lý Lịch Bảo Trì:</strong> Tạo và quản lý lịch bảo trì định
        kỳ cho các thiết bị, bao gồm tần suất và loại bảo trì.
      </p>
      <p>
        <strong>Theo dõi và nhắc nhở:</strong> Cung cấp thông báo về lịch bảo
        trì sắp tới hoặc đã quá hạn.
      </p>

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
              <td>{s.device}</td>
              <td>{s.frequency}</td>
              <td>{s.type}</td>
              <td>{s.due}</td>
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
      <Modal  style={{ marginTop: "50px"}} show={show} onHide={() => setShow(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Thêm Lịch Bảo Trì</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group className="mb-3">
              <Form.Label>Thiết Bị</Form.Label>
              <Form.Control
                type="text"
                value={newSchedule.device}
                onChange={(e) =>
                  setNewSchedule({ ...newSchedule, device: e.target.value })
                }
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Tần Suất</Form.Label>
              <Form.Control
                type="text"
                value={newSchedule.frequency}
                onChange={(e) =>
                  setNewSchedule({ ...newSchedule, frequency: e.target.value })
                }
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Loại Bảo Trì</Form.Label>
              <Form.Control
                type="text"
                value={newSchedule.type}
                onChange={(e) =>
                  setNewSchedule({ ...newSchedule, type: e.target.value })
                }
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Ngày Dự Kiến</Form.Label>
              <Form.Control
                type="date"
                value={newSchedule.due}
                onChange={(e) =>
                  setNewSchedule({ ...newSchedule, due: e.target.value })
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
