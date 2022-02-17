package org.chetan.topscoreranking.beans;

import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class ErrorResponse {
	private String errorMessage;
	private ZonedDateTime timeStamp;
	private String stackTrace;
}
