import React, { useState } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { createTheme, ThemeProvider, CssBaseline } from "@mui/material";
import Header from "./components/Header";
import Footer from "./components/Footer";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import DashboardProposals from "./pages/DashboardProposals";
import "./css/app.css";
import "./css/header.css";
import "./css/footer.css";
import "./css/login.css";
import DashboardTools from "./pages/DashboardTools";
import "./css/Details.css";
import Details from "./pages/Details";
import "./css/register.css"
import FormProposal from "./pages/FormProposal";




const theme = createTheme({
    palette: {
        mode: "light",
        primary: {
            main: "#1976d2",
        },
        secondary: {
            main: "#ff4081",
        },
    },
    typography: {
        fontFamily: "Roboto, Arial, sans-serif",
    },
});

function App() {
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    const handleLogin = () => setIsAuthenticated(true);
    const handleLogout = () => {
        localStorage.removeItem("authToken");

        setIsAuthenticated(false);
    };
    return (
        <ThemeProvider theme={theme}>
            <CssBaseline />
            <Router>
                {isAuthenticated && <Header onLogout={handleLogout} />}
                <div className={isAuthenticated ? "white-background" : "login-background"}>
                    <Routes>
                        {!isAuthenticated ? (
                            <>
                                <Route path="/login" element={<Login onLogin={handleLogin} />} />
                                <Route path="/register" element={<Register />} />
                                <Route path="*" element={<Navigate to="/login" replace />} />
                            </>
                        ) : (
                            <>
                                <Route path="/" element={<Home />} />
                                <Route path="/details" element={<Details />} />
                                <Route path="/dashboardProposals" element={<DashboardProposals />} />
                                <Route path="/dashboardTools" element={<DashboardTools />} />
                                <Route path="/formProposal" element={<FormProposal />} />
                                <Route path="*" element={<Navigate to="/" replace />} />
                                <Route path="/" element={<DashboardProposals />} />
                            </>
                        )}
                    </Routes>
                </div>
                {isAuthenticated && <Footer />}
            </Router>
        </ThemeProvider>
    );
}


export default App;
