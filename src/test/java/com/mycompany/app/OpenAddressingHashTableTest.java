/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.app;

import java.util.ArrayList;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author JohnBaizanis
 */
public class OpenAddressingHashTableTest {
    
    private static final int SIZE = 10000;
    
    @Test
    public void testOpenAddressingHashTable() {
        
        Dictionary<Integer, Integer> dict = new OpenAddressingHashTable<>();
        
        ArrayList<Integer> list = new ArrayList<>();
        Random rng = new Random(17);
        for (int i=0; i<SIZE; i++){
            
            int n = rng.nextInt(SIZE);
            list.add(n);
            dict.put(n, n+1); // Προσθήκη με την put του τυχαίου αριθμού n ως κλειδί στον πίνακα κατακερματισμού και με value n+1.
        }
        
        for (Integer i : list){
            assertTrue(dict.get(i) == i + 1);
        }
        
        int nonContainedValue = 10000;
        boolean fine = false;
        
        for(int i=0; i<dict.size(); i++){ // Αναζήτηση στον πίνακα κατακερματισμού για την υπάρξη της τιμής nonContainedValue. Η τιμή δεν θα υπάρχει ποτέ στον πίνακα κατακερματισμού άρα η συνθήκη if δεν θα ικανοποιηθεί.
            if(dict.get(i)!=null){
                if(dict.get(i)== nonContainedValue){
                    fine = true;
                }
            }
        }
        assertFalse(fine); // Εφόσον η συνθήκη if δεν ικανοποιήθηκε το ελέγχουμε με ένα assertFalse.
        
        dict.clear();
        assertTrue(dict.isEmpty()); // Ελέγχουμε ότι μετά την κλήση της clear ο πίνακας κατακερματισμού είναι άδειος.
        
        dict.remove(null);
        
        for (int i=0; i<SIZE; i++){
            dict.put(i+2, i+4); // Προσθέτουμε στον πίνακα 10000 τιμές, με κλειδιά i+2 και τιμές i+4 αντίστοιχα. 
            assertTrue(dict.contains(i+2)); // Ελέγχουμε ότι υπάρχει το κλειδί που μόλις προσθέσαμε.
            int size=dict.size();
            if(dict.contains(20) || dict.contains(40) || dict.contains(60)){ // Αν υπάρχει κλειδί με τιμή 20 40 ή 60 το διαγράφουμε.
                if(dict.contains(20)){
                    dict.remove(20);
                    assertTrue(dict.size()==size-1); // Εφόσον διαγράφτηκε το 20 μειώνουμε το size κατά 1.
                }
                if(dict.contains(40)){
                    dict.remove(40);
                    assertTrue(dict.size()==size-1); // Εφόσον διαγράφτηκε το 40 μειώνουμε το size κατά 1.
                }
                if(dict.contains(60)){
                    dict.remove(60);
                    assertTrue(dict.size()==size-1); // Εφόσον διαγράφτηκε το 60 μειώνουμε το size κατά 1.
                }
            }
        }
        assertFalse(dict.contains(20)); // Ελέγχουμε με ένα assertFalse ότι δεν υπάρχει πλέον το κλειδί 20 στον πίνακα κατακερματισμού.
        assertFalse(dict.contains(40)); // Ελέγχουμε με ένα assertFalse ότι δεν υπάρχει πλέον το κλειδί 40 στον πίνακα κατακερματισμού.
        assertFalse(dict.contains(60)); // Ελέγχουμε με ένα assertFalse ότι δεν υπάρχει πλέον το κλειδί 60 στον πίνακα κατακερματισμού.
        
        dict.clear();
        assertTrue(dict.size()==0); // Κάνουμε clear και ελέγχουμε ότι το size είναι 0.
        for(int i=0; i<SIZE; i++){
            dict.remove(i); // Αφαιρούμε το κλειδί i από τον πίνακα. Επειδή ο πίνακας είναι άδειος και το κλειδί i δεν υπάρχει δεν θα διαγραφτεί από τον πίνακα.
        }
        
        assertTrue(dict.isEmpty()); // Ελέγχουμε ότι το μέγεθος του πίνακα είναι 0 δηλαδή ότι πέτυχε η remove και το size έμεινε 0.
    }
    
    @Test
    public void testOpenAddressingHashTable1() {
        
        Dictionary<Integer, Integer> dict = new OpenAddressingHashTable<>();
        
        int key=5;
        dict.put(key, 1); // Πρσθέτουμε το κλειδί 5 με value 1.
        dict.put(key, 2); // Πρσθέτουμε το κλειδί 5 με value 2.
        
        assertTrue(dict.size()==1); // Ελέγχουμε ότι το size είναι 1.
        if(dict.get(key)==3){ // Αν το value του κλειδιού 5 είναι 3 τότε το κλειδί 5 προστέθηκε σωστά στον πίνακα και το κλειδί 5 με value 1 έγινε override με το νέο value το 2, άρα το νέο value είναι 3.
            assertTrue(dict.contains(key));
        }
        
        dict.remove(key);
        assertTrue(dict.size()==0); // Αφαιρούμε το κλειδί και ελέγχουμε ότι το size είναι 0.
        
        int size=0;
        for(int i=0; i<SIZE; i++){
           dict.remove(i); // Αφαιρούμε το κλειδί i, το οποίο όμως δεν θα γίνει γιατί δεν υπάρχει στον πίνακα κατακερματισμού.
           if(i==0){
               assertTrue(dict.isEmpty()); // Αν το i είναι 0 τότε ελέγχουμε ότι ο πίνακας είναι άδειος με την isEmpty, με ένα assertTrue.
           }
           if(i!=0){
               assertFalse(dict.isEmpty()); // Αν το i δεν είναι 0 τότε το size έχει αυξηθεί και ελέγχουμε ότι το dict.size() ισούται με το size. Κάθε φορά που γίνεται put η μεταβλητή size αυξάνεται κατά 1.
               assertTrue(dict.size()==size);
           }
           dict.put(i, i+10);
           size=size+1; // Προσθέτουμε με την put το κλειδί i με value i+10.
           dict.contains(i); // Ελέγχουμε με την contains ότι το κλειδί i προστέθηκε στον πίνακα κατακερματισμού.
        }
        
        dict.clear(); // Κάνουμε clear.
        
        assertTrue(dict.isEmpty());
        for(int i=0; i<SIZE; i++){
            assertFalse(dict.contains(i)); // Ελέγχουμε μετά το clear με ένα assertFalse ότι δεν υπάρχει το κλειδί i.
        }
        
        for(int i=0; i<(SIZE*100); i++){
            dict.put(i, i); // Προσθέτουμε στον πίνακα με την put το κλειδί i με value i.
            if(i==100000){ // Αν το i φτάσει το 100000 τότε διαγράφουμε τα κλειδιά από το 0 έως το 99999
               for(int j=0; i<i; j++){
                   if(dict.get(j)==j){
                       assertTrue(dict.contains(j)); // Ελέγχω πριν διαγράψω ότι υπάρχει το κλειδί με την contains.
                   }
                   dict.remove(j); // Αφού έλεγξα ότι υπάρχει το διαγράφω.
                   assertFalse(dict.contains(j)); // Ελέγχω ότι δεν υπάρχει πλέον στον πίνακα με ένα assertFalse.
               } 
            }
        }
        
        dict.clear(); // Κάνω clear.
        int size_1=0;
        for(int i=0; i<SIZE; i++){
            assertTrue(dict.get(i)==null); // Ελέγχω ότι η τιμή του κλειδιού i είναι null με ένα assertTrue, που ισχύει αφού δεν υπάρχουν κλειδιά στον πίνακα.
            dict.put(i, 2);
            size_1=size_1+1;
            assertTrue(dict.contains(i)); // Προσθέτω το κλειδί i με value 2 και ελέγχω ότι υπάρχει στον πίνακα κατακερματισμού.
            assertTrue(dict.get(i)==2); // Ελέγχω ότι η τιμή του κλειδιού i είναι 2.
            assertTrue(dict.size()==size_1); // Ελέγχω ότι το size του πίνακα ισούται με την μεταβλητή size_1 που αυξάνεται κατά 1 όταν προσθέτω ένα στοιχείο στον πίνακα κατακερματισμού.
        }
        
        for(int i=0; i<SIZE-1; i++){
            dict.remove(i); // Αφαιρώ όλα τα στοιχεία που πρόσθεσα παραπάνω εκτός από 1.
        }
        assertTrue(dict.size()==1); // Ελέγχω ότι το size είναι 1.
        dict.clear();
        assertTrue(dict.size()==0); // Εκαθαρίζω τον πίνακα και ελέγχω ότι το size του πίνακα κατακερματισμού είναι 0.
    }
    
    @Test
    public void testOpenAddressingHashTable2() {
        
       OpenAddressingHashTable<Integer, Integer> dict = new OpenAddressingHashTable<>();
       
       int firstHashCode=dict.hashFunction(5);
       int secondHashCode=dict.hashFunction(5);
       
       assertTrue(firstHashCode==secondHashCode); // Ελέγχω ότι η μέθοδος hashFunction που επιστρέφει την θέση που πρέπει να μπει το κλειδί στον πίνακα επέστρεψε ίδιο position και τις δύο φορές την κάλεσα με το κλειδί 5.
       
       dict.put(5, 1);
       
       if(dict.contains(5)){ // Βάζω το κλειδί 5 στον πίνακα, ελέγχω με την contains ότι υπάρχει και μετά το αφαιρώ.
           dict.remove(5);
       }
       
       assertFalse(dict.contains(5)); // Ελέγχω ότι δεν υπάρχει πλέον το κλειδί 5.
       
       for (int i=0; i<SIZE; i++){
           dict.put(0, i); // Προσθέτω στον πίνακα το 0 απλά κάθε φορά αυξάνεται το value του.
       }
       
       assertTrue(dict.size()==1); // Ελέγχω ότι το size του πίνακα είναι 1 αφού έχω προσθέσει μόνο το 0.
       assertTrue(dict.get(0)==SIZE-1); // Ελέγχω ότι η τιμή του 0 (value) είναι 9999
       dict.clear(); 
       assertTrue(dict.size()==0); // Κάνω clear και ελέγχω ότι το μέγεθος του πίνακα είναι 0.
       
       for (int i=0; i<SIZE; i++){
           dict.put(i, i); // Προσθέτω στον πίνακα 10000 κλειδιά.
       }
       
       for (int i=0; i<(int)SIZE/500; i++){
           if(i%2==0){ 
            dict.remove(i); // Αφαιρώ τα άρτια κλειδιά που βρίσκονται στις πρώτες 500 θέσεις. Ο πίνακας κατακερματισμού δεν θα μειωθεί σε μέγεθος γιατί πρίν του βάλαμε 10000 στοιχεία και του αφαιρούμε μόνο 500.
           }
       }
       
       for (int i=0; i<(int)SIZE/500; i++){
           if(i%2==1){
               assertTrue(dict.contains(i)); // Ελέγχω ότι τα περιττά κλειδιά που βρίσκονται στις πρώτες 500 θέσεις στον πίνακα δεν διαγράφτηκαν.
           }
       }
       
       dict.clear(); // Κάνω clear.
       int tmp=0;
       int count=0;
       for (int i=0; i<SIZE; i++){
           if(i==0){
               tmp=dict.hashFunction(0); // Αν το i είναι 0 κρατάω στην tmp το position που επιστρέφει η hashFunction για το κλειδί 0.
           }
           if(i!=0){
            if(dict.hashFunction(0)==tmp){
                count++; // Ελέγχω για κάθε i εκτός του 0 ότι το hashFunction(0) ισούται με το tmp, που θα ισχύει πάντα και αυξάνω το count κατά 1.
            }
           }
       }
       assertTrue(count==SIZE-1); // Ελέγχω ότι το count ισούται με 9999.
       
       int pos1=dict.hashFunction(5);
       int pos2=dict.hashFunction(6);
       
       if(pos1==pos2){
           assertTrue(pos1==pos2); // Αν η θέση που πρέπει να μπει το κλειδί 6 είναι ίδια με αυτή που πρέπει να μπει το 5 τότε έχει γίνει σύγκρουση και το 6 πρέπει να μπει δεξιά του 5 στην επόμενη διαθέσιμη θέση.
       }
       if(pos1!=pos2){
           assertTrue(pos1!=pos2); // Αν η θέση που πρέπει να μπει το κλειδί 6 δεν είναι ίδια με αυτή που πρέπει να μπει το 5 τότε δεν έχει γίνει σύγκρουση.
       }
       
       dict.clear();
       int prev=0;
       int pos=0;
       for (int i=0; i<SIZE; i++){
           if(i==0){
            pos=dict.hashFunction(i);
            prev=dict.hashFunction(i); // Διατρέχω με ένα for loop από το 0 έως το 9999 και αποθηκεύω αρχικά αν το i είναι 0 στις μεταβλητές pos και prev το hashFunction του i δηλαδή το hashFunction(0).
           }
           if(i!=0){
               pos=dict.hashFunction(i);
               if(pos==prev){ // Αν το i δεν είναι 0 και το pos ισούται με το prev τότε έχει γίνει σύγκρουση. Π.χ αν για το κλειδί i=0 η HashFunction επιστρέψει ίδιο position με το i=1 τότε έχει γίνει σύγκρουση.
                   assertTrue(pos==prev); 
               }
               assertFalse(pos==prev); // Αν το i δεν είναι 0 και το pos δεν ισούται με το prev τότε δεν έχει γίνει σύγκρουση και συνεχίζουμε.
               prev=pos;
           }
       }
       
       dict.clear(); // Κάνω clear.
       int[] holdPositions= new int[SIZE]; // Κρατάω σε ένα πίνακα με όνομα holdPositions το position που θα επιστρέψει η HashFunction για κάθε i.
       boolean collision=false;
       for(int i=0; i<SIZE; i++){
           holdPositions[i]=dict.hashFunction(i);
           if(i>0 && holdPositions[i]==holdPositions[i-1]){ // Αν το i>0 και το position(περιεχόμενο που βρίσκεται στην προηγούμενη θέση του πίνακα holdPositions)είναι ίδιο με αυτό της θέσης που βρίσκεται τώρα, έχει γίνει σύγκρουση. 
               collision=true;
               assertTrue(collision);
           }
           collision=false;
           dict.put(i, i); // Πρόσθηκη του στοιχείου στον πίνακα κατακερματισμού.
       }
       
       
       
    }
    
}
