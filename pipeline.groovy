pipeline {
    agent {
    	docker {
    		image 'jenkins'
            args '--user root'
    	}
    }

    stages {
        stage('Setup') {
            steps {
                sh 'cat /etc/os-release'
                sh 'rm -rf markturn-BeanpoleGames'
                sh 'git clone https://github.com/UAComputerScience/markturn-BeanpoleGames.git'
                sh 'cd markturn-BeanpoleGames'
            }
           
        }
        stage('CMake'){
            steps{
                sh 'mkdir -p finalbuild'
                sh 'cd finalbuild; pwd; cmake ../markturn-BeanpoleGames -G Ninja'
            }
        }
        stage('Build') {
            steps {
                sh 'cd build; ninja'
            }
        }
        stage('Test') {
            steps {
                ctest installation: 'cmake', workingDir: 'finalbuild'
            }
        }
        stage('Package') {
            steps {
                cpack arguments: '-G DEB', installation: 'cmake', workingDir: 'finalbuild'
            }
        }
    }
}