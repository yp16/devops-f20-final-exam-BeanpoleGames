pipeline {
	// Add ansicolor to make console output more readable
    options {
        ansiColor('xterm')
    }
    agent {
        docker {
            image 'jpp52'
            args '--user root'
        }
    }

    stages {
        stage('Setup') {
            steps {
            	echo '\033[33mSet up and fetch repository from GitHub\033[0m'
                git branch: 'main', credentialsId: 'GitHub', url: 'https://github.com/UAComputerScience/markturn-BeanpoleGames.git'
            }
        }
        stage('CMake') {
            steps {
            	echo '\033[33mCreate build directory and run CMake\033[0m'
                cmakeBuild buildDir: 'build', cmakeArgs: '-D LINK_STATIC=OFF', generator: 'Ninja', installation: 'cmake'
            }
        }
        stage('Build') {
            steps {
            	echo '\033[33mRun ninja to build\033[0m'
                sh 'cd build; ninja'
            }
        }
        stage('Test') {
            steps {
            	echo '\033[33mRun ctest to test program\033[0m'
                ctest installation: 'cmake', workingDir: 'build'
            }
        }
        stage('Package') {
            steps {
            	echo '\033[33mCreate .deb package using cpack\033[0m'
                cpack arguments: '-G DEB', installation: 'cmake', workingDir: 'build'
            }
        }
    }

    post {
    	// Archive the .deb package on success of stages
        success {
        	echo '\033[33mArchive .deb package\033[0m'
            archiveArtifacts artifacts: 'build/*.deb', onlyIfSuccessful: true
        }
    }
}
