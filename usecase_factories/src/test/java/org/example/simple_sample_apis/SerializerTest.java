package org.example.simple_sample_apis;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.simple_sample_apis.fp.Either;
import org.example.simple_sample_apis.usecase_factories.JsonDeserializationError;
import org.example.simple_sample_apis.usecase_factories.Serializer;
import org.example.simple_sample_apis.usecases.AstronomicPicture;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SerializerTest {
  @Test
  void deserializeAstronomicPictureSucceeds() {
    final var date = "2022-01-21";
    final var explanation = "Laser guide stars and adaptive optics sharpened this stunning ground-based image of stellar jets from the Gemini South Observatory, Chilean Andes, planet Earth. These twin outflows of MHO 2147 are from a young star in formation. It lies toward the central Milky Way and the boundary of the constellations Sagittarius and Ophiuchus at an estimated distance of some 10,000 light-years. At center, the star itself is obscured by a dense region of cold dust. But the infrared image still traces the sinuous jets across a frame that would span about 5 light-years at the system's estimated distance. Driven outward by the young rotating star, the apparent wandering direction of the jets is likely due to precession. Part of a multiple star system, the young star's rotational axis would slowly precess or wobble like a top under the gravitation influence of its nearby companions.";
    final var hdUrl = "https://apod.nasa.gov/apod/image/2201/noirlab2204a.jpg";
    final var mediaType ="image";
    final var serviceVersion = "v1";
    final var title = "Young Star Jet MHO 2147";
    final var url = "https://apod.nasa.gov/apod/image/2201/noirlab2204a_1024.jpg";
    final var astronomicPictureJson =
      "{\"date\":\"" + date +
        "\",\"explanation\":\""+ explanation +
        "\",\"hdurl\":\""+ hdUrl +
        "\",\"media_type\":\""+ mediaType +
        "\",\"service_version\":\""+ serviceVersion +
        "\",\"title\":\""+ title +
        "\",\"url\":\""+ url + "\"}";
    final var eitherAstronomicPicture = Serializer.deserialize(astronomicPictureJson, AstronomicPicture.class);
    assertAll(
      () -> assertNotNull(eitherAstronomicPicture),
      () -> assertTrue(eitherAstronomicPicture instanceof Either.Right)
    );
    eitherAstronomicPicture.fold(
      usecaseError -> {
        assertNull(usecaseError);
        return true;
      },
      astronomicPicture -> {
        assertAll(
          () -> assertNotNull(astronomicPicture),
          () -> assertEquals(date, astronomicPicture.getDate()),
          () -> assertEquals(explanation, astronomicPicture.getExplanation()),
          () -> assertEquals(hdUrl, astronomicPicture.getHdurl())
        );
        return true;
      }
    );
  }

  @Test
  void deserializeAstronomicPictureFails() {
    final var date = "2022-01-21";
    final var explanation = "Laser guide stars and adaptive optics sharpened this stunning ground-based image of stellar jets from the Gemini South Observatory, Chilean Andes, planet Earth. These twin outflows of MHO 2147 are from a young star in formation. It lies toward the central Milky Way and the boundary of the constellations Sagittarius and Ophiuchus at an estimated distance of some 10,000 light-years. At center, the star itself is obscured by a dense region of cold dust. But the infrared image still traces the sinuous jets across a frame that would span about 5 light-years at the system's estimated distance. Driven outward by the young rotating star, the apparent wandering direction of the jets is likely due to precession. Part of a multiple star system, the young star's rotational axis would slowly precess or wobble like a top under the gravitation influence of its nearby companions.";
    final var hdUrl = "https://apod.nasa.gov/apod/image/2201/noirlab2204a.jpg";
    final var mediaType ="image";
    final var serviceVersion = "v1";
    final var title = "Young Star Jet MHO 2147";
    final var url = "https://apod.nasa.gov/apod/image/2201/noirlab2204a_1024.jpg";
    final var astronomicPictureJson =
      "{\"dateUnknown\":\"" + date +
        "\",\"explanation\":\""+ explanation +
        "\",\"hdurl\":\""+ hdUrl +
        "\",\"media_type\":\""+ mediaType +
        "\",\"service_version\":\""+ serviceVersion +
        "\",\"title\":\""+ title +
        "\",\"url\":\""+ url + "\"}";
    final var eitherAstronomicPicture = Serializer.deserialize(astronomicPictureJson, AstronomicPicture.class);
    assertAll(
      () -> assertNotNull(eitherAstronomicPicture),
      () -> assertTrue(eitherAstronomicPicture instanceof Either.Right)
    );
    final var eitherUsecaseError = Serializer.deserialize(astronomicPictureJson, AstronomicPicture.class, true);
    assertAll(
      () -> assertNotNull(eitherUsecaseError),
      () -> assertTrue(eitherUsecaseError instanceof Either.Left)
    );
    eitherUsecaseError.fold(
      usecaseError -> {
        assertAll(
          () -> assertNotNull(usecaseError),
          () -> assertTrue(usecaseError instanceof JsonDeserializationError)
        );
        final var jsonDeserializationError = (JsonDeserializationError)usecaseError;
        assertAll(
          () -> assertNotNull(jsonDeserializationError),
          () -> assertEquals(astronomicPictureJson, jsonDeserializationError.getJson()),
          () -> assertNotNull(jsonDeserializationError.getException()),
          () -> assertTrue(jsonDeserializationError.getException() instanceof JsonProcessingException)
        );
        final var jsonProcessingException = (JsonProcessingException)jsonDeserializationError.getException();
        final var astronomicPictureClassName = AstronomicPicture.class.getName();
        assertAll(
          () -> assertNotNull(jsonProcessingException),
          () -> assertEquals(jsonProcessingException.getOriginalMessage(), "Unrecognized field \"dateUnknown\" (class "+ astronomicPictureClassName + "), not marked as ignorable")
        );
        return true;
      },
      astronomicPicture -> {
        assertNull(astronomicPicture);
        return true;
      }
    );

  }

  @Test
  void serializeAstronomicPictureSucceeds() {
    // todo
  }

  @Test
  void serializeAstronomicPictureFails() {
    // todo
  }
}
