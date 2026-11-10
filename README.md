# SSIMP

Smart Service Support and Issue Management Platform  
Ali Hassan COMP495

## October 16–30

I worked on the ticket form and the pages for viewing tickets. Users can now submit a ticket, see their own tickets in a list, and open one to read the details and check its status. The tickets are saved in MySQL.

The form asks for a title, category, priority, and description. I also added troubleshooting tips for Internet, Email, Printer, and Account Access problems. These are saved tips from the database. I haven't added the local AI part yet.

I tested submitting tickets and leaving fields empty. I also used a second account to check that it couldn't see or open the first account's tickets.

## Next

I'll work on the support agent pages so agents can take a ticket, change its status, and add notes and a solution.

## Running the project

The project uses Java 17, Spring Boot, Thymeleaf, and MySQL 8.4.

1. Download or clone the project.
2. Run [database/setup.sql](database/setup.sql) in MySQL Workbench using an administrator account.
3. For a new setup, create the database account below. Replace the password placeholder in Workbench with your own password.

```sql
CREATE USER 'ssimp_app'@'127.0.0.1'
    IDENTIFIED BY 'REPLACE_WITH_YOUR_OWN_PASSWORD';
GRANT SELECT, INSERT, UPDATE, DELETE
    ON ssimp.* TO 'ssimp_app'@'127.0.0.1';
```

My database uses port `3307`. The connection settings are in `src/main/resources/application.properties`.

Open PowerShell in the project folder, where `pom.xml` is saved, and run:

```powershell
$ssimpCredential = Get-Credential -UserName "ssimp_app" -Message "Enter your SSIMP database password"
$env:SSIMP_DB_PASSWORD = $ssimpCredential.GetNetworkCredential().Password
.\mvnw.cmd spring-boot:run
```

Enter the database password when asked. Once the app starts, open [localhost:8080/register](http://localhost:8080/register) to create an account. Use that account's email and password to log in.
