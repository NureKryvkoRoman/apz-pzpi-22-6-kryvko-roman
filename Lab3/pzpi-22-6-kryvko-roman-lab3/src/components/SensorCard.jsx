import React from 'react';
import { Card, CardContent, Typography, IconButton } from '@mui/material';
import { Delete } from '@mui/icons-material';

const SensorCard = ({ sensor, onDelete }) => {
  return (
    <Card>
      <CardContent>
        <Typography variant="h6">{sensor.name}</Typography>
        <Typography variant="body2">Type: {sensor.sensorType}</Typography>
        <Typography variant="body2">Status: {sensor.isActive ? 'Active' : 'Inactive'}</Typography>
        <IconButton
          color="error"
          onClick={() => onDelete(sensor.id)}
          sx={{ mt: 1 }}
        >
          <Delete />
        </IconButton>
      </CardContent>
    </Card>
  );
};

export default SensorCard;
