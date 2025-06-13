import React, { useEffect, useState } from 'react';
import {
  Container, TextField, Button, Typography, Paper, Box, CircularProgress
} from '@mui/material';
import { toast } from 'react-toastify';
import axios from 'axios';
import { useAuth } from '../context/AuthContext';

const UserProfile = () => {
  const { auth } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [editing, setEditing] = useState(false);
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    phoneNumber: ''
  });

  const fetchProfile = async () => {
    setLoading(true);
    try {
      const res = await axios.get(`http://localhost:8080/api/userinfo/email?email=${auth.email}`);
      setProfile(res.data);
      setFormData({
        firstName: res.data.firstName || '',
        lastName: res.data.lastName || '',
        phoneNumber: res.data.phoneNumber || ''
      });
    } catch (err) {
      if (err.response?.status === 404) {
        toast.info("Profile not found. You can create one.");
        setProfile(null);
      } else {
        toast.error("Failed to load profile.");
      }
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setFormData(prev => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleSave = async () => {
    try {
      if (profile) {
        // Update existing
        await axios.post(`http://localhost:8080/api/userinfo/${profile.id}`, {
          ...formData,
          user: { id: auth.id }
        });
        toast.success("Profile updated successfully.");
      } else {
        // Create new
        const res = await axios.post(`http://localhost:8080/api/userinfo`, {
          ...formData,
          user: { id: auth.id }
        });
        toast.success("Profile created successfully.");
        setProfile(res.data);
      }
      setEditing(false);
      fetchProfile(); // Refresh after save
    } catch (err) {
      toast.error("Failed to save profile.");
    }
  };

  useEffect(() => {
    fetchProfile();
  }, []);

  if (loading) {
    return (
      <Container sx={{ mt: 8, textAlign: 'center' }}>
        <CircularProgress />
      </Container>
    );
  }

  return (
    <Container maxWidth="sm">
      <Paper sx={{ p: 4, mt: 6 }}>
        <Typography variant="h4" gutterBottom>User Profile</Typography>

        <Box component="form" noValidate autoComplete="off" sx={{ mt: 2 }}>
          <TextField
            label="First Name"
            name="firstName"
            fullWidth
            margin="normal"
            value={formData.firstName}
            onChange={handleChange}
            disabled={!editing}
          />
          <TextField
            label="Last Name"
            name="lastName"
            fullWidth
            margin="normal"
            value={formData.lastName}
            onChange={handleChange}
            disabled={!editing}
          />
          <TextField
            label="Phone Number"
            name="phoneNumber"
            fullWidth
            margin="normal"
            value={formData.phoneNumber}
            onChange={handleChange}
            disabled={!editing}
          />

          <Box sx={{ mt: 3, display: 'flex', justifyContent: 'space-between' }}>
            {editing ? (
              <>
                <Button variant="contained" color="success" onClick={handleSave}>Save</Button>
                <Button variant="outlined" color="inherit" onClick={() => setEditing(false)}>Cancel</Button>
              </>
            ) : (
              <Button variant="contained" onClick={() => setEditing(true)}>
                {profile ? 'Edit Profile' : 'Create Profile'}
              </Button>
            )}
          </Box>
        </Box>
      </Paper>
    </Container>
  );
};

export default UserProfile;
