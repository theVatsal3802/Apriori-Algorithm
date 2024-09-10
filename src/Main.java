import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Task> transactions = new ArrayList<>();
        try {
            File inputFile = new File("src/24CS60R26_input.txt");
            Scanner fileReader = new Scanner(inputFile);
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                String taskId = line.substring(0, line.indexOf(':'));
                String[] items = line.substring(line.indexOf(':') + 1).split(",");
                transactions.add(new Task(taskId, new ArrayList<>(Arrays.asList(items))));
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Enter the minimum support required as percentage (without % sign):");
        double minSupport = sc.nextDouble();
        System.out.println("Enter the minimum confidence required as percentage (without % sign):");
        double minConfidence = sc.nextDouble();

        Apriori apriori = new Apriori(transactions, minSupport, minConfidence);
        AprioriResult result = apriori.run();

        List<Set<String>> largeItemSets = result.getLargeItemSets();
        List<String> associationRules = result.getAssociationRules();

        try {
            FileWriter outputFile = new FileWriter("24CS60R26_output.txt");
            outputFile.write("The Large Item Sets are: ");
            outputFile.write("(" + largeItemSets.size() + " Sets)\n");
            for (Set<String> s : largeItemSets) {
                outputFile.write(s.toString() + " \n");
            }
            outputFile.write("\n");
            outputFile.write("The Association Rules are: ");
            outputFile.write("(" + associationRules.size() + " Rules)\n");
            for (String s : associationRules) {
                outputFile.write(s + "\n");
            }
            outputFile.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Output generated successfully");
    }
}