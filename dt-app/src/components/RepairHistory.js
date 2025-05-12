import React, { useState } from 'react';

const RepairHistory = () => {
    const [repairs, setRepairs] = useState([
        { id: 1, device: "Máy in HP", date: "2025-04-15", type: "Thay mực", cost: 150000 },
        { id: 2, device: "Laptop Dell", date: "2025-03-02", type: "Thay bàn phím", cost: 700000 }
    ]);

    const [newRepair, setNewRepair] = useState({
        device: "",
        date: "",
        type: "",
        cost: ""
    });

    const handleChange = (e) => {
        setNewRepair({ ...newRepair, [e.target.name]: e.target.value });
    };

    const handleAddRepair = (e) => {
        e.preventDefault();
        if (!newRepair.device || !newRepair.date || !newRepair.type || !newRepair.cost) return;
        setRepairs([...repairs, { ...newRepair, id: repairs.length + 1 }]);
        setNewRepair({ device: "", date: "", type: "", cost: "" });
    };

    return (
        <div style={{ padding: '80px 20px 20px 20px' }}>
            <h2 style={{ color: "#1976d2" }}>Lịch Sử Sửa Chữa</h2>
            <p>- Ghi nhận lịch sử sửa chữa: Lưu thông tin về ngày, loại sửa chữa và chi phí.</p>
            <p>- Theo dõi chi phí: Hỗ trợ phân tích chi phí và hiệu quả bảo trì.</p>
            <p>- Tạo báo cáo: Xem lại lịch sử theo thiết bị hoặc nhóm thiết bị.</p>

            <table style={{ width: "100%", borderCollapse: "collapse", marginTop: 20 }}>
                <thead>
                    <tr style={{ backgroundColor: "#f0f0f0" }}>
                        <th style={{ padding: 10, border: "1px solid #ccc" }}>Thiết bị</th>
                        <th style={{ padding: 10, border: "1px solid #ccc" }}>Ngày sửa</th>
                        <th style={{ padding: 10, border: "1px solid #ccc" }}>Loại sửa</th>
                        <th style={{ padding: 10, border: "1px solid #ccc" }}>Chi phí (VND)</th>
                    </tr>
                </thead>
                <tbody>
                    {repairs.map(r => (
                        <tr key={r.id}>
                            <td style={{ padding: 10, border: "1px solid #ddd" }}>{r.device}</td>
                            <td style={{ padding: 10, border: "1px solid #ddd" }}>{r.date}</td>
                            <td style={{ padding: 10, border: "1px solid #ddd" }}>{r.type}</td>
                            <td style={{ padding: 10, border: "1px solid #ddd" }}>{r.cost.toLocaleString()}</td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <form onSubmit={handleAddRepair} style={{ marginTop: 30 }}>
                <h4>Thêm sửa chữa mới</h4>
                <input type="text" name="device" placeholder="Thiết bị" value={newRepair.device} onChange={handleChange} required />
                <input type="date" name="date" value={newRepair.date} onChange={handleChange} required />
                <input type="text" name="type" placeholder="Loại sửa chữa" value={newRepair.type} onChange={handleChange} required />
                <input type="number" name="cost" placeholder="Chi phí" value={newRepair.cost} onChange={handleChange} required />
                <button type="submit" style={{ marginLeft: 10 }}>Thêm</button>
            </form>
        </div>
    );
};

export default RepairHistory;
