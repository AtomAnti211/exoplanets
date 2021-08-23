package com.benyei.exoplanets;

import com.benyei.exoplanets.model.Detection;
import com.benyei.exoplanets.model.Planet;
import com.benyei.exoplanets.model.Star;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class PlanetIntegrationTests {

    @LocalServerPort
    private int port;

    private String baseUrl;

    private Star testStar;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void setUp() {
        Star star = new Star(null, "Hope", 4444, 77.7, 1.2, 1.1);
        testStar = testRestTemplate.postForObject("http://localhost:" + port + "/api/stars", star, Star.class);
        this.baseUrl = "http://localhost:" + port + "/api/planets";
    }

    @Test
    public void addNewPlanet_withOnePostedPlanet_shouldReturnSamePlanet() {
        Planet testPlanet = new Planet(null, "Perseverance", 2005,
                Detection.PRIMARY_TRANSIT, true, true, testStar);
        ResponseEntity<Planet> result;
        result = testRestTemplate.postForEntity(baseUrl, testPlanet, Planet.class);
        assertEquals(201, result.getStatusCodeValue());
        assertEquals(result.getBody().getName(), testPlanet.getName());
    }

    @Test
    public void addNewPlanetWithNameShorterThanTwoCharacter_withOnePostedPlanet_shouldReturnBadRequest() {
        Planet testPlanet = new Planet(null, "P", 2005, Detection.PRIMARY_TRANSIT,
                true, true, testStar);
        final ResponseEntity<Object> postResponse = testRestTemplate.postForEntity(baseUrl, testPlanet, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    public void addNewPlanetWith1994YearOfDiscovery_withOnePostedPlanet_shouldReturnBadRequest() {
        Planet testPlanet = new Planet(null, "Perseverance", 1994, Detection.PRIMARY_TRANSIT,
                true, true, testStar);
        final ResponseEntity<Object> postResponse = testRestTemplate.postForEntity(baseUrl, testPlanet, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    public void addNewPlanetWith2099YearOfDiscovery_withOnePostedPlanet_shouldReturnBadRequest() {
        Planet testPlanet = new Planet(null, "Perseverance", 2099, Detection.PRIMARY_TRANSIT,
                true, true, testStar);
        final ResponseEntity<Object> postResponse = testRestTemplate.postForEntity(baseUrl, testPlanet, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    public void addNewPlanetWithId_withOnePostedPlanet_shouldReturnPlanetWithGeneratedId() {
        Planet testPlanet = new Planet(null, "Perseverance", 2005, Detection.PRIMARY_TRANSIT,
                true, true, testStar);
        final Planet result = testRestTemplate.postForObject(baseUrl, testPlanet, Planet.class);

        assertEquals(1L, result.getId());
    }

    @Test
    public void addTwoNewPlanets_withTwoPostedPlanets_shouldSaveTwoPlanets() {
        Planet testPlanet1 = new Planet(null, "Perseverance", 2005, Detection.PRIMARY_TRANSIT,
                true, true, testStar);
        Planet testPlanet2 = new Planet(null, "Perseverance2", 2005, Detection.PRIMARY_TRANSIT,
                true, true, testStar);

        testRestTemplate.postForObject(baseUrl, testPlanet1, Planet.class);
        testRestTemplate.postForObject(baseUrl, testPlanet2, Planet.class);

        final List<Planet> planets = List.of(testRestTemplate.getForObject(baseUrl, Planet[].class));
        assertEquals(2, planets.size());
    }

    @Test
    public void getPlanets_emptyDatabase_returnsEmptyList() {
        List<Planet> PlanetList = List.of(testRestTemplate.getForObject(baseUrl, Planet[].class));
        assertEquals(0, PlanetList.size());
    }

    @Test
    public void getPlanetById_withOnePostedPlanet_returnsPlanetWithSameId() {
        Planet testPlanet = new Planet(null, "Perseverance", 2005, Detection.PRIMARY_TRANSIT,
                true, true, testStar);
        testPlanet = testRestTemplate.postForObject(baseUrl, testPlanet, Planet.class);
        Planet result = testRestTemplate.getForObject(baseUrl + "/" + testPlanet.getId(), Planet.class);
        assertEquals(testPlanet.getId(), result.getId());
    }

    @Test
    void testFindPlanetByIdWhenIdNotExists() {
        final ResponseEntity<Planet> response = testRestTemplate.getForEntity(baseUrl + "/987654321", Planet.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updatePlanet_withOnePostedPlanet_returnsUpdatedPlanet() {
        Planet testPlanet = new Planet(null, "Perseverance", 2005, Detection.PRIMARY_TRANSIT,
                true, true, testStar);
        testPlanet = testRestTemplate.postForObject(baseUrl, testPlanet, Planet.class);

        testPlanet.setName("Updated name");
        testRestTemplate.put(baseUrl + "/" + testPlanet.getId(), testPlanet, Planet.class);
        Planet updatedPlanet = testRestTemplate.getForObject(baseUrl + "/" + testPlanet.getId(), Planet.class);

        assertEquals("Updated name", updatedPlanet.getName());
    }

    @Test
    public void deletePlanetById_withSomePostedPlanets_getAllShouldReturnRemainingPlanets() {
        Planet testPlanet1 = new Planet(null, "Perseverance1", 2005, Detection.PRIMARY_TRANSIT,
                true, true, testStar);
        Planet testPlanet2 = new Planet(null, "Perseverance2", 2005, Detection.PRIMARY_TRANSIT,
                true, true, testStar);
        Planet testPlanet3 = new Planet(null, "Perseverance3", 2005, Detection.PRIMARY_TRANSIT,
                true, true, testStar);
        List<Planet> testPlanets = new ArrayList<>();
        testPlanets.add(testPlanet1);
        testPlanets.add(testPlanet2);
        testPlanets.add(testPlanet3);

        testPlanets.forEach(testPlanet ->
                testPlanet.setId(testRestTemplate.postForObject(baseUrl, testPlanet, Planet.class).getId())
        );

        testRestTemplate.delete(baseUrl + "/" + testPlanet2.getId());
        testPlanets.remove(testPlanet2);

        List<Planet> remainingPlanets = List.of(testRestTemplate.getForObject(baseUrl, Planet[].class));

        assertEquals(testPlanets.size(), remainingPlanets.size());
        for (int i = 0; i < testPlanets.size(); i++) {
            assertEquals(testPlanets.get(i).getName(), remainingPlanets.get(i).getName());
        }
    }
}
