export default function AddControllerDialog({ }) {
  return (
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
  );
}
