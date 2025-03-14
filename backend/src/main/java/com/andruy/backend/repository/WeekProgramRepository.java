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
        if (getWeekProgram(week.id()) == null) {
            String query = "INSERT INTO WEEK_PROGRAM (ID, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY) VALUES (?, ?, ?, ?, ?, ?)";

            return jdbcTemplate.update(query, week.id(), week.dayFlags().get(0).isOn(), week.dayFlags().get(1).isOn(), week.dayFlags().get(2).isOn(), week.dayFlags().get(3).isOn(), week.dayFlags().get(4).isOn());
        } else {
            String query = "UPDATE WEEK_PROGRAM SET MONDAY = ?, TUESDAY = ?, WEDNESDAY = ?, THURSDAY = ?, FRIDAY = ? WHERE ID = ?";

            return jdbcTemplate.update(query, week.dayFlags().get(0).isOn(), week.dayFlags().get(1).isOn(), week.dayFlags().get(2).isOn(), week.dayFlags().get(3).isOn(), week.dayFlags().get(4).isOn(), week.id());
        }
    }
}
