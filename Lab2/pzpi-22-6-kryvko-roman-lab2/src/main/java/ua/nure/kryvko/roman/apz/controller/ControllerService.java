package ua.nure.kryvko.roman.apz.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ua.nure.kryvko.roman.apz.greenhouse.Greenhouse;
import ua.nure.kryvko.roman.apz.greenhouse.GreenhouseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ControllerService {

    private final ControllerRepository controllerRepository;
    private final GreenhouseRepository greenhouseRepository;

    @Autowired
    public ControllerService(ControllerRepository controllerRepository, GreenhouseRepository greenhouseRepository) {
        this.controllerRepository = controllerRepository;
        this.greenhouseRepository = greenhouseRepository;
    }

    @Transactional
    public Controller createController(Controller controller) {
        if (controller.getGreenhouse() == null || !greenhouseRepository.existsById(controller.getGreenhouse().getId())) {
            throw new IllegalArgumentException("Greenhouse must exist to create a controller.");
        }

        Greenhouse owner = greenhouseRepository.findById(controller.getGreenhouse().getId())
                .orElseThrow(() -> new IllegalArgumentException("Greenhouse must exist to create a controller."));
        controller.setGreenhouse(owner);
        return controllerRepository.save(controller);
    }

    public Optional<Controller> getControllerById(Integer id) {
        return controllerRepository.findById(id);
    }

    public List<Controller> getControllerByGreenhouseId(Integer id) {
        return controllerRepository.findByGreenhouseId(id);
    }

    public List<Controller> getAllControllers() {
        return controllerRepository.findAll();
    }

    @Transactional
    public Controller updateController(Integer id, Controller controller) {
        controller.setId(id);
        if (!controllerRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Controller not found for update");
        }

        Greenhouse owner = greenhouseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Greenhouse must exist to create a controller."));
        controller.setGreenhouse(owner);

        controllerRepository.save(controller);
        return controller;
    }

    @Transactional
    public void deleteController(Integer id) {
        if (!controllerRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Controller not found for deletion");
        }
        controllerRepository.deleteById(id);
    }
}