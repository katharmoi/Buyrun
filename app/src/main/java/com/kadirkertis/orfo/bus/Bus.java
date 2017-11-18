package com.kadirkertis.orfo.bus;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

/**
 * Created by Kadir Kertis on 11/13/2017.
 */

public class Bus {
    private static Bus sBus;
    private final Relay<Map<String, Object>> bus = PublishRelay.<Map<String,Object>>create().toSerialized();

    private Bus(){}

    public static synchronized Bus getInstance(){
        if(sBus == null){
            sBus = new Bus();
        }
        return sBus;
    }

    public void post(Map<String,Object> e){
        bus.accept(e);
    }

    public Flowable<Map<String,Object>> asFlowable(){
        return bus.toFlowable(BackpressureStrategy.LATEST);
    }

    public boolean hasObservers(){return bus.hasObservers();}


}
