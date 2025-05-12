import React, { useState } from 'react';

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
  const [formData, setFormData] = useState({
    device: '',
    description: '',
    severity: 'Thấp',
    date: '',
    status: 'Chưa xử lý',
  });

  // Xử lý thay đổi input
  const handleChange = (e) => {
    setFormData({...formData, [e.target.name]: e.target.value});
  };

  // Thêm sự cố mới
  const handleAddIncident = () => {
    if (!formData.device || !formData.description || !formData.date) {
      alert('Vui lòng nhập thiết bị, mô tả và ngày xảy ra sự cố');
      return;
    }
    setIncidents([...incidents, formData]);
    setFormData({
      device: '',
      description: '',
      severity: 'Thấp',
      date: '',
      status: 'Chưa xử lý',
    });
  };

  // Cập nhật trạng thái sự cố
  const updateStatus = (index, newStatus) => {
    const updated = [...incidents];
    updated[index].status = newStatus;
    setIncidents(updated);
  };

  return (
    <div style={containerStyle}>
      <h2 style={{color: '#1976d2'}}>Quản Lý Sự Cố</h2>

      <div>
        <label>Thiết bị:</label>
        <input
          type="text"
          name="device"
          value={formData.device}
          onChange={handleChange}
          style={inputStyle}
          placeholder="Nhập tên hoặc mã thiết bị"
        />

        <label>Mô tả sự cố:</label>
        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
          style={{...inputStyle, height: '80px'}}
          placeholder="Nhập mô tả sự cố"
        />

        <label>Mức độ nghiêm trọng:</label>
        <select name="severity" value={formData.severity} onChange={handleChange} style={selectStyle}>
          <option>Thấp</option>
          <option>Trung bình</option>
          <option>Cao</option>
        </select>

        <label>Thời gian xảy ra:</label>
        <input type="date" name="date" value={formData.date} onChange={handleChange} style={inputStyle} />

        <button onClick={handleAddIncident} style={buttonStyle}>Ghi nhận sự cố</button>
      </div>

      {incidents.length === 0 ? (
        <p>Chưa có sự cố nào được ghi nhận.</p>
      ) : (
        <table style={tableStyle}>
          <thead>
            <tr>
              <th style={thTdStyle}>Thiết bị</th>
              <th style={thTdStyle}>Mô tả</th>
              <th style={thTdStyle}>Nghiêm trọng</th>
              <th style={thTdStyle}>Thời gian</th>
              <th style={thTdStyle}>Trạng thái</th>
              <th style={thTdStyle}>Cập nhật trạng thái</th>
            </tr>
          </thead>
          <tbody>
            {incidents.map((incident, idx) => (
              <tr key={idx}>
                <td style={thTdStyle}>{incident.device}</td>
                <td style={thTdStyle}>{incident.description}</td>
                <td style={thTdStyle}>{incident.severity}</td>
                <td style={thTdStyle}>{incident.date}</td>
                <td style={thTdStyle}>{incident.status}</td>
                <td style={thTdStyle}>
                  <select 
                    value={incident.status} 
                    onChange={(e) => updateStatus(idx, e.target.value)}
                  >
                    <option>Chưa xử lý</option>
                    <option>Đang xử lý</option>
                    <option>Đã xử lý</option>
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

