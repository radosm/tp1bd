SOURCES=informe.tex
OBJS= ./*

all: informe

informe: $(SOURCES) $(OBJS)
	pdflatex $^ > /dev/null
	pdflatex $^ > /dev/null
	mkdir -p logs
	mv -f *.out *.aux *.log *.toc 	./logs &>/dev/null
        
clean:
	rm -fr logs
	rm -f informe.pdf informe.aux informe.log informe.toc informe.out
	
new: clean all
	
