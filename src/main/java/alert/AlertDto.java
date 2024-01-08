package alert;

import com.fasterxml.jackson.annotation.JsonProperty;
public class AlertDto {
    public AlertDto() {
    }

    @JsonProperty("alertId")
    private String alertId;

    @JsonProperty("alertMessage")
    private String alertMessage;

    @JsonProperty("alertProcess")
    private String alertProcess;

    @JsonProperty("userId")
    private int userId;

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getAlertMessage() {
        return alertMessage;
    }

    public void setAlertMessage(String alertMessage) {
        this.alertMessage = alertMessage;
    }

    public String getAlertProcess() {
        return alertProcess;
    }

    public void setAlertProcess(String alertProcess) {
        this.alertProcess = alertProcess;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

