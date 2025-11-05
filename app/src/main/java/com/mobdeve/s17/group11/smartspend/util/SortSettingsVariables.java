package com.mobdeve.s17.group11.smartspend.util;

import java.util.HashSet;
import java.util.Set;

public class SortSettingsVariables {

    public boolean highestFirst = false, lowestFirst = false;
    public boolean newestFirst = false, oldestFirst = false;
    public float highestAmount = 0, lowestAmount = 0;
    public Set<Integer> selectedCategories = new HashSet<>();

    public void reset() {
        resetAmount();
        resetCategory();
        resetDate();
    }

    public void resetAmount() {
        highestFirst = false;
        lowestFirst = false;

        highestAmount = 0;
        lowestAmount = 0;
    }

    public void resetCategory() {
        selectedCategories.clear();
    }

    public void resetDate() {
        newestFirst = false;
        oldestFirst = false;
    }

    public boolean isDefault() {
        return !highestFirst && !lowestFirst && !newestFirst && !oldestFirst
                && highestAmount == 0 && lowestAmount == 0
                && selectedCategories.isEmpty();
    }

}
