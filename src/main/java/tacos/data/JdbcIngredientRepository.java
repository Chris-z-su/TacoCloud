package tacos.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import tacos.Ingredient;

import java.sql.ResultSet;
import java.sql.SQLException;

//构造器注解，
//  为JdbcIngredientRepository添加@Repository注解之后，
//  spring的组件扫描就会自动发现他，
//  并且会将其初始化为Spring应用上下文中的bean
@Repository
public class JdbcIngredientRepository implements IngredientRepository {

    private JdbcTemplate jdbc;

//    当Spring创建JdbcIngredientRepository bean的时候，
//    它会通过@Autowired标注的构造器将JdbcTemplate注入进来
    @Autowired
    public JdbcIngredientRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        return jdbc.query(
                "select id, name, type from Ingredient",
                this::mapRowToIngredient);
    }

    @Override
    public Ingredient findOne(String id) {
        // 方式一：
//        return jdbc.queryForObject(
//                "select id, name, type from Ingredient where id = ?",
//                this::mapRowToIngredient, id);

        // 方式二：
        return jdbc.queryForObject(
                "select id, name, type from Ingredient where id = ?",
                new RowMapper<Ingredient>() {
                    public Ingredient mapRow (ResultSet rs, int rowNum) throws SQLException {
                        return new Ingredient(
                            rs.getString("id"),
                            rs.getString("name"),
                            Ingredient.Type.valueOf(rs.getString("type")));
                    }
                }, id);
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbc.update(
                "insert into Ingredient(id, name, type) values(?, ?, ?)",
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getType().toString());
        return ingredient;
    }

    private Ingredient mapRowToIngredient(ResultSet rs, int rowNum)
            throws SQLException {
        return new Ingredient(
                rs.getString("id"),
                rs.getString("name"),
                Ingredient.Type.valueOf(rs.getString("type")));
    }
}
