package io.github.espresso4j.jettino;

import io.github.espresso4j.espresso.Adapter;
import io.github.espresso4j.espresso.Espresso;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.net.InetSocketAddress;

/**
 *
 */
public class JettinoAdapter implements Adapter {

    private Server server;

    private ServerCustomizer serverCustomizer = new ServerCustomizer.NOOP();

    private BaseAdapterOptions baseAdapterOptions;

    public JettinoAdapter() {
        this.baseAdapterOptions = BaseAdapterOptions.Builder.byDefault();
    }

    public JettinoAdapter(BaseAdapterOptions baseAdapterOptions) {
        this.baseAdapterOptions = baseAdapterOptions;
    }

    public JettinoAdapter(ServerCustomizer serverCustomizer, BaseAdapterOptions baseAdapterOptions) {
        this(baseAdapterOptions);
        this.serverCustomizer = serverCustomizer;
    }

    private void start(AbstractHandler handler) throws Exception {
        if (server != null && (server.isStarted() || server.isStarting())) {
            throw new IllegalStateException("Server is running");
        }

        InetSocketAddress addr = new InetSocketAddress(
                baseAdapterOptions.getAddress(), baseAdapterOptions.getPort());
        server = new Server(addr);

        serverCustomizer.customize(server);

        server.setHandler(handler);
        server.start();

        if (baseAdapterOptions.getJoin()) {
            server.join();
        }
    }

    @Override
    public void start(Espresso espresso) throws Exception {
        EspressoHandler handler = new EspressoHandler(espresso);
        start(handler);
    }

    @Override
    public void start(Espresso.Async app) throws Exception {
        AsyncEspressoHandler handler = new AsyncEspressoHandler(app);
        start(handler);
    }

    @Override
    public void stop() throws Exception {
        if (server != null) {
            server.stop();
        }
    }
}
