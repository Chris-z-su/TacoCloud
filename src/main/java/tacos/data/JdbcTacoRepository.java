package tacos.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import tacos.pojo.Ingredient;
import tacos.pojo.Taco;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Repository
public class JdbcTacoRepository implements TacoRepository {

    private JdbcTemplate jdbc;

    public JdbcTacoRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Taco save(Taco taco) {

        Long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        // for (Ingredient ingredient :
        //         taco.getIngredients()) {
        //     saveIngredientToTaco(ingredient, tacoId);
        // }

        for (String ingredientId : taco.getIngredients()) {
            saveIngredientToTaco(ingredientId, tacoId);
        }

        return taco;
    }

    private long saveTacoInfo(Taco taco){
        taco.setCreateAt(new Date());
        // PreparedStatementCreator psc =
        //         new PreparedStatementCreatorFactory(
        //                 "insert into taco (name, createdAt) values (?, ?)",
        //                 Types.VARCHAR, Types.TIMESTAMP
        //         ).newPreparedStatementCreator(
        //                 Arrays.asList(
        //                         taco.getName(),
        //                         new Timestamp(taco.getCreateAt().getTime())));

        // 参考：https://stackoverflow.com/questions/53655693/keyholder-getkey-return-null
        PreparedStatementCreatorFactory preparedStatementCreatorFactory = new PreparedStatementCreatorFactory(
                "insert into taco (name, createdAt) values (?, ?)",
                Types.VARCHAR, Types.TIMESTAMP
        );
        // By default, returnGeneratedKeys = false so change it to true
        preparedStatementCreatorFactory.setReturnGeneratedKeys(true);

        PreparedStatementCreator psc = preparedStatementCreatorFactory.newPreparedStatementCreator(
                Arrays.asList(
                        taco.getName(),
                        new Timestamp(taco.getCreateAt().getTime())));

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(psc, keyHolder);

        return keyHolder.getKey().longValue();

    }

    private void saveIngredientToTaco(Ingredient ingredient,
                                      long tacoId){
        jdbc.update(
                "insert into Taco_Ingredients (taco, ingredient) " +
                        "values ( ?, ? )",
                tacoId, ingredient.getId());
    }

    private void
    saveIngredientToTaco(String ingredientId,
                                      long tacoId){
        jdbc.update(
                "insert into Taco_Ingredients (taco, ingredient) " +
                        "values ( ?, ? )",
                tacoId, ingredientId);
    }

}
