SOURCES=informe.tex
OBJS= ./*

all: informe

informe: $(SOURCES) $(OBJS)
	pdflatex $^
	pdflatex $^
	mkdir -p logs
	mv -f *.out *.aux *.log *.toc 	./logs &>/dev/null
	clear
	@echo PDF COMPILADO!
        
clean:
	rm -fr logs
	rm -f informe.pdf informe.aux informe.log informe.toc informe.out
	
new: clean all
	
