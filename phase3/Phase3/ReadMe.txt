Team members
  Grant Moyer
  Elie Daou

Steps required to set up and execute this program
 -1. compile with "rm *.class; javac Sender.java; javac Receiver.java"
  0. run "java Receiver"
  1. follow prompts (ex. enter "received.png")

	THEN

  2. Run "java Sender"
  3. follow prompts (ex. enter "testImage.png")
	*To run with no bit errors, select option 1
	*to run with bit errors in the Acks, select option 2, then select a range from 0-1 
		(0 being no bit errors, 1 being 100% of Acks having errors)
	*to run with bit errors in the sent packets, select option 3, then select a range from 0-1 
		(0 being no bit errors, 1 being 100% of packets having errors)


Press ctrl+c at any time to stop execution.

Files and purposes:
  * Sender.java takes user input and initializes the SendingManager
  * SendManager.java opens a socket and manages the SendingStateMachine
  * SendingStateMachine.java defines the logic for the sender's state machine, fabricates bit errors
  * ServerReceived.java stores info for rdt ACKs.
  * Receiver.java takes user input and initializes the ReceiveManager
  * ReceiveManager.java opens a socket and manages the ReceiverStateMachine
  * ReceiverStateMachine.java defines the logic for the receiver's state machine
  * ReceivedPacket.java stores header information and data from rdt data packets.
  * StateMachine.java provides an extendable statemachine that can be advanced by events
  * testImage.png is a sample image to send, but any file can be sent
  * contribution.txt lists how member contributed
