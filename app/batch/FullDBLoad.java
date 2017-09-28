package batch;

import com.google.inject.Inject;
import com.google.inject.Singleton;
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
        ws.url("http://localhost:9091").get().thenApply(wsResponse -> {
            System.out.println(wsResponse.getBody());
            return null;
        });
    }


}
