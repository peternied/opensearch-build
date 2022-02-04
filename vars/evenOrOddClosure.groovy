// vars/evenOrOdd.groovy
def call(int buildNumber, Closure body) {
  if (buildNumber % 2 == 0) {
    pipeline {
      agent any
      stages {
        stage('Even Stage') {
          steps {
            echo "The build number is even"
            script {
              body()
            }
          }
        }
      }
    }
  } else {
    pipeline {
      agent any
      stages {
        stage('Odd Stage') {
          steps {
            echo "The build number is odd"
            script {
              body()
            }
          }
        }
      }
    }
  }
}