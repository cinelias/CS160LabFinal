import java.util.List;

public abstract class CoffeeDecorator implements Coffee {
    private Coffee coffee;

    public CoffeeDecorator(Coffee c) {
        coffee = c;
    }

    public double getCost() { return coffee.getCost(); }

    public List<String> getIngredients() {
        return null;
    }

    public String printCoffee() {
        return coffee.printCoffee();
    }
}
