# Design Document - Fuzzy Logic Library

## Architecture Overview

The library follows a modular, object-oriented design with clear separation of concerns:

### Package Structure

```
fuzzy/
├── membership/          # Membership function implementations
├── variables/           # Linguistic variables and fuzzy sets
├── operators/           # T-norms, S-norms, aggregation, implication
├── inference/           # Inference engines
├── defuzzification/     # Defuzzification methods
├── rules/               # Rule base management
└── system/              # Main system orchestrator
```

## Design Principles

### 1. Interface-Based Design

- Core abstractions (MembershipFunction, InferenceEngine, DefuzzificationMethod, etc.) are defined as interfaces
- Allows easy extension with new implementations
- Promotes loose coupling

### 2. Single Responsibility Principle

- Each class has a single, well-defined responsibility
- Membership functions only calculate membership degrees
- Operators only perform their specific operations
- Inference engines only handle inference logic

### 3. Open/Closed Principle

- System is open for extension (new membership functions, operators, etc.)
- Closed for modification (core interfaces remain stable)

### 4. Dependency Injection

- Operators and inference engines can be configured/swapped
- System accepts external implementations of interfaces

## Component Details

### Membership Functions

- **TriangularMF**: Three-point triangular function
- **TrapezoidalMF**: Four-point trapezoidal function
- **GaussianMF**: Gaussian bell curve function

All implement `MembershipFunction` interface with:

- `calculate(double x)`: Returns membership degree
- `getDomain()`: Returns domain bounds

### Linguistic Variables

- **FuzzySet**: Wraps a membership function with a name
- **LinguisticVariable**: Contains multiple fuzzy sets, manages domain

Features:

- Input validation and clamping
- Domain checking
- Fuzzy set lookup by name

### Operators

#### T-norms (AND)

- **MinTNorm**: Minimum operator
- **ProductTNorm**: Product operator

#### S-norms (OR)

- **MaxSNorm**: Maximum operator
- **SumSNorm**: Bounded sum operator

#### Aggregation

- **MaxAggregation**: Maximum aggregation

#### Implication

- **MinImplication**: Minimum implication
- **ProductImplication**: Product implication

### Inference Engines

#### Mamdani Inference

- Uses fuzzy sets in consequent
- Requires defuzzification
- Supports implication operators
- Aggregates multiple rule outputs

#### Sugeno Inference

- Uses crisp values in consequent
- Zero-order: constant values
- First-order: linear combination (extensible)
- Weighted average output (no defuzzification needed)

### Defuzzification Methods

#### Centroid (Center of Gravity)

- Numerically integrates the aggregated membership function
- Computes weighted average
- Most commonly used method

#### Mean of Maximum (MOM)

- Finds all points with maximum membership
- Returns average of these points
- Simpler but less smooth than centroid

### Rule Base

- **Rule**: Represents IF-THEN rule with:

  - Multiple antecedent conditions (AND/OR connected)
  - Single consequent
  - Enable/disable flag
  - Weight (0-1)

- **RuleBase**: Manages collection of rules
  - CRUD operations
  - Enable/disable rules
  - Set rule weights
  - Filter enabled rules

### Fuzzy Logic System

Main orchestrator that:

1. Manages input/output variables
2. Maintains rule base
3. Configures operators and inference engine
4. Executes complete evaluation pipeline:
   - Fuzzification
   - Inference
   - Aggregation (for Mamdani)
   - Defuzzification (for Mamdani)

## Case Study: Patient Triage System

### Problem Domain

Emergency department patient prioritization based on:

- Pain Level (0-10)
- Body Temperature (35-42°C)
- Blood Pressure (60-200 mmHg)

### Design Choices

#### Membership Functions

- **Triangular** for most sets: Simple, intuitive, computationally efficient
- **Trapezoidal** for "Normal" ranges: Better represents the plateau of normal values
- **Trapezoidal endpoints** for output: Smooth transitions at boundaries

#### Fuzzy Set Distribution

- 5 sets per variable: Provides good granularity without complexity
- Overlapping sets: Ensures smooth transitions
- Domain coverage: All sets cover their respective domains

#### Rule Base

- 15+ rules covering critical combinations
- Rules prioritize based on severity indicators
- Multiple rules can fire simultaneously (aggregation handles this)

#### Output Mapping

- 0-100 scale: Provides fine-grained urgency scoring
- 5 categories: Matches clinical triage levels
- Clear action items for each category

### Justification

1. **Triangular for Pain/Temperature/BP extremes**: These represent clear severity levels where triangular functions capture the concept well.

2. **Trapezoidal for Normal ranges**: Normal values have a wider acceptable range, making trapezoidal more appropriate.

3. **Rule complexity**: Rules use AND operators to ensure all conditions must be met, reflecting that triage considers multiple factors together.

4. **Defuzzification choice**: Centroid provides smooth, continuous output suitable for urgency scoring.

## Extensibility

The library can be extended with:

- New membership functions (implement MembershipFunction)
- New operators (implement TNorm, SNorm, etc.)
- New inference methods (implement InferenceEngine)
- New defuzzification methods (implement DefuzzificationMethod)
- Custom rule evaluation strategies

## Performance Considerations

- Membership function calculations are O(1)
- Fuzzification is O(n\*m) where n=inputs, m=fuzzy sets per input
- Inference is O(r\*c) where r=rules, c=conditions per rule
- Defuzzification is O(s) where s=samples (default 1000)

Overall complexity is reasonable for real-time applications.
