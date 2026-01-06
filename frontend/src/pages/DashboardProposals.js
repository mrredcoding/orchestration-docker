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

function Item({ proposal, isAdmin }) {
    const [error, setError] = useState("");

    const handleAccept = async () => {
        try {
            await fetchWithAuth(`/api/proposals/${proposal.id}/accept`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });
        } catch (error) {
            setError(error.message || "An error occurred while accepting the proposal.");
        }
    };

    const handleReject = async () => {
        try {
            await fetchWithAuth(`/api/proposals/${proposal.id}/refuse`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
            });
        } catch (error) {
            setError(error.message || "An error occurred while refusing the proposal.");
        }
    };

    return (
        <Paper
            style={{
                padding: "16px",
                textAlign: "center",
                color: "#000",
                backgroundColor: "#f5f5f5",
            }}
            elevation={3}
        >
            {error && (
                <Alert severity="error" sx={{ marginBottom: "16px" }}>
                    {error}
                </Alert>
            )}
            <Typography variant="h6" gutterBottom>
                {proposal.tool.title}
            </Typography>
            <Typography variant="body2" gutterBottom>
                Client: {proposal.client.firstName} {proposal.client.lastName}
            </Typography>
            <Typography variant="body2" gutterBottom>
                Date de Création: {new Date(proposal.creationDate).toLocaleDateString()}
            </Typography>

            {/* Only show Accept/Reject buttons for Admin */}
            {isAdmin && (
                <Box
                    sx={{
                        display: "flex",
                        justifyContent: "center",
                        gap: 2,
                        marginTop: 2,
                    }}
                >
                    <Button
                        variant="contained"
                        color="success"
                        onClick={handleAccept}
                    >
                        Accepter
                    </Button>
                    <Button
                        variant="contained"
                        color="error"
                        onClick={handleReject}
                    >
                        Refuser
                    </Button>
                </Box>
            )}
        </Paper>
    );
}

function DashboardProposals() {
    const [proposals, setProposals] = useState([]);
    const [user, setUser] = useState(null);
    const [error, setError] = useState("");
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchData = async () => {
            try {
                // Fetch proposals
                const proposals = await fetchWithAuth("/api/proposals/all", {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    },
                });
                setProposals(proposals);

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

    const handleStatusChange = (proposalId, status) => {
        setProposals((prevProposals) =>
            prevProposals.filter((proposal) => proposal.id !== proposalId)
        );
    };

    const handleCreateProposal = () => {
        navigate("/formProposal");
    };

    const isAdmin = user?.role === "ADMIN";

    return (
        <Container>
            {error && (
                <Alert severity="error" sx={{ marginBottom: "16px" }}>
                    {error}
                </Alert>
            )}
            <Typography variant="h4" gutterBottom>
                Dashboard des Propositions
            </Typography>

            {/* Only show Create Proposal button for non-admins */}
            {!isAdmin && (
                <Box sx={{ display: "flex", justifyContent: "flex-end", marginBottom: 2 }}>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleCreateProposal}
                    >
                        Créer une nouvelle proposition
                    </Button>
                </Box>
            )}

            {loading ? (
                <Typography variant="h6" gutterBottom>
                    Chargement des propositions...
                </Typography>
            ) : proposals.length === 0 ? (
                <Typography variant="h6" gutterBottom>
                    Aucune proposition trouvée.
                </Typography>
            ) : (
                <Grid container spacing={{ xs: 2, md: 3 }} columns={{ xs: 4, sm: 8, md: 12 }}>
                    {proposals.map((proposal) => (
                        <Grid item xs={2} sm={4} md={4} key={proposal.id}>
                            <Item
                                proposal={proposal}
                                onStatusChange={handleStatusChange}
                                isAdmin={isAdmin}
                            />
                        </Grid>
                    ))}
                </Grid>
            )}
        </Container>
    );
}

export default DashboardProposals;