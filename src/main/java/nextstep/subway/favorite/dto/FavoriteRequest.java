package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;

public class FavoriteRequest {

    private Long sourceStationId;
    private Long targetStationId;

    public FavoriteRequest(Long sourceStationId, Long targetStationId) {
        this.sourceStationId = sourceStationId;
        this.targetStationId = targetStationId;
    }

    public Long getSourceStationId() {
        return sourceStationId;
    }

    public Long getTargetStationId() {
        return targetStationId;
    }

}
