package com.andruy.backend.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.andruy.backend.util.DayProgram;
import com.andruy.backend.util.WeekProgram;

public class WeekProgramRowMapper implements RowMapper<WeekProgram> {
    @Override
    public WeekProgram mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("ID");

        DayProgram monday = new DayProgram(
            1,
            rs.getLong("MONDAY_MORNING_IN"),
            rs.getLong("MONDAY_MORNING_OUT"),
            rs.getLong("MONDAY_AFTERNOON_IN"),
            rs.getLong("MONDAY_AFTERNOON_OUT")
        );

        DayProgram tuesday = new DayProgram(
            2,
            rs.getLong("TUESDAY_MORNING_IN"),
            rs.getLong("TUESDAY_MORNING_OUT"),
            rs.getLong("TUESDAY_AFTERNOON_IN"),
            rs.getLong("TUESDAY_AFTERNOON_OUT")
        );

        DayProgram wednesday = new DayProgram(
            3,
            rs.getLong("WEDNESDAY_MORNING_IN"),
            rs.getLong("WEDNESDAY_MORNING_OUT"),
            rs.getLong("WEDNESDAY_AFTERNOON_IN"),
            rs.getLong("WEDNESDAY_AFTERNOON_OUT")
        );

        DayProgram thursday = new DayProgram(
            4,
            rs.getLong("THURSDAY_MORNING_IN"),
            rs.getLong("THURSDAY_MORNING_OUT"),
            rs.getLong("THURSDAY_AFTERNOON_IN"),
            rs.getLong("THURSDAY_AFTERNOON_OUT")
        );

        DayProgram friday = new DayProgram(
            5,
            rs.getLong("FRIDAY_MORNING_IN"),
            rs.getLong("FRIDAY_MORNING_OUT"),
            rs.getLong("FRIDAY_AFTERNOON_IN"),
            rs.getLong("FRIDAY_AFTERNOON_OUT")
        );

        return new WeekProgram(id, List.of(monday, tuesday, wednesday, thursday, friday));
    }
}
