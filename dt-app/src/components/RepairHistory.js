import React, { useEffect, useState } from "react";
import Apis, { endpoints } from "../configs/Apis";
import * as pdfMake from "pdfmake/build/pdfmake";
import * as pdfFonts from "pdfmake/build/vfs_fonts";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  Legend,
  ResponsiveContainer,
} from "recharts";
import styles from "./RepairHistory.module.css"; // Import CSS Module
import { useContext } from "react";
import { DeviceContext, MyUserContext } from "../configs/MyContexts";

const RepairHistory = () => {
  pdfMake.vfs = pdfFonts.vfs;
  const [repairs, setRepairs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [newRepair, setNewRepair] = useState({
    deviceId: "",
    date: "",
    typeId: "",
    cost: "",
  });
  // const [devices, setDevices] = useState([]);
  const { devices } = useContext(DeviceContext);
  const [repairTypes, setRepairTypes] = useState([]);
  const [selectedDevices, setSelectedDevices] = useState([]);

  const [reportData, setReportData] = useState([]);
  const [costAnalysis, setCostAnalysis] = useState({});
  const user = useContext(MyUserContext);
  useEffect(() => {
    const loadData = async () => {
      setLoading(true);
      setError(null);
      try {
        const [repairsRes, repairTypesRes] = await Promise.all([
          Apis.get(endpoints.repairs),
          // Apis.get(endpoints.devices),
          Apis.get(endpoints.repairTypes),
        ]);
        // Lọc dữ liệu sửa chữa theo baseId nếu không phải admin
        const filteredRepairs =
          user.role === "ROLE_ADMIN"
            ? repairsRes.data
            : repairsRes.data.filter(
                (repair) => repair.deviceId?.baseId.id === user.baseId.id
              );

        // Lọc devices nếu không phải admin
        // const filteredDevices =
        //   user.role === "ROLE_ADMIN"
        //     ? devicesRes.data
        //     : devicesRes.data.filter(
        //         (device) => device.baseId?.id === user.baseId?.id
        //       );
        setRepairs(filteredRepairs);
        // setDevices(filteredDevices);
        setRepairTypes(repairTypesRes.data);
      } catch (err) {
        console.error(err);
        setError("Lỗi khi tải dữ liệu lịch sử sửa chữa!");
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, [user.baseId.id, user.role]);

  useEffect(() => {
    const analyzeCost = () => {
      const analysis = {};
      repairs.forEach((repair) => {
        const deviceName =
          repair.deviceId?.name || `Thiết bị ${repair.deviceId?.id}`;
        if (!analysis[deviceName]) {
          analysis[deviceName] = { totalCost: 0, count: 0 };
        }
        analysis[deviceName].totalCost += parseFloat(repair.cost || 0);
        analysis[deviceName].count += 1;
      });

      Object.keys(analysis).forEach((device) => {
        const item = analysis[device];
        item.avgCost = item.count > 0 ? item.totalCost / item.count : 0;
      });

      setCostAnalysis(analysis);
    };

    analyzeCost();
  }, [repairs]);

  const handleChange = (e) => {
    setNewRepair({ ...newRepair, [e.target.name]: e.target.value });
  };

  const handleAddRepair = async (e) => {
    e.preventDefault();
    try {
      const res = await Apis.post(endpoints.repairs, newRepair);
      setRepairs([...repairs, res.data]);
      setNewRepair({ deviceId: "", date: "", typeId: "", cost: "" });
      alert("Thêm sửa chữa mới thành công!");
    } catch (err) {
      console.error(err);
      alert("Lỗi khi thêm lịch sử sửa chữa!");
    }
  };

  const handleGenerateReport = () => {
    const filteredData =
      selectedDevices.length > 0
        ? repairs.filter((r) =>
            selectedDevices.includes(String(r.deviceId?.id))
          )
        : repairs;

    setReportData(filteredData);
  };

  const handleExportPdf = () => {
    const filteredData =
      selectedDevices.length > 0
        ? repairs.filter((r) =>
            selectedDevices.includes(String(r.deviceId?.id))
          )
        : repairs;

    const headers = ["Thiết bị", "Ngày sửa", "Loại sửa", "Chi phí (VND)"];

    const body = [
      headers,
      ...filteredData.map((r) => [
        r.deviceId?.name || "",
        new Date(r.date).toLocaleDateString("vi-VN"),
        r.typeId?.type || "",
        r.cost ? r.cost.toLocaleString("vi-VN") : "",
      ]),
    ];

    const docDefinition = {
      content: [
        { text: "BÁO CÁO LỊCH SỬ SỬA CHỮA", style: "header" },
        {
          text: `Ngày xuất báo cáo: ${new Date().toLocaleDateString("vi-VN")}`,
          margin: [0, 0, 0, 10],
        },
        {
          table: {
            headerRows: 1,
            widths: ["*", "auto", "auto", "auto"],
            body: body,
          },
        },
      ],
      styles: {
        header: {
          fontSize: 16,
          bold: true,
          alignment: "center",
          margin: [0, 0, 0, 10],
        },
      },
      defaultStyle: {
        font: "Roboto",
      },
    };

    pdfMake
      .createPdf(docDefinition)
      .download(
        `bao_cao_sua_chua_${new Date().toLocaleDateString("vi-VN")}.pdf`
      );
  };

  const handleDevicesChange = (e) => {
    const options = e.target.options;
    const selected = [];
    for (let i = 0; i < options.length; i++) {
      if (options[i].selected) {
        selected.push(options[i].value);
      }
    }
    setSelectedDevices(selected);
  };

  if (loading) {
    return <div>Đang tải lịch sử sửa chữa...</div>;
  }

  if (error) {
    return <div>Lỗi: {error}</div>;
  }

  const chartData = Object.entries(costAnalysis).map(([device, data]) => ({
    name: device,
    total: data.totalCost,
    avg: data.avgCost,
  }));

  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>Lịch Sử Sửa Chữa</h2>

      <div className={styles.chartContainer}>
        <h3 className={styles.sectionTitle}>
          Biểu đồ chi phí bảo trì theo thiết bị
        </h3>
        <ResponsiveContainer width="100%" height={300}>
          <BarChart
            data={chartData}
            margin={{ top: 20, right: 30, left: 20, bottom: 5 }}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="name" tick={{ fontSize: 12 }} />
            <YAxis tickFormatter={(value) => value.toLocaleString()} />
            <Tooltip formatter={(value) => `${value.toLocaleString()} VND`} />
            <Legend />
            <Bar dataKey="total" fill="#42a5f5" name="Tổng chi phí" />
            <Bar dataKey="avg" fill="#66bb6a" name="Chi phí TB/lần" />
          </BarChart>
        </ResponsiveContainer>
      </div>

      <div className={styles.sectionTitle}>Theo dõi chi phí</div>
      <table className={styles.table}>
        <thead>
          <tr>
            <th>Thiết bị</th>
            <th>Số lần sửa chữa</th>
            <th>Tổng chi phí (VND)</th>
            <th>Chi phí trung bình (VND)</th>
          </tr>
        </thead>
        <tbody>
          {Object.entries(costAnalysis).map(([device, data]) => (
            <tr key={device}>
              <td>{device}</td>
              <td>{data.count}</td>
              <td>{data.totalCost.toLocaleString()}</td>
              <td>{data.avgCost.toFixed(0).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className={styles.reportContainer}>
        <h3 className={styles.reportTitle}>Tạo báo cáo</h3>
        <div className={styles.filterContainer}>
          <label htmlFor="devices" className={styles.filterLabel}>
            Chọn thiết bị:
          </label>
          <select
            multiple
            id="devices"
            value={selectedDevices}
            onChange={handleDevicesChange}
            className={styles.filterSelect}
          >
            {devices.map((d) => (
              <option key={d.id} value={d.id}>
                {d.name || `Thiết bị ${d.id}`}
              </option>
            ))}
          </select>
          <button onClick={handleGenerateReport} className={styles.button}>
            Xem Báo Cáo
          </button>
          <button onClick={handleExportPdf} className={styles.button}>
            Xuất PDF
          </button>
        </div>

        {reportData.length > 0 && (
          <div className={styles.reportTableContainer}>
            <h4 className={styles.reportTitle}>Báo Cáo Sửa Chữa</h4>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>Thiết bị</th>
                  <th>Ngày sửa</th>
                  <th>Loại sửa</th>
                  <th>Chi phí (VND)</th>
                </tr>
              </thead>
              <tbody>
                {reportData.map((r) => (
                  <tr key={r.id}>
                    <td>{r.deviceId?.name}</td>
                    <td>{new Date(r.date).toLocaleDateString("vi-VN")}</td>
                    <td>{r.typeId?.type}</td>
                    <td>{r.cost ? r.cost.toLocaleString() : ""}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            {reportData.length === 0 && (
              <p className={styles.infoText}>
                Không có dữ liệu sửa chữa cho thiết bị này.
              </p>
            )}
          </div>
        )}
      </div>

      <h3 className={styles.sectionTitle}>Lịch Sử Sửa Chữa Chi Tiết</h3>
      <table className={styles.table}>
        <thead>
          <tr>
            <th>Thiết bị</th>
            <th>Ngày sửa</th>
            <th>Loại sửa</th>
            <th>Chi phí (VND)</th>
          </tr>
        </thead>
        <tbody>
          {repairs.map((r) => (
            <tr key={r.id}>
              <td>{r.deviceId?.name}</td>
              <td>{new Date(r.date).toLocaleDateString("vi-VN")}</td>
              <td>{r.typeId?.type}</td>
              <td>{r.cost ? r.cost.toLocaleString() : ""}</td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className={styles.formContainer}>
        <h3 className={styles.formTitle}>Thêm sửa chữa mới</h3>
        <form onSubmit={handleAddRepair}>
          <div className={styles.formGroup}>
            <label htmlFor="deviceId" className={styles.formLabel}>
              Thiết bị:
            </label>
            <select
              id="deviceId"
              name="deviceId"
              value={newRepair.deviceId}
              onChange={handleChange}
              className={styles.formSelect}
              required
            >
              <option value="">Chọn thiết bị</option>
              {devices.map((d) => (
                <option key={d.id} value={d.id}>
                  {d.name || `Thiết bị ${d.id}`}
                </option>
              ))}
            </select>
          </div>
          <div className={styles.formGroup}>
            <label htmlFor="date" className={styles.formLabel}>
              Ngày sửa:
            </label>
            <input
              type="date"
              id="date"
              name="date"
              value={newRepair.date}
              onChange={handleChange}
              className={styles.formInput}
              required
            />
          </div>
          <div className={styles.formGroup}>
            <label htmlFor="typeId" className={styles.formLabel}>
              Loại sửa chữa:
            </label>
            <select
              id="typeId"
              name="typeId"
              value={newRepair.typeId}
              onChange={handleChange}
              className={styles.formSelect}
              required
            >
              <option value="">Chọn loại sửa chữa</option>
              {repairTypes.map((t) => (
                <option key={t.id} value={t.id}>
                  {t.type || `Loại ${t.id}`}
                </option>
              ))}
            </select>
          </div>
          <div className={styles.formGroup}>
            <label htmlFor="cost" className={styles.formLabel}>
              Chi phí:
            </label>
            <input
              type="number"
              id="cost"
              name="cost"
              placeholder="Chi phí"
              value={newRepair.cost}
              onChange={handleChange}
              className={styles.formInput}
              required
            />
          </div>
          <button
            type="submit"
            className={`${styles.button} ${styles.addButton}`}
          >
            Thêm
          </button>
        </form>
      </div>
    </div>
  );
};

export default RepairHistory;
