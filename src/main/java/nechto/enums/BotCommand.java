package nechto.enums;

import java.util.EnumSet;
import java.util.Optional;
import java.util.regex.Pattern;

import static nechto.enums.Authority.ROLE_ADMIN;
import static nechto.enums.Authority.ROLE_OWNER;

public enum BotCommand {
    CREATE_GAME("^/create_game$", EnumSet.of(ROLE_ADMIN, ROLE_OWNER), BotState.CREATE_GAME),
    REGISTER("^/register$", EnumSet.allOf(Authority.class), BotState.START_REGISTRATION),
    START_ADD("^/start\\s+add.*$", EnumSet.allOf(Authority.class), BotState.ADD_USER),
    START_COUNT("^/start_count$", EnumSet.of(ROLE_ADMIN, ROLE_OWNER), BotState.START_COUNT),
    SHOW_RESULTS("^/show_results$", EnumSet.allOf(Authority.class), BotState.SHOW_RESULTS),
    CHANGE_GAME("^/change_game$", EnumSet.of(ROLE_ADMIN, ROLE_OWNER), BotState.START_CHANGE_GAME),
    MAKE_ADMIN("^/make_admin$", EnumSet.of(ROLE_OWNER), BotState.MAKE_ADMIN_START),
    MAKE_USER("^/make_user$", EnumSet.of(ROLE_OWNER), BotState.MAKE_USER_START);

    private final Pattern pattern;
    private final EnumSet<Authority> allowed;
    private final BotState targetState;

    BotCommand(String regex, EnumSet<Authority> allowed, BotState targetState) {
        this.pattern = Pattern.compile(regex);
        this.allowed = allowed;
        this.targetState = targetState;
    }

    public static Optional<BotCommand> match(String text) {
        if (text == null) return Optional.empty();
        for (var c : values()) {
            if (c.pattern.matcher(text).matches()) return Optional.of(c);
        }
        return Optional.empty();
    }

    public boolean isAllowed(Authority role) { return allowed.contains(role); }

    public BotState state() { return targetState; }

}
