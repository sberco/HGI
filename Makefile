SRCS=$(shell find -name "*.java")
OBJS=$(SRCS:.java=.class)

default: all

all: ${OBJS}
	@echo "Done making!"

%.class: %.java
	javac -cp .:./jar/* $<

clean:
	rm -f ${OBJS}
