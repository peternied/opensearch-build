void call(Closure body) {
    return  pipeline {
        stages{
            stage("Stage") {
                steps {
                    script {
                        echo "hello"
                        body()
                    }
                }
            }
        }
        post {
            always {
                script {
                    postCleanup() // Update to also do docker cleanup too
                }
            }
        }
    }
}