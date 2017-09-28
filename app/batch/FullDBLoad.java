package batch;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.ConfigFactory;
import play.libs.ws.WSClient;

@Singleton
public class FullDBLoad {
    private final WSClient ws;

    @Inject
    public FullDBLoad(WSClient ws) {
        this.ws = ws;
        loadDd();
    }

    private void loadDd() {
        String endPoint = ConfigFactory.load().getString("sync.data.service.endpoint");
        ws.url(endPoint).get().thenApply(wsResponse -> {
            System.out.println(wsResponse.getBody());
            return null;
        });
    }


}
