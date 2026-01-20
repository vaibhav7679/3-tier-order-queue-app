pipeline {
  agent any
  tools {
    maven 'Maven3'   // <-- replace with your Maven name in Jenkins config
  }
  stages {
    stage('Checkout') {
      steps { checkout scm }
    }
    stage('Build with Maven') {
      steps {
        sh 'mvn -f backend/pom.xml clean package -DskipTests'
        sh 'mvn -f worker/pom.xml clean package -DskipTests'
      }
    }
    stage('Build Docker Images') {
      steps {
        sh 'docker-compose build'
      }
    }
    stage('Deploy Locally') {
      steps {
        sh 'docker-compose up -d'
      }
    }
  }
}
