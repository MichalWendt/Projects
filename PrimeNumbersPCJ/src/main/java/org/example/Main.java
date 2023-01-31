package org.example;

import org.pcj.*;

import java.io.IOException;
import java.util.*;

import static java.lang.Math.floor;
import static java.lang.Math.sqrt;

@RegisterStorage(Main.Shared.class)
public class Main implements StartPoint {

    @Storage(Main.class)
    enum Shared {
        lowerRange,
        upperRange,
        threads,
        mode,
        smallPrimes,
        bigPrimes,
        longBigPrimes,
        segments;
    }
    long lowerRange;
    long upperRange;
    static int threads;
    int mode;
    Vector<Integer> smallPrimes;
    int segments;
    BitSet[] longBigPrimes;
    BitSet bigPrimes;

    @Override
    public void main() {
        PCJ.barrier();
        if (PCJ.myId() == 0) {
            System.out.println("Sposob uzycia: <liczba oznaczajaca tryb> <dwie liczby oznaczajace przedzial liczbowy>");
            System.out.println("Przyklad: 1 100 1000000");  // limit 4 611 686 014 132 420 609 (int size ^ 2)
            System.out.println("Prosze wybrac tryb");
            System.out.println("1 - Wypisz wszystkie liczby pierwsze z przedzialu");
            System.out.println("2 - Wypisz najwieksza z przedzialu");
            System.out.println("3 - Policz ilosc liczb piewszych w przedziale");
            System.out.println("0 - Zakoncz dzialanie");
        }
        while(chooseMode());
    }


    public static void main(String[] args) throws IOException {
        System.out.println("Prosze podac ilosc watkow");
        System.out.println("Przyklad: 2");

        Scanner scan = new Scanner(System.in);
        threads = scan.nextInt();

        String[] nodes = new String[threads];
        for(int i = 0; i < threads; ++i){
            nodes[i] = "localhost";
        }
        PCJ.executionBuilder(Main.class)
                .addNodes(nodes)
                .start();

//        String nodesFile = "nodes.txt";
//        PCJ.executionBuilder(Main.class)
//                .addNodes(new File("nodes.txt"))
//                .start();
    }


    // Pozwalam na wybranie jednego z trzech trybów pracy.
    // Ze względu na sposób działania sita różnia się jedynie zwracaną użytkownikowi wartością.
    private boolean chooseMode() {
        Scanner scan = new Scanner(System.in);

        if (PCJ.myId() == 0) {  // Tylko główny wątek pobiera tryb pracy po czym udostępnia tę informację pozostałym
            mode = scan.nextInt();
        }
        if (PCJ.myId() == 0 ) PCJ.broadcast(mode, Shared.mode);
        PCJ.waitFor(Shared.mode);
        switch (mode) {
            case 1: // Wypisanie wszystkich liczb pierwszych z przedziału (wypisywanie może chwile zająć)
                if (PCJ.myId() == 0) {
                    lowerRange = scan.nextLong();   // Pobieram zarówno wartość minimalną jak i maksymalną
                    upperRange = scan.nextLong();
                    if(lowerRange >= upperRange){
                        System.out.println("Podano zle wejscie");
                        break;
                    }
                    PCJ.broadcast(lowerRange, Shared.lowerRange);
                    PCJ.broadcast(upperRange, Shared.upperRange);
                }
                PCJ.waitFor(Shared.lowerRange);
                PCJ.waitFor(Shared.upperRange);
                if(upperRange > 2000000000L){
                    printAllInLongRange(lowerRange, upperRange);
                } else {
                    printAllInRange(lowerRange, upperRange);
                }
                break;
            case 2: // Wypiasanie największej liczby pierwszej z przedziału
                if (PCJ.myId() == 0) {
                    lowerRange = scan.nextLong();
                    upperRange = scan.nextLong();
                    if(lowerRange >= upperRange){
                        System.out.println("Podano zle wejscie");
                        break;
                    }
                    PCJ.broadcast(lowerRange, Shared.lowerRange);
                    PCJ.broadcast(upperRange, Shared.upperRange);
                }
                PCJ.waitFor(Shared.lowerRange);
                PCJ.waitFor(Shared.upperRange);
                if(upperRange > 2000000000L){
                    printBiggestLong(lowerRange, upperRange);
                } else {
                    printBiggest(lowerRange, upperRange);
                }
                break;
            case 3: // Podanie ilości liczb pierwszych w przedziale
                if (PCJ.myId() == 0) {
                    lowerRange = scan.nextLong();
                    upperRange = scan.nextLong();
                    if(lowerRange >= upperRange){
                        System.out.println("Podano zle wejscie");
                        break;
                    }
                    PCJ.broadcast(lowerRange, Shared.lowerRange);
                    PCJ.broadcast(upperRange, Shared.upperRange);
                }
                PCJ.waitFor(Shared.lowerRange);
                PCJ.waitFor(Shared.upperRange);
                if(upperRange > 2000000000L){
                    countInLongRange(lowerRange, upperRange);
                } else {
                    countInRange(lowerRange, upperRange);
                }
                break;
            case 0:
                return false;
            default:
                System.out.println("Podano zle wejscie");
                break;
        }
        return true;
    }

    // Ta metoda wypisze wszystkie liczby pierwsze z podanego przedziału
    private void printAllInRange(long lowerRange, long upperRange) {
        if(lowerRange < 2) lowerRange = 2L;
        segmentedSieve((int) lowerRange, (int) upperRange);
        if (PCJ.myId() == 0) {
            System.out.println("Wszystkie liczby pierwsze w podanym przedziale:");
            for (int i = (int)lowerRange; i <= (int)upperRange; i++) {
                if (bigPrimes.get(i - (int)lowerRange)) {
                    System.out.print(i + " ");
                }
            }
            System.out.println();
        }
    }

    // Ta metoda wypisze wszystkie liczby pierwsze z podanego przedziału dla wartości typu long
    private void printAllInLongRange(long lowerRange, long upperRange) {
        if(lowerRange < 2) lowerRange = 2L;
        longSegmentedSieve(lowerRange, upperRange);
        if (PCJ.myId() == 0) {
            System.out.println("Wszystkie liczby pierwsze w podanym przedziale:");
            for (long i = lowerRange; i <= upperRange; i++) {
                if (longBigPrimes[(int) ((i - lowerRange) / 2000000000L)].get((int) ((i - lowerRange) % 2000000000L))) {
                    System.out.print(i + " ");
                }
            }
            System.out.println();
        }
    }

    // Ta metoda wypisze największą liczbę pierwszą z podanego przedziału
    private void printBiggest(long lowerRange, long upperRange) {
        if(lowerRange < 2) lowerRange = 2L;
        segmentedSieve((int) lowerRange, (int) upperRange);
        if (PCJ.myId() == 0) {
            System.out.print("W podanym przedziale najwieksza liczba pierwsza to ");
            for (int i = (int)upperRange; i >= (int)lowerRange; i--) {
                if (bigPrimes.get(i - (int)lowerRange)) {
                    System.out.println(i + " ");
                    break;
                }
            }
        }
    }

    // Ta metoda wypisze największą liczbę pierwszą z podanego przedziału dla wartości typu long
    private void printBiggestLong(long lowerRange, long upperRange) {
        if(lowerRange < 2) lowerRange = 2L;
        longSegmentedSieve(lowerRange, upperRange);
        if (PCJ.myId() == 0) {
            System.out.print("W podanym przedziale najwieksza liczba pierwsza to ");
            for (long i = upperRange; i >= lowerRange; i++) {
                if (longBigPrimes[(int) ((i - lowerRange) / 2000000000L)].get((int) ((i - lowerRange) % 2000000000L))) {
                    System.out.print(i + " ");
                    break;
                }
            }
        }
    }

    // Ta metoda wypisze ilość liczb pierwszych z podanego przedziału
    private void countInRange(long lowerRange, long upperRange) {
        if(lowerRange < 2) lowerRange = 2L;
        segmentedSieve((int) lowerRange, (int) upperRange);
        long value = 0L;
        if (PCJ.myId() == 0) {
            for (int i = (int)lowerRange; i <= (int)upperRange; i++) {
                if (bigPrimes.get(i - (int)lowerRange)) {
                    value++;
                }
            }
            System.out.println("W podanym przedziale jest " + value + " liczb pierwszych");
        }
    }

    // Ta metoda wypisze ilość liczb pierwszych z podanego przedziału dla wartości typu long
    private void countInLongRange(long lowerRange, long upperRange) {
        if(lowerRange < 2) lowerRange = 2L;
        longSegmentedSieve(lowerRange, upperRange);
        long value = 0L;
        if (PCJ.myId() == 0) {
            for(int s = 0; s < segments; ++s) {
                for (long i = lowerRange; i <= upperRange; i++) {
                    if (longBigPrimes[s].get((int) ((i - lowerRange) % 2000000000L))) {
                        value++;
                    }
                }
            }
            System.out.println("W podanym przedziale jest " + value + " liczb pierwszych");
        }
    }


    // Proste sito Eratosthenesa obliczające liczby pierwsze z przedziału <2, sqrt(upperRange)>
    // Te liczby będą potrzebne do wyznaczenia wszystkich pozostałych z całego przedziału
    public void simpleSieve(int sqrtUpperRange, Vector<Integer> smallPrimes) {
        boolean[] isPrime = new boolean[sqrtUpperRange+1];   // Kolekcja małych liczb pierwszych (Mieści się w pamięci bez problemu)
        Arrays.fill(isPrime,true);  // Na początku uznaję, że każda może być pierwsza
        isPrime[0]=false;
        isPrime[1]=false;   // 0 i 1 nie są liczbami pierwszymi

        for(int i = 2; i <= sqrtUpperRange; ++i) {   // Oznaczam wszystkie liczby nie będące pierwszymi
            if(isPrime[i]) {
                for(int j = i * i; j <= sqrtUpperRange && j > 0; j = j + i) {  // Usuwam wszystkie wielokrotności i (j>0 sprawdza czy j nie wyszło poza wartość int)
                    isPrime[j] = false;
                }
            }
        }
        for(int i = 2; i <= sqrtUpperRange; ++i) {   //  Sprawdzam oznaczone liczby i zapisuje jedynie pierwsze
            if(isPrime[i]) {
                smallPrimes.add(i); // Zapisuje jedynie liczby pierwsze oszczędzając pamięć i czas dostepu
            }
        }
    }

    // Metoda segmentowanego sita w implementacji dla typu int (szybsza ale tylko do maksymalnej wielkości int)
    public void segmentedSieve(int lowRange, int upRange) {
        int rangeSqrt = (int) Math.ceil(Math.sqrt(upRange));
        smallPrimes = new Vector<Integer>();
        bigPrimes = new BitSet();

        if (PCJ.myId() == 0) {  // Wystarczy tylko raz policzyc male liczby pierwsze, a potem je udostepnić
            double simpleTime = System.nanoTime();
            simpleSieve(rangeSqrt, smallPrimes);
            simpleTime = System.nanoTime() - simpleTime;
            if (PCJ.myId() == 0) {
                System.out.format("Czas znalezenia malych liczb pierwszych to %.7fs \n", simpleTime * 1.0E-9);
            }
            PCJ.broadcast(smallPrimes, Shared.smallPrimes); // Udostępniam małe liczby pierwsze
        }
        PCJ.waitFor(Shared.smallPrimes);
        PCJ.barrier();

        bigPrimes.set(0, upRange, true);   // Wypełniam zbiór potencjalnych liczb pierwszych wartością true

        double time = System.nanoTime();

        for (int k = PCJ.myId(); k < smallPrimes.size(); k += PCJ.threadCount()) {  // Wątki dostają do przetworzenia kolejne małe liczby pierwsze
            int i = smallPrimes.get(k);
            int optimizedLow = (lowRange / i);  // Zwiększanie dolnej granicy w celu zmniejszenia liczby obliczeń
            if (optimizedLow <= 1) {
                optimizedLow = i + i;
            } else if (lowRange % i != 0) {
                optimizedLow = (optimizedLow * i) + i;
            } else {
                optimizedLow = (optimizedLow * i);
            }
            // Odkomentowując kolejną linijkę można zobaczyć, który wątek aktualnie przetwarza daną małą liczbę pierwszą
            //System.out.println("Hello from PCJ Thread " + PCJ.myId() + " out of " + PCJ.threadCount() + " with prime " + i);
            for (int j = optimizedLow; j <= upRange; j = j + i) {
                bigPrimes.set(j - lowRange, false);
            }
        }

        PCJ.barrier();  // W tym miejscu trzeba poczekać na wszystkie wątki i ich wyniki dla dużych liczb pierwszych

        time = System.nanoTime() - time;    // Obliczam i wypisuje czas działania (Bez wypisywania i łączenia wyników)

        PcjFuture cL[] = new PcjFuture[PCJ.threadCount()];
        if (PCJ.myId() == 0) {
            for (int p = 1; p < PCJ.threadCount(); p++) {
                cL[p] = PCJ.asyncGet(p, Shared.bigPrimes);
            }
            for (int p = 1; p < PCJ.threadCount(); p++) {
                bigPrimes = concatenateVectors(bigPrimes, (BitSet) cL[p].get());
            }
        }

        PCJ.barrier();  // W tym miejscu trzeba poczekać na połączenie wyników w jedną kolekcję
        if (PCJ.myId() == 0) {
            System.out.format("Czas znalezenia wszystkich liczb pierwszych to %.7fs \n", time * 1.0E-9);
        }
    }

    // Metoda "łącząca" dwa bitsety
    // Szukam w niej wykreślonych z dwóch zbiorów liczb i wykreślam te same ze zbioru wynikowego
    // Implementacja dla zwykłych bitsetów
    static BitSet concatenateVectors(BitSet vector_1_in, BitSet vector_2_in) {
        BitSet vector_1_in_clone = (BitSet)vector_1_in.clone();
        BitSet vector_2_in_clone = (BitSet)vector_2_in.clone();
        int index = -1;
        while (index <= (vector_2_in_clone.length() - 1)) {
            index++;
            if(!vector_1_in_clone.get(index) || !vector_2_in_clone.get(index)) {
                vector_1_in_clone.set(index, false);
            } else {
                vector_1_in_clone.set(index, true);
            }
        }
        return vector_1_in_clone;
    }

    // Metoda segmentowanego sita w implementacji dla typu long (wolniejsza ale dla większych liczb)
    public void longSegmentedSieve(long lowRange, long upRange) {
        int rangeSqrt = (int) Math.ceil(Math.sqrt(upRange));
        smallPrimes = new Vector<Integer>();
        segments = (int) (upRange / 2000000000) + 1;   // Na tyle tablic musze podzielic wybrany zbior
        longBigPrimes = new BitSet[segments];

        if (PCJ.myId() == 0) {  // Wystarczy tylko raz policzyc male liczby pierwsze, a potem je udostepnić
            double simpleTime = System.nanoTime();
            simpleSieve(rangeSqrt, smallPrimes);
            simpleTime = System.nanoTime() - simpleTime;
            if (PCJ.myId() == 0) {
                System.out.format("Czas znalezenia malych liczb pierwszych to %.7fs \n", simpleTime * 1.0E-9);
            }
            PCJ.broadcast(smallPrimes, Shared.smallPrimes); // Udostępniam małe liczby pierwsze
        }

        PCJ.waitFor(Shared.smallPrimes);
        PCJ.barrier();

        for(int s = 0; s < segments; ++s) { // Dla ogromnych zbiorów (>2000000000 liczb) muszę podzielić je na zmniejsze kolekcje
            longBigPrimes[s] = new BitSet();
            if(s == segments - 1){  // Ostatni ze zbiorów nie musi mieć wielkości 2000000000 (wystarczy reszta z dzielenia)
                longBigPrimes[s].set(0, (int) (upRange % 2000000000), true);
            } else {    // Dziele na zbiory wielkości 2000000000 (mój wybór bliski maxInt)
                longBigPrimes[s].set(0, 2000000000, true);  // Wypełniam zbiór potencjalnych liczb pierwszych wartością true
            }
        }

        double time = System.nanoTime();

        for (int k = PCJ.myId(); k < smallPrimes.size(); k += PCJ.threadCount()) {  // Wątki dostają do przetworzenia kolejne małe liczby pierwsze
            int i = smallPrimes.get(k);
            long optimizedLow = lowRange / i;   // Zwiększanie dolnej granicy w celu zmniejszenia liczby obliczeń
            if (optimizedLow <= 1) {
                optimizedLow = i + i;
            } else if (lowRange % i != 0) {
                optimizedLow = (optimizedLow * i) + i;
            } else {
                optimizedLow = (optimizedLow * i);
            }
            // Odkomentowując kolejną linijkę można zobaczyć, który wątek aktualnie przetwarza daną małą liczbę pierwszą
            //System.out.println("Hello from PCJ Thread " + PCJ.myId() + " out of " + PCJ.threadCount() + " with prime " + i);
            for (long j = optimizedLow; j <= upRange; j = j + i) {
                longBigPrimes[(int) ((j - lowRange) / 2000000000L)].set((int) ((j - lowRange) % 2000000000L), false);
            }
        }
        PCJ.barrier();  // W tym miejscu trzeba poczekać na wszystkie wątki i ich wyniki dla dużych liczb pierwszych

        time = System.nanoTime() - time;    // Obliczam i wypisuje czas działania (Bez wypisywania i łączenia wyników)

        PcjFuture cL[] = new PcjFuture[PCJ.threadCount()];
        if (PCJ.myId() == 0) {
            for (int p = 1; p < PCJ.threadCount(); p++) {
                cL[p] = PCJ.asyncGet(p, Shared.longBigPrimes);
            }
            for (int p = 1; p < PCJ.threadCount(); p++) {
                longBigPrimes = concatenateLongVectors(longBigPrimes, (BitSet[]) cL[p].get());
            }
        }

        if (PCJ.myId() == 0) {
            System.out.format("Czas znalezenia wszystkich liczb pierwszych to %.7fs \n", time * 1.0E-9);
        }
    }

    // Metoda "łącząca" dwa bitsety
    // Szukam w niej wykreślonych z dwóch zbiorów liczb i wykreślam te same ze zbioru wynikowego
    // Implementacja dla tablic bitsetów
    BitSet[] concatenateLongVectors(BitSet[] vector_1_in, BitSet[] vector_2_in) {
        BitSet[] vector_1_in_clone = (BitSet[])vector_1_in.clone();
        BitSet[] vector_2_in_clone = (BitSet[])vector_2_in.clone();
        int index = -1;
        for(int s = 0; s < segments; ++s) {
            while (index <= (vector_2_in_clone[s].length() - 1)) {
                index++;
                if(!vector_1_in_clone[s].get(index) || !vector_2_in_clone[s].get(index)) {
                    vector_1_in_clone[s].set(index, false);
                } else {
                    vector_1_in_clone[s].set(index, true);
                }
            }
        }
        return vector_1_in_clone;
    }




    //--------------------------Dalsze metody nie są używane i pozostawiam je tutaj w celach poglądowych

    // Moje pierwsze podejście do nieogranicenie wielkich liczb pierwszych pozostawione jako pokaz postepów
    // Ta metoda nie działa poprawnie przez nadmiar wykorzystyswanej pamięci
    static void mySegmentedSieve(long n) {
        boolean numbers[][] = new boolean[(int) Math.ceil(sqrt(n))][(int) Math.ceil(sqrt(n))];
        double limit = Math.ceil(sqrt(n));
        for (int i = 0; i < numbers.length; i++){
            for (int j = 0; j < numbers.length; j++){
                numbers[i][j] = true;
            }
        }
        numbers[0][0] = false;
        numbers[0][1] = false;

        for (int i=0; i<limit; ++i) {
            for (int j=0; j<limit; ++j) {
                if (numbers[i][j]) {
                    long value = (long) (limit * i + j);
                    for (long p = (long) Math.pow(value,2); p < n/*(limit * (i + 1))*/; p += value) {
                        int pomI = (int)Math.ceil(p / (limit * (i + 1)));
                        int pomJ = (int)Math.ceil(p % (limit * (i + 1)));
                        numbers[pomI-1][pomJ] = false;
                    }
                }
            }
        }
        for (int i=0; i<limit; ++i) {
            for (int j=0; j<limit; ++j) {
                if (numbers[i][j]) {
                    //prime.add(p);
                    System.out.print(limit * i + j + " ");
                }
            }
        }
    }



    // Naiwne implementacje wyszukiwania liczb pierwszych

    // Proste sprawdzenie pierwszości jednej liczby
    static boolean isPrime(long n) {
        for (long i = 2L; i < n; i++) {
            if (n % i == 0L) {
                return false;
            }
        }
        return true;
    }


    // Udoskonalone sprawdzenie pierwszości jednej liczby
    static boolean isPrime2(long n) {
        if (n == 2L) return true;
        if (n % 2 == 0) return false;   // Unikanie liczb parzystych poza 2
        for (int i = 3; (long) i * i <= n; i += 2) {   // Sprawdzanie liczb tylko do pierwiastka
            if (n % i == 0)
                return false;
        }
        return true;
    }

    // Wywołanie funkcji sprawdzjącej w pętli
    public static void naivePrimes(int n){
        for(long i = 2L; i <= n; ++i) {
            if(isPrime(i)){
                System.out.printf("%d ", i);
            };
        }
        System.out.println();
        for(long i = 2L; i <= n; ++i) {
            if(isPrime2(i)){
                System.out.printf("%d ", i);
            };
        }
        System.out.println();
    }
}