package org.example.simple_sample_apis.fp;

import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import java.util.function.Function;

// slightly based on https://github.com/spencerwi/Either.java/blob/master/src/main/java/com/spencerwi/either/Either.java
public class Either<L, R> {
    protected L getLeftValue() { throw new EitherException("Tried to getLeft from a Right"); }
    protected R getRightValue() { throw new EitherException("Tried to getRight from a Left"); }

    public final static class Left<L,R> extends Either<L,R> {
        private final L value;
        private Left(L value) { this.value = value; }

        @Override
        protected L getLeftValue() { return value; }
    }

    public final static class Right<L,R> extends Either<L,R> {
        private final R value;
        private Right(R value) { this.value = value; }

        @Override
        protected R getRightValue() { return value; }
    }

    public static <L,R> Either<L,R> left(@NotNull L left) { return new Left<>(left); }
    public static <L,R> @NotNull Either<L,R> right(@NotNull R right) { return new Right<>(right); }

    public <T> Either<L,T> flatMap(@NotNull Function<R, Either<L,T>> f) { return (this instanceof Either.Left)? Left.left(((Left<L, R>) this).value) : f.apply(this.getRightValue()); }
    public <T> Either<L,T> map(@NotNull Function<R, T> f) { return flatMap(value -> Right.right(f.apply(value))); }

    public <T> Mono<Either<L,T>> flatMapMono(@NotNull Function<R, Mono<Either<L,T>>> f) { return (this instanceof Either.Left)? Mono.just(Left.left(((Left<L, R>) this).value)) : f.apply(this.getRightValue());}
    public <T> Mono<Either<L,T>> mapMono(@NotNull Function<R, T> f) { return flatMapMono(value -> Mono.just(Right.right(f.apply(value)))); }

    public <T> T fold(@NotNull Function<L,T> left, @NotNull Function<R,T> right) { return (this instanceof Left)? left.apply(this.getLeftValue()) : right.apply(this.getRightValue()); }
}
