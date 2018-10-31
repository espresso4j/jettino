package io.github.espresso4j.jettino;

import org.eclipse.jetty.server.Server;

public interface ServerCustomizer {

    public void customize(Server server);

    static class NOOP implements ServerCustomizer {

        @Override
        public void customize(Server server) {
        }
    }

}
