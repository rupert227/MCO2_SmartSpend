package com.mobdeve.s17.group11.smartspend.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Algorithm {

    public static List<String> filterStringSearch(List<String> inputStrings, String query) {
        List<String> filteredStrings = new ArrayList<>();

        inputStrings.forEach(str -> {
            if(str.toLowerCase().contains(query.toLowerCase()))
                filteredStrings.add(str);
        });

        return filteredStrings;
    }

    public static List<String> filterStringSearch(String[] inputStrings, String query) {
        List<String> filteredStrings = new ArrayList<>();

        Arrays.stream(inputStrings).forEach(str -> {
            if(str.toLowerCase().contains(query.toLowerCase()))
                filteredStrings.add(str);
        });

        return filteredStrings;
    }

}
