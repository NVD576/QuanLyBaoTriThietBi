import React, { use, useEffect, useState } from 'react';
import Apis, { endpoints } from '../configs/Apis';

const containerStyle = {
  maxWidth: '700px',
  margin: '20px auto',
  padding: '15px',
  boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
  borderRadius: '8px',
  backgroundColor: '#fff',
  fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
};

const inputStyle = {
  width: '100%',
  padding: '8px',
  marginBottom: '12px',
  borderRadius: '4px',
  border: '1px solid #ccc',
  boxSizing: 'border-box',
};

const selectStyle = {
  ...inputStyle,
};

const buttonStyle = {
  backgroundColor: '#1976d2',
  color: 'white',
  padding: '10px 15px',
  border: 'none',
  borderRadius: '5px',
  cursor: 'pointer',
  fontWeight: 'bold',
  marginBottom: '20px',
};

const tableStyle = {
  width: '100%',
  borderCollapse: 'collapse',
};

const thTdStyle = {
  border: '1px solid #ccc',
  textAlign: 'left',
  padding: '8px',
};

const IncidentManagement = () => {
  const [incidents, setIncidents] = useState([]);
  const [issues, setIssues] = useState([]);
  const [levels, setLevels] = useState([]);
  const [devices, setDevices] = useState([]);
  const [formData, setFormData] = useState({
    deviceId: '',
    des: '',
    levelId: '',
    date: '',
    isResolved: 'false',
  });

  const handleChange = (e) => {
    setFormData({...formData, [e.target.name]: e.target.value});
  };
  useEffect(() => {
    loadIssues();
    loadLevels();
    loadDevices();
  }, []);
  const loadIssues = async () => {
    try {
      const response = await Apis.get(endpoints.issues);
      setIssues(response.data);
    } catch (err) {
      console.error(err);
      alert('Lỗi khi tải dữ liệu!');
    }
  };
  const loadLevels = async () => {
    try {
      const response = await Apis.get(endpoints.levels);
      setLevels(response.data);
    } catch (err) {
      console.error(err);
      alert('Lỗi khi tải dữ liệu!');
    }
  };
  const loadDevices = async () => {
    try {
      const response = await Apis.get(endpoints.devices);
      setDevices(response.data);
    } catch (err) {
      console.error(err);
      alert('Lỗi khi tải dữ liệu!');
    }
  };

const handleAddIncident = async () => {
  try {
    const response = await Apis.post(endpoints.issue, formData);
    setIncidents([...incidents, response.data]);
    setFormData({ deviceId: '', des: '', levelId: '', date: '', isResolved: 'false' });
    alert('Ghi nhận sự cố thành công!');
  } catch (err) {
    console.error(err);
    alert('Lỗi khi gửi dữ liệu!');
  }
};


  const updateStatus = (index, newStatus) => {
    const updated = [...incidents];
    updated[index].isResolved = newStatus;
    setIncidents(updated);
  };

  return (
    <div style={containerStyle}>
      <h2 style={{ color: '#1976d2' }}>Quản Lý Sự Cố</h2>

      <div>
        <label>Thiết bị:</label>
        <select
          name="deviceId"
          value={formData.deviceId}
          onChange={handleChange}
          style={selectStyle}
        >
          <option value="">Chọn thiết bị</option>
          {devices.map((d) => (
            <option key={d.id} value={d.id}>
              {d.name || `Thiết bị ${d.id}`}
            </option>
          ))}
        </select>

        <label>Mô tả sự cố:</label>
        <textarea
          name="des"
          value={formData.des}
          onChange={handleChange}
          style={{ ...inputStyle, height: '80px' }}
          placeholder="Nhập mô tả sự cố"
        />

        <label>Mức độ nghiêm trọng:</label>
        <select
          name="levelId"
          value={formData.levelId}
          onChange={handleChange}
          style={selectStyle}
        >
          <option value="">Chọn mức độ</option>
          {levels.map((lvl) => (
            <option key={lvl.id} value={lvl.id}>
              {lvl.name || `Mức độ ${lvl.id}`}
            </option>
          ))}
        </select>

        <label>Thời gian xảy ra:</label>
        <input
          type="date"
          name="date"
          value={formData.date}
          onChange={handleChange}
          style={inputStyle}
        />

        <button onClick={handleAddIncident} style={buttonStyle}>
          Ghi nhận sự cố
        </button>
      </div>

      {incidents.length === 0 ? (
        <p>Chưa có sự cố nào được ghi nhận.</p>
      ) : (
        <table style={tableStyle}>
          <thead>
            <tr>
              <th style={thTdStyle}>Thiết bị ID</th>
              <th style={thTdStyle}>Mô tả</th>
              <th style={thTdStyle}>Mức độ</th>
              <th style={thTdStyle}>Ngày</th>
              <th style={thTdStyle}>Trạng thái</th>
              <th style={thTdStyle}>Cập nhật</th>
            </tr>
          </thead>
          <tbody>
            {incidents.map((incident, idx) => (
              <tr key={idx}>
                <td style={thTdStyle}>{incident.deviceId}</td>
                <td style={thTdStyle}>{incident.des}</td>
                <td style={thTdStyle}>{incident.levelId}</td>
                <td style={thTdStyle}>{incident.date}</td>
                <td style={thTdStyle}>
                  {incident.isResolved ? 'Đã xử lý' : 'Chưa xử lý'}
                </td>
                <td style={thTdStyle}>
                  <select
                    value={incident.isResolved ? 'true' : 'false'}
                    onChange={(e) => updateStatus(idx, e.target.value)}
                  >
                    <option value="false">Chưa xử lý</option>
                    <option value="true">Đã xử lý</option>
                  </select>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default IncidentManagement;