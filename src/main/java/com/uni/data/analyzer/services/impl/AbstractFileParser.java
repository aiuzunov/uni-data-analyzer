package com.uni.data.analyzer.services.impl;

import com.uni.data.analyzer.services.FileParser;

import java.util.List;

public abstract class AbstractFileParser implements FileParser {

    protected boolean validateHeadings(List<String> headers) {
        if (headers.size() == 2) {
            return headers.get(0).equals("ID") && headers.get(1).equals("Result");
        }

        if (headers.size() == 5) {
            return (headers.get(0).equals("Time") || headers.get(0).equals("\uFEFFTime"))
                    && headers.get(1).equals("Event context")
                    && headers.get(2).equals("Component") && headers.get(3).equals("Event name")
                    && headers.get(4).equals("Description");
        }

        return false;
    }
}
