import java.util.ArrayList;
import java.util.List;

public class WithSugar extends CoffeeDecorator {
    public WithSugar(Coffee c) {
        super(c);
    }

    @Override
    public double getCost() {
        return super.getCost() + 0.15;
    }

    @Override
    public List<String> getIngredients() {
        List<String> ingred = new ArrayList<>(super.getIngredients());
        ingred.add("Sugar");
        return ingred;
    }

    @Override
    public String printCoffee() {
        return super.printCoffee() + " with sugar";
    }
}
