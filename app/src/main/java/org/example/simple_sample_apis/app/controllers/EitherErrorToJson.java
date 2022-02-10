package org.example.simple_sample_apis.app.controllers;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.simple_sample_apis.fp.EitherError;

final class EitherErrorToJson {
  static <T> String execute(@NotNull EitherError eitherError, String context, @NotNull Class<T> clazz) {
    String errorText = eitherError.toString();
    if(context != null) { errorText += String.format("\n(context: %s)", context); }
    Exception errorException = null;
    try {
      var eitherErrorJson = String.format("{\"error\": { \"context\": \"%s\", \"type\": \"%s\", \"cause\": { \"%s\": %s } } }", context, eitherError.getClass().getName(), lowercaseFirstCharacter(eitherError.getClass().getSimpleName()), new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false).writeValueAsString(eitherError));
      errorText += String.format("\n(error: %s)", eitherErrorJson);
      return eitherErrorJson;
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

  private static String lowercaseFirstCharacter(@NotNull String value) {
    return value.substring(0,1).toLowerCase() + value.substring(1);
  }
}
