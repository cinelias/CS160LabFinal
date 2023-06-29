import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


public class Main {
    private static Map<String, Integer> inventory = new TreeMap<>();
    private static List<CoffeeOrder> orders = new ArrayList<>();
    private static String logFile = "OrderLog.txt";
    private static String inventoryFile = "Inventory.txt";





    public static void main(String[] args) {
        System.out.println("Welcome to Java Coffee Co.!");
        try (Scanner input = new Scanner(System.in)) {
            boolean addOrder = false;
            do {
                System.out.println("Select an option:");
                System.out.println("1. New Order");
                System.out.println("2. Reload Inventory");
                System.out.println("3. Update Inventory");
                System.out.println("4. Update Order Log");
                System.out.println("5. Exit Application");
                int option = 0;
                while (option < 1 || option > 5) {
                    if (!input.hasNextInt()) {
                        System.out.println("Please enter a valid number.");
                        input.nextLine();
                    } else {
                        option = input.nextInt();
                        if (option < 1 || option > 5) {
                            System.out.println("Please enter a valid option.");
                        }
                    }
                }
                input.nextLine(); // Consume the newline character

                switch (option) {
                    case 1:
                        buildOrder();
                        break;
                    case 2:
                        System.out.println("Printing Inventory...");
                        inventory = readInventory(inventoryFile);
                        System.out.println(inventory);
                        break;
                    case 3:
                        writeInventory(inventoryFile);
                        break;
                    case 4:
                        writeOrderLog(logFile);
                        break;
                    case 5:
                        addOrder = false;
                        writeInventory(inventoryFile);
                        writeOrderLog(logFile);
                        break;
                    default:
                        System.out.println("Invalid option.");
                }

                if (option != 5) {
                    System.out.println("\nWould you like to perform another action (Y or N)?");
                    String yn = input.nextLine();
                    while (!(yn.equalsIgnoreCase("N") || yn.equalsIgnoreCase("Y"))) {
                        System.out.println("Please enter Y or N.");
                        yn = input.nextLine();
                    }
                    addOrder = yn.equalsIgnoreCase("Y");
                }
            } while (addOrder);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        if (!orders.isEmpty()) {
            writeOrderLog(logFile);
        }
    }

    private static CoffeeOrder buildOrder() {
        CoffeeOrder order = new CoffeeOrder();
        try {
            Scanner input = new Scanner(System.in);
            boolean addCoffee = true;
            while (addCoffee) {
                // prompt user to select base coffee type
                System.out.println("Select coffee type:");
                System.out.println("\t1. Black Coffee");
                System.out.println("\t2. Espresso");
                Coffee coffee;

                int option = 0;
                while (option < 1 || option > 2) {
                    if (!input.hasNextInt()) {
                        System.out.println("Please enter a valid number.");
                        input.nextLine();
                    } else {
                        option = input.nextInt();
                        if (option < 1 || option > 2) {
                            System.out.println("Please enter a valid option.");
                        }
                    }
                }
                input.nextLine(); // nextInt() doesn't consume newline
                if (option == 2) {
                    // Espresso is a specific case
                    String ingredient = "Espresso";
                    int quantity = inventory.get(ingredient);
                    if (quantity > 0) {
                        coffee = new Espresso();
                        inventory.put(ingredient, quantity - 1);
                    } else {
                        System.out.println("No " + ingredient + " left in inventory!");
                    }
                } else {
                    // make BlackCoffee the default case
                    String ingredient = "Black Coffee";
                    int quantity = inventory.get(ingredient);
                    if (quantity > 0) {
                        coffee = new BlackCoffee();
                        inventory.put(ingredient, quantity - 1);
                    } else {
                        System.out.println("No " + ingredient + " left in inventory!");
                    }
                }
                coffee = new BlackCoffee();

                // prompt user for any customizations
                while (option != 0) {
                    System.out.println(String.format("Coffee brewing: %s.", coffee.printCoffee()));
                    System.out.println("Would you like to add anything to your coffee?");
                    System.out.println("\t1. Flavored Syrup");
                    System.out.println("\t2. Hot Water");
                    System.out.println("\t3. Milk");
                    System.out.println("\t4. Sugar");
                    System.out.println("\t5. Whipped Cream");
                    System.out.println("\t0. NO - Finish Coffee");

                    while (!input.hasNextInt()) {
                        System.out.println("Please enter a valid number.");
                        input.nextLine();
                    }
                    option = input.nextInt();
                    input.nextLine();
                    coffee = switch (option) {
                        case 1 -> {
                            System.out.println("Please select a flavor:");
                            for (WithFlavor.Syrup flavor : WithFlavor.Syrup.values()) {
                                System.out.println("\t" + String.format("%d. %s", flavor.ordinal() + 1, flavor));
                            }
                            int max = WithFlavor.Syrup.values().length;
                            option = 0;
                            while (option < 1 || option > max) {
                                if (!input.hasNextInt()) {
                                    System.out.println("Please enter a valid number.");
                                    input.nextLine();
                                } else {
                                    option = input.nextInt();
                                    if (option < 1 || option > max) {
                                        System.out.println("Please enter a valid option.");
                                    }
                                }
                            }
                            input.nextLine();
                            WithFlavor.Syrup flavor = WithFlavor.Syrup.values()[option - 1];
                            option = 1;
                            String ingredient = flavor.toString() + " Syrup";
                            if (isInInventory(ingredient) && inventory.get(ingredient) > 0) {
                                coffee = new WithFlavor(coffee, flavor);
                                int quantity = inventory.get(ingredient);
                                if (quantity > 0) {
                                    inventory.put(ingredient, quantity - 1);
                                } else {
                                    System.out.println("No " + ingredient + " left in inventory!");
                                    option = 0;
                                    yield coffee;
                                }
                            } else {
                                System.out.println("No " + ingredient + " left in inventory!");
                            }
                            yield coffee;
                        }
                        case 2 -> {
                            String ingredient = "Hot Water";
                            if (isInInventory(ingredient) && inventory.get(ingredient) > 0 ) {
                                int quantity = inventory.get(ingredient);
                                if (quantity > 0) {
                                    coffee = new WithHotWater(coffee);
                                    inventory.put(ingredient, quantity - 1);
                                } else {
                                    System.out.println("No " + ingredient + " left in inventory!");
                                    option = 0;
                                    yield coffee;
                                }
                            } else {
                                System.out.println("No " + ingredient + " left in inventory!");
                            }
                            yield coffee;
                        }
                        case 3 -> {
                            String ingredient = "Milk";
                            if (isInInventory(ingredient) && inventory.get(ingredient) > 0 ) {
                                int quantity = inventory.get(ingredient);
                                if (quantity > 0) {
                                    coffee = new WithMilk(coffee);
                                    inventory.put(ingredient, quantity - 1);
                                } else {
                                    System.out.println("No " + ingredient + " left in inventory!");
                                    option = 0;
                                    yield coffee;
                                }
                            } else {
                                System.out.println("No " + ingredient + " left in inventory!");
                            }
                            yield coffee;
                        }
                        case 4 -> {
                            String ingredient = "Sugar";
                            if (isInInventory(ingredient) && inventory.get(ingredient) > 0) {
                                int quantity = inventory.get(ingredient);
                                if (quantity > 0) {
                                    coffee = new WithSugar(coffee);
                                    inventory.put(ingredient, quantity - 1);
                                } else {
                                    System.out.println("No " + ingredient + " left in inventory!");
                                    option = 0;
                                    yield coffee;
                                }
                            } else {
                                System.out.println("No " + ingredient + " left in inventory!");
                            }
                            yield coffee;
                        }
                        case 5 -> {
                            String ingredient = "Whipped Cream";
                            if (isInInventory(ingredient) && inventory.get(ingredient) > 0) {
                                int quantity = inventory.get(ingredient);
                                if (quantity > 0) {
                                    coffee = new WithWhippedCream(coffee);
                                    inventory.put(ingredient, quantity - 1);
                                } else {
                                    System.out.println("No " + ingredient + " left in inventory!");
                                    option = 0;
                                    yield coffee;
                                }
                            } else {
                                System.out.println("No " + ingredient + " left in inventory!");
                            }
                            yield coffee;
                        }
                        default -> {
                            if (option != 0) {
                                System.out.println("Please enter a valid option.");
                            }
                            yield coffee;
                        }
                    };
                }
                order.addCoffee(coffee);

                System.out.println("Would you like to order another coffee (Y or N)?");
                String yn = input.nextLine();
                while (!(yn.equalsIgnoreCase("N") || yn.equalsIgnoreCase("Y"))) {
                    System.out.println("Please enter Y or N.");
                    yn = input.nextLine();
                }
                addCoffee = yn.equalsIgnoreCase("Y");
            }
        } catch (Exception e) {
            System.out.println("Error building order: " + e.getMessage());
        }
        return order;
    }


    private static Map<String, Integer> readInventory(String filePath) throws FileNotFoundException {
        Map<String, Integer> inventory = new HashMap<>();
        try (Scanner scan = new Scanner(new File(filePath))) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] invenTemp = line.split("=");
                String ingredient = invenTemp[0].trim();
                Integer quantity = Integer.parseInt(invenTemp[1].trim());
                inventory.put(ingredient, quantity);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Inventory file not found!");
            throw e;
        }
        return inventory;
    }

    private static void writeInventory(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                String ingredient = entry.getKey();
                Integer quantity = entry.getValue();
                writer.write(ingredient + " = " + quantity);
                writer.newLine();
            }
            System.out.println("Inventory has been updated successfully!");
        } catch (Exception e) {
            System.out.println("Error writing inventory: " + e.getMessage());
        }
    }

    private static void writeOrderLog(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            for (CoffeeOrder order : orders) {
                writer.write(order.printOrder());
                writer.newLine();
            }
            orders.clear();
            System.out.println("Order log has been updated successfully!");
        } catch (Exception e) {
            System.out.println("Error writing order log: " + e.getMessage());
        }
    }

    private static boolean isInInventory(String ingredient) {
        return inventory.containsKey(ingredient) && inventory.get(ingredient) > 0;
    }
}
