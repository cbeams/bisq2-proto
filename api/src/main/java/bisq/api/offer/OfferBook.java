package bisq.api.offer;

import bisq.api.event.EventSource;

import java.io.IOException;
import java.util.List;

public interface OfferBook extends EventSource<String> {

    List<String> findAll() throws IOException;

    String findById(int id) throws IOException;

    String save(String json) throws IOException;

    void delete(int id) throws IOException;

    void deleteAll() throws IOException;
}
