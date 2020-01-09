<h1>Cloudera Installation</h1>

- [1.1  Prerequisites](#11-prerequisites)
   - [1.1.1 System Requirements](#111-system-requirements)
   - [1.1.2  Install Java](#112-install-java)
   - [1.1.3  Configuring SSH](#113-configuring-ssh)
   - [1.1.4  Setting Static ip](#114-setting-static-ip)
   - [1.1.5  FQDN](#115-fqdn) 
   - [1.1.6  Upadate Hostnames](#116-upadate-hostnames)
   
- [1.2 ClouderaManager Configuration](#12-installation)
     - [1.2.1   Install Cloudera Manager](#121-install-cloudera-manager)





##  1.1 Prerequisites

### 1.1.1 System Requirements

| Operating System |Ubuntu 14.04 64bit (Recommended)|
|------            |-------                         | 
|      RAM         |         4GB +                  |
|      HDD         |         40GB +                 |

### 1.1.2 Install Java
cloudera requires minimum 1.7+ (jdk Java 7) installation. Check if java is already installed or install it using  apt-get command in the terminal everything should be done in rooy user. We have used Oracle Java-8.


**Step 1 : Clone 'ppa:webupd8team/java' repository, Update Source List and install Java 8**

```
 add-apt-repository ppa:webupd8team/java

 apt-get update 

 apt-get install oracle-java8-installer
```
Check if Java is correctly installed using **step 2** command.

**Step 2 : Check if Java is Installed**
```
java -version
```
![](Images/Cloudera/javaver.png)




### 1.1.3 Configuring SSH:
To work seamlessly, SSH needs to be setup to allow password-less login for the user from machines in the cluster. The simplest way to achive this is to generate a public/private key pair, and it will be shared across the cluster. 
cloudera requires SSH access to manage its nodes, i.e. remote machines plus your local machine. 

**Step 1 : Generate an SSH key **
```
ssh-keygen -t rsa -P ""
```
![](Images/Cloudera/sshkey.png)

**Note:**
*  P “”, here indicates an empty password.
* If asked for a filename just leave it blank and press the enter key to continue.

**Step 2 : Enable SSH access to your local machine with this newly created key which is done by the following command.**
```
 cat $HOME/.ssh/id_rsa.pub >> $HOME/.ssh/authorized_keys
```
**Step 3 : The final step is to test the SSH setup by connecting to the local machine with the root user.**
```
ssh localhost
```
![](Images/Cloudera/sshlocal.png)

If the SSH connection fails, we can try the following (optional): 
* Enable debugging with ssh -vvv localhost and investigate the error in detail. 
* Check the SSH server configuration in /etc/ssh/sshd_config. If you made any changes to the SSH server configuration file, you can force a configuration reload with sudo /etc/init.d/ssh reload.

### 1.1.4 Setting Static ip:
**Step 1: Assign a static ip for respective welcome system.**
<h6> configure in perticular path /etc/network/interfaces</h6>
```
##network interface
auto eth0
iface eth0 inet static
	address 192...
	netmask 255.255.252.0
	gateway 192...
	dns-nameservers 8.8.8.8 8.8.4.4
```

### 1.1.5 FQDN:
**Set a fully qualified domain name (FQDN) for each perticular host**
```
hostnamectl set-hostname namenode-cdh.sakha.com
```
![](Images/Cloudera/fqdn.png)

### 1.1.6 Upadate Hostnames:
**Upadate hostnames in /etc/hosts in each hosts**
```
<ip_address1>	<fqdn>	<alias_name>
<ip_address2>	<fqdn>	<alias_name>
<ip_address3>	<fqdn>	<alias_name>
<ip_address4>	<fqdn>	<alias_name>
```
## 1.2 Cloudera Manager
### 1.2.1 Install Cloudera Manager
**root or sudo access on a single hos**
```
wget https://archive.cloudera.com/cm5/installer/latest/cloudera-manager-installer.bin
```
![](Images/Cloudera/wget.png)
**Set the installer file to have executable permissions**
```
chmod u+x cloudera-manager-installer.bin
```
**Run the Cloudera Manager installer**
```
sudo ./cloudera-manager-installer.bin
```
<br/>
<br/>
**Press Enter to choose Next for the Readme file:**
<br/>
<br/>
![](Images/Cloudera/cm1.png)
**Now use the right arrow key to choose Yes for the license agreement.**
<br/>
<br/>
![](Images/Cloudera/cm2.png)
<br/>
<br/>
**The Oracle Binary Code License Agreement appears now. Press Enter to choose Next and then choose Yes on the ‘Accept this license’ screen.**
<br/>
<br/>
![](Images/Cloudera/cm4.png)
<br/>
<br/>
<br/>
<br/>
![](Images/Cloudera/cm5.png)
<br/>
<br/>
<br/>
<br/>
**Connect to Cloudera Manager Admin Console to install CDH**
**From Chrome or Firefox, go to the server URL for Cloudera Manager:**
**http://<your public hostname or ip>:7180**
<br/>
```
Log in to Cloudera Manager using the default credentials:

Username: admin
Password: admin
```
<br/>
<br/>
<br/>
![](Images/Cloudera/cm13.png)
<br/>
<br/>
<br/>
**The Cloudera Manager welcome screen and edition selector now appears. Click on Cloudera Express and then Continue at the bottom of the screen:**
<br/>
<br/>
![](Images/Cloudera/cm14a.png)
<br/>
<br/>
<br/>
<br/>
**On the Thank you screen, just click Continue: **
<br/>
<br/>
![](Images/Cloudera/cm19a.png)
<br/>
<br/>
<br/>
<br/>
**Type in your public IP and click Search. You can copy + paste the IP from the URL:**
<br/>
<br/>
![](Images/Cloudera/cm20.png)
<br/>
<br/><br/>
<br/>
**Your local Virtual Machine should now be found and automatically checked. Click Continue:**
<br/>
<br/>
![](Images/Cloudera/cm21a.png)
<br/>
<br/>
<br/>
<br/>
**On the Cluster Installation screen, verify that your settings are the same as the screenshot below and click Continue. Do NOT install Accumulo, or the Sqoop connectors at this time as they will not be used in this lab and will consume extra resources on the VM. This lab was created using CDH 5.2.1 and it is highly recommended that you stick with this version of this lab, so you have a consistent environment as the instructor and other students.**

<br/>
<br/>
![](Images/Cloudera/cm22a.png)
<br/>
<br/>
<br/>
<br/>
**The next screen gives you the option to install Oracle JDK. Since the Oracle JDK is the most widely deployed and tested JDK for Hadoop, we’ll also run with the Oracle version. Just click Continue:**
<br/>
<br/>
![](Images/Cloudera/cm23a.png)
<br/>
<br/>
<br/>
<br/>
**On the ‘SSH login credentials’ page, choose to:**
**Login To All Hosts As: using RSA Public key**
**Authentication Method: All hosts accept same public key **
**In the pop-up to ‘Continue SSH login with no passphrase?’, just click OK**
<br/>
<br/>
![](Images/Cloudera/cm26a.png)
<br/>
<br/>
<br/>
<br/>
**When it is finished, click Continue:**
<br/>
<br/>
![](Images/Cloudera/cm27a.png)

<br/>
<br/>
<br/>
<br/>
**The cluster installation will now kick off and run for ~1 minute:**
**When it is finished, click Continue:**
<br/>
<br/>
![](Images/Cloudera/cm28a.png)
<br/>
<br/>
<br/>
<br/>
**Choose the services that you want to install in your cluster and click continue**
<br/>
<br/>
![](Images/Cloudera/cdhservices.PNG)
<br/>
<br/>
<br/>
<br/>
**Cluster installation status**
<br/>
<br/>
![](Images/Cloudera/CDHpackage.PNG)
<br/>
<br/>
**Cluster is ready to use**
<br/>
<br/>
![](Images/Cloudera/cdhpackagecomplete.PNG)
