@echo off
echo Compilando o projeto...
javac -cp ".;lib\*" *.java modelo\*.java controle\*.java persistencia\*.java relatorios\*.java comunicacao\*.java telas\*.java
if %errorlevel% equ 0 (
    echo Compilacao concluida com sucesso!
    echo Para executar console: java -cp ".;lib\*" Main
) else (
    echo Erro na compilacao!
)
pause 