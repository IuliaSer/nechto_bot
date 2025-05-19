package nechto.utils;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class BotUtils {

    public static SendMessage getSendMessageWithInlineMarkup(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        return message;
    }

    public static SendMessage getSendMessageWithInlineMarkup(long chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    public static EditMessageText getEditMessageWithInlineMarkup(long chatId, int messageId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setMessageId(messageId);
        message.setText(text);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }
}
