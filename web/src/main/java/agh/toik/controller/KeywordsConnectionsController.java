package agh.toik.controller;

import agh.toik.model.KeywordConnection;
import agh.toik.repository.KeywordConnectionRepository;
import agh.toik.service.KeywordsConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * Created by grzegorz.miejski on 23/05/15.
 */
@RestController
@RequestMapping(value = "keywords/connections")
public class KeywordsConnectionsController {

    @Autowired
    private KeywordConnectionRepository keywordConnectionRepository;

    @Autowired
    private KeywordsConnectionService keywordsConnectionService;

    @RequestMapping(method = RequestMethod.POST)
    public KeywordConnection createNewConnection(@RequestParam String firstKeyword, @RequestParam String secondKeyword) {
        KeywordConnection keywordConnection = new KeywordConnection(firstKeyword, secondKeyword);
        if (isOk(keywordConnection)) {
            keywordsConnectionService.save(keywordConnection);
            return keywordConnection;
        }
        return null;
    }

    @RequestMapping(produces = "application/json", method = RequestMethod.GET)
    public List<KeywordConnection> getConnections() {

        System.out.println("getting keywords:");
        List<KeywordConnection> all = keywordConnectionRepository.findAll();
        System.out.println(Arrays.toString(all.toArray()));
        return all;
    }

    @RequestMapping(value = "/clear", method = RequestMethod.POST)
    public void clearKeywordsConnections() {
        keywordConnectionRepository.deleteAll();
    }


    private boolean isOk(KeywordConnection keywordConnection) {
        return keywordConnection.getFirstKeyword() != null && !keywordConnection.getFirstKeyword().isEmpty()
                && keywordConnection.getSecondKeyword() != null && !keywordConnection.getSecondKeyword().isEmpty();
    }

}
