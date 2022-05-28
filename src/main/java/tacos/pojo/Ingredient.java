package tacos.pojo;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor    //注解在类，为类中需要特殊处理的字段完成构造方法，比如final和@NonNull
public class Ingredient {

    private final String id;
    private final String name;
    private final Type type;

    public static enum Type{
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
