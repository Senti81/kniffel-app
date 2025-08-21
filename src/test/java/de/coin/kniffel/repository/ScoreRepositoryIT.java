package de.coin.kniffel.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.coin.kniffel.util.DatabaseUtil;

public class ScoreRepositoryIT {

    ScoreRepository scoreRepository = new ScoreRepository();

    @BeforeEach
    void setUp() {
        DatabaseUtil.cleanDatabase();
    }

    @Test
    void insertScore() {

    }
}
