package hieu.nv.dang8.model;

public class AppUsageModel {
    private int id;
    private String appName;
    private int timeLimit; // Giới hạn thời gian sử dụng (đơn vị: phút)
    private String parentPhone; // Số điện thoại phụ huynh nhận SMS cảnh báo

    public AppUsageModel(int id, String appName, int timeLimit, String parentPhone) {
        this.id = id;
        this.appName = appName;
        this.timeLimit = timeLimit;
        this.parentPhone = parentPhone;
    }

    public int getId() { return id; }
    public String getAppName() { return appName; }
    public int getTimeLimit() { return timeLimit; }
    public String getParentPhone() { return parentPhone; }
}