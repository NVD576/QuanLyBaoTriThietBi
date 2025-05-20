// src/pages/CostAnalyticsAndReportingPage.js
import React, { useState, useEffect, useContext } from 'react'; // Import useContext
import Apis, { endpoints } from '../configs/Apis'; // Import Apis và endpoints
import { MyUserContext } from '../configs/MyContexts'; // Import MyUserContext
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
import * as pdfMake from "pdfmake/build/pdfmake";
import * as pdfFonts from "pdfmake/build/vfs_fonts";
import styles from "../components/RepairHistory.module.css"; // Vẫn sử dụng CSS module chung

// Khởi tạo fonts cho pdfMake một lần duy nhất
pdfMake.vfs = pdfFonts.vfs;

const CostAnalyticsAndReportingPage = () => {
  const [repairs, setRepairs] = useState([]);
  const [devices, setDevices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const [costAnalysis, setCostAnalysis] = useState({});
  const [selectedDevices, setSelectedDevices] = useState([]);
  const [reportData, setReportData] = useState([]);

  const user = useContext(MyUserContext); // Sử dụng context để lấy thông tin user

  // === LOGIC TẢI DỮ LIỆU RIÊNG CHO TRANG NÀY ===
  useEffect(() => {
    const loadData = async () => {
      setLoading(true);
      setError(null);
      try {
        const [repairsRes, devicesRes] = await Promise.all([
          Apis.get(endpoints.repairs),
          Apis.get(endpoints.devices),
        ]);

        let filteredRepairsByBaseId = repairsRes.data;
        let filteredDevicesByBaseId = devicesRes.data;

        if (user && user.role !== "ROLE_ADMIN" && user.baseId) {
          filteredRepairsByBaseId = repairsRes.data.filter(
            (repair) => repair.deviceId?.baseId?.id === user.baseId.id
          );
          filteredDevicesByBaseId = devicesRes.data.filter(
            (device) => device.baseId?.id === user.baseId.id
          );
        }

        setRepairs(filteredRepairsByBaseId);
        setDevices(filteredDevicesByBaseId);
      } catch (err) {
        console.error("Lỗi khi tải dữ liệu cho trang phân tích:", err);
        setError("Lỗi khi tải dữ liệu phân tích và báo cáo!");
      } finally {
        setLoading(false);
      }
    };

    if (user) { // Chỉ tải dữ liệu khi user context đã sẵn sàng
      loadData();
    }
  }, [user]); // Chạy lại khi thông tin user thay đổi


  // Logic phân tích chi phí
  useEffect(() => {
    const safeRepairs = Array.isArray(repairs) ? repairs : [];
    if (safeRepairs.length > 0) {
      const analysis = {};
      safeRepairs.forEach((repair) => {
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
    } else {
      setCostAnalysis({});
    }
  }, [repairs]);

  // Logic tạo báo cáo PDF và hiển thị báo cáo
  const handleDevicesChange = (e) => {
    const options = e.target.options;
    const selected = [];
    if (options) {
      for (let i = 0; i < options.length; i++) {
        if (options[i].selected) {
          selected.push(options[i].value);
        }
      }
    }
    setSelectedDevices(selected);
  };

  const handleGenerateReport = () => {
    const safeRepairs = Array.isArray(repairs) ? repairs : [];
    const filteredData =
      selectedDevices.length > 0
        ? safeRepairs.filter((r) => selectedDevices.includes(String(r.deviceId?.id)))
        : safeRepairs;
    setReportData(filteredData);
  };

  const handleExportPdf = () => {
    const safeRepairs = Array.isArray(repairs) ? repairs : [];
    const filteredData =
      selectedDevices.length > 0
        ? safeRepairs.filter((r) => selectedDevices.includes(String(r.deviceId?.id)))
        : safeRepairs;

    if (filteredData.length === 0) {
        alert("Không có dữ liệu để xuất báo cáo PDF.");
        return;
    }

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
    const totalCost = filteredData.reduce(
      (sum, r) => sum + parseFloat(r.cost || 0),
      0
    );

    body.push([
      { text: "Tổng chi phí", colSpan: 3, alignment: "right", bold: true },
      {},
      {},
      { text: totalCost.toLocaleString("vi-VN"), bold: true },
    ]);
    const docDefinition = {
      content: [
        { text: "BÁO CÁO PHÂN TÍCH CHI PHÍ VÀ SỬA CHỮA", style: "header" },
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
        `bao_cao_phan_tich_chi_phi_${new Date().toLocaleDateString("vi-VN")}.pdf`
      );
  };

  const chartData = Object.entries(costAnalysis || {}).map(([device, data]) => ({
    name: device,
    total: data.totalCost,
    avg: data.avgCost,
  }));

  if (loading) {
    return <div className={styles.container}><p className={styles.infoText}>Đang tải dữ liệu phân tích và báo cáo...</p></div>;
  }

  if (error) {
    return <div className={styles.container}><p className={styles.errorText}>Lỗi: {error}</p></div>;
  }

  if (!Array.isArray(repairs) || repairs.length === 0) {
      return (
        <div className={styles.container}>
            <h2 className={styles.heading}>Phân tích và Báo cáo Chi phí</h2>
            <p className={styles.infoText}>Không có dữ liệu sửa chữa để phân tích hoặc tạo báo cáo.</p>
        </div>
      );
  }

  return (
    <div className={styles.container}> {/* Đảm bảo có container cho trang */}
      <h2 className={styles.heading}>Phân tích và Báo cáo Chi phí</h2>

      {/* Biểu đồ chi phí bảo trì theo thiết bị */}
      <div className={styles.chartContainer}>
        <h3 className={styles.sectionTitle}>
          Biểu đồ chi phí bảo trì theo thiết bị
        </h3>
        {chartData.length > 0 ? (
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
        ) : (
          <p className={styles.noDataFound}>Không có dữ liệu chi phí để hiển thị biểu đồ.</p>
        )}
      </div>

      {/* Theo dõi chi phí */}
      <h3 className={styles.sectionTitle}>Theo dõi chi phí</h3>
      {Object.keys(costAnalysis || {}).length > 0 ? (
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
      ) : (
        <p className={styles.noDataFound}>Không có dữ liệu chi phí để theo dõi.</p>
      )}


      {/* Tạo báo cáo */}
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
            {Array.isArray(devices) && devices.length > 0 ? (
              devices.map((d) => (
                <option key={d.id} value={d.id}>
                  {d.name || `Thiết bị ${d.id}`}
                </option>
              ))
            ) : (
              <option disabled>Không có thiết bị để chọn</option>
            )}
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
                {Array.isArray(reportData) && reportData.map((r) => (
                  <tr key={r.id}>
                    <td>{r.deviceId?.name}</td>
                    <td>{new Date(r.date).toLocaleDateString("vi-VN")}</td>
                    <td>{r.typeId?.type}</td>
                    <td>{r.cost ? r.cost.toLocaleString() : ""}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
        {reportData.length === 0 && selectedDevices.length > 0 && (
            <p className={styles.infoText}>
                Không có dữ liệu sửa chữa cho thiết bị đã chọn.
            </p>
        )}
        {reportData.length === 0 && selectedDevices.length === 0 && (
            <p className={styles.infoText}>
                Hãy chọn thiết bị và nhấn "Xem Báo Cáo".
            </p>
        )}
      </div>
    </div>
  );
};

export default CostAnalyticsAndReportingPage;