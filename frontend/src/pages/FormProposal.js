import React, { useState } from "react";
import { Container, TextField, Button, Typography, Box, Select, MenuItem, FormControl, InputLabel } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { fetchWithAuth } from "../apiHelper";

function FormProposal() {
    const [error, setError] = useState("");
    const [title, setTitle] = useState("");
    const [domainType, setDomainType] = useState("");
    const [description, setDescription] = useState("");
    const [link, setLink] = useState("");
    const [steps, setSteps] = useState([]);
    const navigate = useNavigate();

    const domainTypes = [
        "SERVICES", "DEVELOPMENT", "CYBERSECURITY", "CLOUD", "E_LEARNING", "MATHEMATICS",
        "ELECTRONICS", "DATA_SCIENCE", "PROJECT_MANAGEMENT", "DOCUMENT_GENERATOR", "NETWORK",
        "DATABASE", "VIRTUAL_REALITY", "DOCKER", "VIRTUAL_ENVIRONMENT"
    ];

    const addStep = () => {
        setSteps((prevSteps) => [
            ...prevSteps,
            { order: prevSteps.length + 1, description: "" },
        ]);
    };

    const handleStepChange = (index, value) => {
        setSteps((prevSteps) =>
            prevSteps.map((step, i) =>
                i === index ? { ...step, description: value } : step
            )
        );
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await fetchWithAuth("/api/proposals/create", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    title,
                    domainType,
                    description,
                    link,
                    steps
                }),
            });

            navigate("/dashboard");
        } catch (err) {
            setError(error.message || "An error occurred while creating the proposal.");
        }
    };

    return (
        <Container>
            <Typography variant="h4" gutterBottom>
                Créer une nouvelle proposition
            </Typography>
            {error && (
                <Typography color="error" gutterBottom>
                    {error}
                </Typography>
            )}
            <Box
                component="form"
                onSubmit={handleSubmit}
                sx={{ display: "flex", flexDirection: "column", gap: 2, mt: 2 }}
            >
                <TextField
                    label="Titre"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    required
                />
                <FormControl required>
                    <InputLabel>Type de domaine</InputLabel>
                    <Select
                        value={domainType}
                        onChange={(e) => setDomainType(e.target.value)}
                        label="Type de domaine"
                    >
                        {domainTypes.map((type, index) => (
                            <MenuItem key={index} value={type}>
                                {type}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>
                <TextField
                    label="Description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    multiline
                    rows={4}
                />
                <TextField
                    label="Lien"
                    value={link}
                    onChange={(e) => setLink(e.target.value)}
                />
                <Typography variant="h6">Étapes</Typography>
                {steps.map((step, index) => (
                    <Box key={index} sx={{ display: "flex", gap: 2, alignItems: "center" }}>
                        <Typography>{step.order}</Typography>
                        <TextField
                            label={`Description de l'étape ${step.order}`}
                            value={step.description}
                            onChange={(e) => handleStepChange(index, e.target.value)}
                            required
                        />
                    </Box>
                ))}
                <Button variant="outlined" onClick={addStep}>
                    Ajouter une étape
                </Button>
                <Button
                    variant="contained"
                    color="primary"
                    type="submit"
                >
                    Soumettre
                </Button>
            </Box>
        </Container>
    );
}

export default FormProposal;