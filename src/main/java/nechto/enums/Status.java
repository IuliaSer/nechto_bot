package nechto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Status {
        NECHTO("н"),
        CONTAMINATED("з"),
        HUMAN("ч"),
        WON("выиграл(а)"),
        LOOSE("проиграл(а)"),
        DANGEROUS("0.2(о)"),
        USEFULL("0.2(п)"), // сократить
        VICTIM("0.5(ж)"),
        FLAMETHROWER("огнемет"),
        ANTI_HUMAN_FLAMETHROWER("огнемет против человека"),
        BURNED("с"),
        PENALTY("штраф");

        private String name;

        Status(String name) {
                this.name = name;
        }
}

