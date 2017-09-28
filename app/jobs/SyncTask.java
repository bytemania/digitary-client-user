package jobs;

import akka.actor.ActorSystem;
import play.mvc.Controller;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class SyncTask extends Controller {

    private final ActorSystem actorSystem;
    private final ExecutionContext executionContext;

    @Inject
    public SyncTask(ActorSystem actorSystem, ExecutionContext executionContext) {
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;

        this.initialize();
    }

    private void initialize() {
        this.actorSystem.scheduler().schedule(
                Duration.create(10, TimeUnit.SECONDS), // initialDelay
                Duration.create(15, TimeUnit.SECONDS), // interval
                () -> execute(),
                this.executionContext
        );
    }

    private void execute()
    {
        System.out.println("Running block of code");
    }
}
