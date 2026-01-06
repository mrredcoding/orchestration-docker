import React, { useState, useEffect } from "react";
import {Container, Typography, Button, Grid, Card, CardContent, Box, Alert} from "@mui/material";
import { Link } from "react-router-dom";
import { fetchWithAuth } from "../apiHelper";

function Home() {
    const [error, setError] = useState("");
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

                setUser(user);
            } catch (error) {
                setError(error.message || "An error occurred while fetching your data.");
            } finally {
                setLoading(false);
            }
        };

        fetchUser();
    }, []);

    return (
        <Box sx={{ backgroundColor: "#F1F1F9" }}> {/* Light gray background */}
            <Container sx={{ padding: "2rem 1rem" }}>
                {error && (
                    <Alert severity="error" sx={{ marginBottom: "16px" }}>
                        {error}
                    </Alert>
                )}
                <Typography variant="h4" gutterBottom>
                    {loading ? "Loading..." : `Welcome to LOLA, ${user ? user.firstName : "User"}!`}
                </Typography>
                <Typography variant="body1" gutterBottom>
                    This is a modern web app designed to help educators and students manage their teaching tools and platforms more efficiently.
                </Typography>

                {/* Call to Action Buttons */}
                <section style={{ marginTop: "3rem" }}>
                    <Button
                        component={Link}
                        to="/dashboardProposals"
                        sx={{
                            background: "linear-gradient(90deg, #351944 0%, #663469 100%)",
                            color: "white",
                            fontWeight: "bold",
                            textTransform: "none",
                            borderRadius: 2,
                            px: 3,
                            py: 1,
                            "&:hover": {
                                background: "linear-gradient(90deg, #663469 0%, #351944 100%)",
                            },
                        }}
                    >
                        Go to Dashboard Proposals
                    </Button>
                    <Button
                        component={Link}
                        to="/dashboardTools"
                        sx={{
                            background: "linear-gradient(90deg, #351944 0%, #663469 100%)",
                            color: "white",
                            fontWeight: "bold",
                            textTransform: "none",
                            borderRadius: 2,
                            px: 3,
                            py: 1,
                            "&:hover": {
                                background: "linear-gradient(90deg, #663469 0%, #351944 100%)",
                            },
                        }}
                        style={{ marginLeft: "1rem" }}
                    >
                        Go to Dashboard Tools
                    </Button>
                </section>

                {/* Description Section */}
                <section style={{ marginTop: "2rem" }}>
                    <Typography variant="h5" gutterBottom>
                        What You Can Do with This Application
                    </Typography>
                    <Typography variant="body1" paragraph>
                        This application provides an easy-to-use interface for accessing and managing tools and platforms used in various educational activities like courses, projects, and practical sessions. You can:
                    </Typography>
                    <ul>
                        <li>Explore the dashboard for different tools and proposals.</li>
                        <li>View, modify, and propose new tools for the educational environment.</li>
                        <li>Collaborate with administrators and other users in a seamless environment.</li>
                    </ul>
                </section>

                {/* Features Section */}
                <section style={{ marginTop: "3rem" }}>
                    <Typography variant="h5" gutterBottom>
                        Key Features
                    </Typography>
                    <Grid container spacing={3}>
                        <Grid item xs={12} sm={6} md={4}>
                            <Card>
                                <CardContent>
                                    <Typography variant="h6">Full Stack Development</Typography>
                                    <Typography variant="body2">
                                        The app is built with Java and Spring Boot to deliver robust, scalable, and secure web experiences.
                                    </Typography>
                                </CardContent>
                            </Card>
                        </Grid>
                        <Grid item xs={12} sm={6} md={4}>
                            <Card>
                                <CardContent>
                                    <Typography variant="h6">User Roles</Typography>
                                    <Typography variant="body2">
                                        Designed to cater to three different user profiles: Administrators, Teachers, and Students, each with unique capabilities.
                                    </Typography>
                                </CardContent>
                            </Card>
                        </Grid>
                        <Grid item xs={12} sm={6} md={4}>
                            <Card>
                                <CardContent>
                                    <Typography variant="h6">Search & Manage Tools</Typography>
                                    <Typography variant="body2">
                                        The app allows easy access to educational tools with multi-criteria search and management options.
                                    </Typography>
                                </CardContent>
                            </Card>
                        </Grid>
                    </Grid>
                </section>

                {/* About Section */}
                <section style={{ marginTop: "3rem" }}>
                    <Typography variant="h5" gutterBottom>
                        About This Project
                    </Typography>
                    <Typography variant="body1" paragraph>
                        This project is a Full Stack Development initiative focused on building a centralized hub for educational tools and platforms. It aims to simplify and unify the usage of various tools across courses, projects, and practical sessions.
                    </Typography>
                </section>

                {/* Contact Section */}
                <section style={{ marginTop: "3rem", marginBottom: "3rem" }}>
                    <Typography variant="h5" gutterBottom>
                        Get in Touch
                    </Typography>
                    <Typography variant="body1" paragraph>
                        Have questions or feedback? Feel free to reach out to us anytime!
                    </Typography>
                    <Button
                        variant="outlined"
                        sx={{
                            color: "#1976d2",
                            borderColor: "#1976d2",
                            textTransform: "none",
                            "&:hover": {
                                backgroundColor: "#e3f2fd",
                                borderColor: "#1976d2",
                            },
                        }}
                    >
                        Contact Us
                    </Button>
                </section>
            </Container>
        </Box>
    );
}

export default Home;