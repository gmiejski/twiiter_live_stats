package agh.toik.service;

import agh.toik.model.Keyword;
import agh.toik.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by grzegorz.miejski on 27/05/15.
 */
@Service
public class KeywordsService {

    @Autowired
    private KeywordRepository keywordRepository;

    public List<Keyword> findAll(LocalDateTime startDate, LocalDateTime endDate) {
        List<Keyword> keywords = keywordRepository.findAll();
        if (startDate == null && endDate == null) {
            return keywords;
        }

        return keywordsByDates(startDate, endDate, keywords);
    }

    private List<Keyword> keywordsByDates(LocalDateTime startDate, LocalDateTime endDate, List<Keyword> keywords) {
        keywords.stream().forEach(keyword -> removeDatesOutOfQuery(startDate, endDate, keyword));

        return keywords.stream()
                .filter(keyword -> !keyword.getOccurrences().isEmpty())
                .collect(Collectors.toList());
    }

    private boolean removeDatesOutOfQuery(LocalDateTime startDate, LocalDateTime endDate, Keyword keyword) {
        return keyword.getOccurrences().removeIf(date -> {
            LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
            if (startDate != null && ldt.isBefore(startDate)) {
                return true;
            }
            if (endDate != null && ldt.isAfter(endDate)) {
                return true;
            }
            return false;
        });
    }
}
