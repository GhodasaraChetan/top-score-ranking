package org.chetan.topscoreranking.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;
import java.util.stream.IntStream;
import org.chetan.topscoreranking.beans.ScoreBean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestClientException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ScoreControllerTest {
	//Integration test

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	//POST
	@Test
	public void test_create_score() {
		//insert one
		String url = "http://localhost:" + port + "/scores";
		ScoreBean inputScoreBean = getScoreBean(10);
		ScoreBean expectedScoreBean = getScoreBeanWithId(1, 10);

		ResponseEntity<ScoreBean> scoreBeanResponseEntity = restTemplate.postForEntity(url, inputScoreBean, ScoreBean.class);

		assertThat(scoreBeanResponseEntity.getBody())
				.usingRecursiveComparison()
				.ignoringFieldsOfTypes(ZonedDateTime.class)
				.isEqualTo(expectedScoreBean);
	}

	@Test
	public void test_multiple_create_score() {
		//insert multiple
		String url = "http://localhost:" + port + "/scores";
		IntStream.range(1, 4).forEach(x -> {
			ScoreBean inputScoreBean = getScoreBean(x * 10);
			ScoreBean expectedScoreBean = getScoreBeanWithId(x, x * 10);

			ResponseEntity<ScoreBean> scoreBeanResponseEntity = restTemplate.postForEntity(url, inputScoreBean, ScoreBean.class);

			assertThat(scoreBeanResponseEntity.getBody())
					.usingRecursiveComparison()
					.ignoringFieldsOfTypes(ZonedDateTime.class)
					.isEqualTo(expectedScoreBean);
		});

	}

	@Test
	public void test_create_score_with_validation_exception() {
		//insert one with null values
		String url = "http://localhost:" + port + "/scores";
		ScoreBean inputScoreBean = ScoreBean.builder().build();

		Assertions.assertThrows(RestClientException.class, () -> restTemplate.postForEntity(url, inputScoreBean, ScoreBean.class));

		assertThat(restTemplate.postForEntity(url, inputScoreBean, null).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}


	//GET
	@Test
	public void test_get_score() {
		//insert
		String postUrl = "http://localhost:" + port + "/scores";
		ScoreBean inputScoreBean = getScoreBean(10);
		restTemplate.postForEntity(postUrl, inputScoreBean, ScoreBean.class);

		//get by id
		ScoreBean expectedScoreBean = getScoreBeanWithId(1, 10);
		String getUrl = "http://localhost:" + port + "/scores/" + 1;
		ResponseEntity<ScoreBean> scoreBeanResponseEntity = restTemplate.getForEntity(getUrl, ScoreBean.class);

		assertThat(scoreBeanResponseEntity.getBody())
				.usingRecursiveComparison()
				.ignoringFieldsOfTypes(ZonedDateTime.class)
				.isEqualTo(expectedScoreBean);
	}

	@Test
	public void test_get_score_after_multiple_insertions() {
		//multiple insert
		String postUrl = "http://localhost:" + port + "/scores";
		IntStream.range(1, 4).forEach(i -> {
			ScoreBean inputScoreBean = getScoreBean(i * 10);
			restTemplate.postForEntity(postUrl, inputScoreBean, ScoreBean.class);
		});

		//get by any of those ids
		ScoreBean expectedScoreBean = getScoreBeanWithId(3, 30);
		String getUrl = "http://localhost:" + port + "/scores/" + 3;
		ResponseEntity<ScoreBean> scoreBeanResponseEntity = restTemplate.getForEntity(getUrl, ScoreBean.class);

		assertThat(scoreBeanResponseEntity.getBody())
				.usingRecursiveComparison()
				.ignoringFieldsOfTypes(ZonedDateTime.class)
				.isEqualTo(expectedScoreBean);
	}

	@Test
	public void test_get_score_not_found() {
		//insert
		String postUrl = "http://localhost:" + port + "/scores";
		ScoreBean inputScoreBean = getScoreBean(10);
		restTemplate.postForEntity(postUrl, inputScoreBean, ScoreBean.class);

		//get by another id
		String getUrl = "http://localhost:" + port + "/scores/" + 2;
		assertThat(restTemplate.getForEntity(getUrl, ScoreBean.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	public void test_get_score_empty_data() {
		//directly get by id
		String getUrl = "http://localhost:" + port + "/scores/" + 2;
		assertThat(restTemplate.getForEntity(getUrl, ScoreBean.class).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}



	//DELETE
	@Test
	public void test_delete_score_found() {
		//insert
		String postUrl = "http://localhost:" + port + "/scores";
		ScoreBean inputScoreBean = getScoreBean(10);
		restTemplate.postForEntity(postUrl, inputScoreBean, ScoreBean.class);

		//get by id
		String getDeleteUrl = "http://localhost:" + port + "/scores/" + 1;
		ScoreBean expectedScoreBean = getScoreBeanWithId(1, 10);
		ResponseEntity<ScoreBean> beforeDeleteEntity = restTemplate.getForEntity(getDeleteUrl, ScoreBean.class);
		assertThat(beforeDeleteEntity.getBody())
				.usingRecursiveComparison()
				.ignoringFieldsOfTypes(ZonedDateTime.class)
				.isEqualTo(expectedScoreBean);

		//delete
		restTemplate.delete(getDeleteUrl);

		//get by id
		ResponseEntity<ScoreBean> afterDeleteEntity = restTemplate.getForEntity(getDeleteUrl, ScoreBean.class);
		assertThat(afterDeleteEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	//private methods
	private ScoreBean getScoreBean(int score) {
		return ScoreBean.builder()
				.player("testone")
				.score(score)
				.time(ZonedDateTime.now())
				.build();
	}

	private ScoreBean getScoreBeanWithId(long i, int score) {
		return ScoreBean.builder()
				.id(i)
				.player("TESTONE")
				.score(score)
				.time(ZonedDateTime.now())
				.build();
	}
}