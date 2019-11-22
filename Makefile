# Java Makefile
# Dr Alun Moon
# alun.moon@northumbria.ac.uk

%.class:%.java
	javac $<

main:=Lander
jarfile:=LunarLander.jar
sources:=$(wildcard *.java */*.java)
assets=Defaults.properties

classes=$(sources:%.java=%.class)
innerclasses=$(classes:%.class=%$\*.class)

all: tags $(jarfile)

jarfile: $(jarfile)

tags: $(sources)
	ctags $(sources)

$(jarfile): $(classes) $(assets)
	jar cfe $@ $(main) $(classes) $(innerclasses) $(assets)

.PHONY: clean
clean:
	rm -f *.class $(jarfile) tags

.PHONY: mostlyclean
mostlyclean:
	rm  -f *.class $(jarfile)

.PHONY: test
test: $(main).class
	java $(main)

.PHONY: run
run: $(jarfile)
	java -jar $(jarfile)

.PHONY: pretty
pretty: $(sources)
	astyle -q $(sources)
