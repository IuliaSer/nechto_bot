package nechto.telegram_bot.button;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.ScoresStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Status.ANTI_HUMAN_FLAMETHROWER;
import static nechto.enums.Status.FLAMETHROWER;
import static nechto.utils.CommonConstants.END_COUNT_BUTTON;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public class EndCountButton implements Button {
    private final ScoresStateCash scoresStateCash;
    private final ScoresService scoresService;
    private final InlineKeyboardService inlineKeyboardService;

    @Override
    public String getButtonName() {
        return END_COUNT_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        RequestScoresDto requestScoresDto = scoresStateCash.getScoresStateMap().get(SCORES);
        long userIdToCount = requestScoresDto.getUserId();
        long gameId = requestScoresDto.getGameId();
        int flamethrowerAmount = requestScoresDto.getFlamethrowerAmount();
        int antiHumanFlamethrowerAmount = requestScoresDto.getAntiHumanFlamethrowerAmount();

        for (int i = 0; i < flamethrowerAmount; i++) {
            scoresService.addStatus(FLAMETHROWER, userIdToCount, gameId);
        }

        for (int i = 0; i < antiHumanFlamethrowerAmount; i++) {
            scoresService.addStatus(ANTI_HUMAN_FLAMETHROWER, userIdToCount, gameId);
        }
        requestScoresDto.setFlamethrowerAmount(0);
        requestScoresDto.setAntiHumanFlamethrowerAmount(0);
        requestScoresDto.setFlamethrowerPressed(false);
        requestScoresDto.setAntiHumanFlamethrowerPressed(false);
        return inlineKeyboardService.returnButtonsToEndGameOrCountNext(userId);
    }
}
