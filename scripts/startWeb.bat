set MAIN_DIR=@SOFTWARE_PATH@
cd %MAIN_DIR%\Web
java -jar -Xmx2G -Xms2G -Dagrona.disable.bounds.checks=true  -XX:+UseG1GC -XX:+UseLargePages -XX:+OptimizeStringConcat -d64 -server -XX:+UseStringDeduplication -XX:+UseCondCardMark -XX:+UnlockDiagnosticVMOptions -XX:GuaranteedSafepointInterval=300000 Web-1.0.jar > %MAIN_DIR%\Web.log &