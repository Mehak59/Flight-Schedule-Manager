package flightsystem.models;

public class Notification {
    private int notificationId;
    private String type;
    private String message;
    private String status;

    public Notification(int notificationId, String type, String message, String status) {
        this.notificationId = notificationId;
        this.type = type;
        this.message = message;
        this.status = status;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}