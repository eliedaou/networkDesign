Team members
  Grant Moyer
  Elie Daou

ALL COMMAND SHOULD BE RUN FROM "phase5" DIRECTORY

Project can be opened and compiled with IntelliJ IDEA.
Project can also be compiled on command line by running "mkdir bin && javac -d ./bin -cp ./src $(find ./src -name '*.java')" in a posix compliant shell.
  
Steps required to set up and execute this program (assuming using a bash command line)
  0. run command "java -jar out/artifacts/Receiver_jar/Receiver.jar" (or "java -cp ./bin daoumoyer.receiver.Receiver" if you compiled it yourself)
  1. follow prompts (ex. enter "received.png")

	THEN

  2. Run "java -jar out/artifacts/Sender_jar/Sender.jar" (or "java -cp ./bin daoumoyer.sender.Sender" if you compiled it yourself)
  3. follow prompts (ex. enter "testImage.png")
	*To run with no bit errors, select option 1
	*to run with bit errors in the Acks, select option 2, then select a range from 0-1 
		(0 being no bit errors, 1 being 100% of Acks having errors)
	*to run with bit errors in the sent packets, select option 3, then select a range from 0-1 
		(0 being no bit errors, 1 being 100% of packets having errors)
	*To run with ack loss, select option 4, then select a range from 0-1 
		(0 being no loss, 1 being 100% of Acks being dropped)
	*To run with data packet loss, select option 5, then select a range from 0-1 
		(0 being no loss, 1 being 100% of Acks being dropped)

Files and purposes:
	phase5/
	├── .idea/ - IntelliJ IDEA project files
	├── charts.xlsx - contains the charts specified in Phase 5 Description
	├── MoyerDaou.doc - contains a description of the classes used
	├── out/
	│   ├── artifacts/ - contain packaged jar files for Sender and Receiver
	│   └── production/ - contains compiled class files
	├── phase5.iml - IntelliJ IDEA project file
	├── bigTestImage.jpg - an about 1MB image file to send
	├── ReadMe.txt - this file
	├── src/ - contains all the phase 5 source files. The purpose of each file is detailed in MoyerDaou.doc
	└── testImage.png - a small image file to send

