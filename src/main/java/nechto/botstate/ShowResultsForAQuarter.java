package nechto.botstate;

import lombok.RequiredArgsConstructor;
import nechto.service.results.ShowResultsService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import static nechto.enums.BotState.SHOW_RESULTS_FOR_A_QUARTER;

@RequiredArgsConstructor
@Component
public class ShowResultsForAQuarter implements BotState {
    private final ShowResultsService showResultsService;

    @Override
    public nechto.enums.BotState getBotState() {
        return SHOW_RESULTS_FOR_A_QUARTER;
    }

    @Override
    public BotApiMethod<?> process(Message message) {
        long userId = message.getFrom().getId();
        return showResultsService.showResultsForAQuarter(userId);

    }
}
