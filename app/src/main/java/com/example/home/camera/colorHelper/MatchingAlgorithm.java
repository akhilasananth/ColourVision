package com.example.home.camera.colorHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robertfernandes on 1/17/2017.
 */

public interface MatchingAlgorithm {

    public List<Integer> getMatchingColors(int color);

    public boolean isMatch(int color1, int color2);
}
