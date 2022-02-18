package org.chetan.topscoreranking.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.chetan.topscoreranking.beans.ScoreBean;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ScoreControllerSearchTest {
	//Integration test

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port = 8080;

	static Stream<Arguments> parameters() {
		ZonedDateTime baseZdt = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.of("+09:00"));
		return Stream.of(
				Arguments.of(null, null, null, null, null, Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L)),
				Arguments.of(Arrays.asList(), null, null, null, null, Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L)),
				Arguments.of(Arrays.asList(), baseZdt.plusDays(1).plusMinutes(1), null, null, null, Arrays.asList(1L)),
				Arguments.of(Arrays.asList(), baseZdt.plusDays(3).plusMinutes(1), baseZdt.plusDays(1).plusMinutes(1), null, null, Arrays.asList(2L, 3L)),
				Arguments.of(Arrays.asList(), baseZdt.plusDays(9).minusMinutes(1), baseZdt.plusDays(1).plusMinutes(1), 0, null, Arrays.asList(2L, 3L, 4L, 5L, 6L, 7L, 8L)),
				Arguments.of(Arrays.asList(), baseZdt.plusDays(9).minusMinutes(1), baseZdt.plusDays(1).plusMinutes(1), 0, 2, Arrays.asList(2L, 3L)),
				Arguments.of(Arrays.asList(), baseZdt.plusDays(9).minusMinutes(1), baseZdt.plusDays(1).plusMinutes(1), 1, 2, Arrays.asList(4L, 5L)),
				Arguments.of(Arrays.asList(), baseZdt.plusDays(9).minusMinutes(1), baseZdt.plusDays(1).plusMinutes(1), 2, 2, Arrays.asList(6L, 7L)),

				Arguments.of(Arrays.asList("test60"), baseZdt.plusDays(9).minusMinutes(1), null, null, null, Arrays.asList(6L)),
				Arguments.of(Arrays.asList("test60"), baseZdt.plusDays(9).minusMinutes(1), baseZdt.plusDays(1).plusMinutes(1), null, null, Arrays.asList(6L)),
				Arguments.of(Arrays.asList("test60"), baseZdt.plusDays(9).minusMinutes(1), baseZdt.plusDays(1).plusMinutes(1), 0, null, Arrays.asList(6L)),
				Arguments.of(Arrays.asList("test60"), baseZdt.plusDays(9).minusMinutes(1), baseZdt.plusDays(1).plusMinutes(1), 0, 1, Arrays.asList(6L)),

				Arguments.of(Arrays.asList("test60", "test70", "test80"), baseZdt.plusDays(9).minusMinutes(1), null, null, null, Arrays.asList(6L, 7L, 8L)),
				Arguments.of(Arrays.asList("test60", "test70", "test80"), baseZdt.plusDays(9).minusMinutes(1), baseZdt.plusDays(1).plusMinutes(1), null, null, Arrays.asList(6L, 7L, 8L)),
				Arguments.of(Arrays.asList("test60", "test70", "test80"), baseZdt.plusDays(9).minusMinutes(1), baseZdt.plusDays(1).plusMinutes(1), 0, null, Arrays.asList(6L, 7L, 8L)),
				Arguments.of(Arrays.asList("test60", "test70", "test80"), baseZdt.plusDays(9).minusMinutes(1), baseZdt.plusDays(1).plusMinutes(1), 0, 1, Arrays.asList(6L)),
				Arguments.of(Arrays.asList("test60", "test70", "test80"), baseZdt.plusDays(9).minusMinutes(1), baseZdt.plusDays(1).plusMinutes(1), 1, 1, Arrays.asList(7L)),
				Arguments.of(Arrays.asList("test60", "test70", "test80"), baseZdt.plusDays(9).minusMinutes(1), baseZdt.plusDays(1).plusMinutes(1), 2, 1, Arrays.asList(8L))
		);
	}

	void initializeDb() {
		ZonedDateTime baseZdt = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneOffset.of("+09:00"));
		String url = "http://localhost:" + port + "/scores";
		IntStream.range(1, 10).forEach(i -> {
			ScoreBean inputScoreBean = getScoreBean(i * 10, baseZdt.plusDays(i));
			restTemplate.postForEntity(url, inputScoreBean, ScoreBean.class);
		});
	}

	@ParameterizedTest
	@MethodSource("parameters")
	void test_searchScore(List<String> players, ZonedDateTime beforeZdt, ZonedDateTime afterZdt, Integer offset, Integer size, List<Long> expectedIds) {

		initializeDb(); //Todo: Make it common

		String url = "http://localhost:{port}/scores?players={players}&beforeZdt={beforeZdt}&afterZdt={afterZdt}&offset={offset}&size={size}";
		ResponseEntity<List<ScoreBean>> actualScoresEntity =
				restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<ScoreBean>>() {},
						port, null == players ? null : String.join(",", players), beforeZdt, afterZdt, offset, size);

		List<Long> actualIds = Objects.requireNonNull(actualScoresEntity.getBody()).stream().map(ScoreBean::getId).collect(Collectors.toList());
		assertThat(actualIds).containsExactlyInAnyOrderElementsOf(expectedIds);
	}

	//private methods
	private ScoreBean getScoreBean(int score, ZonedDateTime time) {
		return ScoreBean.builder()
				.player("test" + score)
				.score(score)
				.time(time)
				.build();
	}
}