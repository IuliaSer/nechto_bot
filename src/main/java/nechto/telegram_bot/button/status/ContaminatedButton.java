package nechto.telegram_bot.button.status;

import nechto.service.ScoresService;
import nechto.telegram_bot.button.ButtonService;
import nechto.telegram_bot.cache.ButtonsCash;
import nechto.telegram_bot.InlineKeyboardService;
import nechto.telegram_bot.cache.ScoresStateCash;
import org.springframework.stereotype.Component;

import static nechto.enums.Button.CONTAMINATED_BUTTON;

@Component
public class ContaminatedButton extends StatusButton {

    public ContaminatedButton(ScoresStateCash scoresStateCash, ScoresService scoresService, InlineKeyboardService inlineKeyboardService, ButtonService buttonService) {
        super(scoresStateCash, scoresService, inlineKeyboardService, buttonService);
    }

    @Override
    public String getButtonName() {
        return CONTAMINATED_BUTTON.name();
    }

}
