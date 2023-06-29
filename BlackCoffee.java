import java.util.ArrayList;
import java.util.List;

public class BlackCoffee implements Coffee {
    @Override
    public double getCost() {
        return 1.0;
    }

    @Override
    public List<String> getIngredients() {
        List <String> ingred = new ArrayList<>();
        ingred.add("Black Coffee");
        return ingred;
    }

    @Override
    public String printCoffee() {
        return "A black coffee";
    }
}
