import java.util.ArrayList;
import java.util.List;

public class WithHotWater extends CoffeeDecorator {
    public WithHotWater(Coffee c) {
        super(c);
    }

    @Override
    public double getCost() {
        return super.getCost();
    }

    @Override
    public List<String> getIngredients() {
        List<String> ingred = new ArrayList<>(super.getIngredients());
        ingred.add("Hot Water");
        return ingred;
    }

    @Override
    public String printCoffee() {
        return super.printCoffee() + " with hot water";
    }
}
