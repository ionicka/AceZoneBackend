import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/axiosConfig";
import "./Auth.css";

export default function Login() {
  const [form, setForm] = useState({ username: "", password: "" });
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await api.post("/api/auth/login", form);
      localStorage.setItem("token", res.data);
      navigate("/home");
    } catch (err) {
      setError("Username sau parola greșite.");
    }
  };

  return (
    <div className="auth-page">
      <div className="brand">ArenaTenis</div>
      <div className="auth-card">
        <div className="auth-card-header">
          <h2>Log In</h2>
          <Link to="/register">Log In / Sign Up</Link>
        </div>
        <div className="tabs">
          <Link to="/login" className="tab active">Log In</Link>
          <Link to="/register" className="tab">Sign Up</Link>
        </div>
        {error && <p className="error">{error}</p>}
        <form onSubmit={handleSubmit}>
          <label>Email / Username</label>
          <input type="text" value={form.username}
            onChange={e => setForm({...form, username: e.target.value})} required />
          <label>Password</label>
          <input type="password" value={form.password}
            onChange={e => setForm({...form, password: e.target.value})} required />
          <p className="forgot">Forgot Password?</p>
          <button type="submit">Log In</button>
        </form>
        <div className="divider"><hr/><span>OR</span><hr/></div>
        <p className="signup-text">
          Don't have an account? <Link to="/register">Sign up</Link>
        </p>
      </div>
    </div>
  );
}