package lab.jpa.concert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.h2.tools.RunScript;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import lab.jpa.concert.domain.Concert;
import lab.jpa.concert.domain.Genre;
import lab.jpa.concert.domain.Performer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConcertTest {

    private static final String DB_INIT_SCRIPT_DIRECTORY = "src/test/resources";
    private static final String DB_INIT_SCRIPT = "db-init.sql";

    private static final String DATABASE_DRIVER_NAME = "org.h2.Driver";
    private static final String DATABASE_URL = "jdbc:h2:~/test;mv_store=false";
    private static final String DATABASE_USERNAME = "sa";
    private static final String DATABASE_PASSWORD = "sa";

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void initialiseDatabase() throws ClassNotFoundException, SQLException, IOException {
        File file = new File(DB_INIT_SCRIPT_DIRECTORY + "/" + DB_INIT_SCRIPT);

        Class.forName(DATABASE_DRIVER_NAME);

        // Create test data
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD)) {
            FileReader reader = new FileReader(file);
            RunScript.execute(conn, reader);
            reader.close();
        }

        // Create EMF.
        entityManagerFactory = Persistence.createEntityManagerFactory("se325.lab03.concert");
    }

    @After
    public void closeDatabase() {

        // Close EMF
        entityManagerFactory.close();
    }

    @Test
    public void queryAllConcerts() {

        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            List<Concert> concerts = em.createQuery("select c from Concert c order by c.title", Concert.class).getResultList();
            em.getTransaction().commit();

            // Check that the List contains all 22 Concerts stored in the database.
            assertEquals(22, concerts.size());

            // Check that the first Concert is correct.
            Concert concert = concerts.get(0);
            assertEquals("24K Magic World Tour", concert.getTitle());
            assertEquals(LocalDateTime.of(2017, 9, 2, 19, 30), concert.getDate());
            Performer performer = concert.getPerformer();
            assertEquals("Bruno Mars", performer.getName());
            assertEquals("BrunoMars.jpg", performer.getImageUri());
            assertEquals(Genre.RhythmAndBlues, performer.getGenre());

            // Check that where a Performer appears in more than 1 Concert,
            // that the Performer is represented by the same Performer object.
            //
            // Katy Perry appears in two Concerts: "One Love Manchester" and
            // "Witness: The Tour". The list of Concerts retrieved by the call
            // to ConcertDAO#getAll() should return two Concert instances that
            // share a common Performer object for Katy Perry.
            //
            // Find the two Concert instances in the List, extract their
            // Performer objects and check that there's only a single instance
            // that is aliased. The natural ordering for Concerts is based on
            // the title of a Concert, and the ConcertDAO#getAll() call is
            // expected to return a List of Concerts that is ordered
            // alphabetically based on title. Using Collections' binarySearch()
            // method to find the two Concerts of interest.
            Concert oneLoveManchester = new Concert();
            oneLoveManchester.setTitle("One Love Manchester");
            Concert witnessTheTour = new Concert();
            witnessTheTour.setTitle("Witness: The Tour");
            int oneLoveManchesterIndex = Collections.binarySearch(concerts, oneLoveManchester);
            int witnessTheTourIndex = Collections.binarySearch(concerts, witnessTheTour);

            Performer katy1 = concerts.get(oneLoveManchesterIndex).getPerformer();
            Performer katy2 = concerts.get(witnessTheTourIndex).getPerformer();
            assertSame(katy1, katy2);
        } finally {
            em.close();
        }
    }

    @Test
    public void queryConcert() {

        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            Concert concert1 = em.find(Concert.class, 1L);
            Concert concert2 = em.find(Concert.class, 1L);
            em.getTransaction().commit();

            // The DAO is expected to return 2 Concert objects with the same
            // value.
            assertEquals(concert1, concert2);

            // The DAO is expected to return the same Concert.
            assertSame(concert1, concert2);
        } finally {
            em.close();
        }
    }

    @Test
    public void addConcert() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            // Retrieve Ed Sheran's concert "Divide Tour", and the extract the
            // Performer object for Ed.
            em.getTransaction().begin();
            Concert divideTour = em.find(Concert.class, 2L);
            Performer ed = divideTour.getPerformer();

            // Create a new Concert featuring Ed.
            LocalDateTime date = LocalDateTime.of(2017, 12, 1, 16, 00);
            System.out.println(date.toString());
            Concert garbage = new Concert("My Music is Dreadful", date, ed);

            // Save the new Concert.
            em.persist(garbage);
            em.getTransaction().commit();

            // Query all Concerts and pick out the new Concert.
            em.getTransaction().begin();
            Concert concert = em
                    .createQuery("select c from Concert c where c.title = :title", Concert.class)
                    .setParameter("title", "My Music is Dreadful")
                    .getSingleResult();
            em.getTransaction().commit();

            // Check that the new Concert's ID has been assigned.
            assertNotNull(concert.getId());

            // Check that the result of the query for the new Concert equals
            // the newly created Concert.
            assertEquals(garbage, concert);

        } finally {
            em.close();
        }
    }

    @Test
    public void deleteConcert() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            // Query Concert with the ID 18 (this represents "Evolve!").
            Concert evolve = em.find(Concert.class, 18L);

            // Delete the Concert.
            em.remove(evolve);
            em.getTransaction().commit();

            // Requery the Concert to check it's been deleted.
            em.getTransaction().begin();
            evolve = em.find(Concert.class, 18L);
            em.getTransaction().commit();
            assertNull(evolve);

        } finally {
            em.close();
        }
    }

    @Test
    public void updateConcertAndPerformer() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            em.getTransaction().begin();
            // Query Concert with the ID 11 (this represents "Dangerous Woman").
            Concert dangerousWoman = em.find(Concert.class, 11L);

            // Update the Concert's date, postponing it by one week.
            LocalDateTime newDate = LocalDateTime.of(2017, 8, 17, 18, 30);
            dangerousWoman.setDate(newDate);

            // Also change the Concert Performer's image file.
            Performer performer = dangerousWoman.getPerformer();
            String newImage = "new_image.jpg";
            performer.setImageUri(newImage);

            // Save the updated Concert (and Performer).
            em.persist(dangerousWoman);
            em.getTransaction().commit();

            // Requery the Concert.
            em.getTransaction().begin();
            Concert concert = em.find(Concert.class, 11L);

            // Check that the Concert's date has been updated.
            assertEquals(newDate, concert.getDate());

            // Check that the Performer's been updated too.
            performer = concert.getPerformer();
            assertEquals(newImage, performer.getImageUri());
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
