import React from 'react';
import {
  Card,
  CardContent,
  Typography,
  IconButton,
  Box,
  Chip
} from '@mui/material';
import { Delete } from '@mui/icons-material';

const ControllerCard = ({ controller, onDelete }) => {
  return (
    <Card>
      <CardContent>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Typography variant="h6">{controller.name}</Typography>
          <Chip
            label={controller.active ? 'Active' : 'Inactive'}
            color={controller.active ? 'success' : 'default'}
            size="small"
          />
        </Box>
        <Typography variant="body2" sx={{ mt: 1 }}>
          Type: {controller.controllerType}
        </Typography>
        <Typography variant="body2">
          Installed At: {new Date(controller.installedAt).toLocaleString()}
        </Typography>
        <IconButton
          color="error"
          onClick={() => onDelete(controller.id)}
          sx={{ mt: 1 }}
        >
          <Delete />
        </IconButton>
      </CardContent>
    </Card>
  );
};

export default ControllerCard;
