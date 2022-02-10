package org.example.simple_sample_apis.r2dbc.repositories;

import reactor.core.publisher.Flux;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.example.simple_sample_apis.r2dbc.models.DbPlayer;

public interface DbPlayersR2dbcRepository extends ReactiveCrudRepository<DbPlayer, Integer> {
  @Query("select id, name, age from players where name = $1")
  Flux<DbPlayer> findAllByName(String name);

  @Query("select * from players where age = $1")
  Flux<DbPlayer> findByAge(int age);
}
