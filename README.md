
P2Patter System

P2Patter is a peer-to-peer (P2P) microblogging system, similar in functionality to [a popular microblogging social app], but using a P2P architecture instead of a web-based client-server architecture. Each P2Patter user has a microblog consisting of a series of textual messages. A user, such as Alice, can add messages to and remove messages from her own microblog. Another user, such as Bob, can follow Alice and see all her messages as she adds them. Bob can also follow multiple users simultaneously.
When Alice adds a message to her microblog, the microblog assigns a serial number (1, 2, 3, etc.) to the message, records the date and time, and records the message text. Alice can later remove a message from her microblog by specifying the serial number.
When Bob starts the program to follow a user and specifies he wants to follow Alice, the program first prints Alice's two most recently added messages, in ascending order of serial number. (If Alice has added only one message, the program prints just that one; if Alice has not added any messages, the program prints nothing.) Each message is printed in this format:

--------------------------------------------------------------------------------
<name> -- Message <number> -- <year>/<mo>/<da> <hr>:<mi>:<se>
<text>
The first line consists of 80 hyphen characters. On the second line, <name> is replaced with the name of the user that added the message; <number> is replaced with the message's serial number; <year> is replaced with the year portion of the date when the message was added (four digits); <mo> is replaced with the month portion of the date when the message was added (two digits, 01 through 12); <da> is replaced with the day portion of the date when the message was added (two digits, 01 through 31); <hr> is replaced with the hour portion of the time when the message was added (local time, 24-hour format, two digits, 00 through 23); <mi> is replaced with the minute portion of the time when the message was added (two digits, 00 through 59); and <se> is replaced with the second portion of the time when the message was added (two digits, 00 through 59). On the third line, <text> is replaced with the text of the message. Here is an example:
--------------------------------------------------------------------------------
Alice -- Message 2 -- 2012/12/04 11:46:32
I had a tuna sandwich for lunch.
--------------------------------------------------------------------------------
Alice -- Message 3 -- 2012/12/04 18:05:17
I had rare filet mignon for dinner.
Bob can also specify that he wants to follow more than one user, such as Alice and Carol. In this case the program first prints each user's two most recently added messages; the messages are printed in ascending order of date/time; if multiple messages have the same date/time, the messages are printed in ascending order of the users' names; if multiple messages have the same date/time and the same user name, the messages are printed in ascending order of serial number. (If a user has added only one message, the program prints just that one message for that user; if a user has not added any messages, the program prints nothing for that user.) Each message is printed in the format specified above. Here is an example:

--------------------------------------------------------------------------------
Carol -- Message 56 -- 2012/12/04 11:45:59
I had chicken soup with rice for lunch.
--------------------------------------------------------------------------------
Alice -- Message 2 -- 2012/12/04 11:46:32
I had a tuna sandwich for lunch.
--------------------------------------------------------------------------------
Alice -- Message 3 -- 2012/12/04 18:05:17
I had rare filet mignon for dinner.
--------------------------------------------------------------------------------
Carol -- Message 57 -- 2012/12/04 18:05:17
I had a hot dog with mustard for dinner.
After the initial printout, whenever any of the users Bob is following adds a message to that user's microblog, the program immediately prints the message that was added. Each message is printed in the format specified above.

If Bob is following a user, such as Alice, and Alice's microblog fails (for example, if Alice's computer crashes), then the program prints the following message:

--------------------------------------------------------------------------------
<name> -- Failed
The first line consists of 80 hyphen characters. On the second line, <name> is replaced with the name of the user whose microblog failed. Thereafter, the program no longer prints any of Alice's messages. The program must print the above message within at most 20 seconds from the instant when Alice's microblog failed. If Alice's microblog subsequently comes back into existence, the program resumes printing Alice's messages.
The P2Patter distributed system consists of the following components:

A distributed object for one user's microblog.
A client program for adding a message to a microblog.
A client program for removing a message from a microblog.
A client program for following one or more users.
The Registry Server from the Computer Science Course Library.
Any number of microblog objects and any number of clients may be running simultaneously. Only one Registry Server is running. The only objects bound into the Registry Server are the microblog objects. It is assumed that the Registry Server does not fail.

Software Requirements

Microblog Object

The system must have a Java RMI remote object class for a microblog, of which any number of instances may be running.
An instance of the microblog object must be run by typing this command line:
java Start Microblog <host> <port> <name>
<host> is the name of the host computer where the Registry Server is running.
<port> is the port number to which the Registry Server is listening.
<name> is the microblog owner's name.
Note: This means that the microblog object's class must be named Microblog, this class must not be in a package, and this class must define the appropriate constructor for the Start program.

Note: The Registry Server is an instance of class edu.rit.ds.registry.RegistryServer.

The microblog object's constructor must throw an exception if there are any of the following problems with the command line arguments. The exception's detail message must be a meaningful explanation of the problem.
Any required argument is missing.
There are extra arguments.
The port argument cannot be parsed as an integer.
There is no Registry Server running at the given host and port.
Another microblog with the same owner name is already in existence.
Any other error condition is encountered while starting up the microblog object.
The microblog object must not print anything.
The microblog object must continue running until killed externally.
Add Message Client Program

The system must have a client program for adding a message to a microblog, of which any number of instances may be running.
An instance of the client program must be run by typing this command line:
java AddMessage <host> <port> <name> "<text>"
<host> is the name of the host computer where the Registry Server is running.
<port> is the port number to which the Registry Server is listening.
<name> is the microblog owner's name.
<text> is the text of the message to be added. (It is enclosed in quotation marks so the text can include spaces.)
Note: This means that the client program's class must be named AddMessage, and this class must not be in a package.

Note: The AddMessage program is a client program, not a distributed object.

Note: The Registry Server is an instance of class edu.rit.ds.registry.RegistryServer.

The client program must print an error message on the console and must terminate if there are any of the following problems with the command line arguments. The error message must be a meaningful explanation of the problem. The error message may include an exception stack trace.
Any required argument is missing.
There are extra arguments.
The port argument cannot be parsed as an integer.
There is no Registry Server running at the given host and port.
There is no microblog object for the given owner name.
The client program must add the given message to the given owner's microblog, must print on the console the message that was added, and must terminate; the format of the printout must be as follows (see P2Patter System above):
--------------------------------------------------------------------------------
<name> -- Message <number> -- <year>/<mo>/<da> <hr>:<mi>:<se>
<text>
If the client program encounters an error condition not mentioned above, the client program must print an error message on the console and must terminate. The error message must be a meaningful explanation of the problem. The error message may include an exception stack trace.
The client program must not print anything other than specified above.
Remove Message Client Program

The system must have a client program for removing a message from a microblog, of which any number of instances may be running.
An instance of the client program must be run by typing this command line:
java RemoveMessage <host> <port> <name> <number>
<host> is the name of the host computer where the Registry Server is running.
<port> is the port number to which the Registry Server is listening.
<name> is the microblog owner's name.
<number> is the serial number of the message to be removed.
Note: This means that the client program's class must be named RemoveMessage, and this class must not be in a package.

Note: The RemoveMessage program is a client program, not a distributed object.

Note: The Registry Server is an instance of class edu.rit.ds.registry.RegistryServer.

The client program must print an error message on the console and must terminate if there are any of the following problems with the command line arguments. The error message must be a meaningful explanation of the problem. The error message may include an exception stack trace.
Any required argument is missing.
There are extra arguments.
The port argument cannot be parsed as an integer.
The serial number argument cannot be parsed as an integer.
There is no Registry Server running at the given host and port.
There is no microblog object for the given owner name.
The microblog object for the given owner name does not contain a message with the given serial number.
The client program must remove the given message from the given owner's microblog, must print on the console the message that was removed, and must terminate; the format of the printout must be as follows (see P2Patter System above):
--------------------------------------------------------------------------------
<name> -- Message <number> -- <year>/<mo>/<da> <hr>:<mi>:<se>
<text>
If the client program encounters an error condition not mentioned above, the client program must print an error message on the console and must terminate. The error message must be a meaningful explanation of the problem. The error message may include an exception stack trace.
The client program must not print anything other than specified above.
Follow Client Program

The system must have a client program for following one or more users' microblogs, of which any number of instances may be running.
An instance of the client program must be run by typing this command line:
java Follow <host> <port> <name> ...
<host> is the name of the host computer where the Registry Server is running.
<port> is the port number to which the Registry Server is listening.
<name> is the microblog owner's name; there may be one or more names.
Note: This means that the client program's class must be named Follow, and this class must not be in a package.

Note: The Follow program is a client program, not a distributed object.

Note: The Registry Server is an instance of class edu.rit.ds.registry.RegistryServer.

The client program must print an error message on the console and must terminate if there are any of the following problems with the command line arguments. The error message must be a meaningful explanation of the problem. The error message may include an exception stack trace.
Any required argument is missing.
There are extra arguments.
The port argument cannot be parsed as an integer.
There is no Registry Server running at the given host and port.
If there is no microblog object for one of the given owner names, it is not an error; rather, the client program must not print anything for the missing microblog until the missing microblog comes into existence. (See also Requirement 25.)
When the client program starts, the client program must print on the console the zero, one, or two most recently added messages from the given owners' microblogs; the format of each printout must be as follows (see P2Patter System above):
--------------------------------------------------------------------------------
<name> -- Message <number> -- <year>/<mo>/<da> <hr>:<mi>:<se>
<text>
After the client program starts, whenever a message is added to any of the given owners' microblogs, the client program must immediately print on the console the message that was added; the format of the printout must be as follows (see P2Patter System above):
--------------------------------------------------------------------------------
<name> -- Message <number> -- <year>/<mo>/<da> <hr>:<mi>:<se>
<text>
After the client program starts, if any of the given owner's microblogs fails, within 20 seconds the client program must print on the console the microblog that failed; the format of the printout must be as follows (see P2Patter System above):
--------------------------------------------------------------------------------
<name> -- Failed
If a given owner's microblog object did not exist when the client program started, or if a given owner's microblock object failed after the client program started, and then that owner's microblock object comes back into existence, the client program must resume printing messages that are added to that microblog object.
If the client program encounters an error condition not mentioned above, the client program must print an error message on the console and must terminate. The error message must be a meaningful explanation of the problem. The error message may include an exception stack trace.
The client program must not print anything other than specified above.
The client program must continue running until killed externally.
