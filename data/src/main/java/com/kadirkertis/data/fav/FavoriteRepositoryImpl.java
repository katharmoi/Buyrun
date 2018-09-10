package com.kadirkertis.data.fav;

import com.kadirkertis.data.mappers.PlaceToDataPlaceMapperImpl;
import com.kadirkertis.domain.interactor.place.model.Place;
import com.kadirkertis.domain.interactor.place.repository.FavoriteRepository;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Kadir Kertis on 11/9/2017.
 */

public class FavoriteRepositoryImpl implements FavoriteRepository {
    @Override
    public Completable addPlaceToFavorites(Place place) {
        return Completable.fromAction(() -> FavoritePlaceServiceImpl.addToFavPlace(new PlaceToDataPlaceMapperImpl().map(place)));
    }

    @Override
    public Completable deleteFromFavorites(Place place) {
        return Completable.fromCallable(() -> FavoritePlaceServiceImpl.deleteFromFavPlaces(new PlaceToDataPlaceMapperImpl().map(place)));
    }

    @Override
    public Single<Boolean> isPlaceInFav(Place place) {
        return Single.fromCallable(() -> FavoritePlaceServiceImpl.isPlaceInDb(new PlaceToDataPlaceMapperImpl().map(place)));
    }
}
