package batch;

import com.google.inject.AbstractModule;

public class OnStart extends AbstractModule {
    @Override
    protected void configure() {
        bind(FullDBLoad.class).asEagerSingleton();
    }
}
