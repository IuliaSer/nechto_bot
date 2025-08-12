package nechto.telegram_bot.button;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.END_COUNT_BUTTON;
import static nechto.enums.Status.ANTI_HUMAN_FLAMETHROWER;
import static nechto.enums.Status.FLAMETHROWER;

@RequiredArgsConstructor
@Component
public class EndCountButton implements Button {
    private final ScoresStateCache scoresStateCache;
    private final ScoresService scoresService;
    private final InlineKeyboardService inlineKeyboardService;

    @Override
    public nechto.enums.Button getButton() {
        return END_COUNT_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        RequestScoresDto requestScoresDto = scoresStateCache.getScoresStateMap().get(userId);
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
        return inlineKeyboardService.returnButtonsToEndGameOrCountNext(userId);
    }
}
