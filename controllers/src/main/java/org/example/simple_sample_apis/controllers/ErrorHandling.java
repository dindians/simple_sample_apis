package org.example.simple_sample_apis.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.simple_sample_apis.fp.EitherError;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandling {
  static <T> String error(@NotNull EitherError eitherError, String context, @NotNull Class<T> clazz) {
    String errorText = eitherError.toString();
    if(context != null) { errorText += String.format("\n(context: %s)", context); }
    Exception errorException = null;
    try {
      var usecaseErrorJson = String.format("{\"error\": { \"context\": \"%s\", \"type\": \"%s\", \"cause\": %s } }", context, eitherError.getClass().getName(), new ObjectMapper().writeValueAsString(eitherError));
      errorText += String.format("\n(error: %s)", usecaseErrorJson);
      return usecaseErrorJson;
    }
    catch(Exception exception) {
      errorException = exception;
    }
    finally {
      Logger logger = LoggerFactory.getLogger(clazz);
      if(errorException == null) logger.error(errorText);
      else {
        errorText += "\nan exception of type " + errorException.getClass().getName() + " has occurred trying to log the error";
        logger.error(errorText, errorException);
        errorText += "\nexception:\n" + errorException;
      }
    }
    return "{ \"error\": \"" + errorText + "\"}";
  }
}
