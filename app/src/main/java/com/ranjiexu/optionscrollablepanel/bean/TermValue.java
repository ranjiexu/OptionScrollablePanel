package com.ranjiexu.optionscrollablepanel.bean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
*  Created by devpc-05 on 2017/4/11.
**/
public class TermValue {
    private long id;
    private String value;
    private Status status;

    public enum Status {
        RED,
        GREEN,
        GRAY;

        private static final List<Status> VALUES =
                Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();

        public static Status randomStatus() {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
