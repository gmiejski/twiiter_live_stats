package twitter.rest;

import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Created by grzegorz.miejski on 23/05/15.
 */
public abstract class GenericRepository<T> implements Repository<T> {
    protected List<T> findByWeb(String url, Function<LinkedHashMap<String, String>, T> map) {
        RestTemplate restTemplate = new RestTemplate();
        List<LinkedHashMap<String, String>> listResponseEntity = restTemplate.getForObject(url, List.class);

        return listResponseEntity.stream().map(map).collect(toList());
    }
}
