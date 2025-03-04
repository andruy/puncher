package com.andruy.backend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.andruy.backend.mapper.WeekProgramRowMapper;
import com.andruy.backend.util.WeekProgram;

public class WeekProgramRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("deprecation")
    public WeekProgram getWeekProgram(int week) {
        String query = "SELECT * FROM WEEK_PROGRAM WHERE ID = ?";

        return jdbcTemplate.queryForObject(query, new Object[] { week }, new WeekProgramRowMapper());
    }

    public int setWeekProgram(WeekProgram week) {
        String query = "INSERT INTO WEEK_PROGRAM (ID, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY) VALUES (?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(query, week.id(), week.dayPrograms().get(0), week.dayPrograms().get(1), week.dayPrograms().get(2), week.dayPrograms().get(3), week.dayPrograms().get(4));
    }
}
