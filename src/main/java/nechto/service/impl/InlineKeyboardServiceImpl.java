package nechto.service.impl;

import lombok.RequiredArgsConstructor;
import nechto.button.ButtonService;
import nechto.cache.ScoresStateCache;
import nechto.dto.UserDto;
import nechto.enums.Button;
import nechto.service.InlineKeyboardService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static nechto.enums.Button.AGAINST_HUMAN_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.CALNAV_BUTTON;
import static nechto.enums.Button.CALNOOP_BUTTON;
import static nechto.enums.Button.CAL_BUTTON;
import static nechto.enums.Button.CONFIRM_MONTH_BUTTON;
import static nechto.enums.Button.CONTAMINATED_BUTTON;
import static nechto.enums.Button.COUNT_NEXT_BUTTON;
import static nechto.enums.Button.DANGEROUS_BUTTON;
import static nechto.enums.Button.END_COUNT_BUTTON;
import static nechto.enums.Button.END_GAME_BUTTON;
import static nechto.enums.Button.FLAMETHROWER_BUTTON;
import static nechto.enums.Button.FLAMETHROWER_FOR_HUMAN_BUTTON;
import static nechto.enums.Button.HUMAN_BUTTON;
import static nechto.enums.Button.LAST_CONTAMINATED_BUTTON;
import static nechto.enums.Button.MINUS_WITH_AGAINST_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.MINUS_FOR_AGAINST_HUMAN_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.MINUS_BUTTON;
import static nechto.enums.Button.CALNAV_BUTTON_MONTH;
import static nechto.enums.Button.NECHTO_BUTTON;
import static nechto.enums.Button.NO_BURNED_BUTTON;
import static nechto.enums.Button.NO_LAST_CONTAMINATED_BUTTON;
import static nechto.enums.Button.PICKED_ADMIN_BUTTON;
import static nechto.enums.Button.PICKED_USER_BUTTON;
import static nechto.enums.Button.PLUS_WITH_AGAINST_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.PLUS_FOR_AGAINST_HUMAN_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.PLUS_BUTTON;
import static nechto.enums.Button.USEFULL_BUTTON;
import static nechto.enums.Button.VALUE_BUTTON;
import static nechto.enums.Button.VICTIM_BUTTON;
import static nechto.enums.Button.WIN_NECHTO_BUTTON;
import static nechto.enums.Button.WIN_PEOPLE_BUTTON;
import static nechto.enums.Button.YES_BURNED_BUTTON;
import static nechto.enums.Button.YES_LAST_CONTAMINATED_BUTTON;
import static nechto.utils.BotUtils.getEditMessageWithInlineMarkup;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class InlineKeyboardServiceImpl implements InlineKeyboardService {
    private final ButtonService buttonService;
    private final ScoresStateCache scoresStateCache;

    @Override
    public SendMessage returnButtonsWithCommandStatuses(Long chatId) {
        var nechtoWin = createButton("Нечто выиграл(а)", WIN_NECHTO_BUTTON.name());
        var nechtoLoose = createButton("Люди выиграли", WIN_PEOPLE_BUTTON.name());

        List<InlineKeyboardButton> rowInLine = List.of(nechtoWin, nechtoLoose);
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine);

        return getSendMessage(chatId, "Выберите статус:", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage returnButtonsWithRolesForNechtoWin(Long chatId) {
        var buttonHuman = createButton("Человек", HUMAN_BUTTON.name());
        var buttonContaminated = createButton("Зараженный", CONTAMINATED_BUTTON.name());
        var buttonLastContaminated = createButton("Последний зараженный", LAST_CONTAMINATED_BUTTON.name());

        List<InlineKeyboardButton> rowInLine;
        if(scoresStateCache.get(chatId).isNechtoIsChoosen()) {
            rowInLine = List.of(buttonHuman, buttonContaminated);
        } else {
            var buttonNechto = createButton("Нечто", NECHTO_BUTTON.name());
            rowInLine = List.of(buttonHuman, buttonContaminated, buttonNechto);
        }
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonLastContaminated);
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine, rowInLine2);

        return getSendMessage(chatId, "Выберите роль:", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage returnButtonsWithRolesForHumanWin(Long chatId) {
        var buttonHuman = createButton("Человек", HUMAN_BUTTON.name());
        var buttonContaminated = createButton("Зараженный", CONTAMINATED_BUTTON.name());

        List<InlineKeyboardButton> rowInLine = List.of(buttonHuman, buttonContaminated);
        InlineKeyboardMarkup inlineKeyboardMarkup;
        if(!scoresStateCache.get(chatId).isNechtoIsChoosen()) {
            var buttonNechto = createButton("Нечто", NECHTO_BUTTON.name());
            List<InlineKeyboardButton> rowInLine2 = List.of(buttonNechto);
            inlineKeyboardMarkup = createInlineKeyboard(rowInLine, rowInLine2);
        } else {
            inlineKeyboardMarkup = createInlineKeyboard(rowInLine);
        }

        return getSendMessage(chatId, "Выберите роль:", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage returnButtonsToAskIfBurned(Long chatId) {
        var buttonYes = createButton("Да", YES_BURNED_BUTTON.name());
        var buttonNo = createButton("Нет", NO_BURNED_BUTTON.name());

        List<InlineKeyboardButton> rowInLine = List.of(buttonYes, buttonNo);
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine);

        return getSendMessage(chatId, "Сожгли?", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage returnButtonsForHuman(Long chatId) {
        var buttonDangerous = createButton("Опасный", DANGEROUS_BUTTON.name());
        var buttonFlamethrower = createButton("Огнемет", FLAMETHROWER_FOR_HUMAN_BUTTON.name());
        var buttonAntiHumanFlamethrower = createButton("Огнемет против человека", AGAINST_HUMAN_FLAMETHROWER_BUTTON.name());
        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON.name());

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
        var buttonFlamethrower = createButton("Огнемет", FLAMETHROWER_BUTTON.name());
        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON.name());

        List<InlineKeyboardButton> rowInLine = List.of(buttonDangerous, buttonUsefull, buttonFlamethrower);
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonEndCount);

        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine, rowInLine2);

        return getSendMessage(chatId, "Выберите все аттрибуты по очереди:", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage returnButtonsForLastContaminated(Long chatId) {
        var buttonYes = createButton("Да", YES_LAST_CONTAMINATED_BUTTON.name());
        var buttonNo = createButton("Нет", NO_LAST_CONTAMINATED_BUTTON.name());

        List<InlineKeyboardButton> rowInLine = List.of(buttonYes, buttonNo);
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine);

        return getSendMessage(chatId, "Игрок сражался с нечто до конца и проиграл?", inlineKeyboardMarkup); //?
    }

    @Override
    public SendMessage returnButtonsForBurnedContaminated(Long chatId) {
        var buttonDangerous = createButton("Опасный", DANGEROUS_BUTTON.name());
        var buttonUsefull = createButton("Полезный", USEFULL_BUTTON.name());
        var buttonVictim = createButton("Жертва", VICTIM_BUTTON.name());
        var buttonFlamethrower = createButton("Огнемет", FLAMETHROWER_BUTTON.name());
        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON.name());

        List<InlineKeyboardButton> rowInLine = List.of(buttonDangerous, buttonUsefull, buttonVictim, buttonFlamethrower);
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonEndCount);

        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine, rowInLine2);

        return getSendMessage(chatId, "Выберите все аттрибуты по очереди:", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage returnButtonsForBurnedHuman(Long chatId) {
        var buttonDangerous = createButton("Опасный", DANGEROUS_BUTTON.name());
        var buttonUsefull = createButton("Полезный", USEFULL_BUTTON.name());
        var buttonAntiHumanFlamethrower = createButton("Огнемет против человека", AGAINST_HUMAN_FLAMETHROWER_BUTTON.name());
        var buttonFlamethrower = createButton("Огнемет", FLAMETHROWER_FOR_HUMAN_BUTTON.name());
        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON.name());

        List<InlineKeyboardButton> rowInLine = List.of(buttonDangerous, buttonUsefull, buttonFlamethrower, buttonAntiHumanFlamethrower);
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
    public SendMessage getMessageWithInlineMurkupPlusMinusWithAgainstHumanFlamethrower(Long chatId, int flamethrowerAmount) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeybordWithPlusMinusWithAgainstHumanFlamethrower(flamethrowerAmount);

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
        var buttonChangeNext = createButton("Изменить для следующего", COUNT_NEXT_BUTTON.name());
        var buttonEndChanging = createButton("Подтвердить всех", END_GAME_BUTTON.name());

        List<InlineKeyboardButton> rowInLine = List.of(buttonChangeNext, buttonEndChanging);
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine);

        return getSendMessage(chatId, "Что дальше?", inlineKeyboardMarkup);
    }

    @Override
    public EditMessageText editeMessageForInlineKeyboardPlusMinus(long chatId, int messageId, String text, int flamethrowerAmount) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeybordWithPlusMinus(flamethrowerAmount);
        return getEditMessageWithInlineMarkup(chatId, messageId, text, inlineKeyboardMarkup);
    }

    @Override
    public EditMessageText editeMessageForInlineKeyboardPlusMinusForAntiHumanFlamethrower(long chatId, int messageId, String text, int flamethrowerAmount) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeybordWithPlusMinusWithAgainstHumanFlamethrower(flamethrowerAmount);

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
    public InlineKeyboardMarkup getInlineKeybordWithPlusMinusWithAgainstHumanFlamethrower(int flamethrowerAmount) {
        var buttonMinus = createButton("➖", MINUS_WITH_AGAINST_FLAMETHROWER_BUTTON.name());
        var buttonValue = createButton(String.valueOf(flamethrowerAmount), VALUE_BUTTON.name());
        var buttonPlus = createButton("➕", PLUS_WITH_AGAINST_FLAMETHROWER_BUTTON.name());
        var buttonAntiHumanFlamethrower = createButton("Огнемет против человека", AGAINST_HUMAN_FLAMETHROWER_BUTTON.name());
        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON.name());

        List<InlineKeyboardButton> rowInLine = List.of(buttonMinus, buttonValue, buttonPlus);
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonAntiHumanFlamethrower);
        List<InlineKeyboardButton> rowInLine3 = List.of(buttonEndCount);

        return createInlineKeyboard(rowInLine, rowInLine2, rowInLine3);
    }

    @Override
    public InlineKeyboardMarkup getInlineKeybordWithPlusMinusAntiHuman(int flamethrowerAmount) {
        var buttonMinus = createButton("➖", MINUS_FOR_AGAINST_HUMAN_FLAMETHROWER_BUTTON.name());
        var buttonValue = createButton(String.valueOf(flamethrowerAmount), VALUE_BUTTON.name());
        var buttonPlus = createButton("➕", PLUS_FOR_AGAINST_HUMAN_FLAMETHROWER_BUTTON.name());
        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON.name());

        List<InlineKeyboardButton> rowInLine = List.of(buttonMinus, buttonValue, buttonPlus);
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonEndCount);

        return createInlineKeyboard(rowInLine, rowInLine2);
    }

    @Override
    public InlineKeyboardMarkup buildMonthCalendar(long userId, YearMonth ym, Locale locale) {
        String title = ym.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, locale) + " " + ym.getYear();
        var confirmMonth = createButton("Посмотреть за месяц", CONFIRM_MONTH_BUTTON.name() + ":"
                + ym.format(DateTimeFormatter.ofPattern("yyyy-MM")));

        List<InlineKeyboardButton> rowInLine = List.of(
                createButton("◀️", CALNAV_BUTTON_MONTH.name() + ":" + ym.minusMonths(1)),
                createButton("📅 " + title, CALNOOP_BUTTON.name()),
                createButton("▶️", CALNAV_BUTTON_MONTH.name() + ":" + ym.plusMonths(1)));
        List<InlineKeyboardButton> rowInLine2 = List.of(confirmMonth);

        return createInlineKeyboard(rowInLine, rowInLine2);
    }

    @Override
    public SendMessage returnEndGameButton(Long chatId) {
        var buttonEndGame = createButton("Завершить", END_GAME_BUTTON.name());

        List<InlineKeyboardButton> rowInLine = List.of(buttonEndGame);
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine);

        return getSendMessage(chatId, "Все пользователи добавленные в игру посчитаны!", inlineKeyboardMarkup);
    }

    @Override
    public InlineKeyboardMarkup returnButtonsWithUsers(List<UserDto> users) {
        List<List<InlineKeyboardButton>> rowsInLine = addUsersButtonsToInlineKeyboard(users, PICKED_USER_BUTTON);

        return createInlineKeyboard(rowsInLine);
    }

    @Override
    public InlineKeyboardMarkup returnButtonsWithAdmins(List<UserDto> users) {
        List<List<InlineKeyboardButton>> rowsInLine = addUsersButtonsToInlineKeyboard(users, PICKED_ADMIN_BUTTON);

        return createInlineKeyboard(rowsInLine);
    }

    @Override
    public InlineKeyboardMarkup returnButtonsWithEndChangingAndChangeNext(List<UserDto> users) {
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        var buttonEndCount = createButton("Завершить изменения", END_GAME_BUTTON.name());
        var buttonChangeUser = createButton("Изменить очки игроков", COUNT_NEXT_BUTTON.name());

        row1.add(buttonEndCount);
        row2.add(buttonChangeUser);
        return createInlineKeyboard(row1, row2);
    }

    private List<List<InlineKeyboardButton>> addUsersButtonsToInlineKeyboard(List<UserDto> users, Button userButton) {
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        int buttonsInRow = 0;
        int lastRow = users.size() / 3;
        int amountOfRows = 0;
        int amountOfRowsInLastRow = users.size() % 3;

        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        for (UserDto user : users) {
            String callbackDataName = userButton.toString() + ":" + user.getId();
            var buttonUserName = createButton(user.getUsername(), callbackDataName);

            rowInLine.add(buttonUserName);
            buttonsInRow++;
            if (buttonsInRow == 3 || (amountOfRows == lastRow && buttonsInRow == amountOfRowsInLastRow)) {
                rowsInLine.add(rowInLine);
                rowInLine = new ArrayList<>();
                buttonsInRow = 0;
                amountOfRows++;
            }
            buttonService.putButtonsToButtonCache(callbackDataName);
        }
        return rowsInLine;
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
        DayOfWeek firstDow = DayOfWeek.MONDAY;
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

    private InlineKeyboardButton createButton(String name, String callBackDataName) {
        var button = new InlineKeyboardButton(name);
        button.setCallbackData(callBackDataName);
        return button;
    }

    private InlineKeyboardMarkup createInlineKeyboard(List<InlineKeyboardButton> ...rows) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>(Arrays.asList(rows));
        inlineKeyboardMarkup.setKeyboard(rowsInLine);

        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup createInlineKeyboard(List<List<InlineKeyboardButton>> rows) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        inlineKeyboardMarkup.setKeyboard(rows);

        return inlineKeyboardMarkup;
    }
}
