package nechto.telegram_bot.button.status;

import nechto.enums.Button;
import nechto.service.ScoresService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.cache.ButtonStatusCash;
import nechto.telegram_bot.cache.ScoresStateCash;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.HUMAN_BUTTON;

@Component
public class HumanButton extends StatusButton {


    public HumanButton(ScoresStateCash scoresStateCash, ScoresService scoresService,
                       InlineKeyboardService inlineKeyboardService, ButtonService buttonService, ButtonStatusCash buttonStatusCash) {
        super(scoresStateCash, scoresService, inlineKeyboardService, buttonService, buttonStatusCash);
    }

    @Override
    public Button getButton() {
        return HUMAN_BUTTON;
    }

}
