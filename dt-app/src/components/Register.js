import { useRef, useState } from "react";
import {
  Alert,
  Button,
  FloatingLabel,
  Form,
  Container
} from "react-bootstrap";
import Apis, { endpoints } from "../configs/Apis";
import { useNavigate } from "react-router-dom";
import { auth, googleProvider, signInWithPopup } from "../configs/Firebase";
import MySpinner from "./layout/MySpinner";
import { FaUserAlt } from "react-icons/fa";
import { motion } from "framer-motion"; // 👉 Thêm Framer Motion

const Register = () => {
  const info = [
    { label: "Tên", type: "text", field: "firstName" },
    { label: "Tên đăng nhập", type: "text", field: "username" },
    { label: "Mật khẩu", type: "password", field: "password" },
    { label: "Xác nhận mật khẩu", type: "password", field: "confirm" },
    { label: "Email", type: "email", field: "email" },
  ];

  const avatar = useRef();
  const [msg, setMsg] = useState("");
  const [user, setUser] = useState({});
  const [loading, setLoading] = useState(false);
  const nav = useNavigate();

  const setState = (value, field) => {
    setUser({ ...user, [field]: value });
  };

  const register = async (e) => {
    e.preventDefault();
    if (user.password !== user.confirm) {
      setMsg("Mật khẩu không khớp!");
    } else {
      try {
        setLoading(true);
        let form = new FormData();
        for (let f of info)
          if (f.field !== "confirm") form.append(f.field, user[f.field]);
        form.append("avatar", avatar.current.files[0]);

        let res = await Apis.post(endpoints["register"], form, {
          headers: { "Content-Type": "multipart/form-data" },
        });

        if (res.status === 201) nav("/login");
      } catch {
        setMsg("Có lỗi xảy ra khi đăng ký.");
      } finally {
        setLoading(false);
      }
    }
  };

  const loginWithGoogle = async () => {
    try {
      await signInWithPopup(auth, googleProvider);
      nav("/");
    } catch (err) {
      setMsg("Đăng ký Google thất bại!");
    }
  };

  return (
    <Container
      className="d-flex justify-content-center align-items-center"
      style={{
        minHeight: "100vh",
        background: "linear-gradient(to right, #1976d2, #0d47a1)",
      }}
    >
      <motion.div
        className="card shadow-lg p-4"
        style={{
          width: "100%",
          maxWidth: "500px",
          borderRadius: "15px",
          backgroundColor: "#fff",
        }}
        initial={{ opacity: 0, y: 50 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
      >
        <div className="text-center mb-4">
          <FaUserAlt size={50} color="#1976d2" />
          <h2 className="text-success mt-2">ĐĂNG KÝ NGƯỜI DÙNG</h2>
        </div>

        {msg && <Alert variant="danger">{msg}</Alert>}

        <Form onSubmit={register}>
          {info.map((f, idx) => (
            <motion.div
              key={f.field}
              initial={{ opacity: 0, x: -20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: idx * 0.1 }}
            >
              <FloatingLabel
                controlId={`floatingInput-${f.field}`}
                label={f.label}
                className="mb-3"
              >
                <Form.Control
                  type={f.type}
                  placeholder={f.label}
                  required
                  value={user[f.field]}
                  onChange={(e) => setState(e.target.value, f.field)}
                  style={{ borderRadius: "0.375rem" }}
                />
              </FloatingLabel>
            </motion.div>
          ))}

          <motion.div
            initial={{ opacity: 0, x: -20 }}
            animate={{ opacity: 1, x: 0 }}
            transition={{ delay: info.length * 0.1 }}
          >
            <FloatingLabel controlId="floatingFile" label="Ảnh đại diện" className="mb-3">
              <Form.Control type="file" ref={avatar} style={{ borderRadius: "0.375rem" }} />
            </FloatingLabel>
          </motion.div>

          {loading ? (
            <MySpinner />
          ) : (
            <motion.div
              initial={{ opacity: 0 }}
              animate={{ opacity: 1 }}
              transition={{ delay: (info.length + 1) * 0.1 }}
            >
              <Button
                type="submit"
                variant="success"
                className="w-100 py-2 mt-3"
                style={{ borderRadius: "0.375rem" }}
              >
                Đăng ký
              </Button>
            </motion.div>
          )}
        </Form>

        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: (info.length + 2) * 0.1 }}
        >
          <Button
            variant="outline-danger"
            className="w-100 py-2 mt-3"
            onClick={loginWithGoogle}
          >
            Đăng ký bằng Google
          </Button>
        </motion.div>

        <motion.div
          className="text-center mt-3"
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: (info.length + 3) * 0.1 }}
        >
          <p className="text-muted">
            Đã có tài khoản?{" "}
            <span
              onClick={() => nav("/login")}
              className="text-success"
              style={{ cursor: "pointer", textDecoration: "underline" }}
            >
              Đăng nhập
            </span>
          </p>
        </motion.div>
      </motion.div>
    </Container>
  );
};

export default Register;
