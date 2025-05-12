import React, { useState } from 'react';

const containerStyle = {
  maxWidth: '100%',
  margin: '20px',
  padding: '15px',
  boxShadow: '0 2px 8px rgba(0,0,0,0.1)',
  borderRadius: '8px',
  fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
  backgroundColor: '#fff',
  minHeight: 'calc(100vh - 40px)',
};

const inputStyle = {
  width: '100%',
  padding: '8px',
  marginBottom: '8px',
  borderRadius: '4px',
  border: '1px solid #ccc',
  boxSizing: 'border-box',
};

const buttonStyle = {
  backgroundColor: '#1976d2',
  color: 'white',
  padding: '10px 15px',
  border: 'none',
  borderRadius: '5px',
  cursor: 'pointer',
  fontWeight: 'bold',
  width: '100%',
};

const tableStyle = {
  width: '100%',
  borderCollapse: 'collapse',
};

const DeviceManagement = () => {
  const [devices, setDevices] = useState([]);
  const [newDevice, setNewDevice] = useState({
    name: '',
    code: '',
    type: '',
    manufacturer: '',
    purchaseDate: '',
    status: '',
  });

  const handleChange = (e) => {
    setNewDevice({ ...newDevice, [e.target.name]: e.target.value });
  };

  const addDevice = () => {
    if (!newDevice.name || !newDevice.code) {
      alert('Vui lòng nhập Tên và Mã thiết bị');
      return;
    }
    setDevices([...devices, newDevice]);
    setNewDevice({
      name: '',
      code: '',
      type: '',
      manufacturer: '',
      purchaseDate: '',
      status: '',
    });
  };

  return (
    <div style={containerStyle}>
      <h2 style={{ color: '#1976d2' }}>Quản Lý Thiết Bị</h2>
      <div style={{ marginBottom: '10px' }}>
        <input
          type="text"
          name="name"
          placeholder="Tên thiết bị"
          value={newDevice.name}
          onChange={handleChange}
          style={inputStyle}
        />
        <input
          type="text"
          name="code"
          placeholder="Mã thiết bị"
          value={newDevice.code}
          onChange={handleChange}
          style={inputStyle}
        />
        <input
          type="text"
          name="type"
          placeholder="Loại thiết bị"
          value={newDevice.type}
          onChange={handleChange}
          style={inputStyle}
        />
        <input
          type="text"
          name="manufacturer"
          placeholder="Nhà sản xuất"
          value={newDevice.manufacturer}
          onChange={handleChange}
          style={inputStyle}
        />
        <input
          type="date"
          name="purchaseDate"
          placeholder="Ngày mua"
          value={newDevice.purchaseDate}
          onChange={handleChange}
          style={inputStyle}
        />
        <input
          type="text"
          name="status"
          placeholder="Trạng thái"
          value={newDevice.status}
          onChange={handleChange}
          style={inputStyle}
        />
      </div>
      <button onClick={addDevice} style={buttonStyle}>
        Thêm thiết bị
      </button>
      <hr />
      <h3>Danh sách thiết bị</h3>
      {devices.length === 0 ? (
        <p>Chưa có thiết bị nào.</p>
      ) : (
        <table style={tableStyle}>
          <thead>
            <tr>
              <th>Tên</th>
              <th>Mã</th>
              <th>Loại</th>
              <th>Nhà sản xuất</th>
              <th>Ngày mua</th>
              <th>Trạng thái</th>
            </tr>
          </thead>
          <tbody>
            {devices.map((device, index) => (
              <tr key={index}>
                <td>{device.name}</td>
                <td>{device.code}</td>
                <td>{device.type}</td>
                <td>{device.manufacturer}</td>
                <td>{device.purchaseDate}</td>
                <td>{device.status}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default DeviceManagement;
