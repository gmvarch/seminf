package com.gmv.techhip.domain;


import java.io.Serializable;

/**
 * A Vote Aggregation
 */
public class VoteAgg implements Serializable {

    private static final long serialVersionUID = 1L;

    private String option;

    private String value;
    
    public String getOption() {
        return option;
    }

    public VoteAgg option(String option) {
        this.option = option;
        return this;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getValue() {
        return value;
    }

    public VoteAgg value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "VoteAgg{" +
            "option='" + option + "'" +
            ", value='" + value + "'" +
            '}';
    }
}
