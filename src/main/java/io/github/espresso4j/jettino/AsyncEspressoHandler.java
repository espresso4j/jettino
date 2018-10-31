package io.github.espresso4j.jettino;

import io.github.espresso4j.espresso.Espresso;
import io.github.espresso4j.espresso.Response;
import io.github.espresso4j.servleto.ServletRequestFactory;
import io.github.espresso4j.servleto.ServletResponseFactory;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Consumer;

public class AsyncEspressoHandler extends AbstractHandler {

    private Espresso.Async app;

    public AsyncEspressoHandler(Espresso.Async app) {
        this.app = app;
    }

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse) throws IOException, ServletException {
        io.github.espresso4j.espresso.Request erequest = ServletRequestFactory.fromServletRequest(httpServletRequest);

        AsyncContext asyncContext = httpServletRequest.startAsync();
        Consumer<Exception> raiseException = e -> {
            try {
                httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR_500, e.getMessage());
            } catch (IOException e1) {
                // TODO: deal with IOException in this situation
            } finally {
                asyncContext.complete();
            }
        };

        Consumer<Response> sendResponse = response -> {
            try {
                ServletResponseFactory.intoServletResponse(response, httpServletResponse);
                asyncContext.complete();
            } catch (IOException e) {
                raiseException.accept(e);
            }
        };

        app.call(erequest, sendResponse, raiseException);
        baseRequest.setHandled(true);
    }
}
