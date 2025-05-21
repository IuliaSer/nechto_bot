package nechto.telegram_bot.botstate;

import lombok.RequiredArgsConstructor;
import nechto.entity.Scores;
import nechto.enums.BotState;
import nechto.repository.ScoresRepository;
import nechto.service.GameService;
import nechto.service.RoleService;
import nechto.service.ScoresService;
import nechto.service.UserService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.ScoresStateCash;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.persistence.EntityNotFoundException;

import java.util.List;

import static java.lang.String.format;
import static nechto.enums.BotState.COUNT;
import static nechto.utils.BotUtils.getSendMessageWithInlineMarkup;
import static nechto.utils.CommonConstants.SCORES;

@RequiredArgsConstructor
@Component
public class Count implements BotStateInterface {
    private final RoleService roleService;
    private final GameService gameService;
    private final UserService userService;
    private final ScoresStateCash scoresStateCash;
    private final InlineKeyboardService inlineKeyboardService;
    private final ScoresRepository scoresRepository;

    @Override
    public BotState getBotState() {
        return COUNT;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        String messageText = message.getText();
        roleService.checkIsAdmin(userId);
        long userIdToCount;
        try {
            userIdToCount = userService.findByUsername(messageText).getId();
            gameService.addUser(1L, userIdToCount); //na vremya testa
            List<Scores> scores = scoresRepository.findAllByGameId(1L);
            int size = 0;
            if (!scores.isEmpty()) {
                size = scores.get(0).getStatuses().size();
            }
            System.out.println("After add user" + size);
            scoresStateCash.getScoresStateMap().get(SCORES).setGameId(1L);  //na vremya testa
//                    scoresService.findByUserIdAndGameId(1L, userIdToCount); //proverka to  chto scores есть
        } catch (EntityNotFoundException e) {
            return getSendMessageWithInlineMarkup
                    (userId, format("Пользователь с ником %s не существует или не добавлен в игру", messageText)); //TODO разделить в 2 сообщения
        }
        scoresStateCash.getScoresStateMap().get(SCORES).setUserId(userIdToCount);
        return inlineKeyboardService.returnButtonsWithStatusesLooseWin(userId);
    }
}
