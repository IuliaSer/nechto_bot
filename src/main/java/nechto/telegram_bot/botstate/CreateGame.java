package nechto.telegram_bot.botstate;


import lombok.RequiredArgsConstructor;
import nechto.dto.request.RequestGameDto;
import nechto.dto.response.ResponseGameDto;
import nechto.enums.BotState;
import nechto.service.GameService;
import nechto.service.RoleService;
import nechto.telegram_bot.BotStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static java.lang.String.format;
import static nechto.enums.BotState.*;
import static nechto.utils.BotUtils.getSendMessageWithInlineMarkup;

@RequiredArgsConstructor
@Component
public class CreateGame implements BotStateInterface {
    private final BotStateCash botStateCash;
    private final RoleService roleService;
    private final GameService gameService;

    @Override
    public BotState getBotState() {
        return CREATE_GAME;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        long chatId = message.getChatId();

        roleService.checkIsAdmin(userId);
        RequestGameDto requestGameDto = new RequestGameDto(LocalDateTime.now(), new ArrayList<>());
        ResponseGameDto responseGameDto = gameService.save(requestGameDto);

        return getSendMessageWithInlineMarkup(chatId, //chatId ne verno
                format("Перейдите по ссылке, добавьтесь в игру https://t.me/nechto21_bot?start=add_user_to_game_%s",
                        responseGameDto.getId().toString()));
    }
}
