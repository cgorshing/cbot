#JAVAC = /home/gorshing/devl/java/jdk1.5.0_01/bin/javac
JAVAC = javac

RM = rm -f

SOURCES = UrlTask.java \
	UrlManager.java \
	MainEntry.java \
	ProgressCallback.java \
	ErrorCallback.java \
	UrlListener.java

all : $(SOURCES)
	$(JAVAC) -cp .:htmlparser1_5/lib/htmlparser.jar $?

clean :
	$(RM) *.class

design : cbot_design.tex
	pdflatex $?
