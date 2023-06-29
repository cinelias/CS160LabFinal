import java.util.ArrayList;
import java.util.List;

public class WithWhippedCream extends CoffeeDecorator {
    public WithWhippedCream(Coffee c) {
        super(c);
    }

    @Override
    public double getCost() {
        return super.getCost() + 0.25;
    }

    @Override
    public List<String> getIngredients() {
        List<String> ingred = new ArrayList<>(super.getIngredients());
        ingred.add("Whipped Cream");
        return ingred;
    }

    @Override
    public String printCoffee() {
        return super.printCoffee() + " with whipped cream";
    }
}
