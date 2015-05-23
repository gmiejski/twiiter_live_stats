package agh.toik.controller;

import agh.toik.model.Keyword;
import agh.toik.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by grzegorz.miejski on 17/05/15.
 */
@RestController
public class KeywordsController {

    @Autowired
    private KeywordRepository keywordRepository;

    @RequestMapping(value = "keywords", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Keyword> getKeywords() {
        return keywordRepository.findAll();
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
