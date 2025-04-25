package com.andruy.backend.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import com.andruy.backend.model.DayProgram;

public class DayProgramRowMapper implements RowMapper<DayProgram> {
    @Override
    public DayProgram mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        return new DayProgram(
            LocalDate.of(
                rs.getInt("YEAR"),
                rs.getInt("MONTH"),
                rs.getInt("DAY")
            ),
            rs.getBoolean("SWITCH")
        );
    }
}
