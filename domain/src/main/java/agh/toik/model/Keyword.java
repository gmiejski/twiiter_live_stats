package agh.toik.model;

import org.springframework.data.annotation.Id;

/**
 * Created by grzegorz.miejski on 17/05/15.
 */
public class Keyword {

    @Id
    private String id;
    private String value;

    public Keyword(String value) {
        this.value = value;
    }

    public Keyword() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Keyword{value='" + value + '}';
    }
}
