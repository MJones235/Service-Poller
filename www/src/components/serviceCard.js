import { Grid, IconButton, Menu, MenuItem, Paper, Typography } from "@mui/material";
import { useStyles } from "../styles/styles";
import StatusIndicator from "./status";
import MenuIcon from '@mui/icons-material/Menu';
import { useState } from "react";
import LoadingOverlay from 'react-loading-overlay';

const ServiceCard = props => {
    const classes = useStyles();
    const [anchorEl, setAnchorEl] = useState(null);
    const open = Boolean(anchorEl);
    const handleOpen = event => setAnchorEl(event.currentTarget);
    const handleClose = event => setAnchorEl(null);
    const [loading, setLoading] = useState(false);

    var xhr;

    const deleteService = () => {
        handleClose();
        setLoading(true);
        xhr = new XMLHttpRequest();
        xhr.open("POST", "/services/delete");
        xhr.send(JSON.stringify({ name: props.service.name, url: props.service.url, id: props.service.id }));
        xhr.addEventListener("readystatechange", processRequest, false);
    }

    const processRequest = () => {
        if (xhr.readyState === 4) {
            setLoading(false);
            if (xhr.status === 200) {
                props.sendRequest();
                setAnchorEl(null);
            }
        }
    }

    return(
        <Grid item xs={12} sm={6} md={6} lg={4}>
            <LoadingOverlay
                active={loading}
                spinner
                text='Deleting...'
            >
                <Paper className={classes.paper}>
                    <div className={classes.row}>
                        <Typography variant="h4" className={classes.name}>{props.service.name}</Typography>
                        <IconButton
                            onClick={handleOpen}
                        >
                            <MenuIcon />
                        </IconButton>
                        <Menu
                            anchorEl={anchorEl}
                            open={open}
                            onClose={handleClose}
                        >
                            <MenuItem onClick={deleteService}>Delete</MenuItem>
                        </Menu>
                    </div>
                    <Typography variant="h5" className={classes.url}>{props.service.url}</Typography>
                    <Typography className={classes.createdAt}>Created at {props.service.created}</Typography>
                    <Typography>Last updated at {props.service.lastUpdated}</Typography>
                    <StatusIndicator
                        status={props.service.status}
                    />
                </Paper>
            </LoadingOverlay>
        </Grid>
    );
}

export default ServiceCard;