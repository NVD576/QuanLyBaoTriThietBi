package com.nvd.controllers;

import com.nvd.pojo.Device;
import com.nvd.pojo.RepairType;
import com.nvd.service.CategoryService;
import com.nvd.service.DeviceService;
import com.nvd.service.RepairService;
import com.nvd.service.StatusService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import static org.apache.commons.math3.fitting.leastsquares.LeastSquaresFactory.model;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StatControllers {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RepairService repairService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StatusService statusService;

    @GetMapping("/stats")
    public String getRepairStats(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(name = "categoryId", required = false) Integer categoryId,
            @RequestParam(name = "statusId", required = false) Integer statusId,
            Model model) {

        // Xử lý ngày mặc định
        if (startDate == null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, -3);
            startDate = cal.getTime();
        }
        if (endDate == null) {
            endDate = new Date();
        }

        model.addAttribute("repairCostByDay",
                repairService.getRepairCostByTimeRange(startDate, endDate, categoryId, statusId, "day"));
        model.addAttribute("repairCostByMonth",
                repairService.getRepairCostByTimeRange(startDate, endDate, categoryId, statusId, "month"));

        // Lấy dữ liệu thống kê chính
        BigDecimal totalRepairCost = repairService.getTotalRepairCost(startDate, endDate, categoryId, statusId);
        long totalRepairs = repairService.countRepairs(startDate, endDate, categoryId, statusId);
        Device mostRepairedDevice = repairService.getMostRepairedDevice(startDate, endDate, categoryId, statusId);
        RepairType mostCommonRepairType = repairService.getMostCommonRepairType(startDate, endDate, categoryId, statusId);

        model.addAttribute("totalRepairCost", totalRepairCost);
        model.addAttribute("totalRepairs", totalRepairs);
        model.addAttribute("mostRepairedDevice", mostRepairedDevice);
        model.addAttribute("mostCommonRepairType", mostCommonRepairType);

        // Tính toán các giá trị phụ thuộc
        if (mostRepairedDevice != null) {
            long repairCount = repairService.countRepairsByDevice(
                    mostRepairedDevice.getId(), startDate, endDate);
            model.addAttribute("mostRepairedDeviceRepairCount", repairCount);
        } else {
            model.addAttribute("mostRepairedDeviceRepairCount", 0);
        }

        if (mostCommonRepairType != null) {
            long typeCount = repairService.getRepairCountForMostCommonType(
                    startDate, endDate, categoryId, statusId);
            model.addAttribute("mostCommonRepairTypeCount", typeCount);
        } else {
            model.addAttribute("mostCommonRepairTypeCount", 0);
        }

        // Dữ liệu biểu đồ
        model.addAttribute("repairCostByDay",
                repairService.getRepairCostByTimeRange(startDate, endDate, categoryId, statusId, "day"));
        model.addAttribute("repairCostByMonth",
                repairService.getRepairCostByTimeRange(startDate, endDate, categoryId, statusId, "month"));

        Map<RepairType, Long> rawMap = repairService.getRepairCountByType(startDate, endDate, categoryId, statusId);

// Chuyển từ Map<RepairType, Long> → Map<String, Long>
        Map<String, Long> nameCountMap = new LinkedHashMap<>();
        for (Map.Entry<RepairType, Long> entry : rawMap.entrySet()) {
            nameCountMap.put(entry.getKey().getType(), entry.getValue());
        }

        model.addAttribute("repairCountByType", nameCountMap);

        model.addAttribute("deviceRepairStats",
                repairService.getDeviceRepairStats(startDate, endDate, categoryId, statusId));

        // Thông tin filter
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("selectedStatusId", statusId);

        // Danh sách lựa chọn filter
        model.addAttribute("categories", categoryService.getCates());
        model.addAttribute("statuses", statusService.getStatus());

        return "stats";
    }

    private Date getDefaultStartDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -3); // 3 tháng trước
        return cal.getTime();
    }

    @GetMapping("/stats/export")
    public void exportStats(
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @RequestParam(name = "categoryId", required = false) Integer categoryId,
            @RequestParam(name = "statusId", required = false) Integer statusId,
            HttpServletResponse response) throws IOException {

        // Gán ngày mặc định
        if (startDate == null) {
            startDate = getDefaultStartDate();
        }
        if (endDate == null) {
            endDate = new Date();
        }

        // Log tham số đầu vào
        System.out.println("Xuất thống kê: startDate=" + startDate + ", endDate=" + endDate
                + ", categoryId=" + categoryId + ", statusId=" + statusId);

        // Lấy dữ liệu thống kê
        var statsList = repairService.getDeviceRepairStats(startDate, endDate, categoryId, statusId);
        System.out.println("Kích thước statsList: " + statsList.size());
        statsList.forEach(stat -> System.out.println("Stat: " + stat));

        // Tạo workbook và sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Repair Statistics");

        // Tạo dòng tiêu đề
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Tên Thiết Bị");
        header.createCell(1).setCellValue("Số Lần Sửa");
        header.createCell(2).setCellValue("Tổng Chi Phí");

        // Ghi dữ liệu
        int rowNum = 1;
        try {
            for (Map<String, Object> stat : statsList) {
                // Kiểm tra khóa
                if (!stat.containsKey("deviceName") || !stat.containsKey("repairCount") || !stat.containsKey("totalCost")) {
                    System.err.println("Map thống kê không hợp lệ: " + stat);
                    continue;
                }

                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(stat.get("deviceName") != null ? stat.get("deviceName").toString() : "N/A");
                row.createCell(1).setCellValue(stat.get("repairCount") != null
                        ? Long.parseLong(stat.get("repairCount").toString()) : 0);
                row.createCell(2).setCellValue(stat.get("totalCost") != null
                        ? new BigDecimal(stat.get("totalCost").toString()).doubleValue() : 0.0);
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi ghi dòng: " + e.getMessage());
            throw new IOException("Không thể xuất thống kê", e);
        }

        // Tự động điều chỉnh độ rộng cột
        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        // Thiết lập response headers
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=repair_stats.xlsx");

        // Ghi workbook ra response
        try {
            workbook.write(response.getOutputStream());
        } finally {
            workbook.close();
        }
    }

}
