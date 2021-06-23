package bisq.api.client;

import java.io.IOException;

public interface OfferApi {

    String list() throws IOException;

    String view(int id) throws IOException;

    String create(String json) throws IOException;

    void delete(int id) throws IOException;

    void delete() throws IOException;
}
