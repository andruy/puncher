package com.andruy.backend.repository;

import java.time.DayOfWeek;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BrowserRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public double getCurrentTimeForTheDay(DayOfWeek dayOfWeek, int id) {
        String query = "SELECT " + dayOfWeek + " FROM WEEKLY_HOURS WHERE ID = ?";

        return jdbcTemplate.queryForObject(query, Double.class, id);
    }

    public int updateTime(DayOfWeek dayOfWeek, double time, int id) {
        String query = "UPDATE WEEKLY_HOURS SET " + dayOfWeek + " = ? WHERE ID = ?";

        return jdbcTemplate.update(query, time, id);
    }

    public int enterTime(String action, long time) {
        String query = "INSERT INTO DAILY_HOURS (ACTION, EPOCH) VALUES (?, ?)";

        return jdbcTemplate.update(query, action, time);
    }

    public Map<String, Long> getCurrentHours(String action1, String action2) {
        String query1 = "SELECT EPOCH FROM DAILY_HOURS WHERE ACTION = '" + action1 + "' AND RECORDED_ON = TRUNC(SYSDATE)";
        String query2 = "SELECT EPOCH FROM DAILY_HOURS WHERE ACTION = '" + action2 + "' AND RECORDED_ON = TRUNC(SYSDATE)";

        return Map.of(
            "final", jdbcTemplate.queryForObject(query1, Long.class),
            "initial", jdbcTemplate.queryForObject(query2, Long.class)
        );
    }
}
