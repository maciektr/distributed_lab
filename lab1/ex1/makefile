# Makefile

JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
		$(JC) $(JFLAGS) $*.java
JR = java
.class:
		$(JR) $*

CLASSES = \
		JavaUdpClient.java \
		JavaUdpServer.java

default: classes

classes: $(CLASSES:.java=.class)

run:
		$(JR) JavaUdpServer& $(JR) JavaUdpClient

clean:
		$(RM) *.class
