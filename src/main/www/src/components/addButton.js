import { Button, Dialog, DialogActions, DialogContent, DialogTitle, Grid, Paper, TextField, Typography } from "@mui/material";
import { useStyles } from "../styles/styles";
import AddIcon from '@mui/icons-material/Add';
import { useState } from "react";

const AddButton = props => {
    const classes = useStyles();
    const [open, setOpen] = useState(false);
    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);

    const [name, setName] = useState('');
    const [url, setUrl] = useState('');

    const handleNameChange = event => setName(event.target.value);
    const handleUrlChange = event => setUrl(event.target.value);

    var xhr;

    const addService = () => {
        xhr = new XMLHttpRequest();
        xhr.open("POST", "/api/services");
        xhr.send(JSON.stringify({ name: name, url: url }));
        xhr.addEventListener("readystatechange", processRequest, false);
    }

    const processRequest = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            setOpen(false);
            setName('');
            setUrl('');
        }
    }

    return (
        <>
            <Grid item xs={12} sm={6} md={6} lg={4}>
                <Paper className={classes.button} onClick={handleOpen}>
                    <div className={classes.row}>
                        <AddIcon style={{fontSize: '125px', padding: '16px'}} />
                        <Typography variant="h3">Add a new service</Typography>
                    </div>
                </Paper>
            </Grid>
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>
                    Add a new service
                </DialogTitle>
                <DialogContent>
                    <TextField
                        label="Name of service"
                        fullWidth
                        variant="filled"
                        value={name}
                        onChange={handleNameChange}
                    />
                    <TextField
                        label="URL"
                        fullWidth
                        variant="filled"
                        style={{marginTop: '8px'}}
                        value={url}
                        onChange={handleUrlChange}
                    />
                </DialogContent>
                <DialogActions>
                    <Button
                        onClick={handleClose}
                    >Cancel</Button>
                    <Button
                        onClick={addService}
                        variant="contained"
                    >Confirm</Button>
                </DialogActions>
            </Dialog>
        </>
    );
}

export default AddButton;