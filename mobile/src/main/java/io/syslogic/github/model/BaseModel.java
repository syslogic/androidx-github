package io.syslogic.github.model;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;

/**
 * Base Model
 * @author Martin Zeitler
 */
abstract public class BaseModel implements Observable {

    @Override
    public void addOnPropertyChangedCallback(@NonNull OnPropertyChangedCallback callback) {}

    @Override
    public void removeOnPropertyChangedCallback(@NonNull OnPropertyChangedCallback callback) {}
}
