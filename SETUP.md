# Setup Instruction
## Ubuntu 20.04
* Install git:
```apt update && apt install git```
* Clone project ```git clone https://github.com/profs-3301/WarehouseScanner ```
* Install Gradle:
```sudo apt install -y openjdk-11-jdk && mkdir /opt/gradle && wget https://services.gradle.org/distributions/gradle-7.0.2-bin.zip -P /tmp && sudo unzip -d /opt/gradle /tmp/gradle-7.0.2-bin.zip && echo "PATH=$PATH:/opt/gradle/gradle-7.3.2/bin" >> ~/.profile && source ~/.profile```
* Install Android SDK:
```https://developer.android.com/studio```
* Open project and sync project with gradle file.
