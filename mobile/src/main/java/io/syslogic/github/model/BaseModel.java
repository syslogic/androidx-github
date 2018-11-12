package io.syslogic.github.model;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;

abstract public class BaseModel implements Observable {

    @Override
    public void addOnPropertyChangedCallback(@NonNull OnPropertyChangedCallback callback) {

    }

    @Override
    public void removeOnPropertyChangedCallback(@NonNull OnPropertyChangedCallback callback) {

    }
}
