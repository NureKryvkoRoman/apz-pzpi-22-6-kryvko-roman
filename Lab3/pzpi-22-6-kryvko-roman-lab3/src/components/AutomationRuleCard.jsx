import React from 'react';
import {
  Card,
  CardContent,
  Typography,
  IconButton,
  Box
} from '@mui/material';
import { Delete } from '@mui/icons-material';

const AutomationRuleCard = ({ rule, onDelete }) => {
  return (
    <Card>
      <CardContent>
        <Box display="flex" justifyContent="space-between" alignItems="center">
          <Typography variant="h6">{rule.name}</Typography>
          <IconButton
            color="error"
            onClick={() => onDelete(rule.id)}
            sx={{ ml: 1 }}
          >
            <Delete />
          </IconButton>
        </Box>
        <Typography variant="body2" sx={{ mt: 1 }}>
          Controller ID: {rule.controllerId}
        </Typography>
        <Typography variant="body2">
          Min Value: {rule.minValue}
        </Typography>
        <Typography variant="body2">
          Max Value: {rule.maxValue}
        </Typography>
      </CardContent>
    </Card>
  );
};

export default AutomationRuleCard;
