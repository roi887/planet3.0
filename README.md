Upgrade notes
=============

- Required JDK: Java 21 (LTS)
- Set `JAVA_HOME` to the JDK 21 installation directory and ensure `java -version` shows Java 21.

Quick setup (Windows PowerShell):

```powershell
choco install temurin-21-jdk -y    # or install from Adoptium/Microsoft
$env:JAVA_HOME = 'C:\Program Files\Java\temurin-21-jdk'
[Environment]::SetEnvironmentVariable('JAVA_HOME', $env:JAVA_HOME, 'Machine')
java -version
mvn -v
```
