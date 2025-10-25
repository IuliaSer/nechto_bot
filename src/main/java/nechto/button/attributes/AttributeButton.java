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
        if(!buttonService.isActive(getButton().name())) {
            return null;
        }
        buttonService.deactivateButtons(getButton().name());

        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        long userIdToCount = cachedScoresDto.getUserId();
        long gameId = cachedScoresDto.getGameId();
        scoresService.addStatus(getStatus(), userIdToCount, gameId);
        return null;
    }

    public abstract Status getStatus();
}
