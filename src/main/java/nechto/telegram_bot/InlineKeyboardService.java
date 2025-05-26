package nechto.telegram_bot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public interface InlineKeyboardService {
    SendMessage returnButtonsWithStatusesLooseWin(Long chatId);

    SendMessage returnButtonsWithStatuses(Long chatId);

//    SendMessage returnButtonsWithAttributes(Long chatId);

    SendMessage returnButtonsWithAttributesForHuman(Long chatId);

    SendMessage returnButtonsWithAttributesForContaminated(Long chatId);

    SendMessage returnButtonsWithFlamethrower(Long chatId);

//    SendMessage returnButtonsWithAntiHumanFlamethrower(Long chatId);

    SendMessage getMessageWithInlineMurkupPlusMinus(Long chatId, int flamethrowerAmount);

    SendMessage getMessageWithInlineMurkupPlusMinusWithAntiHumanFlamethrower(Long chatId, int flamethrowerAmount);

    SendMessage returnButtonsToEndGameOrCountNext(Long chatId);

    InlineKeyboardButton createButton(String name, String callBackDataName);

    InlineKeyboardMarkup createInlineKeyboard(List<InlineKeyboardButton> rowInLine);

    InlineKeyboardMarkup createInlineKeyboard(List<InlineKeyboardButton> rowInLine, List<InlineKeyboardButton> rowInLine2);

    InlineKeyboardMarkup createInlineKeyboard(List<InlineKeyboardButton> rowInLine, List<InlineKeyboardButton> rowInLine2,
                                              List<InlineKeyboardButton> rowInLine3);

    EditMessageText editeMessageForInlineKeyboardPlusMinus(long chatId, int messageId, String text, int flamethrowerAmount);

//    EditMessageText editeMessageForInlineKeyboardPlusMinusWithAntiHumanFlamethrower(long chatId, int messageId, String text, int flamethrowerAmount);

    InlineKeyboardMarkup getInlineKeybordWithPlusMinus(int flamethrowerAmount);

    InlineKeyboardMarkup getMessageWithInlineMurkupPlusMinusWithFlamethrowerAntiHuman(int flamethrowerAmount);

    EditMessageText editeMessageForInlineKeyboardPlusMinusWithAntiHumanFlamethrower(long chatId, int messageId, String text, int flamethrowerAmount);

    InlineKeyboardMarkup getInlineKeyboardWithInlineMurkupPlusMinusWithFlamethrowerAntiHuman(int flamethrowerAmount);
}
