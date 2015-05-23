package agh.toik.service;

import agh.toik.model.Keyword;
import agh.toik.model.KeywordConnection;
import agh.toik.repository.KeywordConnectionRepository;
import agh.toik.repository.KeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by grzegorz.miejski on 23/05/15.
 */
@Service
public class KeywordsConnectionService {

    @Autowired
    private KeywordConnectionRepository keywordConnectionRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    public void save(KeywordConnection keywordConnection) {
        Keyword firstKeyword = keywordRepository.findByValue(keywordConnection.getFirstKeyword());
        Keyword secondKeyword = keywordRepository.findByValue(keywordConnection.getSecondKeyword());

        if (firstKeyword == null) {
            keywordRepository.save(new Keyword(keywordConnection.getFirstKeyword()));
        }
        if (secondKeyword == null) {
            keywordRepository.save(new Keyword(keywordConnection.getSecondKeyword()));
        }
        keywordConnectionRepository.save(keywordConnection);
    }
}
