import React, { useEffect, useState } from "react";
import Apis, { endpoints } from "../configs/Apis";
import { useParams } from "react-router-dom";
import "./Device.css"; // Import file CSS

const Device = ({ device }) => {
  const { id } = useParams();
  const [maintenance, setMaintenance] = useState([]);
  const [issue, setIssue] = useState([]);
  const [repair, setRepair] = useState([]);
  const [activeTab, setActiveTab] = useState("maintenance");

  useEffect(() => {
    loadMaintenance();
    loadIssue();
    loadRepair();
  }, [id]); // Thêm dependency để fetch lại dữ liệu khi id thay đổi

  const loadMaintenance = async () => {
    try {
      const res = await Apis.get(endpoints["device"] + `/${id}/maintenances`);
      setMaintenance(res.data);
    } catch (err) {
      console.error("Lỗi khi tải lịch bảo trì:", err);
    }
  };

  const loadIssue = async () => {
    try {
      const res = await Apis.get(endpoints["device"] + `/${id}/issues`);
      setIssue(res.data);
    } catch (err) {
      console.error("Lỗi khi tải sự cố:", err);
    }
  };
  const loadRepair = async () => {
    try {
      const res = await Apis.get(endpoints["device"] + `/${id}/repairs`);
      setRepair(res.data);
    } catch (err) {
      console.error("Lỗi khi tải sửa chữa:", err);
    }
  };

  return (
    <div className="device-container">
      <div className="device-tabs">
        <button
          className={activeTab === "maintenance" ? "active" : ""}
          onClick={() => setActiveTab("maintenance")}
        >
          <i className="fas fa-wrench"></i> Bảo trì
        </button>
        <button
          className={activeTab === "issue" ? "active" : ""}
          onClick={() => setActiveTab("issue")}
        >
          <i className="fas fa-exclamation-triangle"></i> Sự cố
        </button>
        <button
          className={activeTab === "repair" ? "active" : ""}
          onClick={() => setActiveTab("repair")}
        >
          <i className="fas fa-tools"></i> Sửa chữa
        </button>
      </div>

      <div className="device-info">
        {activeTab === "maintenance" && (
          <div className="tab-content">
            <h3><i className="fas fa-wrench"></i> Thông tin Bảo trì</h3>
            {maintenance.length > 0 ? (
              maintenance.map((item) =>
                item.typeId?.type === "Định kỳ" ? (
                  <div key={item.id} className="maintenance-item">
                    <p><i className="far fa-calendar-alt"></i> Lịch: {new Date(item.date).toLocaleDateString()}</p>
                    <p><i className="fas fa-sync-alt"></i> Tần suất: {item.frequencyId?.frequency}</p>
                    <p><i className="fas fa-tag"></i> Loại bảo trì: {item.typeId?.type}</p>
                  </div>
                ) : null
              )
            ) : (
              <p>Không có lịch bảo trì định kỳ.</p>
            )}
          </div>
        )}

        {activeTab === "issue" && (
          <div className="tab-content">
            <h3><i className="fas fa-exclamation-triangle"></i> Thông tin Sự cố</h3>
            {issue.length > 0 ? (
              issue.map((item) => (
                <div key={item.id} className="issue-item">
                  <p><i className="far fa-calendar-alt"></i> Ngày: {new Date(item.date).toLocaleDateString()}</p>
                  <p><i className="fas fa-file-alt"></i> Mô tả: {item.des}</p>
                  <p><i className="fas fa-level-up-alt"></i> Mức độ: <span className={`level-${item.levelId?.level?.toLowerCase()}`}>{item.levelId?.level}</span></p>
                  <p><i className="fas fa-check-circle"></i> Trạng thái: <span className={item.isResolved ? "resolved" : "unresolved"}>{item.isResolved ? "Đã xử lý" : "Chưa xử lý"}</span></p>
                </div>
              ))
            ) : (
              <p>Không có sự cố nào được ghi nhận.</p>
            )}
          </div>
        )}

        {activeTab === "repair" && (
          <div className="tab-content">
            <h3><i className="fas fa-tools"></i> Lịch sử Sửa chữa</h3>
            {repair.length > 0 ? (
              repair.map((item) => (
                <div key={item.id} className="repair-item">
                  <p><i className="far fa-calendar-alt"></i> Ngày sửa: {new Date(item.date).toLocaleDateString()}</p>
                  <p><i className="fas fa-desktop"></i> Thiết bị: {item.deviceId?.name}</p>
                  <p><i className="fas fa-wrench"></i> Loại sửa chữa: {item.typeId?.type}</p>
                  <p><i className="fas fa-dollar-sign"></i> Chi phí: {item.cost?.toLocaleString()} VND</p>
                  <p><i className="fas fa-user"></i> Người thực hiện: {item.accountId?.name}</p>
                </div>
              ))
            ) : (
              <p>Chưa có lịch sử sửa chữa.</p>
            )}
          </div>
        )}

        {!activeTab && <p className="empty-message">Vui lòng chọn một mục để xem thông tin.</p>}
      </div>
    </div>
  );
};

export default Device;