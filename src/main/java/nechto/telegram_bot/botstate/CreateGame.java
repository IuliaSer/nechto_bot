package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestGameDto;
import nechto.service.GameService;
import nechto.service.QrCodeGenerator;
import nechto.telegram_bot.cache.ScoresStateCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static nechto.enums.BotState.CREATE_GAME;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class CreateGame implements BotState {
    private final GameService gameService;
    private final QrCodeGenerator qrCodeGenerator;
    private final ScoresStateCache scoresStateCache;

    @Override
    public nechto.enums.BotState getBotState() {
        return CREATE_GAME;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        RequestGameDto requestGameDto = new RequestGameDto(LocalDateTime.now(), new ArrayList<>());
        long gameId = gameService.save(requestGameDto).getId();

//        qrCodeGenerator.generateQrCode(String.valueOf(gameId), String.valueOf(userId));

        scoresStateCache.put(userId);
        scoresStateCache.get(userId).setGameId(gameId);
        scoresStateCache.get(userId).setGameIsFinished(false);
        return getSendMessage(userId, "Вы успешно создали игру");
    }
}
