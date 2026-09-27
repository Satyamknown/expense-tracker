pipeline {

    agent any

    stages {

        stage('Checkout') {
            steps {
                echo 'Checking out Expense Tracker project...'
            }
        }

        stage('Maven Build') {
            steps {
                bat 'mvn clean package'
            }
        }

        stage('Start Application') {
            steps {
                bat 'start /B java -cp target\\classes com.expensetracker.ExpenseTracker'
                sleep 5
            }
        }

        stage('Selenium Test') {
            steps {
                bat 'mvn test'
            }
        }

    }

    post {

        success {
            echo 'Expense Tracker pipeline completed successfully!'
        }

        failure {
            echo 'Pipeline failed. Check the console output.'
        }

    }
}