package efrei.backend.configuration.servlets;

import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Configuration for customizing the DispatcherServlet in a Spring MVC application.
 *
 * This class sets up a custom DispatcherServlet, registers it with the servlet container,
 * and defines its default path.
 */
@Configuration
public class DispatcherServletConfiguration {

    /**
     * Defines a custom DispatcherServlet bean.
     *
     * A custom implementation of DispatcherServlet (e.g., `CustomDispatcherServlet`) can
     * override default behaviors or provide additional functionality.
     *
     * @return A new instance of the custom DispatcherServlet.
     */
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new CustomDispatcherServlet();
    }

    /**
     * Registers the custom DispatcherServlet with the servlet container.
     *
     * This method maps the servlet to the root context path ("/") and configures it to load on startup.
     *
     * @param dispatcherServlet The custom DispatcherServlet instance.
     * @return A ServletRegistrationBean for the DispatcherServlet.
     */
    @Bean
    public ServletRegistrationBean<DispatcherServlet> dispatcherServletRegistration(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean<DispatcherServlet> registration = new ServletRegistrationBean<>(dispatcherServlet, "/");
        registration.setName("customDispatcherServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }

    /**
     * Defines the servlet path for the DispatcherServlet.
     *
     * This method ensures that the DispatcherServlet is mapped to the root context path ("/").
     * Useful for cases where multiple DispatcherServlet instances are defined.
     *
     * @return A DispatcherServletPath bean that sets the servlet path to "/".
     */
    @Bean
    public DispatcherServletPath dispatcherServletPath() {
        return () -> "/";
    }
}