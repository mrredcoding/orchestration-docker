import React, { useEffect, useState } from "react";
import { AppBar, Toolbar, Button, Box, Typography, CircularProgress } from "@mui/material";
import { Link } from "react-router-dom";
import logo from "../img/logo.png";
import { fetchWithAuth } from "../apiHelper";

function Header({ onLogout }) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const user = await fetchWithAuth("/auth/me", {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    },
                });

                setUser(user);  // Assuming the response contains the user data
            } catch (error) {
                console.error("Error fetching user details:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchUser();
    }, []);

    return (
        <AppBar position="sticky" className="header-appbar" sx={{ padding: "0 20px" }}>
            <Toolbar sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                {/* Left: Logo */}
                <Box sx={{ display: "flex", alignItems: "center" }}>
                    <img
                        src={logo}
                        alt="Logo"
                        style={{ height: "60px", marginRight: "8px" }}
                    />
                </Box>

                {/* Center: Navigation Links */}
                <Box sx={{ display: "flex", justifyContent: "center", flex: 1 }}>
                    <Button
                        className="header-button"
                        component={Link}
                        to="/"
                        sx={{
                            color: "white",
                            textTransform: "none",
                            "&:hover": {
                                color: "#f5f5f5",
                            },
                            margin: "0 16px",
                        }}
                    >
                        Home
                    </Button>
                    <Button
                        className="header-button"
                        component={Link}
                        to="/dashboardProposals"
                        sx={{
                            color: "white",
                            textTransform: "none",
                            "&:hover": {
                                color: "#f5f5f5",
                            },
                            margin: "0 16px",
                        }}
                    >
                        Dashboard Proposals
                    </Button>
                    <Button
                        className="header-button"
                        component={Link}
                        to="/dashboardTools"
                        sx={{
                            color: "white",
                            textTransform: "none",
                            "&:hover": {
                                color: "#f5f5f5",
                            },
                            margin: "0 16px",
                        }}
                    >
                        Dashboard Tools
                    </Button>
                </Box>

                {/* Right: User Info */}
                <Box sx={{ display: "flex", alignItems: "center" }}>
                    {loading ? (
                        <CircularProgress color="inherit" size={20} />
                    ) : user ? (
                        <Typography
                            sx={{
                                color: "white",
                                marginRight: "16px",
                                display: "flex",
                                alignItems: "center",
                            }}
                        >
                            <span
                                style={{
                                    width: "10px",
                                    height: "10px",
                                    borderRadius: "50%",
                                    backgroundColor: "green",
                                    display: "inline-block",
                                    marginRight: "8px",
                                }}
                            ></span>
                            {user.firstName} {user.lastName} ({user.role})
                        </Typography>
                    ) : (
                        <Typography sx={{ color: "red", marginRight: "16px" }}>
                            Not Connected
                        </Typography>
                    )}
                    <Button
                        className="header-button"
                        onClick={onLogout}
                        sx={{
                            color: "white",
                            textTransform: "none",
                            "&:hover": {
                                color: "#f5f5f5",
                            },
                        }}
                    >
                        Logout
                    </Button>
                </Box>
            </Toolbar>
        </AppBar>
    );
}

export default Header;