package com.uni.data.analyzer.exception.handlers;

import com.uni.data.analyzer.controllers.AnalysisController;
import com.uni.data.analyzer.exceptions.AnalysisNameNotFoundException;
import com.uni.data.analyzer.exceptions.IllegalFormatException;
import com.uni.data.analyzer.exceptions.MissingRequiredDataException;
import com.uni.data.analyzer.exceptions.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.status;

@ControllerAdvice(basePackageClasses = AnalysisController.class)
public class AnalysisExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnforeseenException(Exception ex) {
        return buildApiErrorEntity(INTERNAL_SERVER_ERROR, "Internal error occurred");
    }

    @ExceptionHandler(MissingRequiredDataException.class)
    public ResponseEntity<ApiError> handleMissingRequiredDataException(MissingRequiredDataException ex) {
        return buildBadRequestResponse(ex.getMessage());
    }

    @ExceptionHandler(AnalysisNameNotFoundException.class)
    public ResponseEntity<ApiError> handleAnalysisNameNotFoundException(AnalysisNameNotFoundException ex) {
        return buildBadRequestResponse(ex.getMessage());
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<ApiError> handleParseException(ParseException ex) {
        return buildBadRequestResponse(ex.getMessage());
    }

    @ExceptionHandler(IllegalFormatException.class)
    public ResponseEntity<ApiError> handleIllegalFormatException(IllegalFormatException ex) {
        return buildBadRequestResponse(ex.getMessage());
    }

    private ResponseEntity<ApiError> buildBadRequestResponse(String message) {
        return buildApiErrorEntity(BAD_REQUEST, message);
    }

    private ResponseEntity<ApiError> buildApiErrorEntity(HttpStatus status, String message) {
        return status(status).body(new ApiError(status, message));
    }
}