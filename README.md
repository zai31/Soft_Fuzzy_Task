# Fuzzy Logic Library - Phase 2

A comprehensive Java implementation of a Fuzzy Logic system for soft computing applications.

## Features

### Core Components

1. **Membership Functions**

   - Triangular
   - Trapezoidal
   - Gaussian

2. **Linguistic Variables**

   - Support for multiple input and output variables
   - Multiple fuzzy sets per variable
   - Input validation and domain clamping

3. **Inference Engines**

   - Mamdani inference
   - Sugeno inference (zero-order and first-order)

4. **Operators**

   - T-norms (AND): Minimum, Product
   - S-norms (OR): Maximum, Bounded Sum
   - Aggregation: Maximum
   - Implication: Minimum, Product

5. **Defuzzification Methods**

   - Centroid (Center of Gravity)
   - Mean of Maximum (MOM)

6. **Rule Base Management**

   - Create, edit, enable/disable rules
   - Rule weighting
   - Rule persistence support

7. **Evaluation Pipeline**
   - Complete fuzzify → infer → aggregate → defuzzify pipeline
   - Intermediate value access for debugging/visualization

## Case Study: Patient Triage System

The library includes a complete implementation of a Patient Triage Fuzzy Logic System for emergency department prioritization.

### Input Variables

1. **Pain Level** (0-10 scale)

   - Fuzzy Sets: Minimal, Mild, Moderate, Severe, Extreme
   - Membership Function: Triangular

2. **Body Temperature** (35-42°C)

   - Fuzzy Sets: Hypothermic, Low, Normal, Fever, HighFever
   - Membership Function: Trapezoidal for Normal, Triangular for others

3. **Blood Pressure** (60-200 mmHg)
   - Fuzzy Sets: Critical_Low, Low, Normal, High, Critical_High
   - Membership Function: Trapezoidal for Normal, Triangular for others

### Output Variable

**Urgency Score** (0-100)

- Fuzzy Sets: Can_Wait, See_Soon, Urgent, Emergency, Critical
- Membership Function: Triangular with trapezoidal endpoints

### Output Mapping

- **0-25**: Can Wait - See nurse within 60 minutes
- **26-50**: See Soon - See nurse within 30 minutes
- **51-75**: Urgent - See doctor within 15 minutes
- **76-90**: Emergency - See doctor immediately
- **91-100**: Critical - Activate trauma team

## Usage Example

```java
// Create and configure the system
PatientTriageSystem triageSystem = new PatientTriageSystem();

// Evaluate a patient
double urgencyScore = triageSystem.evaluateTriage(
    8.5,  // Pain Level
    39.5, // Temperature (°C)
    180   // Blood Pressure (mmHg)
);

String category = triageSystem.getUrgencyCategory(urgencyScore);
System.out.println("Urgency: " + urgencyScore);
System.out.println("Category: " + category);
```

## Project Structure

```
src/main/java/
├── fuzzy/
│   ├── membership/       # Membership function implementations
│   ├── variables/        # Linguistic variables and fuzzy sets
│   ├── operators/        # T-norms, S-norms, aggregation, implication
│   ├── inference/        # Inference engines (Mamdani, Sugeno)
│   ├── defuzzification/  # Defuzzification methods
│   ├── rules/            # Rule base management
│   └── system/           # Main FuzzyLogicSystem class
└── casestudy/
    └── PatientTriageSystem.java  # Case study implementation
```

## Design Principles

- **Clean Code**: Well-structured, maintainable code with appropriate separation of concerns
- **Extensibility**: Easy to add new membership functions, operators, or inference methods
- **Flexibility**: Configurable operators, inference engines, and defuzzification methods
- **Validation**: Input validation and error handling throughout

## Requirements Compliance

✅ Multiple membership function types (Triangular, Trapezoidal, Gaussian)
✅ Multiple linguistic variables (3 inputs, 1 output)
✅ Multiple fuzzy sets per variable
✅ Two inference engines (Mamdani, Sugeno)
✅ Multiple AND/OR operators (MIN/MAX, Product/Sum)
✅ Rule aggregation and implication operators
✅ Multiple defuzzification methods (Centroid, MOM)
✅ Rule base editor API
✅ Input validation and handling
✅ Complete evaluation pipeline with intermediate access
✅ Configurable defaults with user override capability
✅ Real-world case study (Patient Triage)
