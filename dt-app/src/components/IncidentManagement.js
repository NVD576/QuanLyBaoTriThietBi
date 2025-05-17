import React, { useEffect, useState } from "react";
import Apis, { endpoints } from "../configs/Apis";
import "./IncidentManagement.css"; // Import file CSS
import { useContext } from "react";
import { DeviceContext, MyUserContext } from "../configs/MyContexts";

const IncidentManagement = () => {
  const [issues, setIssues] = useState([]);
  const [levels, setLevels] = useState([]);
  // const [devices, setDevices] = useState([]);
  const{devices} = useContext(DeviceContext);
  const [formData, setFormData] = useState({
    deviceId: "",
    des: "",
    levelId: "",
    date: "",
    isResolved: false,
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showAddForm, setShowAddForm] = useState(false); // State để kiểm soát hiển thị form
  const user = useContext(MyUserContext);
  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError(null);
      try {
        const [issuesRes, levelsRes] = await Promise.all([
          Apis.get(endpoints.issues),
          Apis.get(endpoints.levels),
          // Apis.get(endpoints.devices),
        ]);
        console.log("Issues:", issuesRes.data);
        console.log("user:", user.baseId);
        const filteredIssues =
          user.role === "ROLE_ADMIN"
            ? issuesRes.data
            : issuesRes.data.filter(
                (issue) => issue.deviceId?.baseId.id === user.baseId.id
              );
        setIssues(filteredIssues);
        setLevels(levelsRes.data);
        // setDevices(devicesRes.data);
      } catch (err) {
        console.error(err);
        setError("Lỗi khi tải dữ liệu!");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleAddIncident = async () => {
    try {
      const res = await Apis.post(endpoints.issue, formData);
      setIssues([...issues, res.data]);
      setFormData({
        deviceId: "",
        des: "",
        levelId: "",
        date: "",
        isResolved: false,
      });
      setShowAddForm(false); // Ẩn form sau khi thêm thành công
      alert("Ghi nhận sự cố thành công!");
    } catch (err) {
      console.error(err);
      alert("Lỗi khi gửi dữ liệu!");
    }
  };

  const updateStatus = async (id, newStatus) => {
    try {
      await Apis.patch(`${endpoints.issue}${id}/`, {
        isResolved: newStatus,
      });
      const updatedIssues = issues.map((issue) =>
        issue.id === id ? { ...issue, isResolved: newStatus } : issue
      );
      setIssues(updatedIssues);
    } catch (err) {
      console.error(err);
      alert("Lỗi khi cập nhật trạng thái!");
    }
  };

  if (loading) {
    return <div className="loading-spinner">Đang tải dữ liệu...</div>;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  return (
    <div className="incident-management-container">
      <h2 className="incident-management-title">Quản Lý Sự Cố</h2>

      {!showAddForm && (
        <button onClick={() => setShowAddForm(true)} className="btn btn-add">
          <i className="fas fa-plus"></i> Thêm Sự Cố Mới
        </button>
      )}

      {showAddForm && (
        <div className="incident-form">
          <div className="form-group">
            <label htmlFor="deviceId">Thiết bị:</label>
            <select
              id="deviceId"
              name="deviceId"
              value={formData.deviceId}
              onChange={handleChange}
              className="form-control"
            >
              <option value="">Chọn thiết bị</option>
              {devices.map((d) => (
                <option key={d.id} value={d.id}>
                  {d.name || `Thiết bị ${d.id}`}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="des">Mô tả sự cố:</label>
            <textarea
              id="des"
              name="des"
              value={formData.des}
              onChange={handleChange}
              className="form-control"
              placeholder="Nhập mô tả sự cố"
            />
          </div>

          <div className="form-group">
            <label htmlFor="levelId">Mức độ nghiêm trọng:</label>
            <select
              id="levelId"
              name="levelId"
              value={formData.levelId}
              onChange={handleChange}
              className="form-control"
            >
              <option value="">Chọn mức độ</option>
              {levels.map((lvl) => (
                <option key={lvl.id} value={lvl.id}>
                  {lvl.level || `Mức ${lvl.id}`}
                </option>
              ))}
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="date">Thời gian xảy ra:</label>
            <input
              type="date"
              id="date"
              name="date"
              value={formData.date}
              onChange={handleChange}
              className="form-control"
            />
          </div>

          <div className="form-group">
            <label htmlFor="isResolved">Đã xử lý:</label>
            <input
              type="checkbox"
              id="isResolved"
              name="isResolved"
              checked={formData.isResolved}
              onChange={handleChange}
              className="form-checkbox"
            />
          </div>

          <div className="form-actions">
            <button onClick={handleAddIncident} className="btn btn-primary">
              Ghi nhận
            </button>
            <button
              onClick={() => setShowAddForm(false)}
              className="btn btn-secondary"
            >
              Hủy
            </button>
          </div>
        </div>
      )}

      {issues.length === 0 && !loading ? (
        <div className="empty-message">Chưa có sự cố nào được ghi nhận.</div>
      ) : (
        <div className="incident-table-container">
          <table className="incident-table">
            <thead>
              <tr>
                <th>Thiết bị</th>
                <th>Mô tả</th>
                <th>Mức độ</th>
                <th>Ngày</th>
                <th>Trạng thái</th>
                <th>Cập nhật</th>
              </tr>
            </thead>
            <tbody>
              {issues.map((issue) => (
                <tr key={issue.id}>
                  <td>
                    {issue.deviceId?.name || `Thiết bị ${issue.deviceId?.id}`}
                  </td>
                  <td>{issue.des}</td>
                  <td>{issue.levelId.level}</td>
                  <td>{new Date(issue.date).toLocaleDateString("vi-VN")}</td>
                  <td className={issue.isResolved ? "resolved" : "unresolved"}>
                    {issue.isResolved ? "Đã xử lý" : "Chưa xử lý"}
                  </td>
                  <td>
                    <select
                      value={issue.isResolved}
                      onChange={(e) =>
                        updateStatus(issue.id, e.target.value === "true")
                      }
                      className="status-select"
                    >
                      <option value={false}>Chưa xử lý</option>
                      <option value={true}>Đã xử lý</option>
                    </select>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default IncidentManagement;
