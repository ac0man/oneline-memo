@echo off

if "%1" == "" goto RUN_MEMO
if NOT EXIST "%1" (
  if "%2" == "" goto RUN_WITH_ARG_E
  goto RUN_WITH_ARGS
)

:RUN_MEMO
java -jar memo.jar
exit /B

:RUN_WITH_ARG_E
java -jar memo.jar %1
exit /B

:RUN_WITH_ARGS
java -jar memo.jar %1 %2
exit /B
