package com.kadirkertis.data.mappers;

import com.kadirkertis.data.model.DataPlace;
import com.kadirkertis.domain.interactor.place.model.Place;

import java.util.List;

/**
 * Created by Kadir Kertis on 12/1/2017.
 */

public interface DataPlaceToPlaceMapper {

    Place map(DataPlace dataPlace);
    List<Place> mapList(List<DataPlace> places);
}
