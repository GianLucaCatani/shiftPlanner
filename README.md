# ShiftPlanner

The application allows coordinators to automatically generate work shift schedules for employees, taking into account weekly contractual hours, absences, and employee preferences. Employees can log in to view their assigned shifts and notifications.

## Implemented Use Cases

**Schedule Generation:** Developed the use case for automatically generating shift schedules, including configurable periods, manual adjustments by the coordinator, and final publication with employee notifications.

**User Login:** Users (coordinators and employees) can log in to access their role-specific dashboard. 

**Employee Dashboard:** Employees can view their assigned shifts and notifications after the coordinator publishes a schedule.

## Technical Implementation

### Programming and Design Principles

**Object-Oriented Programming (OOP):** The application is designed using OOP principles to ensure modularity and reusability.

**UML and Software Engineering:** Utilizes UML diagrams for modeling and software engineering best practices for development.

**Design Patterns:** Implements the following design patterns:
- **Abstract Factory** — dynamic selection of the persistence layer at runtime
- **Observer** — automatic employee notifications upon schedule publication
- **MVC** — clean separation between model, view, and controller layers
### Persistence and Data Management

**In-Memory (Demo Mode):** The application can run in demo mode, storing all data in RAM without any persistence. Useful for quick testing with no setup required.  

**File System:** The application can store data using CSV files in the local `data/` directory.

**Relational Database:** Alternatively, the application can use a MySQL relational database to store and manage data.

The persistence mode can be configured by modifying `src/main/resources/config.properties`. Set `app.mode.demo` to `true` for demo mode, or `false` for persistent storage. When using persistent storage, set `app.persistence.type` to either `db` for database persistence or `fs` for file system persistence.

## User Interface

**JavaFX:** The graphical user interface is designed using JavaFX, providing a rich and interactive user experience.  

**Command Line Interface (CLI):** An alternative terminal-style interface is also available.

At startup, the application prompts the user to choose between the JavaFX interface (1) or the CLI (2).

## Directory Structure

`src/main/java/com/shiftplanner/`:
- `bean/` — Data Transfer Objects (DTOs) used to exchange data between layers
- `controller/` — Application controllers and GUI controllers (both FX and CLI)
- `dao/` — Data Access Object interfaces and implementations (DB, FileSystem, InMemory)
- `engineering/` — Core services: `ScheduleGeneratorEngine`, `NotificationManager`
- `exceptions/` — Custom exception classes
- `main/` — Application entry points: `Main.java`, `MainFX.java`
- `model/` — Domain model classes
- `util/` — Utility classes: `ConfigLoader`, `DemoDataLoader`
- `view/` — CLI views and JavaFX FXML files
  
`src/main/resources/`:
- `config.properties` — Configuration file for persistence mode and type
- `db.properties` — MySQL connection parameters *(not committed to version control)*
- `logging.properties` — Logging configuration

`data/`:
- CSV files used for file system persistence

`src/test/java/com/shiftplanner/`:
- Unit and integration tests
- 
## Setup Instructions

**Configure the Application:**
Open `src/main/resources/config.properties` and set the desired mode:

```properties
# Demo mode (no setup required)
app.mode.demo=true

# Database persistence
app.mode.demo=false
app.persistence.type=db

# File system persistence
app.mode.demo=false
app.persistence.type=fs
```

**Set Up MySQL Database (only for `app.persistence.type=db`):**

Ensure MySQL is installed. Create the file `src/main/resources/db.properties`:
```properties
CONNECTION_URL=jdbc:mysql://localhost:3306/shiftplanner
USER=your_database_user
PASS=your_database_password
```

Then run the SQL script `create_users_table.sql` to create and populate the tables.

**Build and Run the Application**
## Demo Credentials

```
| Role        | Username | Password |

|-------------|----------|----------|

| Coordinator | admin    | admin    |

| Employee    | mario    | mario    |

| Employee    | anna     | anna     |

| Employee    | luca     | luca     |

| Employee    | sara     | sara     |

| Employee    | paolo    | paolo    |
```
