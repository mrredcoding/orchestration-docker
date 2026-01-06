import React, { useEffect, useState } from "react";
import {
    Container,
    Grid,
    Typography,
    Paper,
    Button,
    Box,
    Alert,
} from "@mui/material";
import { fetchWithAuth } from "../apiHelper";
import { useNavigate } from "react-router-dom";
import "../css/DashboardTools.css";

function Item({ tool, onDelete, isAdmin }) {
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleDelete = async () => {
        try {
            await fetchWithAuth(`/api/tools/${tool.id}/delete`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                },
            });
            onDelete(tool.id);
        } catch (error) {
            setError(error.message || "An error occurred while deleting the tool.");
        }
    };

    return (
        <Paper
            className="tool-item"
            elevation={3}
            onClick={() =>
                navigate("/details", {
                    state: { ...tool },
                })
            }
            style={{ cursor: "pointer" }}
        >
            {error && (
                <Alert severity="error" sx={{ marginBottom: "16px" }}>
                    {error}
                </Alert>
            )}
            <Typography variant="h6" className="tool-title" gutterBottom>
                Title: {tool.title}
            </Typography>
            <Typography variant="body2" className="tool-domain" gutterBottom>
                Domaine: {tool.domainType}
            </Typography>

            {/* Show Delete button only for Admin */}
            {isAdmin && (
                <Box className="button-group">
                    <Button
                        className="MuiButton-contained-error"
                        onClick={(e) => {
                            e.stopPropagation();
                            handleDelete();
                        }}
                    >
                        Supprimer
                    </Button>
                </Box>
            )}
        </Paper>
    );
}

function DashboardTools() {
    const [error, setError] = useState("");
    const [tools, setTools] = useState([]);
    const [loading, setLoading] = useState(true);
    const [user, setUser] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const tools = await fetchWithAuth("/api/tools/all", {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    },
                });
                setTools(tools);

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

        fetchData();
    }, []);

    const handleDelete = (toolId) => {
        setTools((prevTools) => prevTools.filter((tool) => tool.id !== toolId));
    };

    const isAdmin = user?.role === "ADMIN";

    return (
        <Container className="dashboard-container">
            {error && (
                <Alert severity="error" sx={{ marginBottom: "16px" }}>
                    {error}
                </Alert>
            )}
            <Typography variant="h4" gutterBottom>
                Dashboard des Outils
            </Typography>

            {loading ? (
                <Typography
                    variant="h6"
                    className="dashboard-tools-loading"
                    gutterBottom
                >
                    Chargement des outils...
                </Typography>
            ) : tools.length === 0 ? (
                <Typography
                    variant="h6"
                    className="dashboard-tools-empty"
                    gutterBottom
                >
                    Aucun outil trouvé.
                </Typography>
            ) : (
                <Grid
                    container
                    spacing={{ xs: 2, md: 3 }}
                    columns={{ xs: 4, sm: 8, md: 12 }}
                >
                    {tools.map((tool) => (
                        <Grid item xs={2} sm={4} md={4} key={tool.id}>
                            <Item tool={tool} onDelete={handleDelete} isAdmin={isAdmin} />
                        </Grid>
                    ))}
                </Grid>
            )}
        </Container>
    );
}

export default DashboardTools;