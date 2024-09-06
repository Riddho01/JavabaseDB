
# JavabaseDB
JavabaseDB is a prototype Database Management System (DBMS) implemented using Java. It supports core operations such as creating databases and tables, inserting data, and executing SQL queries. JavabaseDB offers a hands-on approach to understanding database management concepts with built-in user authentication and data handling features.

## Features
- **User Authentication**: Sign up and log in with credentials and CAPTCHA for secure access.
- **SQL Command Execution**: Run SQL commands directly through the command line interface.
- **Transaction Management**: Use commands like `BEGIN`, `COMMIT`, `ROLLBACK`, and `SAVEPOINT` to manage database transactions efficiently.

## Getting Started

### Prerequisites
Before you begin, ensure you have the following installed:

- **Java Development Kit (JDK):** Version 8 or higher. You can download it from the [official Oracle website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or use an open-source version such as OpenJDK.
- **Java-compatible Integrated Development Environment (IDE):** Such as IntelliJ IDEA, Eclipse, or any other IDE that supports Java development.

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/Riddho01/your-repository.git
   ```
2. Navigate to the project directory:
   ```bash
   cd your-repository
   ```
3. Open the project in the IDE of your choice.
4. Run the `Main.java` file to start JavabaseDB.

## Usage
1. Start by signing up with a new account using the provided options on the screen.
2. Log in with your credentials and complete the captcha verification.
3. Once logged in, you can begin executing SQL commands directly in the console.

## Supported Operations

### Data Definition Language (DDL)
* **Create a Database**
  * `CREATE DATABASE <DATABASE_NAME>`  
    * Note: Only one database per user can be created.
* **Create a Table**
  * `CREATE TABLE <TABLE_NAME> (column1 datatype, column2 datatype, ...)`

![DDL](https://github.com/user-attachments/assets/1cbce8d3-89e4-4374-acd6-5eba089fe1fa)

### Data Manipulation Language (DML)
* **Insert Data**
  * `INSERT INTO <TABLE_NAME> VALUES (<value1>, <value2>, ...)`

    ![Insert](https://github.com/user-attachments/assets/9f5deab9-5a92-4fcd-81b4-12af2cd291a1)
* **Query Data**
  * **Retrieve All Columns**
    * `SELECT * FROM <TABLE_NAME>`
    ![selectAll](https://github.com/user-attachments/assets/75706868-8b23-42ef-9f59-d14c4d8c7a9d)
  * **Retrieve Specific Columns**
    * `SELECT <col1>, <col2>, ... FROM <TABLE_NAME>`

    ![selectCols](https://github.com/user-attachments/assets/c09befb1-5737-46e4-9d05-dd32bc3dfea3)
  * **Conditional Queries**
    * **Retrieve All Columns with Condition**
      * `SELECT * FROM <TABLE_NAME> WHERE <column> <operator> <value>`
    ![selectAllCond](https://github.com/user-attachments/assets/cae6a14e-fe15-4b70-aacc-36151946b4da)
    * **Retrieve Specific Columns with Condition**
      * `SELECT <col1>, <col2>, ... FROM <TABLE_NAME> WHERE <column> <operator> <value>`
    ![selectCond](https://github.com/user-attachments/assets/cc9c877c-d59e-413a-bf4b-9ccb4c8ae318)

### Transactions
* `BEGIN TRANSACTION` - Start a new transaction.
* `COMMIT` - Save all changes made during the transaction.
* `ROLLBACK` - Revert all changes made during the transaction.
* `SAVEPOINT <SAVEPOINT_NAME>` - Set a savepoint within the transaction.

### Supported Data Types
* `INT` - Integer data type.
* `VARCHAR` - Variable-length character string.
* `DECIMAL` - Decimal number.

**Note:** For all supported data types, you do not need to specify size, length, precision, or scale.

## Data Storage Format

JavabaseDB organizes user data into a straightforward directory structure:

- Each user has a dedicated folder within the `data/Users` directory.
- When a database is created, it appears as a subdirectory within the user’s folder.
  ![DataStorage](https://github.com/user-attachments/assets/28018ddf-8a53-4fd3-9965-6d6cf39c472f)
- For each table in the database:
  - A **metadata file** (JSON) contains the schema information, such as column names and data types.
  ![Metadata](https://github.com/user-attachments/assets/296e4774-b027-4b9e-ac81-9b29e7201945)
  - A **data file** (CSV) stores the actual table rows.
  ![tbdata](https://github.com/user-attachments/assets/590bb022-911a-4b36-81ab-32b348fcdfb5)
![Database Directory Structure](https://miro.medium.com/v2/resize:fit:1400/1*0KFB17_NGTPB0XWyc4BSgQ.jpeg)
