import React, { useEffect, useState, useContext } from "react";
import Apis, { endpoints } from "../configs/Apis";
import "./IncidentManagement.css"; // Import file CSS
import { MyUserContext } from "../configs/MyContexts";

const IncidentManagement = () => {
  const [issues, setIssues] = useState([]);
  const [levels, setLevels] = useState([]);
  const [devices, setDevices] = useState([]);
  const [formData, setFormData] = useState({
    deviceId: "",
    des: "",
    levelId: "",
    date: "",
    isResolved: false,
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showAddForm, setShowAddForm] = useState(false);
  const [selectedIssueId, setSelectedIssueId] = useState(null);
  const [costInput, setCostInput] = useState("");
  const user = useContext(MyUserContext);

  // Lấy ngày hiện tại ở định dạng YYYY-MM-DD
  const getTodayDate = () => {
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, "0"); // Tháng bắt đầu từ 0
    const day = String(today.getDate()).padStart(2, "0");
    return `${year}-${month}-${day}`;
  };

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      setError(null);
      try {
        const [issuesRes, levelsRes] = await Promise.all([
          Apis.get(endpoints.issues),
          Apis.get(endpoints.levels),
        ]);
        const filteredIssues =
          user.role === "ROLE_ADMIN"
            ? issuesRes.data
            : issuesRes.data.filter(
                (issue) => issue.deviceId?.baseId.id === user.baseId.id
              );
        setIssues(filteredIssues);
        setLevels(levelsRes.data);
      } catch (err) {
        console.error(err);
        setError("Lỗi khi tải dữ liệu!");
      } finally {
        setLoading(false);
      }
    };
    fetchDevices();
    fetchData();
  }, [user.baseId.id, user.role]);

  const fetchDevices = async () => {
    try {
      let url = `${endpoints.devices}?`;
      if (user.role !== "ROLE_ADMIN") {
        url += `&baseId=${user.baseId.id}`;
      }
      const res = await Apis.get(url);
      setDevices(res.data);
    } catch (err) {
      console.error("Lỗi khi tải thiết bị:", err);
    }
  };

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleAddIssue = async () => {
    try {
      const res = await Apis.post(endpoints["issue-add"], formData);
      setIssues([...issues, res.data]);
      setFormData({
        deviceId: "",
        des: "",
        levelId: "",
        date: "",
        isResolved: false,
      });
      setShowAddForm(false);
      alert("Ghi nhận sự cố thành công!");
    } catch (err) {
      console.error(err);
      alert("Lỗi khi gửi dữ liệu!");
    }
  };

  const confirmResolved = async (id, cost) => {
    try {
      const data = new FormData();
      data.append("cost", cost);
      data.append("accountId", user.id);
      console.log("URL:", endpoints["issue-confirm"](id));
      await Apis.post(endpoints["issue-confirm"](id), data);

      const updatedIssues = issues.map((issue) =>
        issue.id === id ? { ...issue, isResolved: true } : issue
      );
      setIssues(updatedIssues);
      setSelectedIssueId(null);
      setCostInput("");
    } catch (err) {
      console.error(err);
      alert("Lỗi khi xác nhận xử lý!");
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
          <i className="fas fa-plus"></i> Báo cáo Sự Cố Mới
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
              // THAY ĐỔI TẠI ĐÂY: Thêm thuộc tính max
              max={getTodayDate()}
            />
          </div>

          <div className="form-actions">
            <button onClick={handleAddIssue} className="btn btn-primary">
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
                    {!issue.isResolved ? (
                      selectedIssueId === issue.id ? (
                        <div>
                          <input
                            type="number"
                            placeholder="Nhập chi phí xử lý"
                            value={costInput}
                            onChange={(e) => setCostInput(e.target.value)}
                            className="border p-1 rounded mr-2"
                          />
                          <button
                            onClick={() => confirmResolved(issue.id, costInput)}
                            className="confirm-button"
                          >
                            Xác nhận
                          </button>
                        </div>
                      ) : (
                        <button
                          onClick={() => setSelectedIssueId(issue.id)}
                          className="confirm-button"
                        >
                          Xác nhận xử lý
                        </button>
                      )
                    ) : (
                      <span className="text-green-600">Đã xử lý</span>
                    )}
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