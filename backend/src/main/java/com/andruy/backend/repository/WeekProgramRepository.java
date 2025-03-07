package com.andruy.backend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.andruy.backend.mapper.WeekProgramRowMapper;
import com.andruy.backend.util.WeekProgram;

@Repository
public class WeekProgramRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public WeekProgram getWeekProgram(int week) {
        String query = "SELECT * FROM WEEK_PROGRAM WHERE ID = ?";

        try {
            return jdbcTemplate.queryForObject(query, new WeekProgramRowMapper(), week);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int setWeekProgram(WeekProgram week) {
        String query = "INSERT INTO WEEK_PROGRAM (ID, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY) VALUES (?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(query, week.id(), week.dayPrograms().get(0), week.dayPrograms().get(1), week.dayPrograms().get(2), week.dayPrograms().get(3), week.dayPrograms().get(4));
    }
}
