import React, { useEffect, useState, useContext } from "react";
import Apis, { endpoints } from "../configs/Apis";
import styles from "./RepairHistory.module.css";
import { MyUserContext } from "../configs/MyContexts";
import { FaCalendarAlt, FaDollarSign, FaTools } from "react-icons/fa";

// Không cần import RepairAnalysisAndReporting ở đây nữa

const RepairHistory = () => {
  const [repairs, setRepairs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [repairTypes, setRepairTypes] = useState([]); // Vẫn cần repairTypes cho bộ lọc

  const user = useContext(MyUserContext);

  // States for filters (để lọc bảng chi tiết)
  const [filterStartDate, setFilterStartDate] = useState("");
  const [filterEndDate, setFilterEndDate] = useState("");
  const [filterRepairType, setFilterRepairType] = useState("");
  const [filterMinCost, setFilterMinCost] = useState("");
  const [filterMaxCost, setFilterMaxCost] = useState("");

  useEffect(() => {
    const loadData = async () => {
      setLoading(true);
      setError(null);
      try {
        const [repairsRes, repairTypesRes] = await Promise.all([
          Apis.get(endpoints.repairs),
          Apis.get(endpoints.repairTypes),
        ]);

        const filteredRepairsByBaseId =
          user.role === "ROLE_ADMIN"
            ? repairsRes.data
            : repairsRes.data.filter(
                (repair) => repair.deviceId?.baseId.id === user.baseId.id
              );

        setRepairs(filteredRepairsByBaseId);
        setRepairTypes(repairTypesRes.data);
      } catch (err) {
        console.error(err);
        setError("Lỗi khi tải dữ liệu lịch sử sửa chữa!");
      } finally {
        setLoading(false);
      }
    };

    if (user) {
        loadData();
    }
  }, [user]);

  // Filtered repairs based on user input for the detailed table
  const filteredRepairs = Array.isArray(repairs) ? repairs.filter((r) => { // Thêm kiểm tra Array.isArray
    const repairDateTime = new Date(r.date).getTime();

    const startDateTimestamp = filterStartDate ? new Date(filterStartDate).getTime() : 0;
    const endDateTimestamp = filterEndDate ? new Date(filterEndDate).setHours(23, 59, 59, 999) : Infinity;

    const matchesDate =
      (filterStartDate === "" || repairDateTime >= startDateTimestamp) &&
      (filterEndDate === "" || repairDateTime <= endDateTimestamp);

    const matchesType = filterRepairType ? String(r.typeId?.id) === filterRepairType : true;
    const matchesMinCost = filterMinCost ? parseFloat(r.cost) >= parseFloat(filterMinCost) : true;
    const matchesMaxCost = filterMaxCost ? parseFloat(r.cost) <= parseFloat(filterMaxCost) : true;

    return matchesDate && matchesType && matchesMinCost && matchesMaxCost;
  }) : [];


  if (loading) {
    return <div>Đang tải lịch sử sửa chữa...</div>;
  }

  if (error) {
    return <div>Lỗi: {error}</div>;
  }

  return (
    <div className={styles.container}>
      <h2 className={styles.heading}>Lịch Sử Sửa Chữa</h2>

      {/* KHÔNG CẦN COMPONENT PHÂN TÍCH VÀ BÁO CÁO Ở ĐÂY NỮA */}

      <h3 className={styles.sectionTitle}>Lịch Sử Sửa Chữa Chi Tiết</h3>

      {/* Filter controls for the detailed table */}
      <div className={styles.filtersSection}>
        <div className={styles.filterGroup}>
          <label htmlFor="filterStartDate" className={styles.filterLabel}>
            <FaCalendarAlt /> Từ ngày:
          </label>
          <input
            type="date"
            id="filterStartDate"
            value={filterStartDate}
            onChange={(e) => setFilterStartDate(e.target.value)}
            className={styles.filterInput}
          />
        </div>
        <div className={styles.filterGroup}>
          <label htmlFor="filterEndDate" className={styles.filterLabel}>
            <FaCalendarAlt /> Đến ngày:
          </label>
          <input
            type="date"
            id="filterEndDate"
            value={filterEndDate}
            onChange={(e) => setFilterEndDate(e.target.value)}
            className={styles.filterInput}
          />
        </div>
        <div className={styles.filterGroup}>
          <label htmlFor="filterRepairType" className={styles.filterLabel}>
            <FaTools /> Loại sửa chữa:
          </label>
          <select
            id="filterRepairType"
            value={filterRepairType}
            onChange={(e) => setFilterRepairType(e.target.value)}
            className={styles.filterSelect}
          >
            <option value="">Tất cả</option>
            {Array.isArray(repairTypes) && repairTypes.map((t) => ( // Thêm kiểm tra Array.isArray
              <option key={t.id} value={t.id}>
                {t.type}
              </option>
            ))}
          </select>
        </div>
        <div className={styles.filterGroup}>
          <label htmlFor="filterMinCost" className={styles.filterLabel}>
            <FaDollarSign /> Chi phí từ:
          </label>
          <input
            type="number"
            id="filterMinCost"
            value={filterMinCost}
            onChange={(e) => setFilterMinCost(e.target.value)}
            className={styles.filterInput}
            placeholder="Min chi phí"
          />
        </div>
        <div className={styles.filterGroup}>
          <label htmlFor="filterMaxCost" className={styles.filterLabel}>
            <FaDollarSign /> Chi phí đến:
          </label>
          <input
            type="number"
            id="filterMaxCost"
            value={filterMaxCost}
            onChange={(e) => setFilterMaxCost(e.target.value)}
            className={styles.filterInput}
            placeholder="Max chi phí"
          />
        </div>
      </div>

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
          {filteredRepairs.length > 0 ? (
            filteredRepairs.map((r) => (
              <tr key={r.id}>
                <td>{r.deviceId?.name}</td>
                <td>{new Date(r.date).toLocaleDateString("vi-VN")}</td>
                <td>{r.typeId?.type}</td>
                <td>{r.cost ? r.cost.toLocaleString() : ""}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="4" className={styles.noDataFound}>
                Không tìm thấy lịch sử sửa chữa phù hợp.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default RepairHistory;