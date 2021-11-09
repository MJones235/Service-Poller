import { Grid, IconButton, Menu, MenuItem, Paper, Typography } from "@mui/material";
import { useStyles } from "../styles/styles";
import StatusIndicator from "./status";
import MenuIcon from '@mui/icons-material/Menu';
import { useState } from "react";

const ServiceCard = props => {
    const classes = useStyles();
    const [anchorEl, setAnchorEl] = useState(null);
    const open = Boolean(anchorEl);
    const handleOpen = event => setAnchorEl(event.currentTarget);
    const handleClose = event => setAnchorEl(null);

    var xhr;

    const deleteService = () => {
        xhr = new XMLHttpRequest();
        xhr.open("POST", "/services/delete");
        xhr.send(JSON.stringify({ name: props.service.name, url: props.service.url, id: props.service.id }));
        xhr.addEventListener("readystatechange", processRequest, false);
    }

    const processRequest = () => {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                props.sendRequest();
                setAnchorEl(null);
            }
        }
    }

    return(
        <Grid item xs={12} sm={6} md={6} lg={4}>
            <Paper className={classes.paper}>
                <div className={classes.row}>
                    <Typography variant="h4" className={classes.name}>{props.service.name}</Typography>
                    <IconButton>
                        <MenuIcon 
                            onClick={handleOpen}
                            id="button"
                            aria-aria-controls="menu"
                            aria-aria-haspopup="true"
                            aria-expanded={open ? "true" : undefined}
                        />
                    </IconButton>
                    <Menu
                        id="menu"
                        anchorEl={anchorEl}
                        open={open}
                        onClose={handleClose}
                        MenuListProps={{
                        'aria-labelledby': 'button',
                        }}
                    >
                        <MenuItem onClick={handleClose}>Update</MenuItem>
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
        </Grid>
    );
}

export default ServiceCard;