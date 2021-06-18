package bisq.api.http.service;

import bisq.api.client.CoreApiClient;
import bisq.api.service.ApiService;
import spark.Spark;

public class HttpApiService implements ApiService {

    private CoreApiClient core;
    private final int port;

    public HttpApiService(CoreApiClient core, int port) {
        this.core = core;
        this.port = port;
    }

    @Override
    public void run() {
        Spark.port(port);
        System.out.println("listening on port " + port);
        Spark.get("/version", (req, res) -> core.getVersion());
        Spark.get("/price", (req, res) -> core.getPrice());
    }

    @Override
    public void stop() {
        Spark.stop();
    }
}
