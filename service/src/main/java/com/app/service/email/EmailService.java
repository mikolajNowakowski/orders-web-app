package com.app.service.email;

public  interface EmailService  {
    void send(String receiver, String subject, String htmlContent);
    void sendWithAttachment(String recipientName, String mailReciver,
                            String subject, String body, String filename, byte[] attachment);
}
