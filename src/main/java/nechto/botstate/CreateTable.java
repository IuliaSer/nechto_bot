package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.cache.ScoresStateCache;
import nechto.cache.TableAdminCache;
import nechto.dto.request.RequestGameDto;
import nechto.entity.Game;
import nechto.entity.Table;
import nechto.entity.User;
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

        int amountOfTables = tableService.findByToday().size();
        User admin = userService.findById(adminId);
        List<User> users = new ArrayList<>();
        users.add(admin);
        Table table = Table.builder()
                .admin(admin)
                .name(String.valueOf(++amountOfTables))
                .date(LocalDateTime.now())
                .currentUsers(users)
                .games(new ArrayList<>())
                .build();
        Table tableSaved = tableService.save(table);
        long tableId = tableSaved.getId();

        RequestGameDto requestGameDto = new RequestGameDto(LocalDateTime.now(), new ArrayList<>(), tableSaved);
        Game gameSaved = gameService.save(requestGameDto);
        long gameId = gameSaved.getId();
        gameService.addUser(gameId, adminId);

        tableSaved.getGames().add(gameSaved);
        tableService.save(tableSaved);
        tableAdminCache.saveAdminTable(adminId, tableId);

        qrCodeGenerator.generateQrCode(String.valueOf(tableId), String.valueOf(adminId));

        scoresStateCache.put(adminId);
        scoresStateCache.get(adminId).setGameId(gameId);
        scoresStateCache.get(adminId).setGameIsFinished(false);
        return getSendMessage(adminId, format("Отсканируйте qr code, добавьтесь в игру %s", gameId));
    }
}
