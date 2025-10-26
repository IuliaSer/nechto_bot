package nechto.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nechto.enums.Authority;
import nechto.cache.UserInfoCache;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeChat;

import java.util.List;
import java.util.Map;

import static nechto.enums.Authority.ROLE_ADMIN;
import static nechto.enums.Authority.ROLE_OWNER;
import static nechto.enums.Authority.ROLE_USER;

@Getter
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

  private static final Map<Authority, List<BotCommand>> menu = Map.of(
      ROLE_ADMIN, List.of(
                  new BotCommand("/create_game", "создать игру"),
                  new BotCommand("/start_count", "посчитать очки"),
                  new BotCommand("/show_results", "показать результаты"),
                  new BotCommand("/change_game", "изменить игру")),
      ROLE_USER,  List.of(
                  new BotCommand("/register", "зарегистрироваться"),
                  new BotCommand("/show_results", "показать результаты")),
      ROLE_OWNER, List.of(
              new BotCommand("/create_game", "создать игру"),
              new BotCommand("/start_count", "посчитать очки"),
              new BotCommand("/show_results", "показать результаты"),
              new BotCommand("/change_game", "изменить игру"),
              new BotCommand("/make_admin", "сделать пользователя админом"),
                  new BotCommand("/make_user", "забрать права админа"))
  );

  private final TelegramFeignClient telegram;
  private final UserInfoCache userInfoCache;

  /**
   * Обновляем menu‑команды в Telegram, когда у пользователя меняется роль
   */
  @Override
  public void refreshCommands(long userId, Authority authority) {
    userInfoCache.get(userId).incrementAndGet();
      telegram.setMyCommands(SetMyCommands.builder()
          .commands(menu.get(authority))
          .scope(new BotCommandScopeChat(String.valueOf(userId)))
          .build());
  }
}
