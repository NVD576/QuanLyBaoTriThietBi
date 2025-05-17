import { useContext, useState } from "react";
import {
  Button,
  Form,
  Card,
  Container,
  Row,
  Col,
  Alert,
  InputGroup,
} from "react-bootstrap";
import {
  PersonFill,
  LockFill,
  EyeFill,
  EyeSlashFill,
} from "react-bootstrap-icons";
import MySpinner from "./layout/MySpinner";
import { auth, googleProvider, signInWithPopup } from "../configs/Firebase";
import Apis, { authApis, endpoints } from "../configs/Apis";
import cookie from "react-cookies";
import { MyDispatchContext } from "../configs/MyContexts";
import { useNavigate } from "react-router-dom";
import "./Login.css";

const Login = () => {
  const dispatch = useContext(MyDispatchContext);
  const nav = useNavigate();

  const [user, setUser] = useState({});
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showPassword, setShowPassword] = useState(false);

  const setState = (value, field) => {
    setUser({ ...user, [field]: value });
  };

  const login = async (e) => {
    e.preventDefault();
    setError(null);

    try {
      setLoading(true);
      const res = await Apis.post(endpoints["login"], { ...user });
      cookie.save("token", res.data.token);

      const u = await authApis().get(endpoints["profile"]);

      dispatch({
        type: "login",
        payload: u.data,
      });

      nav("/devices");
    } catch (ex) {
        console.error('Login error:', ex);

        if (ex.response?.status === 401) {
          setError('Sai tên đăng nhập hoặc mật khẩu');
        } else if (ex.message) {
          setError(ex.message);
        } else {
          setError('Lỗi hệ thống hoặc kết nối. Vui lòng thử lại sau.');
        }
       } finally {
      setLoading(false);
    }
  };
const loginWithGoogle = async () => {
    try {
        const result = await signInWithPopup(auth, googleProvider);
        // const idToken = await result.user.getIdToken();
        const username = result.user.displayName;
        const password = "123456";

        console.log("Google login result:", result);

        const res = await Apis.post(endpoints["login"], { username, password });
        cookie.save("token", res.data.token);

        const u = await authApis().get(endpoints["profile"]);
        dispatch({
            type: "login",
            payload: u.data,
        });

        nav("/devices");
    } catch (err) {
        setError("Đăng nhập Google thất bại!");
    }
};
  return (
    <Container className="d-flex justify-content-center align-items-center"
      style={{ minHeight: "100vh",
        background: "linear-gradient(to right, #6a11cb, #2575fc)",
      }}
    >
      <Row className="w-100 justify-content-center">
        <Col xs={12} md={6} lg={5}>
          <Card className="p-4 shadow card-login">
            <h2 className="text-center text-primary mb-4">
              🔐 Đăng Nhập Hệ Thống
            </h2>

            {error && <Alert variant="danger">{error}</Alert>}

            <Form onSubmit={login}>
              {/* Tên đăng nhập */}
              <Form.Group className="mb-3">
                <Form.Label>Tên đăng nhập</Form.Label>
                <InputGroup>
                  <InputGroup.Text>
                    <PersonFill />
                  </InputGroup.Text>
                  <Form.Control
                    type="text"
                    placeholder="Nhập tên đăng nhập"
                    required
                    value={user.username || ""}
                    onChange={(e) => setState(e.target.value, "username")}
                    className="input-field"
                  />
                </InputGroup>
              </Form.Group>

              {/* Mật khẩu */}
              <Form.Group className="mb-3">
                <Form.Label>Mật khẩu</Form.Label>
                <InputGroup>
                  <InputGroup.Text>
                    <LockFill />
                  </InputGroup.Text>
                  <Form.Control
                    type={showPassword ? "text" : "password"}
                    placeholder="Nhập mật khẩu"
                    required
                    value={user.password || ""}
                    onChange={(e) => setState(e.target.value, "password")}
                    className="input-field"
                  />
                  <Button
                    variant="outline-secondary"
                    onClick={() => setShowPassword(!showPassword)}
                    tabIndex={-1}
                  >
                    {showPassword ? <EyeSlashFill /> : <EyeFill />}
                  </Button>
                </InputGroup>
              </Form.Group>

              {/* Nút submit */}
              {loading ? (
                <MySpinner />
              ) : (
                <Button type="submit" variant="success" className="w-100">
                  🚀 Đăng nhập
                </Button>
              )}
            </Form>
            <Button onClick={loginWithGoogle} variant="outline-danger" className="w-100 py-2 mt-2">
              <i className="bi bi-google"></i> Đăng nhập bằng Google
            </Button>
          </Card>

          <div className="text-center mt-3 text-white">
            © 2024 Your Company – All rights reserved.
          </div>
        </Col>
      </Row>
    </Container>
  );
};

export default Login;
