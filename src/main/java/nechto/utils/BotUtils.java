package nechto.utils;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import static java.lang.String.format;

@Slf4j
public class BotUtils {

    public static SendMessage getSendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        return message;
    }

    public static SendMessage getSendMessageWithMarkDown(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setChatId(String.valueOf(chatId));
        message.setParseMode("MarkdownV2");
        return message;
    }

    public static SendMessage getSendMessage(long chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    public static EditMessageText getEditMessageWithInlineMarkup(long chatId, int messageId, String text,
                                                                 InlineKeyboardMarkup inlineKeyboardMarkup) {
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setMessageId(messageId);
        message.setText(text);
        message.setReplyMarkup(inlineKeyboardMarkup);
        return message;
    }

    public static Long extractUserId(Update update) {
        Long userId;
        if (update.hasMessage()) {
            userId = update.getMessage().getFrom().getId();
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getFrom() != null) {
            userId = update.getCallbackQuery().getFrom().getId();
        } else {
            throw new RuntimeException(format("Unsupported update type: {}", update));
        }
        return userId;
    }
}
