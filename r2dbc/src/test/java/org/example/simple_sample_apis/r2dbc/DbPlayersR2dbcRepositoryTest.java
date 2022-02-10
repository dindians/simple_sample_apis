package org.example.simple_sample_apis.r2dbc;

import java.util.Arrays;
import java.util.List;
import io.r2dbc.h2.H2ConnectionFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.example.simple_sample_apis.r2dbc.models.DbPlayer;
import org.example.simple_sample_apis.r2dbc.repositories.DbPlayersR2dbcRepository;

@SpringBootTest
public class DbPlayersR2dbcRepositoryTest {
  @Autowired
  private DbPlayersR2dbcRepository dbPlayersR2dbcRepository;

  @Autowired
  R2dbcEntityTemplate r2dbcEntityTemplate;

  @Autowired
  H2ConnectionFactory h2ConnectionFactory;

  @BeforeEach
  public void setup() {
    Hooks.onOperatorDebug();

    List<String> statements = Arrays.asList(
      "DROP SEQUENCE IF EXISTS players_id_sequence;",
      "CREATE SEQUENCE players_id_sequence INCREMENT 1 START WITH 1 MINVALUE 1",
      "DROP TABLE IF EXISTS players;",
      "CREATE table players (id INT DEFAULT nextval('players_id_sequence') NOT NULL, name VARCHAR, age INT NOT NULL);");

    statements.forEach(it ->
      r2dbcEntityTemplate
        .getDatabaseClient()
        .sql(it)
        .fetch()
        .rowsUpdated()
        .as(StepVerifier::create)
        .expectNextCount(1)
        .expectComplete()
        .log()
        .verify()
    );
  }

  @Test
  public void whenDeleteAll_then0IsExpected() {
    dbPlayersR2dbcRepository.deleteAll()
      .as(StepVerifier::create)
      .expectNextCount(0)
      .verifyComplete();
  }

  @Test
  public void whenInsert6_then6AreExpected() {
    insertPlayers();

    dbPlayersR2dbcRepository.findAll()
      .as(StepVerifier::create)
      .expectNextCount(6)
      .verifyComplete();
  }

  @Test
  public void whenSearchForCR7_then1IsExpected() {
    insertPlayers();

    dbPlayersR2dbcRepository.findAllByName("CR7")
      .as(StepVerifier::create)
      .expectNextCount(1)
      .verifyComplete();
  }

  @Test
  public void whenSearchFor32YearsOld_then2AreExpected() {
    insertPlayers();

    dbPlayersR2dbcRepository.findByAge(32)
      .as(StepVerifier::create)
      .expectNextCount(2)
      .verifyComplete();
  }

  @Test
  public void whenBatchHas2Operations_then2AreExpected() {
    Mono.from(h2ConnectionFactory.create())
      .flatMapMany(connection -> Flux.from(connection
        .createBatch()
        .add("select * from players")
        .add("select * from players")
        .execute()))
      .as(StepVerifier::create)
      .expectNextCount(2)
      .verifyComplete();
  }

  private void insertPlayers() {
    List<DbPlayer> dbPlayers = Arrays.asList(
      new DbPlayer(null, "Kaka", 37),
      new DbPlayer(null, "Messi", 32),
      new DbPlayer(null, "Mbappé", 20),
      new DbPlayer(null, "CR7", 34),
      new DbPlayer(null, "Lewandowski", 30),
      new DbPlayer(null, "Cavani", 32)
    );

    dbPlayersR2dbcRepository.saveAll(dbPlayers).subscribe();
  }
}

