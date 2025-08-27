@echo off
echo Criando JAR do Sistema ToDo List...

echo Compilando todas as classes...
javac -cp ".;lib\*" *.java modelo\*.java controle\*.java persistencia\*.java relatorios\*.java comunicacao\*.java telas\*.java

if %errorlevel% neq 0 (
    echo Erro na compilacao!
    pause
    exit /b 1
)

echo Criando JAR...
"C:\Program Files\Java\jdk-24\bin\jar.exe" cfm ToDoList.jar MANIFEST.MF *.class modelo\*.class controle\*.class persistencia\*.class relatorios\*.class comunicacao\*.class telas\*.class

if %errorlevel% equ 0 (
    echo JAR criado com sucesso: ToDoList.jar
    echo Para executar: java -jar ToDoList.jar
) else (
    echo Erro ao criar JAR!
)

pause 