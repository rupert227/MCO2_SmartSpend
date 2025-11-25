package com.mobdeve.s17.group11.smartspend.util;

import java.util.HashSet;
import java.util.Set;

public class PopupSettingsVariables {

    public static class Analytics {

        public Date endDate = new Date(-1, -1, -1);
        public Date startDate = new Date(-1, -1, -1);
        public boolean hasSetOnce = false;

    }

    public static class Sort {

        public Set<Integer> selectedCategories = new HashSet<>();
        public boolean highestFirst = false, lowestFirst = false;
        public boolean newestFirst = false, oldestFirst = false;
        public float highestAmount = 0, lowestAmount = 0;

        public boolean isDefault() {
            return !highestFirst && !lowestFirst && !newestFirst && !oldestFirst
                    && highestAmount == 0 && lowestAmount == 0
                    && selectedCategories.isEmpty();
        }

    }

}
