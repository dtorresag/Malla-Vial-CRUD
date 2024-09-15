package modules;

import akka.actor.ActorSystem;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;


public class Module extends AbstractModule {

    @Override
    protected void configure() {
        // Puedes agregar bindings adicionales aquí si es necesario
    }

    @Provides
    ActorSystem provideActorSystem() {
        return ActorSystem.create("my-system");
    }

    
}
