package com.benyei.exoplanets;

import com.benyei.exoplanets.model.Detection;
import com.benyei.exoplanets.model.Moon;
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
public class MoonIntegrationTests {

    @LocalServerPort
    private int port;

    private String baseUrl;

    private Planet testPlanet;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void setUp() {
        Star star = new Star(null, "Hope", 4444, 77.7, 1.2, 1.1);
        Star testStar = testRestTemplate.postForObject("http://localhost:" + port + "/api/stars", star, Star.class);
        Planet planet = new Planet(null, "Perseverance", 2005, Detection.PRIMARY_TRANSIT, true, true, testStar);
        testPlanet = testRestTemplate.postForObject("http://localhost:" + port + "/api/planets", planet, Planet.class);
        this.baseUrl = "http://localhost:" + port + "/api/moons";
    }

    @Test
    public void addNewMoon_withOnePostedMoon_shouldReturnSameMoon() {
        Moon testMoon = new Moon(null, "Spirit", false, testPlanet);
        ResponseEntity<Moon> result;
        result = testRestTemplate.postForEntity(baseUrl, testMoon, Moon.class);
        assertEquals(201, result.getStatusCodeValue());
        assertEquals(result.getBody().getName(), testMoon.getName());
    }

    @Test
    public void addNewMoonWithNameShorterThanTwoCharacter_withOnePostedMoon_shouldReturnBadRequest() {
        Moon testMoon = new Moon(null, "S", false, testPlanet);
        final ResponseEntity<Object> postResponse = testRestTemplate.postForEntity(baseUrl, testMoon, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    public void addNewMoonWithId_withOnePostedMoon_shouldReturnMoonWithGeneratedId() {
        Moon testMoon = new Moon(null, "Spirit", false, testPlanet);
        final Moon result = testRestTemplate.postForObject(baseUrl, testMoon, Moon.class);

        assertEquals(1L, result.getId());
    }

    @Test
    public void addTwoNewMoons_withTwoPostedMoons_shouldSaveTwoMoons() {
        Moon testMoon1 = new Moon(null, "Spirit1", false, testPlanet);
        Moon testMoon2 = new Moon(null, "Spirit2", false, testPlanet);

        testRestTemplate.postForObject(baseUrl, testMoon1, Moon.class);
        testRestTemplate.postForObject(baseUrl, testMoon2, Moon.class);

        final List<Moon> moons = List.of(testRestTemplate.getForObject(baseUrl, Moon[].class));
        assertEquals(2, moons.size());
    }

    @Test
    public void getMoons_emptyDatabase_returnsEmptyList() {
        List<Moon> MoonList = List.of(testRestTemplate.getForObject(baseUrl, Moon[].class));
        assertEquals(0, MoonList.size());
    }

    @Test
    public void getMoonById_withOnePostedMoon_returnsMoonWithSameId() {
        Moon testMoon = new Moon(null, "Spirit", false, testPlanet);
        testMoon = testRestTemplate.postForObject(baseUrl, testMoon, Moon.class);
        Moon result = testRestTemplate.getForObject(baseUrl + "/" + testMoon.getId(), Moon.class);
        assertEquals(testMoon.getId(), result.getId());
    }

    @Test
    void testFindStarByIdWhenIdNotExists() {
        final ResponseEntity<Moon> response = testRestTemplate.getForEntity(baseUrl + "/987654321", Moon.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void updateMoon_withOnePostedMoon_returnsUpdatedMoon() {
        Moon testMoon = new Moon(null, "Spirit", false, testPlanet);
        testMoon = testRestTemplate.postForObject(baseUrl, testMoon, Moon.class);

        testMoon.setName("Updated name");
        testRestTemplate.put(baseUrl + "/" + testMoon.getId(), testMoon, Moon.class);
        Moon updatedMoon = testRestTemplate.getForObject(baseUrl + "/" + testMoon.getId(), Moon.class);

        assertEquals("Updated name", updatedMoon.getName());
    }

    @Test
    public void deleteMoonById_withSomePostedMoons_getAllShouldReturnRemainingMoons() {
        Moon testMoon1 = new Moon(null, "Spirit1", false, testPlanet);
        Moon testMoon2 = new Moon(null, "Spirit2", false, testPlanet);
        Moon testMoon3 = new Moon(null, "Spirit3", false, testPlanet);
        List<Moon> testMoons = new ArrayList<>();
        testMoons.add(testMoon1);
        testMoons.add(testMoon2);
        testMoons.add(testMoon3);

        testMoons.forEach(testMoon ->
                testMoon.setId(testRestTemplate.postForObject(baseUrl, testMoon, Moon.class).getId())
        );

        testRestTemplate.delete(baseUrl + "/" + testMoon2.getId());
        testMoons.remove(testMoon2);

        List<Moon> remainingMoons = List.of(testRestTemplate.getForObject(baseUrl, Moon[].class));

        assertEquals(testMoons.size(), remainingMoons.size());
        for (int i = 0; i < testMoons.size(); i++) {
            assertEquals(testMoons.get(i).getName(), remainingMoons.get(i).getName());
        }
    }
}

