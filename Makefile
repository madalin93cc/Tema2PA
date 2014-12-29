build: javac-p1 javac-p2
	
javac-p1:
	javac NumerePitiprime.java
	
javac-p2:
	javac Scrisori.java
	
run-p1: 
	java NumerePitiprime
	
run-p2:
	java Scrisori
	
clean:
	rm *.class
	
