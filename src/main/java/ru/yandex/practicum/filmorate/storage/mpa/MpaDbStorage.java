package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Slf4j
@Repository
@AllArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbc;
    private final MpaMapper mpaMapper;

    @Override
    public List<Mpa> getAllMpa() {
        String query = "SELECT * FROM mpa ORDER BY mpa_id";
        return jdbc.query(query, mpaMapper);
    }

    @Override
    public Mpa getMpaById(Integer id) {
        try {
            String query = "SELECT * FROM mpa WHERE mpa_id = ?";
            return jdbc.queryForObject(query, mpaMapper, id);
        } catch (Exception e) {
            throw new NotFoundException("Такой рейтинг не найден");
        }
    }
}
