package com.epam.esm.exception;

import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.epam.esm.exception.ExceptionCodes.*;

public class ErrorCodeToHttpStatusMapper {

    private static final Set<String> BAD_REQUEST = new HashSet<>(Arrays.asList(
        CERTIFICATE_NAME_MUST_BE_SPECIFIED,
        CERTIFICATE_NAME_LENGTH_CONSTRAINT,
        CERTIFICATE_DESCRIPTION_LENGTH_CONSTRAINT,
        CERTIFICATE_PRICE_MUST_BE_SPECIFIED,
        CERTIFICATE_PRICE_MUST_BE_POSITIVE,
        CERTIFICATE_DURATION_MUST_BE_POSITIVE,
        CERTIFICATE_DURATION_MUST_BE_SPECIFIED,
        TAG_NAME_MUST_BE_SPECIFIED,
        TAG_NAME_OR_ID_MUST_BE_SPECIFIED,
        SORT_TYPES_MUST_BE_LESS_OR_EQUALS_THAN_COLUMNS
    ));

    private static final Set<String> NOT_FOUND = new HashSet<>(Arrays.asList(
            CERTIFICATE_NOT_FOUND,
            TAG_NOT_FOUND,
            UNABLE_TO_SAVE_CERTIFICATE,
            UNABLE_TO_SAVE_TAG
    ));

    private static final Set<String> INTERNAL_ERROR = new HashSet<>(Arrays.asList(

    ));


    public HttpStatus errorCodeToStatus(String errorCode) {
        if (BAD_REQUEST.contains(errorCode)) {
            return HttpStatus.BAD_REQUEST;
        }
        if (NOT_FOUND.contains(errorCode)) {
            return HttpStatus.NOT_FOUND;
        }
        if (CONTENT_MEDIA_TYPE_NOT_SUPPORTED.equals(errorCode)) {
            return HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

}
