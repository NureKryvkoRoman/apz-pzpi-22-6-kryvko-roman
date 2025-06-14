package ua.nure.kryvko.roman.apz.notification;

public class NotificationResponseMapper {
    public static NotificationResponse toDto(Notification notification) {
        Integer greenhouseId;
        try {
            greenhouseId = notification.getGreenhouse().getId();
        } catch (NullPointerException e) {
            greenhouseId = -1;
        }

        return new NotificationResponse(
                notification.getId(),
                notification.getUser().getId(),
                greenhouseId,
                notification.getMessage(),
                notification.getIsRead(),
                notification.getTimestamp(),
                notification.getNotificationUrgency()
        );
    }
}
