package agh.toik.model;

import org.springframework.data.annotation.Id;

import java.util.*;

/**
 * Created by grzegorz.miejski on 17/05/15.
 */
public class Keyword {

    @Id
    private String id;
    private String value;

    private List<Date> occurrences;
    private Map<String, Integer> occurrencesByCountry;

    public Keyword(String value, List<Date> occurrences, Map<String, Integer> occurrencesByCountry) {
        this.value = value;
        this.occurrences = new ArrayList<>(occurrences);
        this.occurrencesByCountry = new HashMap<>(occurrencesByCountry);
    }

    public Keyword(String value) {
        this(value, new ArrayList<>(), new HashMap<>());
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

    public Map<String, Integer> getOccurrencesByCountry() {
        return occurrencesByCountry;
    }

    public void setOccurrencesByCountry(Map<String, Integer> occurrencesByCountry) {
        this.occurrencesByCountry = occurrencesByCountry;
    }

    @Override
    public String toString() {
        return "Keyword{value='" + value + '}';
    }
}
