package hieu.nv.dang3.model;

public class AlarmModel {
    private int id;
    private int hour;
    private int minute;
    private int isActive; // 1 là đang bật chuông, 0 là tắt

    public AlarmModel(int id, int hour, int minute, int isActive) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.isActive = isActive;
    }

    public int getId() { return id; }
    public int getHour() { return hour; }
    public int getMinute() { return minute; }
    public int getIsActive() { return isActive; }

    public void setIsActive(int isActive) { this.isActive = isActive; }
}