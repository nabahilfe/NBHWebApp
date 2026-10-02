package eu.nabahilfe.webapp.system.stats;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

@Component
public class ApiRequestStatisticsInterceptor
        implements HandlerInterceptor {

    private final ApiRequestCounter counter;

    public ApiRequestStatisticsInterceptor(ApiRequestCounter counter) {
        this.counter = counter;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception exception) {

        // Nur echte Controller-Methoden berücksichtigen
        if (!(handler instanceof HandlerMethod)) {
            return;
        }

        Object pattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        /*
         * Nur Requests zählen, die tatsächlich
         * einem Spring-Controller zugeordnet wurden.
         */
        if (pattern == null) {
            return;
        }

        counter.increment(request.getMethod(), pattern.toString(), response.getStatus());
    }
}