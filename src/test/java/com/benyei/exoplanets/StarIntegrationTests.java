package com.benyei.exoplanets;

import com.benyei.exoplanets.model.Star;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class StarIntegrationTests {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    private TestRestTemplate testRestTemplate;


    @BeforeEach
    public void setUp() {
        this.baseUrl = "http://localhost:" + port + "/api/stars";
    }

    @Test
    public void addNewStar_withOnePostedStar_shouldReturnSameStar() {
        Star testStar = new Star(null, "Hope", 4444, 77.7, 1.2, 1.1);
        ResponseEntity<Star> result;
        result = testRestTemplate.postForEntity(baseUrl, testStar, Star.class);
        assertEquals(201, result.getStatusCodeValue());
        assertEquals(result.getBody().getName(), testStar.getName());
    }

    @Test
    public void addNewStarWithNameShorterThanTwoCharacter_withOnePostedStar_shouldReturnBadRequest() {
        final Star testStar = new Star(null, "H", 4444, 77.7, 1.2, 1.1);
        final ResponseEntity<Object> postResponse = testRestTemplate.postForEntity(baseUrl, testStar, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    public void addNewStarWithZeroTemperature_withOnePostedStar_shouldReturnBadRequest() {
        final Star testStar = new Star(null, "Hope", 0, 77.7, 1.2, 1.1);
        final ResponseEntity<Object> postResponse = testRestTemplate.postForEntity(baseUrl, testStar, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    public void addNewStarWithNegativeTemperature_withOnePostedStar_shouldReturnBadRequest() {
        final Star testStar = new Star(null, "Hope", -1, 77.7, 1.2, 1.1);
        final ResponseEntity<Object> postResponse = testRestTemplate.postForEntity(baseUrl, testStar, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    public void addNewStarWithZeroDistance_withOnePostedStar_shouldReturnBadRequest() {
        final Star testStar = new Star(null, "Hope", 4500, 0.0, 1.2, 1.1);
        final ResponseEntity<Object> postResponse = testRestTemplate.postForEntity(baseUrl, testStar, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    public void addNewStarWithNegativeDistance_withOnePostedStar_shouldReturnBadRequest() {
        final Star testStar = new Star(null, "Hope", 4500, -1.0, 1.2, 1.1);
        final ResponseEntity<Object> postResponse = testRestTemplate.postForEntity(baseUrl, testStar, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    public void addNewStarWithZeroRadius_withOnePostedStar_shouldReturnBadRequest() {
        final Star testStar = new Star(null, "Hope", 4500, 10.03, 0.0, 1.1);
        final ResponseEntity<Object> postResponse = testRestTemplate.postForEntity(baseUrl, testStar, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    public void addNewStarWithNegativeRadius_withOnePostedStar_shouldReturnBadRequest() {
        final Star testStar = new Star(null, "Hope", 4500, 10.03, -1.0, 1.1);
        final ResponseEntity<Object> postResponse = testRestTemplate.postForEntity(baseUrl, testStar, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    public void addNewStarWithZeroMass_withOnePostedStar_shouldReturnBadRequest() {
        final Star testStar = new Star(null, "Hope", 4500, 10.03, 1.01, 0.0);
        final ResponseEntity<Object> postResponse = testRestTemplate.postForEntity(baseUrl, testStar, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    public void addNewStarWithNegativeMass_withOnePostedStar_shouldReturnBadRequest() {
        final Star testStar = new Star(null, "Hope", 4500, 10.03, 1.01, -1.0);
        final ResponseEntity<Object> postResponse = testRestTemplate.postForEntity(baseUrl, testStar, Object.class);

        assertEquals(HttpStatus.BAD_REQUEST, postResponse.getStatusCode());
    }

    @Test
    public void addNewStarWithId_withOnePostedStar_shouldReturnStarWithGeneratedId() {
        final Star testStar = new Star(9876543210L, "Hope", 4500, 10.03, 1.01, 1.02);
        final Star result = testRestTemplate.postForObject(baseUrl, testStar, Star.class);

        assertEquals(1L, result.getId());
    }

    @Test
    public void addTwoNewStars_withTwoPostedStars_shouldSaveTwoStars() {
        final Star testStar1 = new Star(null, "Hope1", 4500, 10.03, 1.01, 1.02);
        final Star testStar2 = new Star(null, "Hope2", 5500, 110.03, 0.21, 0.22);

        testRestTemplate.postForObject(baseUrl, testStar1, Star.class);
        testRestTemplate.postForObject(baseUrl, testStar2, Star.class);

        final List<Star> stars = List.of(testRestTemplate.getForObject(baseUrl, Star[].class));
        assertEquals(2, stars.size());
    }

    @Test
    public void getStars_emptyDatabase_returnsEmptyList() {
        List<Star> StarList = List.of(testRestTemplate.getForObject(baseUrl, Star[].class));
        assertEquals(0, StarList.size());
    }

    @Test
    public void getStarById_withOnePostedStar_returnsStarWithSameId() {
        Star testStar = new Star(null, "Hope", 4444, 77.7, 1.2, 1.1);
        testStar = testRestTemplate.postForObject(baseUrl, testStar, Star.class);
        Star result = testRestTemplate.getForObject(baseUrl + "/" + testStar.getId(), Star.class);
        assertEquals(testStar.getId(), result.getId());
    }

    @Test
    public void updateStar_withOnePostedStar_returnsUpdatedStar() {
        Star testStar = new Star(null, "Hope", 4500, 10.03, 1.01, 1.02);
        testStar = testRestTemplate.postForObject(baseUrl, testStar, Star.class);

        testStar.setName("Updated name");
        testRestTemplate.put(baseUrl + "/" + testStar.getId(), testStar, Star.class);
        Star updatedStar = testRestTemplate.getForObject(baseUrl + "/" + testStar.getId(), Star.class);

        assertEquals("Updated name", updatedStar.getName());
    }

    @Test
    public void deleteStarById_withSomePostedStars_getAllShouldReturnRemainingStars() {
        Star testStar1 = new Star(null, "Hope1", 4500, 10.03, 1.01, 1.02);
        Star testStar2 = new Star(null, "Hope2", 4500, 10.03, 1.01, 1.02);
        Star testStar3 = new Star(null, "Hope3", 4500, 10.03, 1.01, 1.02);
        List<Star> testStars = new ArrayList<>();
        testStars.add(testStar1);
        testStars.add(testStar2);
        testStars.add(testStar3);

        testStars.forEach(testStar ->
                testStar.setId(testRestTemplate.postForObject(baseUrl, testStar, Star.class).getId())
        );

        testRestTemplate.delete(baseUrl + "/" + testStar2.getId());
        testStars.remove(testStar2);

        List<Star> remainingStars = List.of(testRestTemplate.getForObject(baseUrl, Star[].class));

        assertEquals(testStars.size(), remainingStars.size());
        for (int i = 0; i < testStars.size(); i++) {
            assertEquals(testStars.get(i).getName(), remainingStars.get(i).getName());
        }
    }
}