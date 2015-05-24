package twitter.rest;

import java.util.List;

/**
 * Created by grzegorz.miejski on 23/05/15.
 */
public interface Repository<T> {

    List<T> getAll();

}
