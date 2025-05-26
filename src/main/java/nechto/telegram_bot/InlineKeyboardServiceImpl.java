package nechto.telegram_bot;

import nechto.utils.BotUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static nechto.utils.BotUtils.getEditMessageWithInlineMarkup;
import static nechto.utils.BotUtils.getSendMessageWithInlineMarkup;
import static nechto.utils.CommonConstants.*;
import static nechto.utils.CommonConstants.END_COUNT_BUTTON;

@Component
public class InlineKeyboardServiceImpl implements InlineKeyboardService {

    @Override
    public SendMessage returnButtonsWithStatusesLooseWin(Long chatId) {
        var buttonWin = createButton("Выиграл(а)", WIN_BUTTON);
        var buttonLoose = createButton("Проиграл(а)", LOOSE_BUTTON);
        var buttonBurned = createButton("Сожгли", BURNED_BUTTON);

        List<InlineKeyboardButton> rowInLine = List.of(buttonWin, buttonLoose, buttonBurned);
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine);

        return BotUtils.getSendMessageWithInlineMarkup(chatId, "Выберите статус:", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage returnButtonsWithStatuses(Long chatId) {
        var buttonHuman = createButton("Человек", HUMAN_BUTTON);
        var buttonContaminated = createButton("Зараженный", CONTAMINATED_BUTTON);
        var buttonNechto = createButton("Нечто", NECHTO_BUTTON);

        List<InlineKeyboardButton> rowInLine = List.of(buttonHuman, buttonContaminated, buttonNechto);
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine);

        return BotUtils.getSendMessageWithInlineMarkup(chatId, "Выберите статус:", inlineKeyboardMarkup);
    }

//    @Override
//    public SendMessage returnButtonsWithAttributes(Long chatId) {
//        var buttonDangerous = createButton("Опасный", DANGEROUS_BUTTON);
//        var buttonUsefull = createButton("Полезный", USEFULL_BUTTON);
//        var buttonVictim = createButton("Жертва", VICTIM_BUTTON);
//        var buttonContinue = createButton("Дальше", CONTINUE_ANTI_HUMAN_FLAMETHROWER_BUTTON);
//
//        List<InlineKeyboardButton> rowInLine = List.of(buttonDangerous, buttonUsefull, buttonVictim);
//        List<InlineKeyboardButton> rowInLine2 = List.of(buttonContinue);
//
//        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine, rowInLine2);
//
//        return BotUtils.getSendMessageWithInlineMarkup(chatId, "Выберите все аттрибуты:", inlineKeyboardMarkup);
//    }

    @Override
    public SendMessage returnButtonsWithAttributesForHuman(Long chatId) {
        var buttonDangerous = createButton("Опасный", DANGEROUS_BUTTON);
        var buttonUsefull = createButton("Полезный", USEFULL_BUTTON);
        var buttonVictim = createButton("Жертва", VICTIM_BUTTON);
        var buttonFlamethrower = createButton("Огнемет", FLAMETHROWER_BUTTON_FOR_HUMAN);
        var buttonAntiHumanFlamethrower = createButton("Огнемет против человека", ANTI_HUMAN_FLAMETHROWER_BUTTON);
        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON);

        List<InlineKeyboardButton> rowInLine = List.of(buttonDangerous, buttonUsefull, buttonVictim);
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonFlamethrower, buttonAntiHumanFlamethrower);
        List<InlineKeyboardButton> rowInLine3 = List.of(buttonEndCount);

        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine, rowInLine2, rowInLine3);

        return BotUtils.getSendMessageWithInlineMarkup(chatId, "Выберите все аттрибуты:", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage returnButtonsWithAttributesForContaminated(Long chatId) {
        var buttonDangerous = createButton("Опасный", DANGEROUS_BUTTON);
        var buttonUsefull = createButton("Полезный", USEFULL_BUTTON);
        var buttonVictim = createButton("Жертва", VICTIM_BUTTON);
        var buttonFlamethrower = createButton("Огнемет", FLAMETHROWER_BUTTON);
        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON);

        List<InlineKeyboardButton> rowInLine = List.of(buttonDangerous, buttonUsefull, buttonVictim, buttonFlamethrower);
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonEndCount);

        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine, rowInLine2);

        return BotUtils.getSendMessageWithInlineMarkup(chatId, "Выберите все аттрибуты:", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage returnButtonsWithFlamethrower(Long chatId) {
        var buttonFlamethrower = createButton("Огнемет", FLAMETHROWER_BUTTON);
        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON);

        List<InlineKeyboardButton> rowInLine = List.of(buttonFlamethrower);
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonEndCount);

        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine, rowInLine2);

        return getSendMessageWithInlineMarkup(chatId, "Использовался ли огнемет? ", inlineKeyboardMarkup);
    }

//    @Override
//    public SendMessage returnButtonsWithAntiHumanFlamethrower(Long chatId) {
//        var buttonFlamethrower = createButton("Огнемет против человека", ANTI_HUMAN_FLAMETHROWER_BUTTON);
//        var buttonContinue = createButton("Дальше", CONTINUE_ANTI_HUMAN_FLAMETHROWER_BUTTON);
//        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON);
//
//        List<InlineKeyboardButton> rowInLine = List.of(buttonFlamethrower);
//        List<InlineKeyboardButton> rowInLine2 = List.of(buttonContinue, buttonEndCount);
//
//        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine, rowInLine2);
//
//        return getSendMessageWithInlineMarkup(chatId, "Использовался ли огнемет против человека? ", inlineKeyboardMarkup);
//    }

    @Override
    public SendMessage getMessageWithInlineMurkupPlusMinus(Long chatId, int flamethrowerAmount) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeybordWithPlusMinus(flamethrowerAmount);

        return getSendMessageWithInlineMarkup(chatId, "Выберите количество:\n", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage getMessageWithInlineMurkupPlusMinusWithAntiHumanFlamethrower(Long chatId, int flamethrowerAmount) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getMessageWithInlineMurkupPlusMinusWithFlamethrowerAntiHuman(flamethrowerAmount);

        return getSendMessageWithInlineMarkup(chatId, "Выберите количество:\n", inlineKeyboardMarkup);
    }

    @Override
    public SendMessage returnButtonsToEndGameOrCountNext(Long chatId) {
        var buttonCountNext = createButton("Посчитать следующего", COUNT_NEXT_BUTTON);
        var buttonEndGame = createButton("Закончить игру", END_GAME_BUTTON);

        List<InlineKeyboardButton> rowInLine = List.of(buttonCountNext, buttonEndGame);
        InlineKeyboardMarkup inlineKeyboardMarkup = createInlineKeyboard(rowInLine);

        return BotUtils.getSendMessageWithInlineMarkup(chatId, "Что дальше?", inlineKeyboardMarkup);
    }

    @Override
    public InlineKeyboardButton createButton(String name, String callBackDataName) {
        var button = new InlineKeyboardButton(name);
        button.setCallbackData(callBackDataName);
        return button;
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

//    @Override
//    public EditMessageText editeMessageForInlineKeyboardPlusMinusWithAntiHumanFlamethrower(long chatId, int messageId, String text, int flamethrowerAmount) {
//        InlineKeyboardMarkup inlineKeyboardMarkup = getMessageWithInlineMurkupPlusMinusWithFlamethrowerAntiHuman(flamethrowerAmount);
//        return getEditMessageWithInlineMarkup(chatId, messageId, text, inlineKeyboardMarkup);
//    }

    @Override
    public InlineKeyboardMarkup getInlineKeybordWithPlusMinus(int flamethrowerAmount) {
        var buttonMinus = createButton("➖", MINUS_BUTTON);
        var buttonValue = createButton(String.valueOf(flamethrowerAmount), VALUE_BUTTON);
        var buttonPlus = createButton("➕", PLUS_BUTTON);
        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON);

        List<InlineKeyboardButton> rowInLine = List.of(buttonMinus, buttonValue, buttonPlus);
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonEndCount);

        return createInlineKeyboard(rowInLine, rowInLine2);
    }

    @Override
    public InlineKeyboardMarkup getMessageWithInlineMurkupPlusMinusWithFlamethrowerAntiHuman(int flamethrowerAmount) {
        return null;
    }

    @Override
    public EditMessageText editeMessageForInlineKeyboardPlusMinusWithAntiHumanFlamethrower(long chatId, int messageId, String text, int flamethrowerAmount) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineKeyboardWithInlineMurkupPlusMinusWithFlamethrowerAntiHuman(flamethrowerAmount);
        return getEditMessageWithInlineMarkup(chatId, messageId, text, inlineKeyboardMarkup);
    }


    @Override
    public InlineKeyboardMarkup getInlineKeyboardWithInlineMurkupPlusMinusWithFlamethrowerAntiHuman(int flamethrowerAmount) {
        var buttonMinus = createButton("➖", MINUS_BUTTON);
        var buttonValue = createButton(String.valueOf(flamethrowerAmount), VALUE_BUTTON);
        var buttonPlus = createButton("➕", PLUS_BUTTON);
        var buttonFlamethrower = createButton("Огнемет против человека", ANTI_HUMAN_FLAMETHROWER_BUTTON);
        var buttonEndCount = createButton("Посчитать", END_COUNT_BUTTON);

        List<InlineKeyboardButton> rowInLine = List.of(buttonMinus, buttonValue, buttonPlus);
        List<InlineKeyboardButton> rowInLine2 = List.of(buttonFlamethrower);
        List<InlineKeyboardButton> rowInLine3 = List.of(buttonEndCount);

        return createInlineKeyboard(rowInLine, rowInLine2, rowInLine3);
    }
}
