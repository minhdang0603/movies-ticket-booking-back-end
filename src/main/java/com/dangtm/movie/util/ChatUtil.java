package com.dangtm.movie.util;

public class ChatUtil {
    public static String generateChatTopic(String senderEmail, String recipientEmail) {
        // Get the part before the "@" symbol
        String senderName = senderEmail.split("@")[0];
        String recipientName = recipientEmail.split("@")[0];

        // Create the topic name
        return (senderName.compareTo(recipientName) < 0)
                ? senderName + "-" + recipientName
                : recipientName + "-" + senderName;
    }

}
