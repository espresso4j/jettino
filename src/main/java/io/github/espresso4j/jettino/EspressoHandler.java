package io.github.espresso4j.jettino;

import io.github.espresso4j.espresso.Espresso;
import io.github.espresso4j.espresso.Response;
import io.github.espresso4j.servleto.ServletRequestFactory;
import io.github.espresso4j.servleto.ServletResponseFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EspressoHandler extends AbstractHandler {

    private Espresso espresso;

    EspressoHandler(Espresso app) {
        this.espresso = app;
    }

    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse) throws IOException, ServletException {
        try {
            io.github.espresso4j.espresso.Request erequest = ServletRequestFactory.fromServletRequest(httpServletRequest);

            Response response = this.espresso.call(erequest);
            ServletResponseFactory.intoServletResponse(response, httpServletResponse);
        } catch (Throwable e) {
            httpServletResponse.sendError(500);
        } finally {
            request.setHandled(true);
        }
    }
}
