package lab.jpa.concert.services;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lab.jpa.concert.common.Config;
import lab.jpa.concert.domain.Concert;

/**
 * Class to implement a simple REST Web service for managing Concerts.
 * <p>
 * ConcertResource implements a WEB service with the following interface:
 * - GET    <base-uri>/concerts/{id}
 * Retrieves a Concert based on its unique id. The HTTP response
 * message has a status code of either 200 or 404, depending on
 * whether the specified Concert is found.
 * <p>
 * - GET    <base-uri>/concerts?start&size
 * Retrieves a collection of Concerts, where the "start" query
 * parameter identifies an index position, and "size" represents the
 * max number of successive Concerts to return. The HTTP response
 * message returns 200.
 * <p>
 * - POST   <base-uri>/concerts
 * Creates a new Concert. The HTTP post message contains a
 * representation of the Concert to be created. The HTTP Response
 * message returns a Location header with the URI of the new Concert
 * and a status code of 201.
 * <p>
 * - DELETE <base-uri>/concerts
 * Deletes all Concerts, returning a status code of 204.
 * <p>
 * Where a HTTP request message doesn't contain a cookie named clientId
 * (Config.CLIENT_COOKIE), the Web service generates a new cookie, whose value
 * is a randomly generated UUID. The Web service returns the new cookie as part
 * of the HTTP response message.
 */
public class ConcertResource {

    private static Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);

    private Map<Long, Concert> concertDB = new ConcurrentHashMap<>();
    private AtomicLong idCounter = new AtomicLong();


    public Response retrieveConcert(long id, Cookie clientId) {

        throw new NotImplementedException("retrieveConcert");
    }


    public Response retrieveConcerts(long start, int size, Cookie clientId) {
        // The Response object should store an ArrayList<Concert> entity. The
        // ArrayList can be empty depending on the start and size arguments,
        // and Concerts stored.
        //
        // Because of type erasure with Java Generics, any generically typed
        // entity needs to be wrapped by a javax.ws.rs.core.GenericEntity that
        // stores the generic type information. Hence to add an ArrayList as a
        // Response object's entity, you should use the following code:
        //
        // List<Concert> concerts = new ArrayList<Concert>();
        // GenericEntity<List<Concert>> entity = new GenericEntity<List<Concert>>(concerts) {};
        // ResponseBuilder builder = Response.ok(entity);

        throw new NotImplementedException("retrieveConcerts");
    }


    public Response createConcert(Concert concert, Cookie clientId) {

        // Reminder: You won't need to annotate the "concert" argument above - arguments without annotations are
        // assumed by JAX-RS to come from the HTTP request body.

        throw new NotImplementedException("createConcert");
    }


    public Response deleteAllConcerts(Cookie clientId) {

        throw new NotImplementedException("deleteAllConcerts");
    }

    /**
     * Helper method that can be called from every service method to generate a
     * NewCookie instance, if necessary, based on the clientId parameter.
     *
     * @param clientId the Cookie whose name is Config.CLIENT_COOKIE, extracted
     *                 from a HTTP request message. This can be null if there was no cookie
     *                 named Config.CLIENT_COOKIE present in the HTTP request message.
     * @return a NewCookie object, with a generated UUID value, if the clientId
     * parameter is null. If the clientId parameter is non-null (i.e. the HTTP
     * request message contained a cookie named Config.CLIENT_COOKIE), this
     * method returns null as there's no need to return a NewCookie in the HTTP
     * response message.
     */
    private NewCookie makeCookie(Cookie clientId) {
        NewCookie newCookie = null;

        if (clientId == null) {
            newCookie = new NewCookie(Config.CLIENT_COOKIE, UUID.randomUUID().toString());
            LOGGER.info("Generated cookie: " + newCookie.getValue());
        }

        return newCookie;
    }
}
