import './App.css';
import { Grid } from "@mui/material";
import ServiceCard from './components/serviceCard';
import { useStyles } from './styles/styles';
import AddButton from './components/addButton';


function App() {

  const classes = useStyles();

  return (
    <div className="App">
      <Grid container className={classes.grid} spacing={2} rowSpacing={2}>
        <AddButton />
        <ServiceCard />
        <ServiceCard />
      </Grid>
    </div>
  );
}

export default App;
