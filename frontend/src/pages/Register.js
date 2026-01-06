import React, { useState } from "react";
import {
    Box,
    Button,
    Typography,
    TextField,
    Alert,
    FormControl,
    InputLabel,
    Select,
    MenuItem,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import logo from "../img/logo.png";
import { customFetch } from "../apiHelper";

function Register() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [role, setRole] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [error, setError] = useState("");
    const [success, setSuccess] = useState(false);
    const navigate = useNavigate();

    const handleRegister = async (e) => {
        e.preventDefault();
        if (password !== confirmPassword) {
            setError("Passwords do not match.");
            return;
        }

        try {
            await customFetch("/auth/signup", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email,
                    password,
                    role,
                    firstName,
                    lastName,
                }),
            });

            setError("");
            setSuccess(true);
            setTimeout(() => navigate("/login"), 2000);
        } catch (error) {
            setError(error.message || "An error occurred during registration.");
        }
    };

    const handleGoToLogin = () => {
        navigate("/login");  // Redirects the user to the login page
    };

    return (
        <div className="register-container">
            <Typography variant="h3" className="website-name">
                <img src={logo} alt="Logo" className="logo" />
            </Typography>
            <Box className="register-box">
                <form onSubmit={handleRegister}>
                    <Typography variant="h5" className="title">
                        Register
                    </Typography>
                    {error && (
                        <Alert severity="error" sx={{ marginBottom: "16px" }}>
                            {error}
                        </Alert>
                    )}
                    {success && (
                        <Alert severity="success" sx={{ marginBottom: "16px" }}>
                            Registration successful! Redirecting to login...
                        </Alert>
                    )}
                    <TextField
                        fullWidth
                        label="First Name"
                        variant="outlined"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                        sx={{
                            marginTop: "16px",
                            "& .MuiOutlinedInput-root": {
                                borderColor: "#663469",
                            },
                        }}
                    />
                    <TextField
                        fullWidth
                        label="Last Name"
                        variant="outlined"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                        sx={{
                            marginTop: "16px",
                            "& .MuiOutlinedInput-root": {
                                borderColor: "#663469",
                            },
                        }}
                    />
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
                    <TextField
                        fullWidth
                        label="Confirm Password"
                        type="password"
                        variant="outlined"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        sx={{
                            marginTop: "16px",
                            "& .MuiOutlinedInput-root": {
                                borderColor: "#663469",
                            },
                        }}
                    />
                    {/* Role Dropdown */}
                    <FormControl fullWidth variant="outlined" sx={{ marginTop: "16px" }}>
                        <InputLabel>Role</InputLabel>
                        <Select
                            value={role}
                            onChange={(e) => setRole(e.target.value)}
                            label="Role"
                            sx={{
                                "& .MuiOutlinedInput-root": {
                                    borderColor: "#663469",
                                },
                            }}
                        >
                            <MenuItem value="PROFESSOR">Professor</MenuItem>
                            <MenuItem value="STUDENT">Student</MenuItem>
                        </Select>
                    </FormControl>

                    <Button
                        type="submit"
                        fullWidth
                        sx={{
                            marginTop: "16px",
                            backgroundColor: "#351944",
                            color: "#FFFFFF",
                        }}
                    >
                        Register
                    </Button>
                </form>

                {/* Go to Login button */}
                <Button
                    onClick={handleGoToLogin}
                    variant="outlined"
                    fullWidth
                    sx={{
                        marginTop: "16px",
                        color: "#351944",
                        borderColor: "#663469",
                        background: "transparent",
                    }}
                >
                    Already have an account ? log in
                </Button>
            </Box>
        </div>
    );
}

export default Register;