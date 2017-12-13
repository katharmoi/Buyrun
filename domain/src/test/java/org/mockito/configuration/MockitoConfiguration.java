package org.mockito.configuration;

import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;

/**
 * Created by Kadir Kertis on 12/12/2017.
 */

public class MockitoConfiguration extends DefaultMockitoConfiguration {

    public Answer<Object> getDefaultAnswer() {
        return new ReturnsEmptyValues() {
            @Override
            public Object answer(InvocationOnMock inv) {
                Class<?> type = inv.getMethod().getReturnType();
                if (type.isAssignableFrom(Observable.class)) {
                    return Observable.error(createException(inv));
                } else if (type.isAssignableFrom(Single.class)) {
                    return Single.error(createException(inv));
                } else if (type.isAssignableFrom(Maybe.class)) {
                    return Maybe.error(createException(inv));
                } else if (type.isAssignableFrom(Completable.class)) {
                    return Completable.error(createException(inv));
                } else if (type.isAssignableFrom(Flowable.class)) {
                    return Flowable.error(createException(inv));
                } else {
                    return super.answer(inv);
                }
            }
        };
    }

    @NonNull
    private RuntimeException createException(
            InvocationOnMock invocation) {
        String s = invocation.toString();
        return new RuntimeException(
                "No mock defined for invocation " + s);
    }
}
