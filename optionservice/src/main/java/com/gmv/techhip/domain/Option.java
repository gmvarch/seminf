package com.gmv.techhip.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Option.
 */
@Entity
@Table(name = "option")
public class Option implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "option")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Option option = (Option) o;
        if (option.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, option.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Option{" +
            "id=" + id +
            ", option='" + option + "'" +
            '}';
    }
}
