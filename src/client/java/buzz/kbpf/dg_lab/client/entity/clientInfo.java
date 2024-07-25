package buzz.kbpf.dg_lab.client.entity;

public class clientInfo {
    private String type = "", clientId = "", targetId = "", message  = "";



    public clientInfo() {}

    public clientInfo(String type, String clientId, String targetId, String message) {
        this.type = type;
        this.clientId = clientId;
        this.targetId = targetId;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
