package agh.toik.controller;

import agh.toik.model.Keyword;
import agh.toik.repository.KeywordRepository;
import agh.toik.service.KeywordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by grzegorz.miejski on 17/05/15.
 */
@RestController
public class KeywordsController {

    private static final String REQUEST_DATE_FORMAT = "yyyy-MM-dd-HH:mm";

    @Autowired
    private KeywordRepository keywordRepository;

    @Autowired
    private KeywordsService keywordsService;

    @RequestMapping(value = "keywords", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Keyword> getKeywords(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = REQUEST_DATE_FORMAT) LocalDateTime startDate, @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = REQUEST_DATE_FORMAT) LocalDateTime endDate) {
        return keywordsService.findAll(startDate, endDate);
    }

    @RequestMapping(value = "keywords", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Keyword> enterNewKeyword(Keyword keyword) {
        Keyword saved = keywordRepository.save(keyword);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @RequestMapping(value = "keywords/clear", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> clearKeywords() {
        keywordRepository.deleteAll();
        return new ResponseEntity<>("deleted", HttpStatus.OK);
    }

}
