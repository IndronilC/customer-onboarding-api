package com.kanini.corebanking.custonboard.customeronboarding.common.exception.handler;

import com.kanini.corebanking.custonboard.api.model.ErrorResponse;
import com.kanini.corebanking.custonboard.customeronboarding.controller.exception.CustomerOnboardingRequestNotFoundException;
import com.kanini.corebanking.custonboard.customeronboarding.services.exception.CustomerOnboardingBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomerOnboardingGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomerOnboardingRequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleCustomerRequestNotFoundException(
            CustomerOnboardingRequestNotFoundException customerRequestNotFoundException,
            WebRequest request
    ){
        log.error("Failed to find the requested customer data ", customerRequestNotFoundException);
        return buildErrorResponse(customerRequestNotFoundException, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(CustomerOnboardingBusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleCustomerOnboardingBusinessException(
            CustomerOnboardingBusinessException customerOnboardingBusinessException,
            WebRequest webRequest
    ){
        log.error("Failed to save the requested customer, exception is {} ", customerOnboardingBusinessException);
        return buildErrorResponse(customerOnboardingBusinessException, HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleAllUncaughtException(
            Exception exception,
            WebRequest request){
        log.error("Unknown error occurred", exception);
        return buildErrorResponse(
                exception,
                "Unknown error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }


    @Override
    public ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable
    Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request){
        return buildGeneralErrorResponse(ex,statusCode,request);
    }


    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            WebRequest request
    ) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                httpStatus,
                request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request
    ) {
        Integer errorCode = Integer.valueOf(httpStatus.value());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(errorCode);
        errorResponse.setErrorReason(message);
        errorResponse.setErrorSource(exception.getStackTrace().toString());

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private ResponseEntity<Object> buildGeneralErrorResponse(
            Exception exception,HttpStatusCode status, WebRequest request
    ){
        Integer errorCode = Integer.valueOf(status.value());
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(errorCode);
        errorResponse.setErrorReason(exception.getMessage());
        errorResponse.setErrorSource(exception.getStackTrace().toString());
        return ResponseEntity.status(status).body(errorResponse);
    }

}
