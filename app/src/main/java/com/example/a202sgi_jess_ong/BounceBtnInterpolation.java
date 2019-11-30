package com.example.a202sgi_jess_ong;

import android.view.animation.Interpolator;

public class BounceBtnInterpolation implements Interpolator {

    private double mAmplitude = 1;
    private double mFrequency = 10;

    BounceBtnInterpolation(double amplitude, double frequency){
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    @Override
    public float getInterpolation(float time) {
        return (float)(-1 * Math.pow(Math.E, - time/mAmplitude) * Math.cos(mFrequency * time) + 1);
    }
}
