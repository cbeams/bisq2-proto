package bisq.api;

import java.io.IOException;

public interface OfferBook {

    String list() throws IOException;

    String view(int id) throws IOException;

    String create(String json) throws IOException;

    void delete(int id) throws IOException;

    void delete() throws IOException;
}
