package casestudy;

import fuzzy.defuzzification.*;
import fuzzy.inference.*;
import fuzzy.operators.*;
import fuzzy.system.FuzzyLogicSystem;
import fuzzy.variables.*;

import java.util.*;

/**
 * Comprehensive demo of the Patient Triage System showing:
 * - Different inference engines (Mamdani vs Sugeno)
 * - Different defuzzification methods
 * - Rule manipulation
 * - Intermediate value inspection
 */
public class PatientTriageDemo {

    public static void main(String[] args) {
        System.out.println("=== Patient Triage Fuzzy Logic System - Comprehensive Demo ===\n");

        PatientTriageSystem triageSystem = new PatientTriageSystem();
        FuzzyLogicSystem system = triageSystem.getSystem();

        double pain = 8.5;
        double temp = 39.5;
        double bp = 180;

        System.out.println("Test Patient:");
        System.out.printf("  Pain Level: %.1f/10\n", pain);
        System.out.printf("  Temperature: %.1f°C\n", temp);
        System.out.printf("  Blood Pressure: %.1f mmHg\n\n", bp);

        Map<String, Double> inputs = new HashMap<>();
        inputs.put("PainLevel", pain);
        inputs.put("Temperature", temp);
        inputs.put("BloodPressure", bp);

        System.out.println("=== Using Mamdani Inference with Centroid Defuzzification ===\n");
        system.setInferenceEngine(new MamdaniInference(
                new MinTNorm(), new MaxSNorm(),
                new MinImplication(), new MaxAggregation()));
        system.setDefuzzificationMethod(new CentroidDefuzzification());

        Map<String, Map<String, Double>> fuzzified = system.getFuzzificationResults(inputs);
        System.out.println("Fuzzification Results:");
        for (Map.Entry<String, Map<String, Double>> entry : fuzzified.entrySet()) {
            System.out.println("  " + entry.getKey() + ":");
            for (Map.Entry<String, Double> setEntry : entry.getValue().entrySet()) {
                if (setEntry.getValue() > 0) {
                    System.out.printf("    %s: %.3f\n", setEntry.getKey(), setEntry.getValue());
                }
            }
        }

        Map<String, Double> inferred = system.getInferenceResults(inputs);
        System.out.println("\nInference Results:");
        for (Map.Entry<String, Double> entry : inferred.entrySet()) {
            System.out.printf("  %s: %.3f\n", entry.getKey(), entry.getValue());
        }

        double urgency1 = system.evaluate(inputs);
        System.out.printf("\nFinal Urgency Score: %.2f\n", urgency1);
        System.out.println("Category: " + triageSystem.getUrgencyCategory(urgency1));

        System.out.println("\n=== Using Mamdani Inference with Mean of Maximum ===\n");
        system.setDefuzzificationMethod(new MeanOfMaximumDefuzzification());
        double urgency2 = system.evaluate(inputs);
        System.out.printf("Final Urgency Score: %.2f\n", urgency2);
        System.out.println("Category: " + triageSystem.getUrgencyCategory(urgency2));

        System.out.println("\n=== Rule Base Information ===\n");
        System.out.println("Total Rules: " + system.getRuleBase().size());
        System.out.println("Enabled Rules: " + system.getRuleBase().getEnabledRules().size());
        System.out.println("\nFirst 5 Rules:");
        for (int i = 0; i < Math.min(5, system.getRuleBase().size()); i++) {
            System.out.println("  " + (i + 1) + ". " + system.getRuleBase().getRule(i));
        }

        System.out.println("\n=== Testing Rule Weighting ===\n");
        system.getRuleBase().setRuleWeight(0, 0.5);
        System.out.println("Reduced weight of first rule to 0.5");
        double urgency3 = system.evaluate(inputs);
        System.out.printf("Urgency Score with weighted rule: %.2f\n", urgency3);

        System.out.println("\n=== Testing Rule Disabling ===\n");
        system.getRuleBase().disableRule(0);
        System.out.println("Disabled first rule");
        double urgency4 = system.evaluate(inputs);
        System.out.printf("Urgency Score with disabled rule: %.2f\n", urgency4);
        system.getRuleBase().enableRule(0);

        System.out.println("\n=== Multiple Test Cases ===\n");
        double[][] testCases = {
                { 9.0, 40.0, 190 }, // Critical case
                { 6.0, 38.0, 100 }, // Urgent case
                { 4.0, 37.0, 120 }, // Moderate case
                { 2.0, 36.5, 110 }, // Mild case
                { 0.5, 36.0, 105 } // Minimal case
        };

        for (int i = 0; i < testCases.length; i++) {
            inputs.put("PainLevel", testCases[i][0]);
            inputs.put("Temperature", testCases[i][1]);
            inputs.put("BloodPressure", testCases[i][2]);

            double urgency = system.evaluate(inputs);
            System.out.printf("Case %d: Pain=%.1f, Temp=%.1f, BP=%.0f -> Score=%.2f (%s)\n",
                    i + 1, testCases[i][0], testCases[i][1], testCases[i][2],
                    urgency, triageSystem.getUrgencyCategory(urgency));
        }
    }
}
