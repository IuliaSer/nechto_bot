package nechto.service.results;

import nechto.dto.ScoresDto;
import nechto.entity.Scores;
import nechto.enums.Status;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static nechto.enums.Status.CONTAMINATED;
import static nechto.enums.Status.DANGEROUS;
import static nechto.enums.Status.FLAMETHROWER;
import static nechto.enums.Status.HUMAN;
import static nechto.enums.Status.LAST_CONTAMINATED;
import static nechto.enums.Status.NECHTO;
import static nechto.enums.Status.USEFULL;
import static nechto.enums.Status.VICTIM;

@Component
public class ScoresDtoMapper {

    public List<ScoresDto> mapGameScores(List<Scores> scores) {
        List<ScoresDto> out = new ArrayList<>(scores.size());
        for (Scores s : scores) {
            double flamethrower = 0d;
            List<String> opj = new ArrayList<>(3);
            String statusMain = null;

            for (Status st : s.getStatuses()) {
                if (st == NECHTO || st == CONTAMINATED || st == HUMAN || st == LAST_CONTAMINATED) {
                    statusMain = st.getName();
                } else if (st == FLAMETHROWER) {
                    flamethrower += 0.3d;
                } else if (st == DANGEROUS || st == USEFULL || st == VICTIM) {
                    opj.add(st.getName());
                }
            }

            out.add(ScoresDto.builder()
                    .username(s.getUser().getUsername())
                    .status(statusMain)
                    .flamethrowerScores((float) flamethrower)
                    .opjStatusScores(opj)
                    .scores(s.getScores())
                    .build());
        }
        return out;
    }
}

