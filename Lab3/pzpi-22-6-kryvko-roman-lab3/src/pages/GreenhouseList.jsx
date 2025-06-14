import React, { useEffect, useState } from 'react';
import {
  Grid,
  Card,
  CardContent,
  Typography,
  Container,
  CircularProgress,
  Box
} from '@mui/material';
import { toast } from 'react-toastify';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router';

const GreenhouseList = () => {
  const { user } = useAuth();
  const [greenhouses, setGreenhouses] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const fetchGreenhouses = async () => {
    setLoading(true);
    try {
      const res = await fetch(
        `http://localhost:8080/api/greenhouses/summary/user/${user.id}`,
        {
          headers: {
            Authorization: `Bearer ${user.accessToken}`
          }
        }
      );

      if (!res.ok) throw new Error('Failed to fetch greenhouses');
      const data = await res.json();
      console.log(data)
      setGreenhouses(data);
    } catch (err) {
      console.error(err);
      toast.error('Error loading greenhouses.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (user?.email) fetchGreenhouses();
  }, [user]);

  if (!user) return null;

  return (
    <Container maxWidth="lg" sx={{ mt: 6 }}>
      <Typography variant="h4" gutterBottom>
        My Greenhouses
      </Typography>

      {loading ? (
        <Box display="flex" justifyContent="center" mt={4}>
          <CircularProgress />
        </Box>
      ) : greenhouses.length === 0 ? (
        <Typography variant="body1">You don’t have any greenhouses yet.</Typography>
      ) : (
        <Grid container spacing={3} mt={2}>
          {greenhouses.map((gh) => (
            <Grid size={{ xs: 12, sm: 6, md: 4 }} key={gh.id}>
              <Card
                sx={{ height: '100%' }}
                onClick={() => navigate(`/greenhouses/${gh.id}`)}
                style={{ cursor: 'pointer' }}
              >
                <CardContent>
                  <Typography variant="h6" gutterBottom>
                    {gh.name || `Greenhouse #${gh.id}`}
                  </Typography>
                  <Box textAlign="left">
                    <Typography variant="body2" color="text.secondary">
                      Created: {gh.createdAt ? new Date(gh.createdAt).toLocaleString() : 'no data'}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Latitude: {gh.latitude}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Longitude: {gh.longitude}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Sensors: {gh.sensorCount}
                    </Typography>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}
    </Container>
  );
};

export default GreenhouseList;
