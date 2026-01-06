import React, { useState, useEffect } from "react";
import {
    Box,
    Button,
    Typography,
    TextField,
    Alert,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import logo from "../img/logo.png";
import { customFetch } from "../apiHelper";

function Login({ onLogin }) {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const [loggedIn, setLoggedIn] = useState(false);
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const data = await customFetch("/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ email, password }),
            });
            const { token } = data;

            localStorage.setItem("authToken", token);

            setLoggedIn(true);
            onLogin();
            navigate("/dashboardProposals");
        } catch (err) {
            setError(err.message || "Login failed. Please try again.");
        }
    };

    const handleRegisterRedirect = () => {
        navigate("/register");
    };

    return (
        <div className={`login-container ${loggedIn ? "logged-in" : ""}`}>
            <Typography variant="h3" className="website-name">
                <img src={logo} alt="Logo" className="logo" />
            </Typography>
            <Box className="login-box">
                <form onSubmit={handleLogin}>
                    <Typography variant="h5" className="title">
                        Login
                    </Typography>
                    {error && (
                        <Alert severity="error" sx={{ marginBottom: "16px" }}>
                            {error}
                        </Alert>
                    )}
                    <TextField
                        fullWidth
                        label="Email"
                        type="email"
                        variant="outlined"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        sx={{
                            marginTop: "16px",
                            "& .MuiOutlinedInput-root": {
                                borderColor: "#663469",
                            },
                        }}
                    />
                    <TextField
                        fullWidth
                        label="Password"
                        type="password"
                        variant="outlined"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        sx={{
                            marginTop: "16px",
                            "& .MuiOutlinedInput-root": {
                                borderColor: "#663469",
                            },
                        }}
                    />
                    <Button
                        type="submit"
                        fullWidth
                        sx={{
                            marginTop: "16px",
                            backgroundColor: "#351944",
                            color: "#FFFFFF",
                        }}
                    >
                        Login
                    </Button>
                </form>
                <Button
                    onClick={handleRegisterRedirect}
                    variant="outlined"
                    fullWidth
                    sx={{
                        marginTop: "16px",
                        color: "#351944",
                        borderColor: "#663469",
                        background: "transparent",
                    }}
                >
                    No account ? Sign up
                </Button>
            </Box>
        </div>
    );
}

export default Login;