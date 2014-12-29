
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Colezea
 */
public class Scrisori {
    private static int N; // numar de orase
    private static int K; // numar de corbi
    private static int A; // risc porumbel
    private static int B; // risc corb
    private ArrayList< ArrayList<Integer> > graf; // graful tinut ca o lista de adiacenta cu arcele din fisier
    private Integer[] distance1; // vector de distante din nodul 1
    private Integer[] distance2; // vector de distante din nodul N
    
    public Scrisori(String filename) throws FileNotFoundException, IOException{
        // citire date din fisier si creare lista de adiacenta
        BufferedReader bufferin = new BufferedReader(new FileReader(filename));
        BufferedWriter bufferout = new BufferedWriter(new FileWriter("scrisori.out"));
        String[] data = bufferin.readLine().split(" ");
        N = Integer.parseInt(data[0]);
        K = Integer.parseInt(data[1]);
        A = Integer.parseInt(data[2]);
        B = Integer.parseInt(data[3]);
        initGraf(); // initializare graf
        for (int i = 0; i<K; i++){ // citire cele K arce
            data = bufferin.readLine().split(" ");
            graf.get(Integer.parseInt(data[0])).add(Integer.parseInt(data[1])); // sursa -> destinatie
            graf.get(Integer.parseInt(data[1])).add(Integer.parseInt(data[0])); // destinatie -> sursa
        }
        // sortez fiecare lista pentru a putea folosi binarySearch
        for (int i = 1; i<=N; i++){ 
            Collections.sort(graf.get(i));
        }
        // initializare distante
        distance1 = new Integer[N+1];
        distance2 = new Integer[N+1];
        // gaseste drum de cost minim si il scrie in fisier 
        bufferout.write(parcurgere(1) + "\n");
        // inchidere fisiere
        bufferin.close();
        bufferout.close();
    }
    
    // metoda ce initializeaza lista de adiacenta cu liste nule pentru fiecare nod
    public void initGraf(){
        graf = new ArrayList<> (N+1);
        graf.add(new ArrayList());
        for (int i = 1; i <= N; i++){
            ArrayList<Integer> edges = new ArrayList<>();
            graf.add(edges);
        }
    }
    
    // metoda ce intoarce costul minim de la nodul sursa la nodul N
    public int parcurgere(int sursa){
        // tratez doua cazuri in functie de care cost este mai mare
        if (A > B){ // cost porumbel > cost corb
            // initializari
            Arrays.fill(distance1, Integer.MAX_VALUE);
            Arrays.fill(distance2, Integer.MAX_VALUE);
            // in distance1 tin distantele fata de sursa
            distance1[1] = 0;
            // // in distance2 tin distantele fata de destinatie
            distance2[N] = 0;
            Queue<Integer> queue1 = new LinkedList<>();
            Queue<Integer> queue2 = new LinkedList<>();
            // o coada pentru BFS(sursa) si una pt BFS(destinatie)
            queue1.add(sursa);
            queue2.add(N);
            // mark[i] = 0 daca nu s-a ajuns in nodul i si 1 altfel
            // cate un mark pentru fiecare nod (sursa si N)
            Integer[] mark1= new Integer[N+1];
            Arrays.fill(mark1, 0);
            Integer[] mark2= new Integer[N+1];
            Arrays.fill(mark2, 0);
            mark1[1] = 1;
            mark2[N] = 1;
            // cat timp cozile nu sunt goale cozile 
            while ( (!queue1.isEmpty()) && (!queue2.isEmpty()) ){
                // BFS pentru sursa
                int u1 = queue1.peek();    
                // verific daca am nod direct direct de cost minim 
                if (Collections.binarySearch(graf.get(u1), N) >= 0){
                    return distance1[u1] + B;
                }
                // caut vecini
                for (int i = 2; i < N; i++){
                    if (mark1[i] == 0) // daca nu e deja marcat
                    if (Collections.binarySearch(graf.get(u1), i) >= 0){
                        // daca este vecin actualizez distantele
                        int dist = distance1[u1] + B;
                        if (dist < distance1[i]){
                            distance1[i] = dist;
                        }
                        // daca nu as mai putea gasi o solutie mai buna decat calea directa
                        if (distance1[i] > A) return A;
                        // il adaug in coada corespunzatoare
                        queue1.add(i);
                        // il marchez
                        mark1[i] ++;
                        // daca a fost descoperit si din sursa si din N atunci am gasit drumul minim
                        if ( (mark1[i] != 0) && (mark2[i] != 0) ){
                            if (distance1[i] + distance2[i] < A)
                                return distance1[i] + distance2[i];
                            else 
                                return A;
                        }
                    }
                }
                // scot din coada nodul curent
                queue1.remove();
                
                // BFS pentru destinatie
                int u2 = queue2.peek();     
                // verific daca am drum direct de cost minim
                if (Collections.binarySearch(graf.get(u2), 1) >= 0){
                    return distance2[u2] + B;
                }
                // caut vecini
                for (int i = N - 1; i > 1; i--){
                    if (mark2[i] == 0) // daca nu a fost descoperit
                    if (Collections.binarySearch(graf.get(u2), i) >= 0){
                        // daca am gasit vecin actualizez distantele
                        int dist = distance2[u2] + B;
                        if (dist < distance2[i]){
                            distance2[i] = dist;
                        }
                        // daca nu as mai putea gasi o solutie mai buna decat calea directa
                        if (distance2[i] > A) return A;
                        // il adaug in coada corespunzatoare
                        queue2.add(i);
                        // il marchez
                        mark2[i] ++;
                        // daca a fost descoperit si din sursa si din destinatie am gasit drumul minim
                        if ( (mark1[i] != 0) && (mark2[i] !=0) ){
                            if (distance1[i] + distance2[i] < A)
                                return distance1[i] + distance2[i];
                            else 
                                return A;
                        }
                    }
                }
                // scot din coada nodul curent
                queue2.remove();
            }
            // daca una din cozi este goala inseamna ca nu se poate ajunge pe un drum de cost mai mic decat costul maxim din sursa in N
            // returnez costul maxim - legatura directa
            return A;
        }
        else { // cost porumbel > cost corb
            // fac acelasi lucru ca pe ramura if doar ca de data asta tin cont ca A este drumul minim
            // si ca trebuie sa verific ca in lista de adiacenta nu am arcul 
            Arrays.fill(distance1, Integer.MAX_VALUE);
            Arrays.fill(distance2, Integer.MAX_VALUE);
            distance1[1] = 0;
            distance2[N] = 0;
            Queue<Integer> queue1 = new LinkedList<>();
            Queue<Integer> queue2 = new LinkedList<>();
            queue1.add(sursa);
            queue2.add(N);
            Integer[] mark1= new Integer[N+1];
            Arrays.fill(mark1, 0);
            Integer[] mark2= new Integer[N+1];
            Arrays.fill(mark2, 0);
            mark1[1] = 1;
            mark2[N] = 1;
            while ( (!queue1.isEmpty()) && (!queue2.isEmpty()) ){
                // BFS pentru sursa
                int u1 = queue1.peek();     
                if (Collections.binarySearch(graf.get(u1), N) < 0){
                    return distance1[u1] + A;
                }
                for (int i = 2; i < N; i++){
                    if (mark1[i] == 0)
                    if (Collections.binarySearch(graf.get(u1), i) < 0){
                        int dist = distance1[u1] + A;
                        if (dist < distance1[i]){
                            distance1[i] = dist;
                        }
                        if (distance1[i] > B) return B;
                        queue1.add(i);
                        mark1[i] ++;
                        if ( (mark1[i] != 0) && (mark2[i] != 0) ){
                            if (distance1[i] + distance2[i] < B)
                                return distance1[i] + distance2[i];
                            else 
                                return B;
                        }
                    }
                }
                queue1.remove();
                
                // BFS pentru destinatie
                int u2 = queue2.peek();     
                if (Collections.binarySearch(graf.get(u2), 1) < 0){
                    return distance2[u2] + A;
                }
                for (int i = N - 1; i > 1; i--){
                    if (mark2[i] == 0)
                    if (Collections.binarySearch(graf.get(u2), i) < 0){
                        int dist = distance2[u2] + A;
                        if (dist < distance2[i]){
                            distance2[i] = dist;
                        }
                        if (distance2[i] > B) return B;
                        queue2.add(i);
                        mark2[i] ++;
                        if ( (mark1[i] != 0) && (mark2[i] !=0) ){
                            if (distance1[i] + distance2[i] < B)
                                return distance1[i] + distance2[i];
                            else 
                                return B;
                        }
                    }
                }
                queue2.remove();
            }
            return B;
        }
    }
    
    public static void main(String args[]) throws FileNotFoundException, IOException{
        Scrisori s = new Scrisori("scrisori.in");
    }
}
