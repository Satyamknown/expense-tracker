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
                bat 'start /B java -Dserver.port=8083 -cp target\\classes com.expensetracker.ExpenseTracker'
                sleep 5
            }
        }

        stage('Selenium Test') {
            steps {
                bat 'mvn test -Dapp.url=http://localhost:8083'
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t satyam222006/expense-tracker:%BUILD_NUMBER% .'
                bat 'docker tag satyam222006/expense-tracker:%BUILD_NUMBER% satyam222006/expense-tracker:latest'
            }
        }

        stage('Docker Hub Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-pat',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    bat 'echo %DOCKER_PASSWORD% | docker login -u %DOCKER_USERNAME% --password-stdin'
                    bat 'docker push satyam222006/expense-tracker:%BUILD_NUMBER%'
                    bat 'docker push satyam222006/expense-tracker:latest'
                }
            }
        }
    }

    post {
        success {
            echo 'Expense Tracker DevOps pipeline completed successfully!'
        }

        failure {
            echo 'Pipeline failed. Check the console output.'
        }
    }
}