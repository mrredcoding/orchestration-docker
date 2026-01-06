import React from "react";
import { Box, Typography } from "@mui/material";

function Footer() {
    return (
        <Box component="footer" className="footer-container">
            <Typography variant="body2" className="footer-text">
                &copy; {new Date().getFullYear()} HOPE. All Rights Reserved.
            </Typography>
            <Typography variant="body2" className="footer-italic">
                Crafted with ❤️ by La Team
            </Typography>
        </Box>
    );
}

export default Footer;
