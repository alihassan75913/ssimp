
Smart Service Support and Issue Management Platform
auther: Ali Hassan, COMP495
Building a small website where users can report technical problems and track their support tickets.

Progress
The login work for October 1 to 15 is finished. The registration, login, and logout are working with MySQL. Passwords are stored as BCrypt hashes, and new accounts get the USER role.
I've manually checked wrong passwords, duplicate email addresses, and access for user, support agent, and administrator accounts. Each role can open its own test page and is blocked from the other roles' test pages.
Everyone still lands on the same basic home page after logging in. The ticket pages are next.

Built with
Java 17, Spring Boot, Spring Security, Spring Data JPA, Thymeleaf, MySQL 8.4, and Maven Wrapper.

Run on Windows
1.	Install Java 17 and MySQL 8.4, then clone or download the repository.
2.	Run [setup.sql](database/setup.sql)
3.	For a new setup, create the database account below. You can replace the password placeholder in Workbench, 
CREATE USER 'ssimp_app'@'127.0.0.1'
    IDENTIFIED BY 'REPLACE_WITH_YOUR_OWN_PASSWORD';
GRANT SELECT, INSERT, UPDATE, DELETE
    ON ssimp.* TO 'ssimp_app'@'127.0.0.1';
The database connection uses port 3307. If yours is different, change it in src/main/resources/application.properties.
Open PowerShell in the folder containing pom.xml and run:
$ssimpCredential = Get-Credential -UserName "ssimp_app" -Message "Enter your SSIMP database password"
$env:SSIMP_DB_PASSWORD = $ssimpCredential.GetNetworkCredential().Password
.\mvnw.cmd spring-boot:run
Enter the ssimp_app database password when asked. Once the app starts, open localhost:8080/register. Use your registered email in the Username field when logging in.
The role test pages are /user/check, /agent/check, and /admin/check. Each requires an account with the matching role.

October 16–31
Next, I'll add the ticket form, checks for missing or invalid entries, troubleshooting suggestions, and pages where users can view their own tickets.

