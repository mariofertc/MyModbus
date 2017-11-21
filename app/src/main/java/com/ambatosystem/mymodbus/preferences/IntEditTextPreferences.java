package com.ambatosystem.mymodbus.preferences;

import android.preference.EditTextPreference;
import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by mario-ivonne on 11/20/2017.
 */

public class IntEditTextPreferences extends EditTextPreference {

    public IntEditTextPreferences(Context context) {
        super(context);
    }

    public IntEditTextPreferences(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntEditTextPreferences(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /*public IntEditTextPreference(Context context) {
       super(context);

    }

    public IntEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IntEditTextPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }*/

    @Override
    protected String getPersistedString(String defaultReturnValue) {
        return String.valueOf(getPersistedInt(-1));
    }

    @Override
    protected boolean persistString(String value) {
        return persistInt(Integer.valueOf(value));
    }
}
