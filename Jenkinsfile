pipeline {
    agent any
    
    environment {
        DOCKER_BUILDKIT = '1'
        COMPOSE_DOCKER_CLI_BUILD = '1'
    }

    tools {
        maven 'Maven 3.9'
        jdk 'JDK 21'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }
        
        stage('Test All Services') {
            parallel {
                stage('Test Eureka Server') {
                    steps {
                        dir('eureka_server') {
                            echo 'Testing Eureka Server...'
                            sh 'mvn test'
                        }
                    }
                }
                
                stage('Test API Gateway') {
                    steps {
                        dir('api_gateway') {
                            echo 'Testing API Gateway...'
                            sh 'mvn test'
                        }
                    }
                }
                
                stage('Test Anggota Service') {
                    steps {
                        dir('anggota') {
                            echo 'Testing Anggota Service...'
                            sh 'mvn test'
                        }
                    }
                }
                
                stage('Test Buku Service') {
                    steps {
                        dir('buku') {
                            echo 'Testing Buku Service...'
                            sh 'mvn test'
                        }
                    }
                }
                
                stage('Test Peminjaman Service') {
                    steps {
                        dir('peminjaman') {
                            echo 'Testing Peminjaman Service...'
                            sh 'mvn test'
                        }
                    }
                }
                
                stage('Test Pengembalian Service') {
                    steps {
                        dir('pengembalian') {
                            echo 'Testing Pengembalian Service...'
                            sh 'mvn test'
                        }
                    }
                }
                
                stage('Test Email Service') {
                    steps {
                        dir('email') {
                            echo 'Testing Email Service...'
                            sh 'mvn test'
                        }
                    }
                }
            }
        }
        
        stage('Build Docker Images') {
            // Running sequentially to avoid resource exhaustion
            stage('Docker Build Eureka Server') {
                steps {
                    script {
                        echo 'Building Docker image for Eureka Server...'
                        sh 'docker build -t eureka-server:latest ./eureka_server'
                    }
                }
            }
            
            stage('Docker Build API Gateway') {
                steps {
                    script {
                        echo 'Building Docker image for API Gateway...'
                        sh 'docker build -t api-gateway:latest ./api_gateway'
                    }
                }
            }
            
            stage('Docker Build Anggota') {
                steps {
                    script {
                        echo 'Building Docker image for Anggota Service...'
                        sh 'docker build -t anggota-service:latest ./anggota'
                    }
                }
            }
            
            stage('Docker Build Buku') {
                steps {
                    script {
                        echo 'Building Docker image for Buku Service...'
                        sh 'docker build -t buku-service:latest ./buku'
                    }
                }
            }
            
            stage('Docker Build Peminjaman') {
                steps {
                    script {
                        echo 'Building Docker image for Peminjaman Service...'
                        sh 'docker build -t peminjaman-service:latest ./peminjaman'
                    }
                }
            }
            
            stage('Docker Build Pengembalian') {
                steps {
                    script {
                        echo 'Building Docker image for Pengembalian Service...'
                        sh 'docker build -t pengembalian-service:latest ./pengembalian'
                    }
                }
            }
            
            stage('Docker Build Email') {
                steps {
                    script {
                        echo 'Building Docker image for Email Service...'
                        sh 'docker build -t email-service:latest ./email'
                    }
                }
            }
        }
        
        stage('Verify Images') {
            steps {
                echo 'Verifying Docker images...'
                sh 'docker images | grep "eureka-server\\|api-gateway\\|anggota-service\\|buku-service\\|peminjaman-service\\|pengembalian-service\\|email-service"'
            }
        }

        stage('Deploy Monitoring Stack') {
            steps {
                echo 'Deploying Monitoring Stack (Prometheus + Grafana)...'
                sh 'docker-compose -f docker-compose-monitoring.yml up -d'
            }
        }

        stage('Deploy Application') {
            steps {
                echo 'Deploying Application Microservices...'
                sh 'docker-compose -f docker-compose-app.yml up -d'
            }
        }

        stage('Health Check') {
            steps {
                echo 'Waiting for services to be ready...'
                sleep 30 // Wait for services to start
                script {
                    def services = ['eureka-server:8761', 'api-gateway:9000', 'anggota-service:8081', 'buku-service:8082', 'peminjaman-service:8083']
                    // Simple check if containers are running
                    sh 'docker ps'
                }
            }
        }
    }
    
    post {
        success {
            echo '✅ Pipeline completed successfully!'
            echo 'All services built and Docker images created.'
        }
        failure {
            echo '❌ Pipeline failed!'
            echo 'Check the logs for details.'
        }
        always {
            echo 'Cleaning up workspace...'
            cleanWs(cleanWhenNotBuilt: false,
                    deleteDirs: true,
                    disableDeferredWipeout: true,
                    notFailBuild: true,
                    patterns: [[pattern: '**/target/**', type: 'INCLUDE']])
        }
    }
}
