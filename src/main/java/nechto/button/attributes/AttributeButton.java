 package nechto.button.attributes;

 import lombok.RequiredArgsConstructor;
 import nechto.button.ButtonService;
 import nechto.dto.CachedScoresDto;
 import nechto.enums.Status;
 import nechto.service.ScoresService;
 import nechto.button.Button;
 import nechto.cache.ScoresStateCache;
 import org.springframework.stereotype.Component;
 import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
 import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

 import static nechto.utils.BotUtils.getButtonNameWithMessageId;

 @RequiredArgsConstructor
@Component
public abstract class AttributeButton implements Button {
    private final ScoresStateCache scoresStateCache;
    private final ScoresService scoresService;
    private final ButtonService buttonService;

    @Override
    public abstract nechto.enums.Button getButton();

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        String buttonName = getButtonNameWithMessageId(callbackQuery, getButton());
        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        long userIdToCount = cachedScoresDto.getUserId();
        long gameId = cachedScoresDto.getGameId();
        scoresService.addStatus(getStatus(), userIdToCount, gameId);
        buttonService.deactivateButtons(buttonName);
        return null;
    }

    public abstract Status getStatus();
}
