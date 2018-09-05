node {
agent  { label 'win' }
stage('Stage Checkout')
{
  checkout scm
 }
stage('Gradle Clean') {
     bat 'gradle clean'
  }
  stage('Gradle Build'){
   bat 'gradle build'
  }
  stage('End to End Test'){
      bat 'gradle task E2E'
    }
  stage('Reporting'){
      bat 'gradle allureReport'
  }

allure includeProperties: false, jdk: '', results: [[path: 'build\\allure-results']]
publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'build\\reports\\tests\\E2E', reportFiles: 'index.html', reportName: 'Gradle Report', reportTitles: ''])

}