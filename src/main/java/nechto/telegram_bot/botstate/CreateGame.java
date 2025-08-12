package nechto.telegram_bot.botstate;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestGameDto;
import nechto.dto.response.ResponseGameDto;
import nechto.enums.BotState;
import nechto.service.GameService;
import nechto.service.QrCodeGenerator;
import nechto.service.RoleService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.lang.String.format;
import static nechto.enums.BotState.CREATE_GAME;
import static nechto.utils.BotUtils.getSendMessage;

@RequiredArgsConstructor
@Component
public class CreateGame implements BotStateInterface {
    private final RoleService roleService;
    private final GameService gameService;
    private final QrCodeGenerator qrCodeGenerator;
    @Override
    public BotState getBotState() {
        return CREATE_GAME;
    }

    @Override
    public BotApiMethod<?> process(Message message) throws IOException, WriterException {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();

        roleService.isAdmin(userId);
        RequestGameDto requestGameDto = new RequestGameDto(LocalDateTime.now(), new ArrayList<>());
        ResponseGameDto responseGameDto = gameService.save(requestGameDto);
//        qrCodeGenerator.generateQrCode(String.valueOf(responseGameDto.getId()), String.valueOf(chatId));
//        return BotUtils.getSendMessage(chatId,
//                format("Перейдите по ссылке, добавьтесь в игру %s",
//                        responseGameDto.getId().toString()));
        return getSendMessage(chatId,
                format("Перейдите по ссылке, добавьтесь в игру https://t.me/nechto21_bot?start=add_user_to_game_%s",
                        responseGameDto.getId().toString()));
    }
}
