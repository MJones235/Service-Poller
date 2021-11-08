import './App.css';
import { Grid } from "@mui/material";
import ServiceCard from './components/serviceCard';
import { useStyles } from './styles/styles';
import AddButton from './components/addButton';
import { useEffect, useState } from 'react';


function App() {

  const classes = useStyles();
  const [services, setServices] = useState([]);

  var xhr;

  const sendRequest = () => {
    xhr = new XMLHttpRequest();
    xhr.open("GET", "/api/services");
    xhr.send();
    xhr.addEventListener("readystatechange", processRequest, false);
  }
  const processRequest = () => {
    if (xhr.readyState === 4 & xhr.status === 200) {
      var response = JSON.parse(xhr.responseText);
      setServices(response);
    }
  }

  useEffect(() => {
    sendRequest();
    const interval = setInterval(() => {
      sendRequest();
    }, 10000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="App">
      <Grid container className={classes.grid} spacing={2} rowSpacing={2}>
        <AddButton />
        {
          services.map(service =>
              <ServiceCard  
                key={service.id}
              />
            )
        }
      </Grid>
    </div>
  );
}

export default App;
