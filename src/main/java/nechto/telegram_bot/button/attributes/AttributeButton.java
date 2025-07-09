 package nechto.telegram_bot.button.attributes;

 import lombok.RequiredArgsConstructor;
 import nechto.dto.request.RequestScoresDto;
 import nechto.enums.Status;
 import nechto.service.ScoresService;
 import nechto.telegram_bot.button.Button;
 import nechto.telegram_bot.button.ButtonService;
 import nechto.telegram_bot.cache.ScoresStateCash;
 import org.springframework.stereotype.Component;
 import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
 import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

 import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public abstract class AttributeButton implements Button {
    private final ScoresStateCash scoresStateCash;
    private final ScoresService scoresService;
    private final ButtonService buttonService;

    @Override
    public abstract String getButtonName();

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        if(!buttonService.isActive(getButtonName(), getButtonName())) {
            return null;
        }
        RequestScoresDto requestScoresDto = scoresStateCash.getScoresStateMap().get(SCORES);
        long userIdToCount = requestScoresDto.getUserId();
        long gameId = requestScoresDto.getGameId();
        scoresService.addStatus(getStatus(), userIdToCount, gameId);
        return null;
    }

    public abstract Status getStatus();
}
