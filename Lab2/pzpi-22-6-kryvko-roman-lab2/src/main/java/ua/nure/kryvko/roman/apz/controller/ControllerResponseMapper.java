package ua.nure.kryvko.roman.apz.controller;

public class ControllerResponseMapper {
    public static ControllerResponse toDto(Controller controller) {
        return new ControllerResponse(
                controller.getId(),
                controller.getGreenhouse().getId(),
                controller.isActive(),
                controller.getInstalledAt(),
                controller.getName(),
                controller.getControllerType(),
                controller.getAutomationRule()
        );
    }
}
