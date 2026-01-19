# BugTracker 

## Overview - Manta Adrian-Gabriel 323CA
**BugTracker** is a modular ticket tracking system built as a learning-focused project around real-world workflow constraints.
The system is organized into clearly separated packages, each responsible for a specific part of the domain, with an emphasis on keeping logic readable and easy to extend as features evolved.

---

## Architecture & Package Structure
The entry point is `App.java`, covering command parsing and execution. The core logic is distributed across several packages:

- **`commands/`**: Implements the Command pattern. Each user action (e.g., reporting tickets, assigning, commenting, milestone management) is encapsulated as a command class.  
	- `CommandFactory` dynamically instantiates commands based on input. 

	- `CommandValidationStrategy` ensures role-based and contextual validation.
	
	- multiple packages containing implementations for different commands + utility classes and strategies for validation (in some cases created by leveraging the factory pattern);
		- **`advanced/`**: Contains searching algorithms for ticket and developer retrievals; notifications gathering + utility classes needed for displaying the results found. 

		- **`commentcommands/`**: Manages ticket comments, including addition and undo operations, enforcing business rules (e.g., restrictions on anonymous tickets).
		- **`context/`**: Defines the execution context for commands, including input parameters and result handling.
		- **`flow/`**: Handles workflow transitions (e.g., starting new testing phases, handling investor loss).
		- **`milestonecommands/`**: Manages the creation of a Milestone requested by the command
		- **`query/`**: Provides views for tickets, milestones, and assignments, tailored to user roles.
		- **`reports/`**: Implements metric calculations (customer impact, risk, efficiency) using extensible strategies, visitors for data retrieval across a group of objects sharing a common property, service classes contributing to the Open-Closed Principle and Single Responsibility Principle.
		- **`ticket/`**: Commands related to ticket assignment and deassignment to a certain developer, status modifying actions and history viewing.

- **`comment/`**: Encapsulates the individual comments in an object form that can be attached to tickets
- **`errors/`**: Implements validation and error-handling for command execution.
It leverages the 'Chain of Responsibility' to enforce an ordered validation flow for all commands in the system.
- **`milestone/`**:
	- **`Milestone`**: The main object of the package
	- **`MilestoneContainer`**: Singleton managing all milestones in the system, providing lookup and association utilities.
	- **`MilestoneService`**: Provides business logic for milestone operations.
	- **`MilestoneFactory`**: Responsible for constructing milestone instances from parsed data
	- **`state/`**: Implements the `State Design Pattern` for milestones. It defines how a milestone's behavior changes based on its current state(locked or unlocked).
- **`notification/`**: Centralizes notification generation and delivery, ensuring users are informed of significant events (e.g., milestone changes, ticket status updates).		
- **`parsing/`**: Utility classes for input parsing and validation
- **`ticket/`**: The backbone of the system, encapsulating all logic and data related to tickets. Defines shared properties and behaviors for all ticket types.
- **`users/`**: Models user roles (`Reporter`, `Developer`, `Manager`), including expertise, and seniority.

---

## Entity Interactions
- **Initialization:**
  - `App.java` initializes the system, loads data, and delegates to the command layer.
- **Command Cycle:**

  - **Parsing:** Input is parsed and mapped to a command via `CommandFactory`.

  - **Validation:** Each command runs through a layered validation strategy (`CommandValidationStrategy`).
  - **Execution:** The command executes, interacting with domain services classes and entities.
  - **Result Handling:** Results and errors are encapsulated in `CommandResult` and returned to the user.
- **Domain Management:**
  - `UserContainer` and `MilestoneContainer` provide efficient lookups.
  - `TicketContainer` is used for ticket management, supporting queries and state transitions.
- **Workflow & State:**
  - The system supports workflows, including milestone blocking/unblocking, phased testing...
- **Notifications:**
  - `NotificationManager` automatically dispatches notifications to users, decoupling notification logic from business actions.
- **History & Auditing:**
  - `HistoryManager` tracks all ticket actions and changes made during the whole process of development.
---

## Design Patterns
- **Command Pattern**: Simplifying extension and maintenance were the reasons I have chosen this pattern. Since for each command there is a vast logic and validation, creating sepparate objects would drastically improve readability and control.  

- **Factory Pattern**: Used for dynamic instantiation of commands, tickets, milestones, and users. Also combined with the creation of multiple user role-dependant validation strategies, command execution rules etc.
- **Strategy Pattern**: Powers flexible filtering and reporting, calculations using data and information retrieval based on the roles had by an user.
- **Observer**: Ensures users are notified of relevant events automatically 
- **Chain of Responsibility**: Each command has a common ground for validation. Since the order is important, creating sepparate handlers to either manage a certain validation or pass it to the next handler (as in `commands/Command#executeCommand()`) makes the choice obvious: mentaining a clean and modular design.
- **Visitor Pattern**: Used for data retrieval & calculation from different types of tickets during the report creation process.
- **State pattern**: Each milestone has different behaviors based on their state. By letting the state decide which action to be performed, the code would become less 'if-statement dependent'.
- **Singleton**: Used for the implementation of all containers (for milestones, tickets, users, history etc.). Provides common access to data, does not overload with multiple instances, provides ease of debugging.

## Advanced Java

- **Java Streams & Optionals & Generics**: Used for  data processing, reusability of the code and type-safety.  
- **LocalDate & ChronoUnit**: For date calculations, critical for milestone and ticket management.
- **Jackson Annotations**: For JSON serialization/deserialization, and custom date formats and property inclusion.

---

## Challenges & Solutions
- **Complex Role-Based Validation**: Implemented layered validation strategies to handle multiple permission checks, especially for developer seniority/expertise and milestone assignment.  
 
- **Extensible Metrics & Reports**: Adopted the Open-Closed Principle, using strategy interfaces for metrics, allowing new metrics to be added with minimal changes.  
- **Notification Scalability**: Decoupled notification logic from business actions  
- **Edge Case Handling**: Special attention to cases like ticket priority cases, milestone unblocking, and anonymous ticket restrictions.

---

# LLM usage
- Used LLMs	mostly for synthax and common methods for already existent classes (e.g. : 'What does `Optional<T> reduce()` do on a stream?', 'show usage examples for `computeIfPresent()`')

- Helpful provider of examples for Javadoc. Learnt how to better enhance a method's functionality by writing documentation.
- Checked for small pieces of code in case I thought of having too many responsibilities. Discussed my ideas in terms of refactoring some of the written code in a new method / class.
- Asked questions related the usage of design patterns in industry. Asked for more examples, other than the ones learnt during uni classes.
- Learnt about using multiple JSON annotations, such as `@JsonIgnore`, `@JsonInclude`, `@JsonAnyGetter`, `@JsonFormat`(for LocalDate, since i discovered issues regarding date format in the outputs), `@JsonProperty`.

## Final Thoughts
- Interesting scenario for a bug tracking system, clear explanations for the functionalities needed to be implemented (appreciated that often there were reminders or hints that would lead to questioning the type of design pattern to use / principle to follow)

- Good opportunity for learning & practicing the use of design patterns, as well as reminding other OOP principles such as OCP, SRP; the serialization part is also helpful, this time requesting further documentation in terms of annotations.

- Never thought of spending this much time into perfecting code and making sure the overall design is good :D