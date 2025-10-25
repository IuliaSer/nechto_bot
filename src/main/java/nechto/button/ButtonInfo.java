package nechto.button;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Getter
@Setter
@AllArgsConstructor
public class ButtonInfo {
    private boolean isActive;

    private InlineKeyboardButton button;
}
