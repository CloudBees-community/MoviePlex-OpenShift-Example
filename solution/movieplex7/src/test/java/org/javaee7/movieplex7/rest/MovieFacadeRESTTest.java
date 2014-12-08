package org.javaee7.movieplex7.rest;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.javaee7.movieplex7.entities.Movie;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 * @author arungupta
 */
@RunWith(Arquillian.class)
public class MovieFacadeRESTTest {
    
    private WebTarget target;
    private static final String WEBAPP_SRC = "src/main/webapp";
    private static final String RESOURCE_SRC = "src/main/resources";

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addPackages(true, "org.javaee7.movieplex7");
        
        for (File file : new File(WEBAPP_SRC + "/WEB-INF").listFiles()) {
            war.addAsWebInfResource(file);
        }
        
        for (File file : new File(RESOURCE_SRC).listFiles()) {
            war.addAsResource(file);
        }
        
        System.out.println(war.toString(true));
        return war;
    }
    
    @ArquillianResource
    private URL base;

    @Before
    public void setUp() throws MalformedURLException {
        Client client = ClientBuilder.newClient();
        target = client.target(URI.create(new URL(base, "webresources/movie").toExternalForm()));
        target.register(Movie.class);
    }
    
    @Test
    public void testGetAll() throws Exception {
        Movie[] list = target.request().get(Movie[].class);
        assertEquals(20, list.length);
    }
    
}