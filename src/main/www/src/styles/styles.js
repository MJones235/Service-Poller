import { makeStyles } from "@mui/styles";

export const useStyles = makeStyles({
    grid: {
        padding: '16px'
    },
    paper: {
        padding: '16px',
        height: 'calc(100% - 32px)'
    },
    button: {
        padding: '16px',
        height: 'calc(100% - 32px)',
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center',
        '&:hover': {
            backgroundColor: 'lightgrey',
            cursor: 'pointer'
        }
    },
    url: {
        color: 'grey'
    },
    createdAt: {
        paddingTop: '16px'
    },
    ok: {
        border: '4px solid green',
        color: 'green',
        padding: '8px',
        marginTop: '16px',
        textAlign: 'center',
        borderRadius: '4px',
        backgroundColor: 'lightgreen'
    },
    fail: {
        border: '4px solid red',
        color: 'red',
        padding: '8px',
        marginTop: '16px',
        textAlign: 'center',
        borderRadius: '4px',
        backgroundColor: 'lightpink'
    },
    row: {
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center'
    },
});