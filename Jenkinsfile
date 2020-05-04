@Library(['private-pipeline-library', 'jenkins-shared@mvnw']) _

mavenPipeline(
  javaVersion: 'Java 7',
  useMvnw: true,
  usePublicSettingsFile: true,
  useEventSpy: false, 
  testResults: [ '**/target/*-reports/*.xml' ]
)
