package com.kadirkertis.orfo.ui.main.fragments.places;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.kadirkertis.domain.interactor.place.GetAllPlacesUseCase;
import com.kadirkertis.domain.interactor.place.model.Place;
import com.kadirkertis.orfo.utils.Response;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Kadir Kertis on 9/6/2018.
 */

public class PlacesFragmentViewModel extends ViewModel {

    private final GetAllPlacesUseCase getAllPlacesUseCase;
    private final Scheduler mainThreadSchedular;

    private final CompositeDisposable disposables = new CompositeDisposable();

    private MutableLiveData<Response<List<Place>>> placeResponse = new MutableLiveData<>();

    public PlacesFragmentViewModel(GetAllPlacesUseCase getAllPlacesUseCase, Scheduler mainThreadSchedular) {
        this.getAllPlacesUseCase = getAllPlacesUseCase;
        this.mainThreadSchedular = mainThreadSchedular;
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }

    public MutableLiveData<Response<List<Place>>> getPlaceResponse() {
        return placeResponse;
    }

    void loadAllPlaces() {
        disposables.add(getAllPlacesUseCase.execute()
                .doOnSubscribe(__ -> placeResponse.setValue(Response.loading()))
                .observeOn(mainThreadSchedular)
                .subscribe(
                        result -> placeResponse.setValue(Response.success(result)),
                        error -> placeResponse.setValue(Response.error(error))

                ));
    }
}
