package nechto.service;

import lombok.RequiredArgsConstructor;
import nechto.cache.UserInfoCache;
import nechto.enums.Authority;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeChat;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

  private static final List<BotCommand> COMMON_RESULTS = List.of(
          cmd("/results_for_a_game", "показать результаты за последнюю игру"),
          cmd("/results_for_a_day", "показать результаты за день"),
          cmd("/results_for_a_month", "показать результаты за месяц"),
          cmd("/results_for_a_quarter", "показать результаты за квартал")
  );

  private static final List<BotCommand> USER_ONLY = List.of(
          cmd("/register", "зарегистрироваться")
  );

  private static final List<BotCommand> ADMIN_CORE = List.of(
          cmd("/create_game", "создать игру"),
          cmd("/count", "посчитать очки"),
          cmd("/change_game", "изменить последнюю игру")
  );

  private static final List<BotCommand> OWNER_EXTRA = List.of(
          cmd("/change_game", "изменить последнюю игру"),
          cmd("/make_admin", "сделать пользователя админом"),
          cmd("/make_user", "забрать права админа")
  );

  private static final Map<Authority, List<BotCommand>> MENU = Map.of(
          Authority.ROLE_USER,  concat(USER_ONLY, COMMON_RESULTS),
          Authority.ROLE_ADMIN, concat(ADMIN_CORE, COMMON_RESULTS),
          Authority.ROLE_OWNER, concat(
                  // OWNER = ADMIN + свои доп. команды (и при желании переопределяем /change_game описанием)
                  ADMIN_CORE.stream()
                          .filter(c -> !c.getCommand().equals("/change_game"))
                          .toList(),
                  COMMON_RESULTS,
                  OWNER_EXTRA
          )
  );

  private static BotCommand cmd(String c, String d) {
    return new BotCommand(c, d);
  }

  private static List<BotCommand> concat(List<BotCommand>... parts) {
    return java.util.Arrays.stream(parts).flatMap(List::stream).toList();
  }

  private final TelegramFeignClient telegram;
  private final UserInfoCache userInfoCache;

  @Override
  public void refreshCommands(long userId, Authority authority) {
    userInfoCache.get(userId).incrementAndGet();

    telegram.setMyCommands(SetMyCommands.builder()
            .commands(MENU.getOrDefault(authority, List.of()))
            .scope(new BotCommandScopeChat(String.valueOf(userId)))
            .build());
  }
}

