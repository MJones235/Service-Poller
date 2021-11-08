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
    const [error, setError] = useState('');

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
        if (xhr.readyState === 4) {
            console.log(xhr);
            var response = JSON.parse(xhr.responseText.replaceAll("\'", "\""));
            if (xhr.status === 200) {
                props.sendRequest();
                setOpen(false);
                setName('');
                setUrl('');
                setError('');
            } else {
                setError(response['error']);
            }
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
                    <Typography className={classes.error}>{error}</Typography>
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