@echo off
echo ========================================
echo    Sistema ToDoList - Gradle Build
echo ========================================
echo.

echo Compilando projeto...
call gradlew build

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERRO: Falha na compilacao!
    pause
    exit /b 1
)

echo.
echo Compilacao concluida com sucesso!
echo.
echo Escolha uma opcao:
echo 1 - Executar aplicacao console
echo 2 - Executar interface grafica
echo 3 - Gerar JAR executavel
echo 4 - Executar testes
echo.
set /p opcao="Digite sua opcao (1-4): "

if "%opcao%"=="1" (
    echo.
    echo Executando aplicacao console...
    call gradlew run
) else if "%opcao%"=="2" (
    echo.
    echo Executando interface grafica...
    call gradlew runGUI
) else if "%opcao%"=="3" (
    echo.
    echo Gerando JAR executavel...
    call gradlew jar
    echo JAR gerado em: build\libs\
) else if "%opcao%"=="4" (
    echo.
    echo Executando testes...
    call gradlew test
) else (
    echo Opcao invalida!
)

echo.
pause