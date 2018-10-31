if [ -n `which java` ]; then
	echo "Environment is already set up"
else
	echo "Java Not Installed"
	sudo apt-get update && apt-get upgrade -y
	sudo apt-get install default-jdk -y
	sudo add-apt-repository ppa:linuxuprising/java -y
	sudo apt-get update
	sudo apt-get install oracle-java10-installer -y
	sudo apt install oracle-java10-set-default -y
	export JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:/bin/java::")
fi

javac ChordProtocol.java
if [ $# -eq 2 ]; then
	
	if [ $1 = "chord" ]; then
		java ChordProtocol chord $2
	else
		echo "Please enter valid inputs"
	fi
	
elif [ $# -eq 4 ]; then

	if [ $1 = "chord" ]; then
		java ChordProtocol chord -i $3 $4
	else
		echo "Please Enter valid arguments"
	fi

else
	echo "Please enter valid arguments"

fi
