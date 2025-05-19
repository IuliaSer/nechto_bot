package nechto.telegram_bot.button;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestScoresDto;
import nechto.repository.ScoresRepository;
import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.ScoresStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Status.FLAMETHROWER;
import static nechto.utils.CommonConstants.END_COUNT_BUTTON;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public class EndCountButton implements Button {
    private final ScoresStateCash scoresStateCash;
    private final ScoresService scoresService;
    private final ScoresRepository scoresRepository;
    private final InlineKeyboardService inlineKeyboardService;

    @Override
    public String getButtonName() {
        return END_COUNT_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        scoresRepository.findAllByGameId(24L).get(0).getStatuses();

        RequestScoresDto requestScoresDto = scoresStateCash.getScoresStateMap().get(SCORES);
        long userIdToCount = requestScoresDto.getUserId();
        long gameId = requestScoresDto.getGameId();
        int flamethrowerAmount = requestScoresDto.getFlamethrowerAmount();

        for (int i = 0; i < flamethrowerAmount; i++) {
            scoresService.addStatus(FLAMETHROWER, userIdToCount, gameId);
        }
//        scoresService.findByUserIdAndGameId(userIdToCount, gameId).getStatuses();
        return inlineKeyboardService.returnButtonsToEndGameOrCountNext(userId);
    }
}
