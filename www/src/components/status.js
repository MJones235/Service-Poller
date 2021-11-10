import { Container, Typography } from "@mui/material"
import { useStyles } from "../styles/styles";

const StatusIndicator = props => {
    const classes = useStyles();
    const className = props.status === 'OK' ? classes.ok
        : props.status === 'FAIL' ? classes.fail
        : classes.pending;

    return (
        <Container className={className}>
            <Typography variant="h6">{props.status}</Typography>
        </Container>
    )
}

export default StatusIndicator;

