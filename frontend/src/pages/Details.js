import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
    Typography,
    Button,
    Paper,
    TextField,
    List,
    ListItem,
    Alert,
    MenuItem,
    Select,
    FormControl,
    InputLabel,
} from "@mui/material";
import "../css/Details.css";
import { fetchWithAuth } from "../apiHelper";

function Details() {
    const [error, setError] = useState("");
    const navigate = useNavigate();
    const location = useLocation();
    const [tool, setTool] = useState({ ...location.state });
    const [user, setUser] = useState(null);
    const [newStepDescription, setNewStepDescription] = useState("");
    const [newComment, setNewComment] = useState("");

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
            }
        };

        fetchUser();
    }, []);

    const updateTool = async (updatedTool) => {
        try {
            await fetchWithAuth(`/api/tools/${tool.id}/update`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(updatedTool),
            });

            // Update the tool state to reflect the changes
            setTool(updatedTool);
        } catch (error) {
            setError(error.message);
        }
    };

    const handleAddStep = async () => {
        if (newStepDescription.trim()) {
            // Add new step with correct order
            const updatedSteps = [
                ...tool.steps,
                {
                    order: tool.steps.length + 1, // Dynamically calculate the new step order
                    description: newStepDescription.trim(),
                },
            ];

            const updatedTool = { ...tool, steps: updatedSteps };
            setTool(updatedTool);

            await updateTool(updatedTool);

            setNewStepDescription("");
        }
    };

    const handleDeleteStep = async (index) => {
        const updatedSteps = tool.steps
            .filter((_, stepIndex) => stepIndex !== index)
            .map((step, order) => ({ ...step, order: order + 1 }));

        const updatedTool = { ...tool, steps: updatedSteps };
        setTool(updatedTool);

        await updateTool(updatedTool);
    };

    const handleAddComment = async () => {
        if (newComment.trim()) {
            const updatedFeedbacks = [
                ...tool.feedbacks,
                { owner: user.lastName, description: newComment.trim() },
            ];

            const updatedTool = { ...tool, feedbacks: updatedFeedbacks };
            setTool(updatedTool);

            await updateTool(updatedTool);

            setNewComment("");
        }
    };

    const handleUpdateTool = async () => {
        await updateTool(tool);
    };

    const isEditable = user?.role === "ADMIN";

    return (
        <div className="details-container">
            <Button
                onClick={() => navigate("/dashboardTools")}
                variant="outlined"
                className="back-button"
            >
                Back to Dashboard
            </Button>

            <Paper elevation={3} className="details-box">
                {error && (
                    <Alert severity="error" sx={{ marginBottom: "16px" }}>
                        {error}
                    </Alert>
                )}
                {/* Tool Details */}
                <Typography variant="h4" gutterBottom>
                    Tool Details
                </Typography>

                {/* Title */}
                <TextField
                    label="Title"
                    value={tool.title}
                    onChange={(e) =>
                        isEditable &&
                        setTool((prev) => ({
                            ...prev,
                            title: e.target.value,
                        }))
                    }
                    fullWidth
                    variant="outlined"
                    InputProps={{
                        readOnly: !isEditable,
                        style: {
                            backgroundColor: isEditable ? "white" : "#f0f0f0",
                        },
                    }}
                    sx={{ marginBottom: "16px" }}
                />

                {/* Domain Type */}
                <FormControl fullWidth sx={{ marginBottom: "16px" }}>
                    <InputLabel id="domain-type-label">Domain Type</InputLabel>
                    <Select
                        labelId="domain-type-label"
                        value={tool.domainType}
                        onChange={(e) =>
                            isEditable &&
                            setTool((prev) => ({
                                ...prev,
                                domainType: e.target.value,
                            }))
                        }
                        fullWidth
                        disabled={!isEditable}
                        sx={{
                            backgroundColor: isEditable ? "white" : "#f0f0f0",
                        }}
                    >
                        {[
                            "SERVICES",
                            "DEVELOPMENT",
                            "CYBERSECURITY",
                            "CLOUD",
                            "E_LEARNING",
                            "MATHEMATICS",
                            "ELECTRONICS",
                            "DATA_SCIENCE",
                            "PROJECT_MANAGEMENT",
                            "DOCUMENT_GENERATOR",
                            "NETWORK",
                            "DATABASE",
                            "VIRTUAL_REALITY",
                            "DOCKER",
                            "VIRTUAL_ENVIRONMENT",
                        ].map((domain) => (
                            <MenuItem key={domain} value={domain}>
                                {domain}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>

                {/* Description */}
                <TextField
                    label="Description"
                    value={tool.description}
                    onChange={(e) =>
                        isEditable &&
                        setTool((prev) => ({
                            ...prev,
                            description: e.target.value,
                        }))
                    }
                    fullWidth
                    multiline
                    variant="outlined"
                    InputProps={{
                        readOnly: !isEditable,
                        style: {
                            backgroundColor: isEditable ? "white" : "#f0f0f0",
                        },
                    }}
                    sx={{ marginBottom: "16px" }}
                />

                {/* Link */}
                <TextField
                    label="Link"
                    value={tool.link}
                    onChange={(e) =>
                        isEditable &&
                        setTool((prev) => ({
                            ...prev,
                            link: e.target.value,
                        }))
                    }
                    fullWidth
                    variant="outlined"
                    InputProps={{
                        readOnly: !isEditable,
                        style: {
                            backgroundColor: isEditable ? "white" : "#f0f0f0",
                        },
                    }}
                    sx={{ marginBottom: "16px" }}
                />

                {/* Steps */}
                <Typography variant="h6" sx={{ marginTop: "16px" }}>
                    Steps
                </Typography>
                <List>
                    {tool.steps.map((step, index) => (
                        <ListItem
                            key={index}
                            sx={{
                                backgroundColor: isEditable ? "white" : "#f0f0f0",
                                borderRadius: "4px",
                                marginBottom: "8px",
                                display: "flex",
                                justifyContent: "space-between",
                            }}
                        >
                            {`Step ${step.order}: ${step.description}`}
                            {isEditable && (
                                <Button
                                    onClick={() => handleDeleteStep(index)}
                                    variant="outlined"
                                    color="error"
                                    size="small"
                                >
                                    Delete
                                </Button>
                            )}
                        </ListItem>
                    ))}
                </List>

                {isEditable && (
                    <div style={{ display: "flex", gap: "8px", marginTop: "8px" }}>
                        <TextField
                            label="New Step"
                            value={newStepDescription}
                            onChange={(e) => setNewStepDescription(e.target.value)}
                            fullWidth
                            variant="outlined"
                        />
                        <Button
                            onClick={handleAddStep}
                            variant="contained"
                        >
                            Add Step
                        </Button>
                    </div>
                )}
            </Paper>

            {/* Comments Section */}
            <div className="comments-section">
                <Typography variant="h5" gutterBottom>
                    Comments
                </Typography>
                <div className="comment-list">
                    {tool.feedbacks.map((feedback, index) => (
                        <ListItem key={index} className="comment-item">
                            {`${feedback.owner}: ${feedback.description}`}
                        </ListItem>
                    ))}
                </div>

                {!isEditable && (
                    <div className="comment-input">
                        <TextField
                            label="Add a comment"
                            variant="outlined"
                            value={newComment}
                            onChange={(e) => setNewComment(e.target.value)}
                            fullWidth
                        />
                        <Button
                            onClick={handleAddComment}
                            variant="contained"
                            className="comment-button"
                        >
                            Add
                        </Button>
                    </div>
                )}
            </div>

            {isEditable && (
                <Button
                    onClick={handleUpdateTool}
                    variant="contained"
                    className="update-tool-button"
                >
                    Update Tool
                </Button>
            )}
        </div>
    );
}

export default Details;