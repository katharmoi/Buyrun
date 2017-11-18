package com.kadirkertis.data.mappers;

import com.kadirkertis.data.model.DataPlace;
import com.kadirkertis.domain.model.Place;

import java.util.List;

/**
 * Created by Kadir Kertis on 11/14/2017.
 */

public interface PlaceToDataPlaceMapper {

    DataPlace map(Place place);
    List<DataPlace> mapList(List<Place> places);
}
