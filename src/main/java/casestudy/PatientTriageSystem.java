package casestudy;

import fuzzy.defuzzification.*;
import fuzzy.inference.*;
import fuzzy.membership.*;
import fuzzy.operators.*;
import fuzzy.rules.*;
import fuzzy.system.FuzzyLogicSystem;
import fuzzy.variables.*;

import java.util.*;

/**
 * Patient Triage Fuzzy Logic System Case Study
 * 
 * Problem: Emergency departments need to prioritize patients based on severity.
 * This system evaluates patient urgency based on:
 * - Pain Level (0-10 scale)
 * - Body Temperature (35-42°C)
 * - Blood Pressure (60-200 mmHg)
 * 
 * Output: Urgency Score (0-100)
 * - 0-25: Can Wait - See nurse within 60 minutes
 * - 26-50: See Soon - See nurse within 30 minutes
 * - 51-75: Urgent - See doctor within 15 minutes
 * - 76-90: Emergency - See doctor immediately
 * - 91-100: Critical - Activate trauma team
 */
public class PatientTriageSystem {
        private FuzzyLogicSystem system;

        public PatientTriageSystem() {
                setupSystem();
        }

        private void setupSystem() {
                LinguisticVariable outputVariable = createOutputVariable();
                system = new FuzzyLogicSystem(outputVariable);

                system.addInputVariable(createPainLevelVariable());
                system.addInputVariable(createTemperatureVariable());
                system.addInputVariable(createBloodPressureVariable());

                setupRules();

                system.setAndOperator(new MinTNorm());
                system.setOrOperator(new MaxSNorm());
                system.setDefuzzificationMethod(new CentroidDefuzzification());
        }

        private LinguisticVariable createPainLevelVariable() {
                LinguisticVariable painLevel = new LinguisticVariable("PainLevel", 0, 10);

                painLevel.addFuzzySet(new FuzzySet("Minimal", new TriangularMF(-0.5, 0, 2.5)));
                painLevel.addFuzzySet(new FuzzySet("Mild", new TriangularMF(0, 2.5, 5)));
                painLevel.addFuzzySet(new FuzzySet("Moderate", new TriangularMF(2.5, 5, 7.5)));
                painLevel.addFuzzySet(new FuzzySet("Severe", new TriangularMF(5, 7.5, 10)));
                painLevel.addFuzzySet(new FuzzySet("Extreme", new TriangularMF(7.5, 10, 10.5)));

                return painLevel;
        }

        private LinguisticVariable createTemperatureVariable() {
                LinguisticVariable temperature = new LinguisticVariable("Temperature", 35, 42);

                temperature.addFuzzySet(new FuzzySet("Hypothermic", new TriangularMF(34.5, 35, 36)));
                temperature.addFuzzySet(new FuzzySet("Low", new TriangularMF(35, 36, 36.5)));
                temperature.addFuzzySet(new FuzzySet("Normal", new TrapezoidalMF(36, 36.5, 37.5, 38)));
                temperature.addFuzzySet(new FuzzySet("Fever", new TriangularMF(37.5, 38.5, 39.5)));
                temperature.addFuzzySet(new FuzzySet("HighFever", new TriangularMF(38.5, 40, 42.5)));

                return temperature;
        }

        private LinguisticVariable createBloodPressureVariable() {
                LinguisticVariable bloodPressure = new LinguisticVariable("BloodPressure", 60, 200);

                bloodPressure.addFuzzySet(new FuzzySet("Critical_Low", new TriangularMF(59, 60, 80)));
                bloodPressure.addFuzzySet(new FuzzySet("Low", new TriangularMF(70, 85, 100)));
                bloodPressure.addFuzzySet(new FuzzySet("Normal", new TrapezoidalMF(90, 100, 120, 140)));
                bloodPressure.addFuzzySet(new FuzzySet("High", new TriangularMF(130, 150, 170)));
                bloodPressure.addFuzzySet(new FuzzySet("Critical_High", new TriangularMF(160, 180, 201)));

                return bloodPressure;
        }

        private LinguisticVariable createOutputVariable() {
                LinguisticVariable urgency = new LinguisticVariable("UrgencyScore", 0, 100);

                urgency.addFuzzySet(new FuzzySet("Can_Wait", new TrapezoidalMF(-1, 0, 12.5, 25)));
                urgency.addFuzzySet(new FuzzySet("See_Soon", new TriangularMF(12.5, 25, 37.5)));
                urgency.addFuzzySet(new FuzzySet("Urgent", new TriangularMF(25, 50, 75)));
                urgency.addFuzzySet(new FuzzySet("Emergency", new TriangularMF(50, 75, 90)));
                urgency.addFuzzySet(new FuzzySet("Critical", new TrapezoidalMF(75, 90, 100, 101)));

                return urgency;
        }

        private void setupRules() {
                RuleBase ruleBase = system.getRuleBase();

                addRule(ruleBase, "PainLevel", "Extreme", true, "Temperature", "HighFever", true,
                                "BloodPressure", "Critical_High", "Critical");
                addRule(ruleBase, "PainLevel", "Extreme", true, "Temperature", "HighFever", true,
                                "BloodPressure", "Critical_Low", "Critical");
                addRule(ruleBase, "PainLevel", "Extreme", true, "Temperature", "Hypothermic", true,
                                "BloodPressure", "Critical_Low", "Critical");

                addRule(ruleBase, "PainLevel", "Severe", true, "Temperature", "HighFever", true,
                                "BloodPressure", "Normal", "Emergency");
                addRule(ruleBase, "PainLevel", "Severe", true, "Temperature", "Normal", true,
                                "BloodPressure", "Critical_High", "Emergency");
                addRule(ruleBase, "PainLevel", "Severe", true, "Temperature", "Normal", true,
                                "BloodPressure", "Critical_Low", "Emergency");

                addRule(ruleBase, "PainLevel", "Moderate", true, "Temperature", "Fever", true,
                                "BloodPressure", "High", "Urgent");
                addRule(ruleBase, "PainLevel", "Moderate", true, "Temperature", "Fever", true,
                                "BloodPressure", "Low", "Urgent");
                addRule(ruleBase, "PainLevel", "Moderate", true, "Temperature", "Normal", true,
                                "BloodPressure", "High", "Urgent");
                addRule(ruleBase, "PainLevel", "Moderate", true, "Temperature", "Normal", true,
                                "BloodPressure", "Low", "Urgent");

                addRule(ruleBase, "PainLevel", "Mild", true, "Temperature", "Normal", true,
                                "BloodPressure", "Normal", "See_Soon");
                addRule(ruleBase, "PainLevel", "Mild", true, "Temperature", "Low", true,
                                "BloodPressure", "Normal", "See_Soon");
                addRule(ruleBase, "PainLevel", "Minimal", true, "Temperature", "Normal", true,
                                "BloodPressure", "Normal", "Can_Wait");

                addRule(ruleBase, "PainLevel", "Minimal", true, "Temperature", "Low", true,
                                "BloodPressure", "Normal", "Can_Wait");

                addRule(ruleBase, "PainLevel", "Extreme", true, "Temperature", "Normal", true,
                                "BloodPressure", "Normal", "Emergency");
                addRule(ruleBase, "PainLevel", "Severe", true, "Temperature", "Fever", true,
                                "BloodPressure", "Normal", "Urgent");
                addRule(ruleBase, "PainLevel", "Moderate", true, "Temperature", "Normal", true,
                                "BloodPressure", "Normal", "See_Soon");
                addRule(ruleBase, "PainLevel", "Mild", true, "Temperature", "Fever", true,
                                "BloodPressure", "Normal", "See_Soon");
        }

        private void addRule(RuleBase ruleBase, String var1, String set1, boolean and1,
                        String var2, String set2, boolean and2,
                        String var3, String set3, String outputSet) {
                Rule rule = new Rule("UrgencyScore", outputSet);
                rule.addAntecedentCondition(var1, set1, true);
                rule.addAntecedentCondition(var2, set2, and1);
                rule.addAntecedentCondition(var3, set3, and2);
                ruleBase.addRule(rule);
        }

        public double evaluateTriage(double painLevel, double temperature, double bloodPressure) {
                Map<String, Double> inputs = new HashMap<>();
                inputs.put("PainLevel", painLevel);
                inputs.put("Temperature", temperature);
                inputs.put("BloodPressure", bloodPressure);

                return system.evaluate(inputs);
        }

        public String getUrgencyCategory(double urgencyScore) {
                if (urgencyScore <= 25) {
                        return "Can Wait - See nurse within 60 minutes";
                } else if (urgencyScore <= 50) {
                        return "See Soon - See nurse within 30 minutes";
                } else if (urgencyScore <= 75) {
                        return "Urgent - See doctor within 15 minutes";
                } else if (urgencyScore <= 90) {
                        return "Emergency - See doctor immediately";
                } else {
                        return "Critical - Activate trauma team";
                }
        }

        public FuzzyLogicSystem getSystem() {
                return system;
        }

        public static void main(String[] args) {
                PatientTriageSystem triageSystem = new PatientTriageSystem();
                java.util.Scanner scanner = new java.util.Scanner(System.in);

                System.out.println("=== Patient Triage Fuzzy Logic System ===\n");

                // Run default test cases
                System.out.println("--- Default Test Cases ---\n");
                double[][] testCases = {
                                { 8.5, 39.5, 180 }, // Extreme pain, high fever, critical high BP
                                { 7.0, 38.5, 95 }, // Severe pain, fever, low BP
                                { 5.0, 37.0, 150 }, // Moderate pain, normal temp, high BP
                                { 3.0, 36.5, 110 }, // Mild pain, normal temp, normal BP
                                { 1.0, 36.0, 105 } // Minimal pain, low temp, normal BP
                };

                for (int i = 0; i < testCases.length; i++) {
                        double pain = testCases[i][0];
                        double temp = testCases[i][1];
                        double bp = testCases[i][2];

                        double urgency = triageSystem.evaluateTriage(pain, temp, bp);
                        String category = triageSystem.getUrgencyCategory(urgency);

                        System.out.printf("Test Case %d:\n", i + 1);
                        System.out.printf("  Pain Level: %.1f\n", pain);
                        System.out.printf("  Temperature: %.1f°C\n", temp);
                        System.out.printf("  Blood Pressure: %.1f mmHg\n", bp);
                        System.out.printf("  Urgency Score: %.2f\n", urgency);
                        System.out.printf("  Category: %s\n\n", category);
                }

                // Allow user to enter custom values
                System.out.println("--- Custom Input ---\n");
                System.out.println("Would you like to enter your own patient data? (yes/no): ");
                String response = scanner.nextLine().trim().toLowerCase();

                while (response.equals("yes") || response.equals("y")) {
                        try {
                                System.out.print("Enter Pain Level (0-10): ");
                                double pain = scanner.nextDouble();
                                if (pain < 0 || pain > 10) {
                                        System.out.println(
                                                        "Warning: Pain level should be between 0-10. Value will be clamped.");
                                }

                                System.out.print("Enter Temperature in °C (35-42): ");
                                double temp = scanner.nextDouble();
                                if (temp < 35 || temp > 42) {
                                        System.out.println(
                                                        "Warning: Temperature should be between 35-42°C. Value will be clamped.");
                                }

                                System.out.print("Enter Blood Pressure in mmHg (60-200): ");
                                double bp = scanner.nextDouble();
                                if (bp < 60 || bp > 200) {
                                        System.out.println(
                                                        "Warning: Blood pressure should be between 60-200 mmHg. Value will be clamped.");
                                }

                                scanner.nextLine(); // consume newline

                                double urgency = triageSystem.evaluateTriage(pain, temp, bp);
                                String category = triageSystem.getUrgencyCategory(urgency);

                                System.out.println("\n--- Evaluation Result ---");
                                System.out.printf("  Pain Level: %.1f/10\n", pain);
                                System.out.printf("  Temperature: %.1f°C\n", temp);
                                System.out.printf("  Blood Pressure: %.1f mmHg\n", bp);
                                System.out.printf("  Urgency Score: %.2f\n", urgency);
                                System.out.printf("  Category: %s\n\n", category);

                                System.out.print("Would you like to evaluate another patient? (yes/no): ");
                                response = scanner.nextLine().trim().toLowerCase();
                        } catch (java.util.InputMismatchException e) {
                                System.out.println("Error: Invalid input. Please enter numeric values.");
                                scanner.nextLine(); // clear invalid input
                                System.out.print("Would you like to try again? (yes/no): ");
                                response = scanner.nextLine().trim().toLowerCase();
                        }
                }

                System.out.println("Thank you for using the Patient Triage Fuzzy Logic System!");
                scanner.close();
        }
}
