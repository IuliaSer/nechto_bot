package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestGameDto;
import nechto.service.GameService;
import nechto.cache.ScoresStateCache;
import nechto.service.qrcode.QrCodeGenerator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.lang.String.format;
import static nechto.enums.BotState.CREATE_GAME;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class CreateGame implements BotState {
    private final GameService gameService;
    private final ScoresStateCache scoresStateCache;
    private final QrCodeGenerator qrCodeGenerator;

    @Override
    public nechto.enums.BotState getBotState() {
        return CREATE_GAME;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        RequestGameDto requestGameDto = new RequestGameDto(LocalDateTime.now(), new ArrayList<>());
        long gameId = gameService.save(requestGameDto).getId();

        qrCodeGenerator.generateQrCode(String.valueOf(gameId), String.valueOf(userId));

        scoresStateCache.put(userId);
        scoresStateCache.get(userId).setGameId(gameId);
        scoresStateCache.get(userId).setGameIsFinished(false); //?
        return getSendMessage(userId, format("Перейдите по ссылке, добавьтесь в игру %s", gameId));
    }
}
