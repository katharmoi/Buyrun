package com.kadirkertis.data.mappers;

import com.annimon.stream.Stream;
import com.google.firebase.database.ServerValue;
import com.kadirkertis.data.model.DataPlace;
import com.kadirkertis.domain.model.Place;

import java.util.HashMap;
import java.util.List;


/**
 * Created by Kadir Kertis on 11/14/2017.
 */

public class PlaceToDataPlaceMapperImpl implements PlaceToDataPlaceMapper {
    @Override
    public DataPlace map(Place place) {
        HashMap<String,Object> timeAddedObj = new HashMap<>();
        timeAddedObj.put("timestamp", ServerValue.TIMESTAMP);
        return new DataPlace(place.getId(), place.getPlaceName(),place.getOwnerName(),place.getEmail(),place.getAddress(),
                place.getPhone(),place.getPlaceType(),place.getImageUrl(),place.getLatitude(),place.getLongitude(),timeAddedObj);
    }

    @Override
    public List<DataPlace> mapList(List<Place> places) {
        HashMap<String,Object> timeAddedObj = new HashMap<>();
        timeAddedObj.put("timestamp", ServerValue.TIMESTAMP);
        return Stream.of(places)
                .map(this::map)
                .toList();
    }
}
