package nechto.button.burned;

import lombok.RequiredArgsConstructor;
import nechto.button.Button;
import nechto.button.ButtonService;
import nechto.cache.ScoresStateCache;
import nechto.dto.CachedScoresDto;
import nechto.enums.Status;
import nechto.service.InlineKeyboardService;
import nechto.service.ScoresService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import static nechto.enums.Button.NO_BURNED_BUTTON;
import static nechto.enums.Button.YES_BURNED_BUTTON;
import static nechto.utils.BotUtils.getButtonNameWithMessageId;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class YesBurnedButton implements Button {
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresService scoresService;
    private final ScoresStateCache scoresStateCache;
    private final ButtonService buttonService;

    @Override
    public nechto.enums.Button getButton() {
        return YES_BURNED_BUTTON;
    }

    @Override
    public BotApiMethod<?> onButtonPressed(CallbackQuery callbackQuery, Long userId) {
        String buttonName = getButtonNameWithMessageId(callbackQuery, getButton());
        CachedScoresDto cachedScoresDto = scoresStateCache.get(userId);
        long userIdToCount = cachedScoresDto.getUserId();
        long gameId = cachedScoresDto.getGameId();
        Status status = cachedScoresDto.getStatus();

        scoresService.addStatus(Status.BURNED, userIdToCount, gameId);
        buttonService.deactivateButtons(buttonName, getButtonNameWithMessageId(callbackQuery, NO_BURNED_BUTTON));

        if (status.equals(Status.HUMAN)) {
            return inlineKeyboardService.returnButtonsForBurnedHuman(userId);
        } else if (status.equals(Status.CONTAMINATED)) {
            return inlineKeyboardService.returnButtonsForBurnedContaminated(userId);
        } else {
            return getSendMessage(userId, "Не выбран статус человек, зараженный или нечто!");
        }
    }
}
