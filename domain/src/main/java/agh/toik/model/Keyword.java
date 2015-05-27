package agh.toik.model;

import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by grzegorz.miejski on 17/05/15.
 */
public class Keyword {

    @Id
    private String id;
    private String value;

    private List<Date> occurrences;

    public Keyword(String value, List<Date> occurrences) {
        this.value = value;
        this.occurrences = new ArrayList<>(occurrences);
    }

    public Keyword(String value) {
        this(value, new ArrayList<>());
    }

    public Keyword() {
        this.occurrences = new ArrayList<>();
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

    public List<Date> getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(List<Date> occurrences) {
        this.occurrences = occurrences;
    }

    @Override
    public String toString() {
        return "Keyword{value='" + value + '}';
    }
}
