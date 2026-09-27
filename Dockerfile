FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/expense-tracker-1.0-SNAPSHOT.jar app.jar

EXPOSE 8081

CMD ["java", "-cp", "app.jar", "com.expensetracker.ExpenseTracker"]