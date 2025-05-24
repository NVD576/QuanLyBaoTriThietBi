import { useContext, useEffect, useState } from "react";
import {
  Button,
  Container,
  Form,
  Nav,
  Navbar,
  NavDropdown,
} from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import Apis, { endpoints } from "../../configs/Apis";
import { MyDispatchContext, MyUserContext } from "../../configs/MyContexts";
import { useTranslation } from "react-i18next";

const Header = () => {
  const { t } = useTranslation(); // hook dịch
  const [categories, setCategories] = useState([]);
  const [kw, setKw] = useState("");
  const nav = useNavigate();
  const user = useContext(MyUserContext);
  const dispatch = useContext(MyDispatchContext);

  const loadCates = async () => {
    try {
      let res = await Apis.get(endpoints["categories"]);
      setCategories(res.data);
    } catch (error) {
      console.error("Failed to load categories:", error);
    }
  };

  useEffect(() => {
    loadCates();
  }, []);

  const search = (e) => {
    e.preventDefault();
    nav(`/devices?kw=${kw}`);
  };

  return (
    <Navbar expand="lg" className="bg-body-tertiary">
      <Container fluid>
        <Navbar.Brand href="#">{t("brand")}</Navbar.Brand>
        <Navbar.Toggle aria-controls="navbarScroll" />
        <Navbar.Collapse id="navbarScroll">
          <Nav
            className="me-auto my-2 my-lg-0"
            style={{ maxHeight: "100px" }}
            navbarScroll
          >
            <Link to="/devices" className="nav-link">
              {t("Home")}
            </Link>
            {user === null ? (
              <>
                {/* <Link to="/register" className="nav-link text-success">{t("register")}</Link> */}
                <Link to="/login" className="nav-link text-danger">
                  {t("login")}
                </Link>
              </>
            ) : (
              <>
                <NavDropdown title={t("category")} id="navbarScrollingDropdown">
                  {categories.map((c) => {
                    let url = `/devices?cateId=${c.id}`;
                    return (
                      <Link className="dropdown-item" key={c.id} to={url}>
                        {c.name}
                      </Link>
                    );
                  })}
                </NavDropdown>
                <img
                  src={user.avatar}
                  alt="avatar"
                  className="rounded-circle"
                  width="30"
                  height="30"
                />
                <Link to="/profile" className="nav-link text-success">
                  {t("greeting", { username: user.username })}
                </Link>
                <Button
                  variant="danger"
                  onClick={() => {
                    localStorage.removeItem("user");
                    dispatch({ type: "logout" });
                  }}
                >
                  {t("logout")}
                </Button>
              </>
            )}
          </Nav>
          <Form onSubmit={search} className="d-flex">
            <Form.Control
              value={kw}
              onChange={(e) => setKw(e.target.value)}
              type="search"
              placeholder={t("search_placeholder")}
              className="me-2"
              aria-label="Search"
            />
            <Button type="submit" variant="outline-success">
              {t("search")}
            </Button>
          </Form>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;
