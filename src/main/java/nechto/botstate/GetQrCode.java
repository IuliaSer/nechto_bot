package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.cache.TableQrCodeCache;
import nechto.entity.Table;
import nechto.service.TableService;
import nechto.service.UserService;
import nechto.service.qrcode.TelegramQrCodeSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetQrCode implements BotState {
    private final TableQrCodeCache tableQrCodeCache;
    private final TableService tableService;
    private final TelegramQrCodeSender telegramQrCodeSender;
    private final UserService userService;

    @Override
    public nechto.enums.BotState getBotState() {
        return nechto.enums.BotState.GET_QRCODE;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();

        Optional<Table> table = tableService.findByToday()
                .stream()
                .sorted(java.util.Comparator.comparing(Table::getDate).reversed())
                        .filter(t -> t.getCurrentUsers().contains(userService.findById(userId)))
                                .findFirst();

        telegramQrCodeSender.sendPhoto(
                userId,
                tableQrCodeCache.get(table.get().getAdmin().getId()),
                "Сканируйте QR и начинайте",
                "qr.png");

        return null;
    }
}
