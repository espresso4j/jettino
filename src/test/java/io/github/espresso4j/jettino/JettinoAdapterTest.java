package io.github.espresso4j.jettino;

import io.github.espresso4j.espresso.Response;
import org.junit.Test;

public class JettinoAdapterTest {

    @Test
    public void test() throws Exception {
        BaseAdapterOptions options = new BaseAdapterOptions();
        options.setJoin(false);
        options.setPort(32080);

        Jettino jetty = new Jettino(options);

        jetty.start((req) -> Response.of(200));
        jetty.stop();
    }

}
