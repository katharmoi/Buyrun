package com.kadirkertis.orfo.ui.main.fragments.places;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.kadirkertis.domain.interactor.place.GetAllPlacesUseCase;

import javax.inject.Inject;

import io.reactivex.Scheduler;

/**
 * Created by Kadir Kertis on 9/7/2018.
 */

public class PlacesFragmentViewModelFactory implements ViewModelProvider.Factory {
    private final GetAllPlacesUseCase getAllPlacesUseCase;
    private final Scheduler mainThreadScheduler;

    @Inject
    public PlacesFragmentViewModelFactory(GetAllPlacesUseCase getAllPlacesUseCase, Scheduler mainThreadScheduler) {
        this.getAllPlacesUseCase = getAllPlacesUseCase;
        this.mainThreadScheduler = mainThreadScheduler;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PlacesFragmentViewModelFactory.class)) {
            return (T) new PlacesFragmentViewModel(getAllPlacesUseCase, mainThreadScheduler);
        }
        throw new IllegalArgumentException("ViewModel class is not compatible");
    }
}
