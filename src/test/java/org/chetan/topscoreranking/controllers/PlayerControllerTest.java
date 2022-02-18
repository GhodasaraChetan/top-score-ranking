package org.chetan.topscoreranking.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.chetan.topscoreranking.beans.PlayerScoreHistory;
import org.chetan.topscoreranking.beans.ScoreBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PlayerControllerTest {
	//Integration test

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port = 8080;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void test_get_player_history_test0() throws IOException {
		objectMapper = new ObjectMapper()
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.registerModule(new JavaTimeModule())
				.disable(MapperFeature.USE_ANNOTATIONS);
		//Create data
		ZonedDateTime baseZdt = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.of("+09:00"));
		String postUrl = "http://localhost:" + port + "/scores";
		IntStream.range(1, 11).forEach(i -> {
			ScoreBean inputScoreBean = getScoreBean(i * 10, baseZdt.plusDays(i));
			restTemplate.postForEntity(postUrl, inputScoreBean, ScoreBean.class);
		});

		//Prepare Expectations
		String expectedJson = "[{\"bestScore\":{\"score\":90,\"time\":\"2022-02-27T10:26:24.881424Z\"},\"lowestScore\":{\"score\":30,\"time\":\"2022-02-21T10:26:24.826082Z\"},\"averageScore\":60.0,\"scoreHistory\":[{\"score\":30,\"time\":\"2022-02-21T10:26:24.826082Z\"},{\"score\":60,\"time\":\"2022-02-24T10:26:24.85214Z\"},{\"score\":90,\"time\":\"2022-02-27T10:26:24.881424Z\"}]},{\"bestScore\":{\"score\":100,\"time\":\"2022-02-28T10:26:24.89041Z\"},\"lowestScore\":{\"score\":10,\"time\":\"2022-02-19T10:26:24.484972Z\"},\"averageScore\":55.0,\"scoreHistory\":[{\"score\":10,\"time\":\"2022-02-19T10:26:24.484972Z\"},{\"score\":40,\"time\":\"2022-02-22T10:26:24.834057Z\"},{\"score\":70,\"time\":\"2022-02-25T10:26:24.86146Z\"},{\"score\":100,\"time\":\"2022-02-28T10:26:24.89041Z\"}]},{\"bestScore\":{\"score\":80,\"time\":\"2022-02-26T10:26:24.870051Z\"},\"lowestScore\":{\"score\":20,\"time\":\"2022-02-20T10:26:24.815597Z\"},\"averageScore\":50.0,\"scoreHistory\":[{\"score\":20,\"time\":\"2022-02-20T10:26:24.815597Z\"},{\"score\":50,\"time\":\"2022-02-23T10:26:24.842829Z\"},{\"score\":80,\"time\":\"2022-02-26T10:26:24.870051Z\"}]}]";
		List<PlayerScoreHistory> expectedHistories = objectMapper.readValue(new ByteArrayInputStream(expectedJson.getBytes()), new TypeReference<>() {});

		String url = "http://localhost:{port}/players/{playerName}/scorehistory";

		//Fetch by null player name
		ResponseEntity<PlayerScoreHistory> actualHistory_null = restTemplate.exchange(url, HttpMethod.GET, null, PlayerScoreHistory.class, port, "");
		Assertions.assertThat(actualHistory_null.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		//Fetch by empty player name
		ResponseEntity<PlayerScoreHistory> actualHistory_empty = restTemplate.exchange(url, HttpMethod.GET, null, PlayerScoreHistory.class, port, "");
		Assertions.assertThat(actualHistory_empty.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		//Fetch empty player's history
		ResponseEntity<PlayerScoreHistory> actualHistory_33 = restTemplate.exchange(url, HttpMethod.GET, null, PlayerScoreHistory.class, port, "test33");
		Assertions.assertThat(actualHistory_33.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(actualHistory_33.getBody()).usingRecursiveComparison().ignoringFieldsOfTypes(ZonedDateTime.class)
				.isEqualTo(PlayerScoreHistory.builder().averageScore(0d).scoreHistory(Collections.emptyList()).build());

		//Fetch 0th player's history
		ResponseEntity<PlayerScoreHistory> actualHistory_0 = restTemplate.exchange(url, HttpMethod.GET, null, PlayerScoreHistory.class, port, "test0");
		Assertions.assertThat(actualHistory_0.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(actualHistory_0.getBody()).usingRecursiveComparison().ignoringFieldsOfTypes(ZonedDateTime.class)
				.isEqualTo(expectedHistories.get(0));

		//Fetch 1st player's history
		ResponseEntity<PlayerScoreHistory> actualHistory_1 = restTemplate.exchange(url, HttpMethod.GET, null, PlayerScoreHistory.class, port, "test1");
		Assertions.assertThat(actualHistory_1.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(actualHistory_1.getBody()).usingRecursiveComparison().ignoringFieldsOfTypes(ZonedDateTime.class)
				.isEqualTo(expectedHistories.get(1));

		//Fetch 2nd player's history
		ResponseEntity<PlayerScoreHistory> actualHistory_2 = restTemplate.exchange(url, HttpMethod.GET, null, PlayerScoreHistory.class, port, "test2");
		Assertions.assertThat(actualHistory_2.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(actualHistory_2.getBody()).usingRecursiveComparison().ignoringFieldsOfTypes(ZonedDateTime.class)
				.isEqualTo(expectedHistories.get(2));
	}

	//private methods
	private ScoreBean getScoreBean(int score, ZonedDateTime time) {
		return ScoreBean.builder()
				.player("test" + (score % 3))
				.score(score)
				.time(time)
				.build();
	}

}