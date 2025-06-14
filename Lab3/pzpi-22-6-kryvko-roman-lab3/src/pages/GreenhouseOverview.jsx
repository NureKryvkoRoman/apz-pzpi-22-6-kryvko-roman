import React, { useEffect, useState } from 'react';
import {
  Container,
  Typography,
  Grid,
  TextField,
  Button,
  Box,
  Paper,
  Dialog,
  DialogActions,
  MenuItem,
  DialogContent,
  DialogTitle
} from '@mui/material';
import { useParams } from 'react-router';
import { toast } from 'react-toastify';
import { useAuth } from '../context/AuthContext';
import SensorCard from '../components/SensorCard';
import ControllerCard from '../components/ControllerCard';
import AutomationRuleCard from '../components/AutomationRuleCard';

const GreenhouseOverview = () => {
  const { greenhouseId } = useParams();
  const [greenhouse, setGreenhouse] = useState(null);
  const [sensors, setSensors] = useState([]);
  const [controllers, setControllers] = useState([]);
  const [rules, setRules] = useState([]);

  const [editing, setEditing] = useState(false);
  const [formData, setFormData] = useState({ name: '', latitude: 0, longitude: 0 });
  const [addDialogOpen, setAddDialogOpen] = useState(false);
  const [newSensor, setNewSensor] = useState({
    name: '',
    sensorType: 'TEMPERATURE'
  });
  const [newController, setNewController] = useState({
    name: '',
    controllerType: 'IRRIGATION',
  });
  const [newRule, setNewRule] = useState({
    name: '',
    controllerId: '',
    minValue: 0,
    maxValue: 0
  });
  const [addControllerDialogOpen, setAddControllerDialogOpen] = useState(false);
  const [addRuleDialogOpen, setAddRuleDialogOpen] = useState(false);

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
        setControllers(data.controllers);
        setRules(data.automationRules);

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

  const handleControllerChange = (e) => {
    setNewController(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleAddController = async () => {
    try {
      const controllerData = {
        ...newController,
        greenhouse: { id: parseInt(greenhouseId) },
        active: true,
      };

      await fetch('http://localhost:8080/api/controllers', {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${user.accessToken}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(controllerData),
      });

      setAddControllerDialogOpen(false);
      setNewController({ name: '', controllerType: 'IRRIGATION' });
      fetchControllers();
    } catch (err) {
      console.error("Failed to add controller", err);
      toast.error("Failed to add controller");
    }
  };

  const handleRuleChange = (e) => {
    setNewRule(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleAddRule = async () => {
    try {
      const ruleData = {
        ...newRule,
        greenhouse: { id: parseInt(greenhouseId) },
        controller: { id: parseInt(newRule.controllerId) },
        minValue: parseFloat(newRule.minValue),
        maxValue: parseFloat(newRule.maxValue)
      };

      await fetch('http://localhost:8080/api/automation-rules', {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${user.accessToken}`,
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(ruleData),
      });

      setAddRuleDialogOpen(false);
      setNewRule({ name: '', controllerId: '', minValue: 0, maxValue: 0 });
      fetchRules();
    } catch (err) {
      console.error("Failed to add automation rule", err);
      toast.error("Failed to add automation rule");
    }
  };

  const fetchRules = async () => {
    try {
      const res = await fetch(`http://localhost:8080/api/automation-rules/greenhouse/${greenhouseId}`,
        {
          headers: {
            Authorization: `Bearer ${user.accessToken}`,
          }
        })
      if (res.ok) {
        setRules(await res.json());
      } else {
        console.log(res);
        toast.error("Failed to fetch automation rules.");
      }
    } catch (err) {
      console.error(err);
      toast.error("Failed to fetch automation rules.");
    }
  }

  const fetchControllers = async () => {
    try {
      const res = await fetch(`http://localhost:8080/api/controllers/greenhouse/${greenhouseId}`,
        {
          headers: {
            Authorization: `Bearer ${user.accessToken}`,
          }
        })
      if (res.ok) {
        setControllers(await res.json());
      } else {
        console.log(res);
        toast.error("Failed to fetch controllers.");
      }
    } catch (err) {
      console.error(err);
      toast.error("Failed to fetch controllers.");
    }
  }

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

  const handleDeleteController = async (controllerId) => {
    try {
      await fetch(`http://localhost:8080/api/controllers/${controllerId}`, {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${user.accessToken}`,
        }
      });
      fetchControllers();
    } catch (err) {
      console.error("Failed to delete controller", err);
      toast.error("Failed to delete controller");
    }
  };

  const handleDeleteRule = async (ruleId) => {
    try {
      const res = await fetch(`http://localhost:8080/api/automation-rules/${ruleId}`, {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${user.accessToken}`,
        }
      });
      console.log(res);
      fetchRules();
    } catch (err) {
      console.error("Failed to delete automation rule", err);
      toast.error("Failed to delete automation rule");
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
              <SensorCard
                sensor={sensor}
                onDelete={() => handleDeleteSensor(sensor.id)}
              />
            </Grid>
          ))}
        </Grid>
      </Box>
      <Box mt={5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Typography variant="h5" gutterBottom>Controllers</Typography>
          <Button
            variant="outlined"
            sx={{ mt: 4 }}
            onClick={() => setAddControllerDialogOpen(true)}
          >
            Add Controller
          </Button>

        </Box>
        <Grid container spacing={2}>
          {controllers.map(controller => (
            <Grid size={{ xs: 12, sm: 6, md: 4 }} key={controller.id}>
              <ControllerCard
                controller={controller}
                onDelete={() => handleDeleteController(controller.id)}
              />
            </Grid>
          ))}
        </Grid>
      </Box>
      <Box mt={5}>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Typography variant="h5" gutterBottom>Automation Rules</Typography>
          <Button
            variant="outlined"
            sx={{ mt: 4 }}
            onClick={() => setAddRuleDialogOpen(true)}
          >
            Add Rule
          </Button>

        </Box>
        <Grid container spacing={2}>
          {rules.map(rule => (
            <Grid size={{ xs: 12, sm: 6, md: 4 }} key={rule.id}>
              <AutomationRuleCard
                rule={rule}
                onDelete={() => handleDeleteRule(rule.id)}
              />
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
      <Dialog open={addControllerDialogOpen} onClose={() => setAddControllerDialogOpen(false)}>
        <DialogTitle>Add New Controller</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Controller Name"
            name="name"
            value={newController.name}
            onChange={handleControllerChange}
            fullWidth
          />
          <TextField
            select
            label="Controller Type"
            name="controllerType"
            value={newController.controllerType}
            onChange={handleControllerChange}
            fullWidth
            margin="dense"
          >
            <MenuItem value="IRRIGATION">Irrigation</MenuItem>
            <MenuItem value="VENTILATION">Ventilation</MenuItem>
            <MenuItem value="LIGHTING">Lighting</MenuItem>
          </TextField>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setAddControllerDialogOpen(false)}>Cancel</Button>
          <Button onClick={handleAddController} variant="contained">Add</Button>
        </DialogActions>
      </Dialog>
      <Dialog open={addRuleDialogOpen} onClose={() => setAddRuleDialogOpen(false)}>
        <DialogTitle>Add Automation Rule</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            label="Rule Name"
            name="name"
            value={newRule.name}
            onChange={handleRuleChange}
            fullWidth
          />
          <TextField
            select
            label="Controller"
            name="controllerId"
            value={newRule.controllerId}
            onChange={handleRuleChange}
            fullWidth
            margin="dense"
          >
            {controllers.map(controller => (
              <MenuItem key={controller.id} value={controller.id}>
                {controller.name}
              </MenuItem>
            ))}
          </TextField>
          <TextField
            label="Min Value"
            name="minValue"
            type="number"
            value={newRule.minValue}
            onChange={handleRuleChange}
            fullWidth
            margin="dense"
          />
          <TextField
            label="Max Value"
            name="maxValue"
            type="number"
            value={newRule.maxValue}
            onChange={handleRuleChange}
            fullWidth
            margin="dense"
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setAddRuleDialogOpen(false)}>Cancel</Button>
          <Button onClick={handleAddRule} variant="contained">Add</Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default GreenhouseOverview;
