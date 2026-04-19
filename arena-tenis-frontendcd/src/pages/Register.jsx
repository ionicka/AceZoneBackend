import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/axiosConfig";
import "./Auth.css";

export default function Register() {
  const [form, setForm] = useState({
    username: "", password: "", email: "",
    firstName: "", lastName: "", age: "",
    phoneNumber: "", address: ""
  });
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post("/api/auth/register", form);
      navigate("/login");
    } catch (err) {
      setError(err.response?.data || "Eroare la înregistrare.");
    }
  };

  const update = (field) => (e) => setForm({...form, [field]: e.target.value});

  return (
    <div className="auth-page">
      <div className="brand">ArenaTenis</div>
      <div className="auth-card">
        <div className="auth-card-header">
          <h2>Sign Up</h2>
          <Link to="/login">Log In / Sign Up</Link>
        </div>
        <div className="tabs">
          <Link to="/login" className="tab">Log In</Link>
          <Link to="/register" className="tab active">Sign Up</Link>
        </div>
        {error && <p className="error">{error}</p>}
        <form onSubmit={handleSubmit}>
          <label>Username</label>
          <input type="text" value={form.username} onChange={update("username")} required />
          <label>Email</label>
          <input type="email" value={form.email} onChange={update("email")} required />
          <label>Prenume</label>
          <input type="text" value={form.firstName} onChange={update("firstName")} required />
          <label>Nume</label>
          <input type="text" value={form.lastName} onChange={update("lastName")} required />
          <label>Vârstă</label>
          <input type="number" value={form.age} onChange={update("age")} required />
          <label>Telefon</label>
          <input type="text" value={form.phoneNumber} onChange={update("phoneNumber")} />
          <label>Adresă</label>
          <input type="text" value={form.address} onChange={update("address")} />
          <label>Parolă</label>
          <input type="password" value={form.password} onChange={update("password")} required />
          <button type="submit">Create Account</button>
        </form>
        <p className="signup-text" style={{marginTop: "0.8rem"}}>
          Ai deja cont? <Link to="/login">Log in</Link>
        </p>
      </div>
    </div>
  );
}