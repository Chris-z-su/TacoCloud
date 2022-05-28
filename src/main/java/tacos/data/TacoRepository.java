package tacos.data;

import tacos.pojo.Taco;

public interface TacoRepository {

    Taco save(Taco taco);
}
