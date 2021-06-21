package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.station.domain.Station;

public class FavoriteResponse {

    private Long id;
    private Station sourceStation;
    private Station targetStation;

    private FavoriteResponse(Long id, Station sourceStation, Station targetStation) {
        this.id = id;
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public Long getId() {
        return id;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getTargetStation() {
        return targetStation;
    }

    public static FavoriteResponse of(Favorite favorite) {
        return new FavoriteResponse(favorite.getId(), favorite.getSourceStation(), favorite.getTargetStation());
    }
}
