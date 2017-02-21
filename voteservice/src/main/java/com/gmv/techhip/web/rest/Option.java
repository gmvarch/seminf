package com.gmv.techhip.web.rest;

public class Option {

    private Long id;

    private String option;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOption() {
        return option;
    }

    public Option option(String option) {
        this.option = option;
        return this;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
