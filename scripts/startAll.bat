set MAIN_DIR=@SOFTWARE_PATH@
cd %MAIN_DIR%\scripts

.\startLowLatencyDriver.bat
.\startTGDriver.bat
.\startMDGDriver.bat
.\startNativeGateway.bat
.\startMatchingEngine.bat
.\startMarketDataGateway.bat
sleep 10
.\startWeb.bat

