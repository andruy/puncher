package com.andruy.backend.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.andruy.backend.mapper.DayProgramRowMapper;
import com.andruy.backend.model.DayProgram;

@Repository
public class DayProgramRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public DayProgram getDay(int year, int month, int day) {
        String sql = "SELECT SWITCH FROM DAY_COMMAND WHERE YEAR = ? AND MONTH = ? AND DAY = ?";

        return jdbcTemplate.queryForObject(sql, DayProgram.class, year, month, day);
    }

    public List<DayProgram> getMonthDays(int year, int month) {
        String sql = "SELECT * FROM DAY_COMMAND WHERE YEAR = ? AND MONTH = ?";

        return jdbcTemplate.query(sql, new DayProgramRowMapper(), year, month);
    }

    public int setDay(int year, int month, int day, boolean switchValue) {
        String sql = """
                    MERGE INTO DAY_COMMAND USING DUAL ON (YEAR = ? AND MONTH = ? AND DAY = ?)
                        WHEN MATCHED THEN UPDATE SET SWITCH = ?
                        WHEN NOT MATCHED THEN INSERT (YEAR, MONTH, DAY, SWITCH) VALUES (?, ?, ?, ?)
                """;

        return jdbcTemplate.update(sql, year, month, day, switchValue, year, month, day, switchValue);
    }

    public int setDay(DayProgram dayProgram) {
        return setDay(dayProgram.date().getYear(), dayProgram.date().getMonthValue(), dayProgram.date().getDayOfMonth(), dayProgram.switchValue());
    }
}
