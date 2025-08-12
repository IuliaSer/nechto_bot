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
        DANGEROUS("опасный"),
        USEFULL("полезный"), // сократить
        VICTIM("жертва"),
        FLAMETHROWER("огнемет"),
        ANTI_HUMAN_FLAMETHROWER("огнемет против человека"),
        BURNED("с"),
        PENALTY("штраф");

        private String name;

        Status(String name) {
                this.name = name;
        }
}

