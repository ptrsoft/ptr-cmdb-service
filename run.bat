@echo off
echo Starting cmdb

java -Djdk.io.File.enableADS=true -Dserver.profile=%1 -Ddb.server.host=%2 -Ddb.server.port=%3 -Ddb.schema=%4 -Ddb.user=%5 -Ddb.password=%6 -Djdk.tls.acknowledgeCloseNotify=true -Xms1g -Xmx1g -XX:NewRatio=1 -XX:+ResizeTLAB -XX:+UseConcMarkSweepGC -XX:+CMSConcurrentMTEnabled -XX:+CMSClassUnloadingEnabled -XX:-OmitStackTraceInFastThrow -jar core-service-impl\target\core-service-impl-0.0.1-SNAPSHOT.jar