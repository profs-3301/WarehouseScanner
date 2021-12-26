# Setup Instruction
## Ubuntu 20.04

### Install git:
```bash
sudo apt update && \
sudo apt upgrade && \
sudo apt install git
```

### Clone project
```bash
git clone https://github.com/profs-3301/WarehouseScanner
```

### Install Gradle
  ```bash
  sudo apt install -y openjdk-11-jdk && \
  sudo mkdir /opt/gradle && \
  wget https://services.gradle.org/distributions/gradle-7.0.2-bin.zip -P /tmp && \
  sudo unzip -d /opt/gradle /tmp/gradle-7.0.2-bin.zip && \
  echo "PATH=\$PATH:/opt/gradle/gradle-7.0.2/bin" >> ~/.profile && \
  source ~/.profile
  ```
### Setup Android Studio:
* Download it from <https://developer.android.com/studio>
* Install and Run Android Studio
```bash
  cd /opt && \
  sudo tar -xvf ~/Downloads/android-studio-XXXX.X.X.XX-linux.tar.gz && \
  ./android-studio/bin/studio.sh
```

### Install Android SDK
* Open **Tools > SDK Manager > SDK Platforms**
* In Android version list select the one where *API Level 31*
* Check the box and enter ***'Apply'***
* Confirm and download


### Add Virtual Device in Android Studio
* Open **Tools > AVD Manager > Create Virtual Device**
* Select any virtual smartphone (i.e Pixel XL)
* Select Android Q (If it's not downloaded yet - then download it)
* Select newly created Device
