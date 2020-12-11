pipeline {
    agent {
        docker {
            image 'jpp52'
            args '--user root'
        }
    }

    stages {
        stage('Setup') {
            steps {
                git branch: 'main', credentialsId: 'GitHub', url: 'https://github.com/UAComputerScience/markturn-BeanpoleGames.git'
            }
        }
        stage('CMake') {
            steps {
                cmakeBuild buildDir: 'build', cmakeArgs: '-D LINK_STATIC=OFF', generator: 'Ninja', installation: 'cmake'
            }
        }
        stage('Build') {
            steps {
                sh 'cd build; ninja'
            }
        }
        stage('Test') {
            steps {
                ctest installation: 'cmake', workingDir: 'build'
            }
        }
        stage('Package') {
            steps {
                cpack arguments: '-G DEB', installation: 'cmake', workingDir: 'build'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'build/*.deb', onlyIfSuccessful: true
        }
    }
}