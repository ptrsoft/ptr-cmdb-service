1. Create database in postgres  

	create database cmdb;

2. Create directory in java project to store sql scripts  
	cd /opt/mycode/appkube-cloudasset-management-service    
	mkdir dbscript
	
3. create flyway entry in pom.xml  
	`<properties>  
        <flyway.url>jdbc:postgresql://localhost:5432/cmdb</flyway.url>  
        <flyway.user>postgres</flyway.user>  
        <flyway.password>postgres</flyway.password>  
        <flyway.locations>filesystem:dbscript</flyway.locations>  
        <mvnflyway.version>9.8.1</mvnflyway.version>  
        <!-- mvn flyway configuraions-->  
    </properties>  
	<pluginManagement>
		<plugins>
			<plugin>
				<groupId>org.flywaydb</groupId>
				<artifactId>flyway-maven-plugin</artifactId>
				<version>${mvnflyway.version}</version>
			</plugin>
		</plugins>
	</pluginManagement>`

2. Run mvn:flyway script  
	2.1 cd <project directory> (e.g. /opt/mycode/appkube-cloudasset-management-service)  
	2.2	execute below command  
	- 2.2.1 - To run the mvn flyway:migrate command with a specific Maven profile, you can activate the profile using the -P flag followed by the profile ID.   
		 **mvn flyway:migrate -Pprofile-name (e.g. mvn flyway:migrate -Plocal)**  
         or  
         **mvn compile flyway:migrate -Pprofile-name (e.g. mvn compile flyway:migrate -Plocal)**
	