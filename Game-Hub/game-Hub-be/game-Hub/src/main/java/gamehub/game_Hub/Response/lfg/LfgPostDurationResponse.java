package gamehub.game_Hub.Response.lfg;

import lombok.Builder;

@Builder
public record LfgPostDurationResponse(
    String name,
    String label,
    int days,
    boolean supporterOnly
) {

}
