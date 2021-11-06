import { Container, Typography } from "@mui/material"
import { useStyles } from "../styles/styles";
import { status } from "../utils/enums";

const StatusIndicator = props => {
    const classes = useStyles();
    const className = props.status === status.OK ? classes.ok
        : props.status === status.FAIL ? classes.fail
        : '';

    return (
        <Container className={className}>
            <Typography variant="h6">{props.status}</Typography>
        </Container>
    )
}

export default StatusIndicator;

