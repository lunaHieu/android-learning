package com.example.lab05_bai4_vanhieu;


public class OnboardingItem {
    int image;
    String title;
    String description;

    public OnboardingItem(int image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
    }
    // Getter/Setter nếu cần (nhưng ở Adapter bạn đang truy cập trực tiếp nên không bắt buộc)
}