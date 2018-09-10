package com.kadirkertis.orfo.ui.main.fragments.places.di;

import com.kadirkertis.domain.interactor.place.GetAllPlacesUseCase;
import com.kadirkertis.domain.interactor.place.repository.PlaceRepository;
import com.kadirkertis.orfo.di.fragment.FragmentScope;
import com.kadirkertis.orfo.ui.main.fragments.places.adapter.AllPlacesAdapter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Kadir Kertis on 9/6/2018.
 */

@Module
public class PlacesFragmentModule {

    @Provides
    @FragmentScope
    public static AllPlacesAdapter allPlacesAdapter() {
        return new AllPlacesAdapter();
    }

    @Provides
    @FragmentScope
    public static GetAllPlacesUseCase getPlacesUseCase(PlaceRepository placeRepository) {
        return new GetAllPlacesUseCase(placeRepository);
    }

}
