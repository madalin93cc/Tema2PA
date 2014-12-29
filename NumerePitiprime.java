
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Colezea
 */
public class NumerePitiprime {
    private int n; // cate numere sunt in fisier
    private ArrayList<String> numere; // vector cu toate numerele
    Long theBest = 0L; // cel mai mare numar pitiprim gasit
    
    public NumerePitiprime(String filename) throws FileNotFoundException, IOException{
        // deschidere fisiere de input si output
        BufferedReader bufferin = new BufferedReader(new FileReader(filename));
        BufferedWriter bufferout = new BufferedWriter(new FileWriter("pitiprim.out"));
        
        // citeste n 
        n = Integer.parseInt(bufferin.readLine());
        
        // citesc fiecare numar, aflu cel mai mare numar pitiprim si il scriu in fisier
        for (int i = 0; i < n; i++){
            String line = bufferin.readLine();
            char[] characters = line.toCharArray(); //transform linia in vector de caractere
            ArrayList<Integer> number = new ArrayList<>(); // vector cu cifreme numarului 
            for (char c:characters){
                number.add(Character.getNumericValue(c));
            }
            // calculez numarul pitiprim si il afisez
            getPitiprim(number, 0, new ArrayList<Integer>());
            bufferout.write(theBest.toString() + "\n");
            theBest = 0L; // reinitializez cel mai mare numar gasit
        }
        
        // inchidere fisiere
        bufferin.close();
        bufferout.close();
    }
    
    // metoda ce scoate din vectorul de cifre posibile cifreme ce ar duce la un raspuns eronat 
    public ArrayList<Integer> removeValues(ArrayList<Integer> values, int level){
        ArrayList<Integer> aux = new ArrayList(); // vector in care retin cifrele 
        if (level != 0){ // nivel > 0 - nu poate contine 0, 2, 5 si duplicate
            for (int i:values){
                if ( (!aux.contains(i)) && (i >= 1) && (i != 0) && (i != 2) && (i != 5) ){
                    aux.add(i);
                }
            }
        }
        else { // nivelul 0 - poate contine doar 2,3,5,7 fara duplicate
               // daca acestea exista si in vectorul de posibilitati si nu sunt duplicate il adaug
            if ((values.contains(2)) && (!aux.contains(2))){
                aux.add(2);
            }
            if ((values.contains(3)) && (!aux.contains(3))){
                aux.add(3);
            }
            if ((values.contains(5)) && (!aux.contains(5))){
                aux.add(5);
            }
            if ((values.contains(7)) && (!aux.contains(7))){
                aux.add(7);
            }
        }
        return aux;
    }
    
    // metoda ce transforma un array de cifre intr-un numar de tipul Long
    public Long makeNumber(ArrayList<Integer> cifre){
        String s = "";
        for (Integer i:cifre){
            s = s+i.toString();
        }
        return Long.parseLong(s);
    }
    
    // metoda ce verifica daca un numar este prim
    public boolean isPrim(ArrayList<Integer> cifre){
        double nr = makeNumber(cifre); // construiesc numarul
        if (nr < 2) return false;
        if ((nr == 2) || (nr == 3)) return true;
        if (nr % 2 == 0) return false;
        for (int i = 3; i <= Math.sqrt(nr) + 1; i += 2){
            if (nr % i == 0){
                return false;
            }
        }
        return true;
    }
    
    // metoda ce intoarce cel mai mare numar pitiprim ce poate fi format cu cifrele unui numar, date ca parametru
    public void getPitiprim(ArrayList<Integer> nums, int level, ArrayList<Integer> number){
        ArrayList<Integer> usable_nums = removeValues(nums, level); // scot cifrele nefolositoare
        if ( (nums.size() == 0 ) || (usable_nums.size() == 0) ){ // conditia de oprire din recursivitate:
            return; // daca nu mai am cifre sau cifre ce pot duce la o solutie buna ma intorc din recursivitate
        }
        for (int i:usable_nums){ // pentru fiecare cifre ce caut recursiv o solutie 
            // clonez cei doi vectori pentru apelul recursiv
            ArrayList<Integer> aux = new ArrayList(nums);
            ArrayList<Integer> num = new ArrayList(number);
            aux.remove((Object)i); // scot cifra curenta deoarece nu mai poate fi folosita
            num.add(i); // o adaug la vectorul ce contine cifrele ce formeaza numarul pitiprim
            if (isPrim(num)){ // daca numarul format e prim verifica daca e mai mare decat solutia actuala si incerc sa gasesc recursiv o solutie mai buna
                Long x = makeNumber(num);
                if ( x > theBest ){ //daca e mai mare decat cel mai mare de pana acum 
                    theBest = x; // il retin
                }
                getPitiprim(aux, ++level, num);// apelez functia recursiv crescand nivelul = numarul de cifre utilizate, nevesar in metoda remove Values
            }
        }
    }
    public static void main(String args[]) throws FileNotFoundException, IOException{
        NumerePitiprime p = new NumerePitiprime("pitiprim.in"); 
    }
}
