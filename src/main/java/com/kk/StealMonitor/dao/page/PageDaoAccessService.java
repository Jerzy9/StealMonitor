package com.kk.StealMonitor.dao.page;

import com.kk.StealMonitor.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("PostgresPage")
public class PageDaoAccessService implements PageDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PageDaoAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Page> getAllPages() {
        final String sql = "SELECT * FROM pages;";
        return returnListFromDB(sql);
    }

    @Override
    public Optional<Page> selectPageById(UUID id) {
        final String sql = "SELECT * FROM pages WHERE id='" + id + "';";
        return returnListFromDB(sql).stream().findFirst();
    }

    private List<Page> returnListFromDB(String sql) {
        return jdbcTemplate.query(sql, ((resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            int idAuto = Integer.parseInt(resultSet.getString("id_auto"));
            String url = resultSet.getString("url");
            String divClassName = resultSet.getString("div_class_name");
            String scraperClassPath = resultSet.getString("scraper_class_path");
            String updateTime = resultSet.getString("update_time");
            String getTime = resultSet.getString("get_time");

            return new Page(id, idAuto, url, divClassName, scraperClassPath, updateTime, getTime);
        }));
    }
}
