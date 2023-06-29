import java.util.ArrayList;
import java.util.List;

public class WithMilk extends CoffeeDecorator {
    public WithMilk(Coffee c) {
        super(c);
    }

    @Override
    public double getCost() {
        return super.getCost() + 0.55;
    }

    @Override
    public List<String> getIngredients() {
        List<String> ingred = new ArrayList<>(super.getIngredients());
        ingred.add("Milk");
        return ingred;
    }

    @Override
    public String printCoffee() {
        return super.printCoffee() + " with milk";
    }
}
