@echo off
setlocal EnableExtensions

REM --- Configuration ---
set "REPO_URL=https://github.com/Senti81/kniffel-app.git"
set "TARGET_FOLDER=kniffel-app"
set "TARGET_FILE=kniffel.jar"

for /f %%a in ('echo prompt $E ^| cmd') do set "ESC=%%a"

set "GREEN=%ESC%[32m"
set "RESET=%ESC%[0m"

REM --- Always run from this script's directory (important for double-click) ---
cd /d "%~dp0"

echo %GREEN%[STEP 1 / 3] Cloning the repository from %REPO_URL%...%RESET%

REM --- Basic checks ---
where git >nul 2>&1 || (echo ERROR: git not found in PATH.& goto :fail)

REM --- Clone (fail if folder already exists) ---
if exist "%TARGET_FOLDER%\" (
  echo ERROR: Target folder "%TARGET_FOLDER%" already exists. Delete it first or choose another.
  goto :fail
)

git clone "%REPO_URL%" "%TARGET_FOLDER%"
if errorlevel 1 (echo ERROR: git clone failed.& goto :fail)

REM --- Build ---
pushd "%TARGET_FOLDER%" || (echo ERROR: cannot cd into "%TARGET_FOLDER%".& goto :fail)

if not exist "mvnw.cmd" (
  echo ERROR: mvnw.cmd not found in "%TARGET_FOLDER%".
  popd
  goto :fail
)

echo %GREEN%[STEP 2 / 3] Build jar package%RESET%

call ".\mvnw.cmd" clean package
if errorlevel 1 (
  echo ERROR: Maven build failed.
  popd
  goto :fail
)

popd

echo %GREEN%[STEP 3 / 3] Copy target file%RESET%

set "JAR_FOUND="
for %%F in ("%TARGET_FOLDER%\target\kniffel-*.jar") do (
  set "JAR_FOUND=1"
  echo Copying "%%~fF" to "%TARGET_FILE%"
  copy /Y "%%~fF" "%TARGET_FILE%" >nul
  if errorlevel 1 (echo ERROR: copying failed.& goto :fail)
  goto :afterCopy
)

:afterCopy
if not defined JAR_FOUND (
  echo ERROR: No JAR found matching "%TARGET_FOLDER%\target\kniffel-*.jar"
  goto :fail
)

echo Copy successful.

REM --- Cleanup ---
echo Removing "%TARGET_FOLDER%" folder...
rmdir /s /q "%TARGET_FOLDER%"
if errorlevel 1 (echo WARNING: Could not remove "%TARGET_FOLDER%".)

echo Done.
exit /b 0

:fail
echo.
echo FAILED.
REM Optional: cleanup on failure if clone happened
REM if exist "%TARGET_FOLDER%\" rmdir /s /q "%TARGET_FOLDER%"
exit /b 1
