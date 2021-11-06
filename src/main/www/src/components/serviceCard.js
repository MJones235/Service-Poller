import { Grid, Paper, Typography } from "@mui/material";
import { useStyles } from "../styles/styles";
import { status } from "../utils/enums";
import StatusIndicator from "./status";

const ServiceCard = props => {
    const classes = useStyles();

    return(
        <Grid item xs={12} sm={6} md={6} lg={4}>
            <Paper className={classes.paper}>
                <Typography variant="h4" className={classes.name}>Placeholder Name</Typography>
                <Typography variant="h5" className={classes.url}>http://someurl.com</Typography>
                <Typography className={classes.createdAt}>Created at 01/01/2021 21:22:20</Typography>
                <Typography>Last updated at 01/01/2021 21:22:20</Typography>
                <StatusIndicator
                    status={status.OK}
                />
            </Paper>
        </Grid>
    );
}

export default ServiceCard;