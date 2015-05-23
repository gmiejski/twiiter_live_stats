package agh.toik.model;

import org.springframework.data.annotation.Id;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

/**
 * Created by grzegorz.miejski on 23/05/15.
 */
public class KeywordConnection {

    @Id
    private String id;
    private String firstKeyword;
    private String secondKeyword;
    private int totalCount;

    public KeywordConnection(String firstKeyword, String secondKeyword) {
        List<Object> collect = asList(firstKeyword.toLowerCase(), secondKeyword.toLowerCase()).stream().sorted().collect(toList());
        this.firstKeyword = (String) collect.get(0);
        this.secondKeyword = (String) collect.get(1);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstKeyword() {
        return firstKeyword;
    }

    public void setFirstKeyword(String firstKeyword) {
        this.firstKeyword = firstKeyword;
    }

    public String getSecondKeyword() {
        return secondKeyword;
    }

    public void setSecondKeyword(String secondKeyword) {
        this.secondKeyword = secondKeyword;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }


    @Override
    public String toString() {
        return "KeywordConnection{" +
                "firstKeyword='" + firstKeyword + '\'' +
                ", secondKeyword='" + secondKeyword + '\'' +
                '}';
    }
}
