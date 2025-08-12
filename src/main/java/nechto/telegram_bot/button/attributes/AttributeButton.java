 package nechto.telegram_bot.button.attributes;

 import lombok.RequiredArgsConstructor;
 import nechto.dto.request.RequestScoresDto;
 import nechto.enums.Status;
 import nechto.service.ScoresService;
 import nechto.telegram_bot.button.Button;
 import nechto.telegram_bot.button.ButtonService;
 import nechto.telegram_bot.cache.ScoresStateCache;
 import org.springframework.stereotype.Component;
 import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
 import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

@RequiredArgsConstructor
@Component
public abstract class AttributeButton implements Button {
    private final ScoresStateCache scoresStateCache;
    private final ScoresService scoresService;
    private final ButtonService buttonService;

    @Override
    public abstract nechto.enums.Button getButton();

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackquery, Long userId) {
        if(!buttonService.isActive(getButton().name(), getButton().name())) {
            return null;
        }
        RequestScoresDto requestScoresDto = scoresStateCache.getScoresStateMap().get(userId);
        long userIdToCount = requestScoresDto.getUserId();
        long gameId = requestScoresDto.getGameId();
        scoresService.addStatus(getStatus(), userIdToCount, gameId);
        return null;
    }

    public abstract Status getStatus();
}
