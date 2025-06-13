import React, { useEffect, useState } from 'react';
import {
  Container,
  Typography,
  Card,
  CardContent,
  Grid,
  TextField,
  Button,
  Box,
  Paper,
  IconButton,
  Dialog,
  DialogActions,
  MenuItem,
  DialogContent,
  DialogTitle
} from '@mui/material';
import { Delete } from '@mui/icons-material';
import { useParams } from 'react-router';
import { toast } from 'react-toastify';
import { useAuth } from '../context/AuthContext';

const GreenhouseOverview = () => {
  const { greenhouseId } = useParams();
  const [greenhouse, setGreenhouse] = useState(null);
  const [sensors, setSensors] = useState([]);
  const [editing, setEditing] = useState(false);
  const [formData, setFormData] = useState({ name: '', latitude: 0, longitude: 0 });
  const [addDialogOpen, setAddDialogOpen] = useState(false);
  const [newSensor, setNewSensor] = useState({
    name: '',
    sensorType: 'TEMPERATURE'
  });

  const { user } = useAuth();

  useEffect(() => {
    if (!user?.accessToken) return;
    const fetchData = async () => {
      try {
        const res = await fetch(
          `http://localhost:8080/api/greenhouses/${greenhouseId}`,
          {
            headers: {
              Authorization: `Bearer ${user.accessToken}`
            }
          }
        );
        if (!res.ok) {
          console.log(res);
          throw new Error('Failed to fetch greenhouses');
        }
        const data = await res.json();
        console.log(data)
        setGreenhouse(data);
        setSensors(data.sensors);
        setFormData({ name: data.name, latitude: data.latitude, longitude: data.longitude });
      } catch (err) {
        console.error(err);
        toast.error('Error loading greenhouse.');
      }
    };
    fetchData();
  }, [user, greenhouseId]);

  const handleFieldChange = (e) => {
    setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSensorChange = (e) => {
    setNewSensor({ ...newSensor, [e.target.name]: e.target.value });
  };

  const handleSave = async () => {
    setEditing(false);

    const greenhouseData = {
      ...formData,
      id: greenhouseId,
      sensors: sensors,
      user: { id: user.id },
      createadAt: greenhouse.createdAt
    };
    console.log('Saving greenhouse:', greenhouseData);

    try {
      const res = await fetch(
        `http://localhost:8080/api/greenhouses/${greenhouseId}`,
        {
          method: 'PATCH',
          headers: {
            Authorization: `Bearer ${user.accessToken}`,
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(greenhouseData)
        });
      console.log(res);

      if (res.bodyUsed) {
        const data = await res.json();
        console.log(data);
      }
      if (res.ok) {
        toast.success("Greenhouse updates successfully!");
      } else {
        toast.error("Failed to update greenhouse");
      }
    } catch (err) {
      console.error(err);
    }
  };

  const fetchSensors = async () => {
    try {
      const res = await fetch(`http://localhost:8080/api/sensors/greenhouse/${greenhouseId}`,
        {
          headers: {
            Authorization: `Bearer ${user.accessToken}`,
          }
        })
      if (res.ok) {
        setSensors(await res.json());
      } else {
        console.log(res);
        toast.error("Failed to fetch sensors");
      }
    } catch (err) {
      console.error(err);
      toast.error("Failed to fetch sensors");
    }
  }

  const handleAddSensor = async () => {
    try {
      const data = {
        ...newSensor,
        greenhouse: { id: greenhouse.id },
        isActive: true
      };
      console.log(data)
      await fetch('http://localhost:8080/api/sensors', {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${user.accessToken}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      });
      setAddDialogOpen(false);
      setNewSensor({ name: '', sensorType: 'TEMPERATURE' });
      fetchSensors();
    } catch (err) {
      console.error("Failed to add sensor", err);
    }
  };

  const handleDeleteSensor = async (sensorId) => {
    try {
      await fetch(`http://localhost:8080/api/sensors/${sensorId}`, {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${user.accessToken}`,
        }
      });
      fetchSensors();
    } catch (err) {
      console.error("Failed to delete sensor", err);
    }
  };

  if (!greenhouse) return null;

  return (
    <Container maxWidth="md" sx={{ mt: 4 }}>
      <Paper sx={{ p: 4 }}>
        <Typography variant="h4" gutterBottom>Greenhouse Overview</Typography>
        <Box display="flex" flexDirection="column" gap={2}>
          <TextField
            label="Name"
            name="name"
            value={formData.name}
            onChange={handleFieldChange}
            disabled={!editing}
            fullWidth
          />
          <TextField
            label="Latitude"
            name="latitude"
            value={formData.latitude}
            onChange={handleFieldChange}
            disabled={!editing}
            fullWidth
          />
          <TextField
            label="Longitude"
            name="longitude"
            value={formData.longitude}
            onChange={handleFieldChange}
            disabled={!editing}
            fullWidth
          />

          {editing ? (
            <Box>
              <Button variant="contained" color="success" onClick={handleSave}>Save</Button>
              <Button variant="text" sx={{ ml: 2 }} onClick={() => setEditing(false)}>Cancel</Button>
            </Box>
          ) : (
            <Button variant="contained" onClick={() => setEditing(true)}>Edit</Button>
          )}
        </Box>
      </Paper>

      <Box mt={5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Typography variant="h5" gutterBottom>Sensors</Typography>
          <Button
            variant="outlined"
            sx={{ mt: 4 }}
            onClick={() => setAddDialogOpen(true)}
          >
            Add Sensor
          </Button>

        </Box>
        <Grid container spacing={2}>
          {sensors.map(sensor => (
            <Grid size={{ xs: 12, sm: 6, md: 4 }} key={sensor.id}>
              <Card>
                <CardContent>
                  <Typography variant="h6">{sensor.name}</Typography>
                  <Typography variant="body2">Type: {sensor.sensorType}</Typography>
                  <Typography variant="body2">Status: {sensor.isActive ? 'Active' : 'Inactive'}</Typography>
                  <IconButton
                    color="error"
                    onClick={() => handleDeleteSensor(sensor.id)}
                    sx={{ mt: 1 }}
                  >
                    <Delete />
                  </IconButton>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Box>
      <Dialog open={addDialogOpen} onClose={() => setAddDialogOpen(false)}>
        <DialogTitle>Add New Sensor</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Sensor Name"
            name="name"
            value={newSensor.name}
            onChange={handleSensorChange}
            fullWidth
          />
          <TextField
            select
            label="Sensor Type"
            name="sensorType"
            value={newSensor.sensorType}
            onChange={handleSensorChange}
            fullWidth
            margin="dense"
          >
            <MenuItem value="TEMPERATURE">Temperature</MenuItem>
            <MenuItem value="HUMIDITY">Humidity</MenuItem>
            <MenuItem value="LIGHT">Light</MenuItem>
          </TextField>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setAddDialogOpen(false)}>Cancel</Button>
          <Button onClick={handleAddSensor} variant="contained">Add</Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default GreenhouseOverview;
