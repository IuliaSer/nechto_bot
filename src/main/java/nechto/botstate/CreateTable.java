package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.cache.ScoresStateCache;
import nechto.cache.TableAdminCache;
import nechto.dto.request.RequestGameDto;
import nechto.entity.Table;
import nechto.service.GameService;
import nechto.service.TableService;
import nechto.service.UserService;
import nechto.service.qrcode.QrCodeGenerator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static nechto.enums.BotState.CREATE_TABLE;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class CreateTable implements BotState {
    private final GameService gameService;
    private final ScoresStateCache scoresStateCache;
    private final QrCodeGenerator qrCodeGenerator;
    private final TableService tableService;
    private final UserService userService;
    private final TableAdminCache tableAdminCache;

    @Override
    public nechto.enums.BotState getBotState() {
        return CREATE_TABLE;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long adminId = message.getFrom().getId();
        RequestGameDto requestGameDto = new RequestGameDto(LocalDateTime.now(), new ArrayList<>());
        long gameId = gameService.save(requestGameDto).getId();

        Table table = Table.builder()
                .admin(userService.findById(adminId))
                .date(LocalDateTime.now())
                .currentUsers(new ArrayList<>())
                .games(List.of(gameService.findById(gameId)))
                .build();
        long tableId = tableService.save(table).getId();
        tableAdminCache.saveAdminTable(adminId, table);

        qrCodeGenerator.generateQrCode(String.valueOf(tableId), String.valueOf(adminId));

        scoresStateCache.put(adminId);
        scoresStateCache.get(adminId).setGameId(gameId);
        scoresStateCache.get(adminId).setGameIsFinished(false);
        return getSendMessage(adminId, format("Отсканируйте qr code, добавьтесь в игру %s", gameId));
    }
}
