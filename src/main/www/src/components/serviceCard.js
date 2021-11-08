import { Grid, Paper, Typography } from "@mui/material";
import { useStyles } from "../styles/styles";
import { status } from "../utils/enums";
import StatusIndicator from "./status";

const ServiceCard = props => {
    const classes = useStyles();

    return(
        <Grid item xs={12} sm={6} md={6} lg={4}>
            <Paper className={classes.paper}>
                <Typography variant="h4" className={classes.name}>{props.service.name}</Typography>
                <Typography variant="h5" className={classes.url}>{props.service.url}</Typography>
                <Typography className={classes.createdAt}>Created at {props.service.created}</Typography>
                <Typography>Last updated at {props.service.lastUpdated}</Typography>
                <StatusIndicator
                    status={status.OK}
                />
            </Paper>
        </Grid>
    );
}

export default ServiceCard;