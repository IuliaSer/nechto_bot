package nechto.service;

import nechto.dto.response.ResponseUserDto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.time.YearMonth;
import java.util.List;
import java.util.Locale;

public interface InlineKeyboardService {
    SendMessage returnButtonsWithCommandStatuses(Long chatId);

    SendMessage returnButtonsWithRolesForNechtoWin(Long chatId);

    SendMessage returnButtonsWithRolesForHumanWin(Long chatId);

    SendMessage returnButtonsToAskIfBurned(Long chatId);

    SendMessage returnButtonsForHuman(Long chatId);

    SendMessage returnButtonsForContaminated(Long chatId);

    SendMessage returnButtonsForLastContaminated(Long chatId);

    SendMessage returnButtonsForBurnedContaminated(Long chatId);

    SendMessage returnButtonsForBurnedHuman(Long chatId);

    SendMessage returnButtonsForNechto(Long chatId);

    SendMessage getMessageWithInlineMurkupPlusMinus(Long chatId, int flamethrowerAmount);

    SendMessage getMessageWithInlineMurkupPlusMinusAntiHuman(Long chatId, int flamethrowerAmount);

    SendMessage getMessageWithInlineMurkupPlusMinusWithAntiHumanFlamethrower(Long chatId, int flamethrowerAmount);

    SendMessage returnButtonsToEndGameOrCountNext(Long chatId);

    SendMessage returnButtonsForChangingGame(Long chatId);

    InlineKeyboardMarkup returnButtonsWithUsers(List<ResponseUserDto> users);

    InlineKeyboardMarkup buildCalendar(long userId, YearMonth ym, Locale locale);

    EditMessageText editeMessageForInlineKeyboardPlusMinus(long chatId, int messageId, String text, int flamethrowerAmount);


    EditMessageText editeMessageForInlineKeyboardPlusMinusForAntiHumanFlamethrower(long chatId, int messageId, String text, int flamethrowerAmount);

    EditMessageText editeMessageForInlineKeyboardPlusMinusForAntiHuman(long chatId, int messageId, String text, int flamethrowerAmount);

    InlineKeyboardMarkup getInlineKeybordWithPlusMinus(int flamethrowerAmount);

    InlineKeyboardMarkup getInlineKeybordWithPlusMinusAgainstHumanFlamethrower(int flamethrowerAmount);

    InlineKeyboardMarkup getInlineKeybordWithPlusMinusAntiHuman(int flamethrowerAmount);

    InlineKeyboardMarkup buildMonthCalendar(long userId, YearMonth now, Locale ru);
}
