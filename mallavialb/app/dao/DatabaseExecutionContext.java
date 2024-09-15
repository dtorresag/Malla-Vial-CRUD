package dao;

import akka.actor.ActorSystem;
import javax.inject.Inject;
import javax.inject.Singleton;
import scala.concurrent.ExecutionContext;
import scala.concurrent.ExecutionContextExecutor;

/**
 * Custom ExecutionContext for database operations.
 */
@Singleton
public class DatabaseExecutionContext implements ExecutionContextExecutor {

    private final ExecutionContext executionContext;

    @Inject
    public DatabaseExecutionContext(ActorSystem actorSystem) {
        this.executionContext = actorSystem.dispatchers().lookup("contexts.database-dispatcher");
    }

    @Override
    public void execute(Runnable command) {
        executionContext.execute(command);
    }

    @Override
    public void reportFailure(Throwable cause) {
        executionContext.reportFailure(cause);
    }
}
