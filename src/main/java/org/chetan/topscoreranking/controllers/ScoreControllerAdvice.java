package org.chetan.topscoreranking.controllers;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.chetan.topscoreranking.beans.ErrorResponse;
import org.chetan.topscoreranking.exceptions.ScoreNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ScoreControllerAdvice {

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ScoreNotFoundException.class)
	public ErrorResponse notFoundException(ScoreNotFoundException e) {
		ErrorResponse er = ErrorResponse.builder()
				.errorMessage(e.getMessage())
				.timeStamp(ZonedDateTime.now())
				.stackTrace(Arrays.toString(e.getStackTrace()))
				.build();
		log.error(er.toString());
		return er;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
		List<ErrorResponse> errors = e.getBindingResult().getAllErrors().stream()
				.map(x -> ErrorResponse.builder()
						.errorMessage(x.getDefaultMessage())
						.timeStamp(ZonedDateTime.now())
						.stackTrace(x.toString())
						.build())
				.collect(Collectors.toList());
		log.error(errors.toString());
		return errors;
	}


	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ErrorResponse internalError(Exception e) {
		ErrorResponse er = ErrorResponse.builder()
				.errorMessage(e.getMessage())
				.timeStamp(ZonedDateTime.now())
				.stackTrace(Arrays.toString(e.getStackTrace()))
				.build();
		log.error(er.toString());
		return er;
	}

	//Todo: add validation exception
}
