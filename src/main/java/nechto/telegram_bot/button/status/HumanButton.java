package nechto.telegram_bot.button.status;

import nechto.service.ScoresService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.cache.ScoresStateCash;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.HUMAN_BUTTON;

@Component
public class HumanButton extends StatusButton {


    public HumanButton(ScoresStateCash scoresStateCash, ScoresService scoresService, InlineKeyboardService inlineKeyboardService, ButtonService buttonService) {
        super(scoresStateCash, scoresService, inlineKeyboardService, buttonService);
    }

    @Override
    public String getButtonName() {
        return HUMAN_BUTTON.name();
    }

}
