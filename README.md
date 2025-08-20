## Quản Lý Bảo Trì Thiết Bị (QLTB)

Ứng dụng quản lý thiết bị, bảo trì, sự cố và sửa chữa.
- Backend: Java 17, Spring MVC + Hibernate, Jakarta EE 10, đóng gói WAR, deploy trên Tomcat 10.x; CSDL MySQL.
- Frontend: React (Create React App), Axios, Bootstrap.

### Kiến trúc tổng quan
- Context path backend: `/QLTB` (đặt trong `QLTB/src/main/webapp/META-INF/context.xml`).
- API REST dưới `/QLTB/api/...` (ví dụ: `/QLTB/api/devices`, `/QLTB/api/categories`, `/QLTB/api/repairs`, ...).
- Frontend gọi API qua `BASE_URL` mặc định: `http://localhost:8080/QLTB/api/` (xem `dt-app/src/configs/Apis.js`).

### Yêu cầu hệ thống
- Java 17 (JDK 17)
- Maven 3.8+
- Tomcat 10.1+
- MySQL 8.x
- Node.js 18+ và npm 9+ (hoặc yarn)

### Cấu trúc thư mục
```
QuanLyBaoTriThietBi/
  ├─ QLTB/                # Backend (Java, WAR)
  │  ├─ src/main/java     # Source Java (controllers, service, repository, ...)
  │  ├─ src/main/resources# properties, templates, static
  │  ├─ src/main/webapp   # WEB-INF, context.xml
  │  └─ pom.xml           # Maven
  ├─ dt-app/              # Frontend (React)
  │  ├─ src/              # Components, configs
  │  └─ package.json
  └─ QLTB.sql             # Script CSDL (nếu có)
```

## Hướng dẫn cài đặt

### 1) Cơ sở dữ liệu (MySQL)
1. Tạo database `maintsysdb`:
```sql
CREATE DATABASE maintsysdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
2. Cập nhật thông tin kết nối MySQL trong `QLTB/src/main/resources/database.properties`:
   - `hibernate.connection.url=jdbc:mysql://localhost:3306/maintsysdb`
   - `hibernate.connection.username=YOUR_USERNAME`
   - `hibernate.connection.password=YOUR_PASSWORD`
3. (Tuỳ chọn) Import dữ liệu mẫu nếu có file `QLTB.sql`:
```bash
mysql -u YOUR_USERNAME -p maintsysdb < QLTB.sql
```

### 2) Backend (Java, Maven, Tomcat)
- Build WAR:
```bash
cd QLTB
mvn clean package
```
Kết quả: `QLTB/target/QLTB-1.0-SNAPSHOT.war`.

- Deploy lên Tomcat 10.1+
  - Sao chép file WAR vào `TOMCAT_HOME/webapps/`.
  - Có thể đổi tên `QLTB-1.0-SNAPSHOT.war` thành `QLTB.war` để context path đúng `/QLTB`.
  - Đảm bảo `QLTB/src/main/webapp/META-INF/context.xml` có `<Context path="/QLTB"/>`.

- Chạy Tomcat và kiểm tra:
  - Trang chủ: `http://localhost:8080/QLTB/`
  - API ví dụ: `http://localhost:8080/QLTB/api/devices`

- Cấu hình liên quan:
  - Phân trang: `QLTB/src/main/resources/configs.properties` (`PAGE_SIZE = 6`).
  - CORS: cấu hình trong `SpringSecurityConfigs.corsConfigurationSource()`.
    - Mặc định allow origin: `http://localhost:3000/`. Nếu chạy frontend khác host/port, cập nhật danh sách allowed origins cho phù hợp.

### 3) Frontend (React)
- Cài dependencies và chạy dev server:
```bash
cd dt-app
npm install
npm start
```
Dev server mặc định: `http://localhost:3000`.

- Cấu hình API base URL:
  - Sửa `dt-app/src/configs/Apis.js` nếu backend không chạy tại `http://localhost:8080/QLTB/api/`.

- Firebase (đăng nhập Google nếu dùng):
  - Cấu hình tại `dt-app/src/configs/Firebase.js` (`apiKey`, `authDomain`, `projectId`, ...). Cập nhật theo project Firebase của bạn.

- Build production:
```bash
npm run build
```
Output tại `dt-app/build/` để deploy lên static hosting (Nginx, Apache, ...).

## Tính năng chính
- Quản lý danh mục thiết bị (categories), thiết bị (devices)
- Lịch bảo trì (maintenances), tần suất (frequencies), mức độ (levels)
- Quản lý sự cố (issues) và xác nhận xử lý
- Lịch sử sửa chữa (repairs), loại sửa chữa (repairTypes)
- Tài khoản, đăng nhập, cập nhật hồ sơ, phân quyền qua Spring Security

## API nhanh (tham khảo)
- Base URL: `http://localhost:8080/QLTB/api/`
- Ví dụ endpoints (GET nếu không ghi rõ):
  - `GET /devices` — danh sách thiết bị
  - `GET /categories` — danh sách danh mục
  - `GET /repairs` — danh sách sửa chữa
  - `POST /login` — đăng nhập
  - `GET /secure/profile` — thông tin cá nhân (yêu cầu `Authorization: Bearer <token>`)

Lưu ý: Một số endpoint yêu cầu xác thực/role theo `SpringSecurityConfigs`.

## Ghi chú bảo mật & cấu hình
- Không nên commit thông tin nhạy cảm (DB password, Cloudinary API, Firebase keys) vào repo công khai. Dùng biến môi trường/secret manager trong môi trường thực tế.
- Cloudinary hiện được cấu hình trong `SpringSecurityConfigs` (bean `cloudinary`). Hãy đổi sang thông tin của bạn nếu dùng upload ảnh.

## Troubleshooting
- Lỗi CORS: cập nhật `allowedOrigins` trong `SpringSecurityConfigs` để khớp origin frontend.
- Lỗi kết nối DB: kiểm tra `database.properties`, MySQL đang chạy, user/password và quyền.
- Lỗi 404 API: xác nhận WAR đã deploy và context path là `/QLTB` trên Tomcat.

## Bản quyền
Dùng cho mục đích học tập/nội bộ. Kiểm tra và tuân thủ giấy phép của các thư viện bên thứ ba được sử dụng trong dự án.
