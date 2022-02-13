package org.example.simple_sample_apis.fp;

import java.util.function.Function;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

final class EitherTest {
    @Test
//    @Disabled
    void notNullArguments() {
        final Either<Boolean, Integer> left = Either.left(true);
        final Either<Boolean,Integer> right = Either.right(13);
        assertAll(
          () -> assertThrows(IllegalArgumentException.class, () -> Either.left(null)),
          () -> assertThrows(IllegalArgumentException.class, () -> Either.right(null)),
          () -> assertThrows(IllegalArgumentException.class, () -> left.map(null)),
          () -> assertThrows(IllegalArgumentException.class, () -> right.map(null)),
          () -> assertThrows(IllegalArgumentException.class, () -> left.flatMap(null)),
          () -> assertThrows(IllegalArgumentException.class, () -> right.flatMap(null)),
          () -> assertThrows(IllegalArgumentException.class, () -> left.fold(null, null)),
          () -> assertThrows(IllegalArgumentException.class, () -> right.fold(null, null))
        );
    }

    @Test
    void left() {
        final Boolean leftValue = true;
        final Either<Boolean, Integer> left = Either.left(leftValue);
        final Function<Integer, String> mapRightValueToString = value -> { throw new RuntimeException(); };
        final Function<Integer, Either<Boolean,Integer>> mapRightValueToEither = value -> { throw new RuntimeException(); };
        final Function<Integer, Either<Boolean,String>> mapRightValueToEither2 = value -> Either.right(value.toString());
        final Function<Boolean,String> foldLeft = value -> value.toString();
        final Function<Integer, String> foldRight = value -> { throw new RuntimeException(); };
        final String foldedLeft = foldLeft.apply(leftValue);

        assertAll(
          () -> assertNotNull(left),
          () -> assertTrue(left instanceof Either.Left),
          () -> assertEquals(leftValue, left.getLeftValue()),
          () -> assertTrue(left.map(mapRightValueToString) instanceof Either.Left),
          () -> assertEquals(leftValue, left.map(mapRightValueToString).getLeftValue()),
          () -> assertEquals(leftValue, left.map(mapRightValueToString).getLeftValue()),
          () -> assertEquals(leftValue, left.map(mapRightValueToString).getLeftValue()),
          () -> assertTrue(left.flatMap(mapRightValueToEither) instanceof Either.Left),
          () -> assertEquals(leftValue, left.flatMap(mapRightValueToEither).getLeftValue()),
          () -> assertEquals(leftValue, left.flatMap(mapRightValueToEither2).getLeftValue()),
          () -> assertEquals(leftValue, left.flatMap(mapRightValueToEither2).getLeftValue()),
          () -> assertEquals(foldedLeft, left.fold(foldLeft, foldRight)),
          () -> assertThrows(EitherException.class, left::getRightValue)
        );
    }

    @Test
    void right() {
        final var rightValue = 13;
        final Either<Boolean,Integer> right = Either.right(rightValue);
        final Function<Boolean,String> foldLeft = value -> { throw new RuntimeException(); };
        final Function<Integer,String> foldRight = value -> value.toString();
        final String foldedRight = foldRight.apply(rightValue);

        assertAll(
          () -> assertNotNull(right),
          () -> assertTrue(right instanceof Either.Right),
          () -> assertEquals(rightValue, right.getRightValue()),
          () -> assertEquals(foldedRight, right.fold(foldLeft, foldRight))
        );

        final Function<Integer, String> mapRightValue = value -> value.toString();
        final Function<Integer, Either<Boolean,String>> mapRightValueToEither = value -> Either.right(mapRightValue.apply(value));
        final var mappedRightValue = mapRightValue.apply(rightValue);
        final var mappedRight = right.map(mapRightValue);
        final var flatMappedRight = right.flatMap(mapRightValueToEither);

        assertAll(
          () -> assertThrows(IllegalArgumentException.class, () -> right.map(null)),
          () -> assertNotNull(mappedRight),
          () -> assertTrue(mappedRight instanceof Either.Right),
          () -> assertEquals(mappedRightValue, mappedRight.getRightValue()),
          () -> assertTrue(flatMappedRight instanceof Either.Right),
          () -> assertEquals(mappedRightValue, flatMappedRight.getRightValue()),
          () -> assertThrows(EitherException.class, right::getLeftValue)
        );
    }

    // todo add tests for Either.mapMono() and Either.flatMapMono()
}
