package com.andruy.backend.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.andruy.backend.util.DayFlag;
import com.andruy.backend.util.WeekProgram;

public class WeekProgramRowMapper implements RowMapper<WeekProgram> {
    @Override
    public WeekProgram mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("ID");

        DayFlag monday = new DayFlag(
            1,
            rs.getBoolean("MONDAY")
        );

        DayFlag tuesday = new DayFlag(
            2,
            rs.getBoolean("TUESDAY")
        );

        DayFlag wednesday = new DayFlag(
            3,
            rs.getBoolean("WEDNESDAY")
        );

        DayFlag thursday = new DayFlag(
            4,
            rs.getBoolean("THURSDAY")
        );

        DayFlag friday = new DayFlag(
            5,
            rs.getBoolean("FRIDAY")
        );

        return new WeekProgram(id, List.of(monday, tuesday, wednesday, thursday, friday));
    }
}
