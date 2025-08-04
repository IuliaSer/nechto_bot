package nechto.telegram_bot.button.role;

import nechto.enums.Button;
import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ButtonStatusCash;
import nechto.telegram_bot.cache.ScoresStateCash;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.NECHTO_BUTTON;

@Component
public class NechtoButton extends RoleButton {

    public NechtoButton(ScoresStateCash scoresStateCash, ScoresService scoresService,
                        InlineKeyboardService inlineKeyboardService, ButtonService buttonService, ButtonStatusCash buttonStatusCash) {
        super(scoresStateCash, scoresService, inlineKeyboardService, buttonService, buttonStatusCash);
    }

    @Override
    public Button getButton() {
        return NECHTO_BUTTON;
    }

}
