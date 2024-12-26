package edu.medical.demo.models.request;

public enum MailType {
    SEND_ACTIVATION_CODE("Отправка кода активации"),
    SEND_REACTIVATION_CODE("Отправка повторного кода активации"),
    SEND_RESTORE_CODE("Отправка кода на восстановление аккаунта");

    private final String description;

    MailType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}