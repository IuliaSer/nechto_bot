package nechto.telegram_bot.button.status;

import nechto.service.ScoresService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ScoresStateCash;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.NECHTO_BUTTON;

@Component
public class NechtoButton extends StatusButton {

    public NechtoButton(ScoresStateCash scoresStateCash, ScoresService scoresService, InlineKeyboardService inlineKeyboardService, ButtonService buttonService) {
        super(scoresStateCash, scoresService, inlineKeyboardService, buttonService);
    }

    @Override
    public String getButtonName() {
        return NECHTO_BUTTON.name();
    }

}
