import BackupIcon from '@mui/icons-material/Backup';
import RestoreIcon from '@mui/icons-material/Restore';
import React, { useState } from 'react';
import { Container, Typography, Button, Box, CircularProgress } from '@mui/material';
import { toast } from 'react-toastify';
import { useAuth } from '../context/AuthContext';

const BackupPage = () => {
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState('');

  const handleAction = async (endpoint) => {
    setLoading(true);
    setResult('');
    try {
      const res = await fetch(`http://localhost:8080/api/admin/${endpoint}`, {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${user.accessToken}`,
        },
      });

      if (!res.ok) throw new Error(`Failed to ${endpoint}`);

      const text = await res.text();
      setResult(text);
      toast.success(`${endpoint.charAt(0).toUpperCase() + endpoint.slice(1)} completed`);
    } catch (err) {
      console.error(err);
      toast.error(`Error during ${endpoint}`);
    } finally {
      setLoading(false);
    }
  };

  if (!user || user.role !== 'ADMIN') return null;

  return (
    <Container maxWidth="sm" sx={{ mt: 6 }}>
      <Typography variant="h4" gutterBottom>
        Backup & Restore
      </Typography>

      <Box display="flex" justifyContent="space-between" mt={3} gap={2}>
        <Button
          variant="contained"
          color="primary"
          fullWidth={true}
          onClick={() => handleAction('backup')}
          disabled={loading}
          startIcon={<BackupIcon />}
        >
          Backup
        </Button>
        <Button
          variant="contained"
          color="secondary"
          fullWidth={true}
          onClick={() => handleAction('restore')}
          disabled={loading}
          startIcon={<RestoreIcon />}
        >
          Restore
        </Button>
      </Box>

      {loading && (
        <Box mt={4} textAlign="center">
          <CircularProgress />
        </Box>
      )}

      {result && (
        <Box mt={4}>
          <Typography variant="subtitle1" gutterBottom>
            Result:
          </Typography>
          <Typography variant="body1">{result}</Typography>
        </Box>
      )}
    </Container>
  );
};

export default BackupPage;
