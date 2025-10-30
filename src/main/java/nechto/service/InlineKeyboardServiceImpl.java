package nechto.service;

import lombok.RequiredArgsConstructor;
import nechto.button.ButtonService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static nechto.enums.Button.ANTI_HUMAN_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.BURNED_BUTTON;
import static nechto.enums.Button.CALNAV_BUTTON;
import static nechto.enums.Button.CALNOOP_BUTTON;
import static nechto.enums.Button.CAL_BUTTON;
import static nechto.enums.Button.CHANGE_NEXT_BUTTON;
import static nechto.enums.Button.CONFIRM_MONTH_BUTTON;
import static nechto.enums.Button.CONTAMINATED_BUTTON;
import static nechto.enums.Button.COUNT_NEXT_BUTTON;
import static nechto.enums.Button.DANGEROUS_BUTTON;
import static nechto.enums.Button.END_COUNT_BUTTON;
import static nechto.enums.Button.END_GAME_BUTTON;
import static nechto.enums.Button.FLAMETHROWER_BUTTON;
import static nechto.enums.Button.FLAMETHROWER_BUTTON_FOR_HUMAN;
import static nechto.enums.Button.HUMAN_BUTTON;
import static nechto.enums.Button.LAST_CONTAMINATED_BUTTON;
import static nechto.enums.Button.MINUS_ANTI_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.MINUS_ANTI_HUMAN_BUTTON;
import static nechto.enums.Button.MINUS_BUTTON;
import static nechto.enums.Button.NECHTO_BUTTON;
import static nechto.enums.Button.PLUS_ANTI_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.PLUS_ANTI_HUMAN_BUTTON;
import static nechto.enums.Button.PLUS_BUTTON;
import static nechto.enums.Button.USEFULL_BUTTON;
import static nechto.enums.Button.VALUE_BUTTON;
import static nechto.enums.Button.VICTIM_BUTTON;
import static nechto.enums.Button.WIN_NECHTO_BUTTON;
import static nechto.enums.Button.WIN_PEOPLE_BUTTON;
import static nechto.utils.BotUtils.getEditMessageWithInlineMarkup;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class InlineKeyboardServiceImpl implements InlineKeyboardService {
    private final ButtonService buttonService;

    @Override
    public SendMessage returnButtonsWithCommandStatuses(Long chatId) {
        var nechtoWin = createButton("Нечто выиграл(а)", WIN_NECHTO_BUTTON.name());
        var nechtoLoose = createButton("Люди выиграли", WIN_PEOPLE_BUTTON.name());

        buttonService.putButtonsToButtonCache(nechtoWin, nechtoLoose);

        List<InlineKeyboardButton> rowInLine = List.of(nechtoWin, nechtoLoose);
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine);

        return getSendMessage(chatId, "Выберите статус:", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage returnButtonsWithRolesForNechtoWin(Long chatId) {
        var buttonHuman = createButton("Человек", HUMAN_BUTTON.name());
        var buttonContaminated = createButton("Зараженный", CONTAMINATED_BUTTON.name());
        var buttonNechto = createButton("Нечто", NECHTO_BUTTON.name());
        var buttonBurned = createButton("Сожгли", BURNED_BUTTON.name());
        var buttonLastContaminated = createButton("Последний зараженный", LAST_CONTAMINATED_BUTTON.name());

        buttonService.putButtonsToButtonCache(buttonHuman, buttonContaminated, buttonNechto, buttonBurned, buttonLastContaminated);

        List<InlineKeyboardButton> rowInLine = List.of(buttonHuman, buttonContaminated, buttonNechto);
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonLastContaminated, buttonBurned);
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine, rowInLine2);

        return getSendMessage(chatId, "Выберите роль:", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage returnButtonsWithRolesForHumanWin(Long chatId) {
        var buttonHuman = createButton("Человек", HUMAN_BUTTON.name());
        var buttonContaminated = createButton("Зараженный", CONTAMINATED_BUTTON.name());
        var buttonNechto = createButton("Нечто", NECHTO_BUTTON.name());
        var buttonBurned = createButton("Сожгли", BURNED_BUTTON.name());

        buttonService.putButtonsToButtonCache(buttonHuman, buttonContaminated, buttonNechto, buttonBurned);

        List<InlineKeyboardButton> rowInLine = List.of(buttonHuman, buttonContaminated);
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonNechto, buttonBurned);
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine, rowInLine2);

        return getSendMessage(chatId, "Выберите роль:", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage returnButtonsForHuman(Long chatId) {
        var buttonDangerous = createButton("Опасный", DANGEROUS_BUTTON.name());
        var buttonFlamethrower = createButton("Огнемет", FLAMETHROWER_BUTTON_FOR_HUMAN.name());
        var buttonAntiHumanFlamethrower = createButton("Огнемет против человека", ANTI_HUMAN_FLAMETHROWER_BUTTON.name());
        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON.name());

        buttonService.putButtonsToButtonCache(buttonDangerous, buttonAntiHumanFlamethrower);

        List<InlineKeyboardButton> rowInLine = List.of(buttonDangerous);
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonFlamethrower, buttonAntiHumanFlamethrower);
        List<InlineKeyboardButton> rowInLine3 = List.of(buttonEndCount);

        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine, rowInLine2, rowInLine3);

        return getSendMessage(chatId, "Выберите все аттрибуты по очереди:", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage returnButtonsForContaminated(Long chatId) {
        var buttonDangerous = createButton("Опасный", DANGEROUS_BUTTON.name());
        var buttonUsefull = createButton("Полезный", USEFULL_BUTTON.name());
        var buttonVictim = createButton("Жертва", VICTIM_BUTTON.name());
        var buttonFlamethrower = createButton("Огнемет", FLAMETHROWER_BUTTON.name());
        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON.name());

        buttonService.putButtonsToButtonCache(buttonDangerous, buttonUsefull, buttonVictim);

        List<InlineKeyboardButton> rowInLine = List.of(buttonDangerous, buttonUsefull, buttonVictim, buttonFlamethrower);
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonEndCount);

        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine, rowInLine2);

        return getSendMessage(chatId, "Выберите все аттрибуты по очереди:", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage returnButtonsForNechto(Long chatId) {
        var buttonFlamethrower = createButton("Огнемет", FLAMETHROWER_BUTTON.name());
        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON.name());

        List<InlineKeyboardButton> rowInLine = List.of(buttonFlamethrower);
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonEndCount);

        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine, rowInLine2);

        return getSendMessage(chatId, "Использовался ли огнемет? ", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage getMessageWithInlineMurkupPlusMinus(Long chatId, int flamethrowerAmount) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeybordWithPlusMinus(flamethrowerAmount);

        return getSendMessage(chatId, "Выберите количество:\n", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage getMessageWithInlineMurkupPlusMinusAntiHuman(Long chatId, int flamethrowerAmount) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeybordWithPlusMinusAntiHuman(flamethrowerAmount);

        return getSendMessage(chatId, "Выберите количество:\n", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage getMessageWithInlineMurkupPlusMinusWithAntiHumanFlamethrower(Long chatId, int flamethrowerAmount) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeybordWithPlusMinusAntiHumanFlamethrower(flamethrowerAmount);

        return getSendMessage(chatId, "Выберите количество:\n", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage returnButtonsToEndGameOrCountNext(Long chatId) {
        var buttonCountNext = createButton("Посчитать следующего", COUNT_NEXT_BUTTON.name());
        var buttonEndGame = createButton("Закончить игру", END_GAME_BUTTON.name());

        List<InlineKeyboardButton> rowInLine = List.of(buttonCountNext, buttonEndGame);
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine);

        return getSendMessage(chatId, "Что дальше?", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage returnButtonsForChangingGame(Long chatId) {
        var buttonCountNext = createButton("Изменить для следующего", CHANGE_NEXT_BUTTON.name());
        var buttonEndGame = createButton("Подтвердить всех", END_GAME_BUTTON.name());

        List<InlineKeyboardButton> rowInLine = List.of(buttonCountNext, buttonEndGame);
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine);

        return getSendMessage(chatId, "Что дальше?", inlineKeyboardMarkup);
    }

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(List<InlineKeyboardButton> rowInLine) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        rowsInLine.add(rowInLine);
        inlineKeyboardMarkup.setKeyboard(rowsInLine);
        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(List<InlineKeyboardButton> rowInLine,
                                                     List<InlineKeyboardButton> rowInLine2) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        rowsInLine.add(rowInLine);
        rowsInLine.add(rowInLine2);
        inlineKeyboardMarkup.setKeyboard(rowsInLine);
        return inlineKeyboardMarkup;
    }

    @Override
    public InlineKeyboardMarkup createInlineKeyboard(List<InlineKeyboardButton> rowInLine,
                                                     List<InlineKeyboardButton> rowInLine2,
                                                     List<InlineKeyboardButton> rowInLine3) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        rowsInLine.add(rowInLine);
        rowsInLine.add(rowInLine2);
        rowsInLine.add(rowInLine3);
        inlineKeyboardMarkup.setKeyboard(rowsInLine);
        return inlineKeyboardMarkup;
    }

    @Override
    public EditMessageText editeMessageForInlineKeyboardPlusMinus(long chatId, int messageId, String text, int flamethrowerAmount) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeybordWithPlusMinus(flamethrowerAmount);
        return getEditMessageWithInlineMarkup(chatId, messageId, text, inlineKeyboardMarkup);
    }

    @Override
    public EditMessageText editeMessageForInlineKeyboardPlusMinusForAntiHumanFlamethrower(long chatId, int messageId, String text, int flamethrowerAmount) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeybordWithPlusMinusAntiHumanFlamethrower(flamethrowerAmount);

        return getEditMessageWithInlineMarkup(chatId, messageId, text, inlineKeyboardMarkup);
    }

    @Override
    public EditMessageText editeMessageForInlineKeyboardPlusMinusForAntiHuman(long chatId, int messageId, String text, int flamethrowerAmount) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeybordWithPlusMinusAntiHuman(flamethrowerAmount);

        return getEditMessageWithInlineMarkup(chatId, messageId, text, inlineKeyboardMarkup);
    }

    @Override
    public InlineKeyboardMarkup getInlineKeybordWithPlusMinus(int flamethrowerAmount) {
        var buttonMinus = createButton("➖", MINUS_BUTTON.name());
        var buttonValue = createButton(String.valueOf(flamethrowerAmount), VALUE_BUTTON.name());
        var buttonPlus = createButton("➕", PLUS_BUTTON.name());
        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON.name());

        List<InlineKeyboardButton> rowInLine = List.of(buttonMinus, buttonValue, buttonPlus);
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonEndCount);

        return createInlineKeyboard(rowInLine, rowInLine2);
    }

    @Override
    public InlineKeyboardMarkup getInlineKeybordWithPlusMinusAntiHumanFlamethrower(int flamethrowerAmount) {
        var buttonMinus = createButton("➖", MINUS_ANTI_FLAMETHROWER_BUTTON.name());
        var buttonValue = createButton(String.valueOf(flamethrowerAmount), VALUE_BUTTON.name());
        var buttonPlus = createButton("➕", PLUS_ANTI_FLAMETHROWER_BUTTON.name());
        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON.name());
        var buttonAntiHumanFlamethrower = createButton("Огнемет против человека", ANTI_HUMAN_FLAMETHROWER_BUTTON.name());

        List<InlineKeyboardButton> rowInLine = List.of(buttonMinus, buttonValue, buttonPlus);
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonAntiHumanFlamethrower);
        List<InlineKeyboardButton> rowInLine3 = List.of(buttonEndCount);

        return createInlineKeyboard(rowInLine, rowInLine2, rowInLine3);
    }

    @Override
    public InlineKeyboardMarkup getInlineKeybordWithPlusMinusAntiHuman(int flamethrowerAmount) {
        var buttonMinus = createButton("➖", MINUS_ANTI_HUMAN_BUTTON.name());
        var buttonValue = createButton(String.valueOf(flamethrowerAmount), VALUE_BUTTON.name());
        var buttonPlus = createButton("➕", PLUS_ANTI_HUMAN_BUTTON.name());
        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON.name());

        List<InlineKeyboardButton> rowInLine = List.of(buttonMinus, buttonValue, buttonPlus);
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonEndCount);

        return createInlineKeyboard(rowInLine, rowInLine2);
    }

    @Override
    public InlineKeyboardMarkup buildMonthCalendar(long userId, YearMonth ym, Locale locale) {
        String title = ym.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, locale) + " " + ym.getYear();
        var confirmMonth = createButton("Посмотреть за месяц", CONFIRM_MONTH_BUTTON.name() + ":"
                + ym.getYear() + "-" + ym.getMonth().getValue());

        List<InlineKeyboardButton> rowInLine = List.of(
                createButton("◀️", CALNAV_BUTTON.name() + ":" + ym.minusMonths(1)),
                createButton("📅 " + title, CALNOOP_BUTTON.name()),
                createButton("▶️", CALNAV_BUTTON.name() + ":" + ym.plusMonths(1)));
        List<InlineKeyboardButton> rowInLine2 = List.of(confirmMonth);

        return createInlineKeyboard(rowInLine, rowInLine2);
    }

    @Override
    public InlineKeyboardMarkup buildCalendar(long userId, YearMonth ym, Locale locale) {
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        // Заголовок с месяцем
        String title = ym.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, locale) + " " + ym.getYear();
        rows.add(List.of(
                createButton("◀️", CALNAV_BUTTON.name() + ":" + ym.minusMonths(1)),
                createButton("📅 " + title, CALNOOP_BUTTON.name()),
                createButton("▶️", CALNAV_BUTTON.name() + ":" + ym.plusMonths(1))));

        // Шапка дней недели (Пн..Вс)
        DayOfWeek firstDow = DayOfWeek.MONDAY; // или из настроек
        List<InlineKeyboardButton> dowRow = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            DayOfWeek dow = firstDow.plus(i);
            dowRow.add(createButton(dow.getDisplayName(TextStyle.NARROW, locale), CALNOOP_BUTTON.name()));
        }
        rows.add(dowRow);

        // Пустые ячейки до первого дня
        LocalDate first = ym.atDay(1);
        int shift = (first.getDayOfWeek().getValue() - firstDow.getValue() + 7) % 7;
        List<InlineKeyboardButton> week = new ArrayList<>();
        for (int i = 0; i < shift; i++) {
            week.add(createButton(" ", CALNOOP_BUTTON.name()));
        }

        // Дни месяца
        for (int d = 1; d <= ym.lengthOfMonth(); d++) {
            LocalDate date = ym.atDay(d);
            week.add(createButton(String.valueOf(d), CAL_BUTTON.name() + ":" + date));
            if (week.size() == 7) {
                rows.add(week); week = new ArrayList<>();
            }
        }
        if (!week.isEmpty()) {
            while (week.size() < 7) {
                week.add(createButton(" ", CALNOOP_BUTTON.name()));
            }
            rows.add(week);
        }
        return new InlineKeyboardMarkup(rows);
    }

    @Override
    public InlineKeyboardButton createButton(String name, String callBackDataName) {
        var button = new InlineKeyboardButton(name);
        button.setCallbackData(callBackDataName);
        return button;
    }
}
