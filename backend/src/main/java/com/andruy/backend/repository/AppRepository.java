package com.andruy.backend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AppRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int switchOn() {
        String query = "UPDATE SWITCH_MANAGEMENT SET STATE = 1 WHERE NAME = 'APP'";

        return jdbcTemplate.update(query);
    }

    public int switchOff() {
        String query = "UPDATE SWITCH_MANAGEMENT SET STATE = 0 WHERE NAME = 'APP'";

        return jdbcTemplate.update(query);
    }

    public int switchState() {
        String query = "SELECT STATE FROM SWITCH_MANAGEMENT WHERE NAME = 'APP'";

        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public int currentHaltTime() {
        String query = "SELECT CURRENT_VALUE FROM HALTS WHERE IN_REFERENCE = 'COMMON'";

        return (jdbcTemplate.queryForObject(query, Integer.class));
    }

    public int createWeek(int id) {
        String query = "INSERT INTO WEEKLY_HOURS (ID) VALUES (?)";

        return jdbcTemplate.update(query, id);
    }

    public int getLatestWeek() {
        String query = "SELECT ID FROM WEEKLY_HOURS WHERE ROWNUM = 1 ORDER BY ID DESC";

        return jdbcTemplate.queryForObject(query, Integer.class);
    }
}
