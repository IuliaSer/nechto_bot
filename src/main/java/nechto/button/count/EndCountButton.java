package nechto.button.count;

import lombok.RequiredArgsConstructor;
import nechto.button.Button;
import nechto.button.ButtonService;
import nechto.cache.ScoresStateCache;
import nechto.dto.CachedScoresDto;
import nechto.enums.CommandStatus;
import nechto.enums.Status;
import nechto.service.InlineKeyboardService;
import nechto.service.ScoresService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.util.List;

import static nechto.enums.Button.AGAINST_HUMAN_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.DANGEROUS_BUTTON;
import static nechto.enums.Button.END_COUNT_BUTTON;
import static nechto.enums.Button.FLAMETHROWER_BUTTON;
import static nechto.enums.Button.MINUS_FOR_AGAINST_HUMAN_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.MINUS_BUTTON;
import static nechto.enums.Button.MINUS_WITH_AGAINST_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.PLUS_FOR_AGAINST_HUMAN_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.PLUS_BUTTON;
import static nechto.enums.Button.PLUS_WITH_AGAINST_FLAMETHROWER_BUTTON;
import static nechto.enums.Button.USEFULL_BUTTON;
import static nechto.enums.Button.VICTIM_BUTTON;
import static nechto.enums.CommandStatus.NECHTO_WIN;
import static nechto.enums.CommandStatus.PEOPLE_WIN;
import static nechto.enums.Status.ANTI_HUMAN_FLAMETHROWER;
import static nechto.enums.Status.CONTAMINATED;
import static nechto.enums.Status.FLAMETHROWER;
import static nechto.enums.Status.HUMAN;
import static nechto.enums.Status.LOOSE;
import static nechto.enums.Status.NECHTO;
import static nechto.enums.Status.WON;
import static nechto.utils.BotUtils.getButtonNameWithMessageId;

@RequiredArgsConstructor
@Component
public class EndCountButton implements Button {
    private final ScoresStateCache scoresStateCache;
    private final ScoresService scoresService;
    private final InlineKeyboardService inlineKeyboardService;
    private final ButtonService buttonService;

    @Override
    public nechto.enums.Button getButton() {
        return END_COUNT_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        String buttonName = getButtonNameWithMessageId(callbackQuery, getButton());

        
        String minusButtonName = getButtonNameWithMessageId(callbackQuery, MINUS_BUTTON);
        String plusButtonName = getButtonNameWithMessageId(callbackQuery, PLUS_BUTTON);
        String minusAntiHumanButtonName = getButtonNameWithMessageId(callbackQuery, MINUS_FOR_AGAINST_HUMAN_FLAMETHROWER_BUTTON);
        String plusAntiHumanButtonName = getButtonNameWithMessageId(callbackQuery, PLUS_FOR_AGAINST_HUMAN_FLAMETHROWER_BUTTON);
        String minusWithAgainstHumanFlamethrowerButtonName = getButtonNameWithMessageId(callbackQuery, MINUS_WITH_AGAINST_FLAMETHROWER_BUTTON);
        String plusWithAgainstHumanFlamethrowerButtonName = getButtonNameWithMessageId(callbackQuery, PLUS_WITH_AGAINST_FLAMETHROWER_BUTTON);
        String dangerousButtonName = getButtonNameWithMessageId(callbackQuery, DANGEROUS_BUTTON);
        String usefullButtonName = getButtonNameWithMessageId(callbackQuery, USEFULL_BUTTON);
        String victimButtonName = getButtonNameWithMessageId(callbackQuery, VICTIM_BUTTON);
        String flamethrowerButtonName = getButtonNameWithMessageId(callbackQuery, FLAMETHROWER_BUTTON);
        String againstHumanFlamethrowerButtonName = getButtonNameWithMessageId(callbackQuery, AGAINST_HUMAN_FLAMETHROWER_BUTTON );

        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        long userIdToCount = cachedScoresDto.getUserId();
        long gameId = cachedScoresDto.getGameId();
        CommandStatus commandStatus = cachedScoresDto.getCommandStatus();
        int flamethrowerAmount = cachedScoresDto.getFlamethrowerAmount();
        int antiHumanFlamethrowerAmount = cachedScoresDto.getAntiHumanFlamethrowerAmount();

        for (int i = 0; i < flamethrowerAmount; i++) {
            scoresService.addStatus(FLAMETHROWER, userIdToCount, gameId);
        }

        for (int i = 0; i < antiHumanFlamethrowerAmount; i++) {
            scoresService.addStatus(ANTI_HUMAN_FLAMETHROWER, userIdToCount, gameId);
        }
        List<Status> statuses = scoresService.findByUserIdAndGameId(userIdToCount, gameId).getStatuses();

        if ((commandStatus.equals(PEOPLE_WIN) && statuses.contains(HUMAN)) ||
                (commandStatus.equals(NECHTO_WIN) && statuses.contains(CONTAMINATED) ||
                        commandStatus.equals(NECHTO_WIN) && statuses.contains(NECHTO))) {
            scoresService.addStatus(WON, userIdToCount, gameId);
        } else {
            scoresService.addStatus(LOOSE, userIdToCount, gameId);
        }
        cachedScoresDto.setFlamethrowerAmount(0);
        cachedScoresDto.setAntiHumanFlamethrowerAmount(0);
        buttonService.deactivateButtons(buttonName, minusButtonName, plusButtonName, minusAntiHumanButtonName,
                plusAntiHumanButtonName, dangerousButtonName, usefullButtonName, victimButtonName,
                flamethrowerButtonName, againstHumanFlamethrowerButtonName, minusWithAgainstHumanFlamethrowerButtonName,
                plusWithAgainstHumanFlamethrowerButtonName);
        if (cachedScoresDto.isGameIsFinished()) {
            return inlineKeyboardService.returnButtonsForChangingGame(userId);
        }
        return inlineKeyboardService.returnButtonsToEndGameOrCountNext(userId);
    }
}
