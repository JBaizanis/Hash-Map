/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app;

import com.mycompany.app.Dictionary.Entry;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 *
 * @author JohnBaizanis
 */
public class OpenAddressingHashTable<K,V> implements Dictionary<K,V> {
    
    /*Δήλωση μεταβλητών*/
    private int[][] h; 
    private Entry<K,V>[] hashTable; 
    private int[] input; // Πίνακας που θα περιέχει τα bit εισόδου.
    private int[] output; // Πίνακας που θα περιέχει τα bit εξόδου.
    private int size; // Πλήθος στοιχείων που έχουν αποθηκευτεί στον πίνακα κατακερματισμού.
    private static final int INITIAL_SIZE = 4;
    private static int FIRST_SIZE=0;
    Random random = new Random(17);
    
    @SuppressWarnings("unchecked")
    public OpenAddressingHashTable(int m){
        
        this.size=0;
        this.hashTable = (Entry<K,V>[]) new Entry[(int)Math.pow(2, m)]; // Ο πίνακας που θα αποθηκευτούν τα Entries θα έχει μέγεθος 2^m. Δηλαδή 2^INITIAL_SIZE.
        FIRST_SIZE=hashTable.length;
        for (int i=0; i<hashTable.length; i++){
            EntryImpl<K,V> newEntry = new EntryImpl<>(null,null); // Γεμίζω τον πίνακα με null τιμές κλειδιών και τιμών.
            newEntry.key=null;
            newEntry.value=null;
            hashTable[i]=newEntry;
        }
        
        this.h = new int[m][32];
        this.input = new int[32];
        this.output = new int[32];
        for (int i=0; i<m; i++){
            for (int j=0; j<32; j++){
                h[i][j]= (int) (10*Math.random()) & 1; // Γεμίζω τον πίνακα h με 0 και 1.
            }
        } 
    }
    
    @SuppressWarnings("unchecked")
    public OpenAddressingHashTable(){
        this(INITIAL_SIZE);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void put(K key, V value) {
        
        if(key==null){ // Αν το κλειδί που θέλει να βάλει ο χρήστης είναι null επιστρέφουμε, ο πίνακας κατακερματισμού δεν δέχεται null κλειδιά.
            return;
        }
        
        rehashIfNeeded(); // Καλώ την μέθοδο rehashIfNeeded για να ελέγξω αν χρειάζεται επανακατακερματισμός.
        insert(key,value); // Καλώ την insert ώστε να προσθέσει το κλειδί και την τιμή του στον πίνακα κατακερματισμού.
    }
    
    public void insert(K key, V value){
        
        boolean foundSpot=false;
        int position=hashFunction(key); // Καλώ την HashFunction για να μου επιστρέψει σε ποια θέση του πίνακα hashTable θα εισαχθεί το κλειδί.

        while(foundSpot==false){ // Όσο το κλειδί δεν έχει εισαχθεί σε κάποια θέση του πίνακα το while loop δεν τερματίζει.
            if(hashTable[position].getKey()==null){ // Αν η θέση στο hashTable, που θέλω να βάλω το κλειδί είναι null, απλά προσθέτω το κλειδί σε αυτή τη θέση.
                EntryImpl<K,V> newEntry = new EntryImpl<>(key,value);
                hashTable[position]=newEntry;
                size++; // Εφόσον προστέθηκε νέο στοιχείο στον πίνακα, αυξάνω το size.
                foundSpot=true;
            }
            if(key.equals(hashTable[position].getKey())){ // Αν η θέση που θέλω να βάλω το κλειδί δεν είναι null και το κλειδί που θέλω να βάλω υπάρχει ήδη στο hashTable τότε κάνω override την παλιά τιμή του κλειδιού με την καινούρια.
                EntryImpl<K,V> newEntry = new EntryImpl<>(key,value);
                hashTable[position]=newEntry;
                foundSpot=true;
            }
            if(foundSpot==false){ // Αν δεν βρέθηκε θέση, μεταβαίνουμε στην επόμενη. Αν φτάσουμε στο τέλος του πίνακα, τότε ψάχνουμε στην αρχή του, γι'αυτό γίνεται η παρακάτω πράξη.
                position=(position+1)%hashTable.length;
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public V remove(K key) {
        
        if(key==null){ // Αν το κλειδί που θέλει να διαγράψει ο χρήστης είναι null επιστρέφουμε, ο πίνακας κατακερματισμού δεν δέχεται null κλειδιά άρα δεν μπορώ να διαγράψω null κλειδί γιατί ο πίνακας κατακερματισμού έχει αρχικοποιηθεί με null keys και values οπότε δεν πρέπει να διαγραφτεί κάποια κενή θέση του πίνακα, δηλαδή με null key και value.
            return null;
        }
        
        rehashIfNeeded(); // Καλώ την μέθοδο rehashIfNeeded για να ελέγξω αν χρειάζεται επανακατακερματισμός.
        
        V value;
        EntryImpl<K,V> deletedEntry = new EntryImpl<>(null,null);
        for (int i=0; i<hashTable.length; i++){
            
            if(key.equals(hashTable[i].getKey())){ // Ψάχνω στο hashTable για να βρω το κλειδί που θέλει να διαγράψει ο χρήστης.
                value=hashTable[i].getValue(); // Αν βρεθεί τότε κρατάω σε μια μεταβλητή την τιμή του κλειδιού(value) για να την επιστρέψω στον χρήστη.
                hashTable[i]=deletedEntry; // Στην θέση του στοιχείου που διαγράφτηκε βάζω null.
                size--; // Μειώνω το size, αφού αφαιρέθηκε ένα κλειδί.
                hashTableRearrangement(i); // Καλώ την hashTableRearrangement ώστε να δει αν χρειάζετε τα στοιχεία που βρίσκονται μετά το κλειδί που διαγράφτηκε, να αλλάξουν θέση.
                return value; // Επιστρέφω στον χρήστη την τιμή του κλειδιού που διέγραψε.
            }
        }
        
        return null; // Αν η τιμή δεν βρεθεί επιστρέφω null.
    }

    @Override
    public V get(K key) {
        
        for (int i=0; i<hashTable.length; i++) {
            if (key.equals(hashTable[i].getKey())){ // Διατρέχω τον πίνακα ώστε να επιστρέψω στον χρήστη την τιμή του κλειδιού(value) που ζήτησε.
                return hashTable[i].getValue();
            }
        }
        return null; // Αν δεν υπάρχει το κλειδί τότε, αντί για τιμή επιστρέφεται null.
    }

    @Override
    public boolean contains(K key) {

        for (int i=0; i<hashTable.length; i++) {
            if (key.equals(hashTable[i].getKey())) { // Διατρέχω τον πίνακα ώστε να επιστρέψω στον χρήστη true, αν το κλειδί που ζήτησε υπάρχει στον πίνακα.
                
                return true;
            }
        } 
        return false; // Αν το κλειδί δεν υπάρχει επιστρέφω false.
    }

    @Override
    public boolean isEmpty() {
        return size == 0; // Αν το size είναι 0 τότε ο πίνακας δεν περιέχει στοιχεία.
    }

    @Override
    public int size() {
        
        return size; // Επιστρέφω το πλήθος των στοιχείων που είναι αποθηκευμένα στον πίνακα.
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        
        this.size=0; // Μηδενίζω το πλήθος των στοιχείων του πίνακα κατακερματισμού.
        EntryImpl<K,V> nullEntry = new EntryImpl<>(null,null);
        for (int i=0; i<hashTable.length; i++){ // Γεμίζω τον πίνακα κατακερματισμού με nulls αφού κλήθηκε η clear.
            hashTable[i]=nullEntry;
        }
    }
    
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableIterator(); // Επιστρέφω τον Iterator του hashTable που είναι υλοποιημένος στην κλάση HashTableIterator.
    }
    
    private class HashTableIterator implements Iterator<Entry<K,V>>{
        
        private int cur; // Ακέραιος που δείχνει στην πρώτη θέση του πίνακα.
        
        public HashTableIterator(){
            this.cur=0;
        }

        @Override
        public boolean hasNext() {
            
            while(cur<hashTable.length && hashTable[cur].getKey()==null){ // Όσο ο ακέραιος είναι μικρότερος του μεγέθους του hashTable και όσο δεν υπάρχει στην θέση cur του hashTable κλειδί τότε μεταβαίνουμε στην επόμενη θέση μέχρι να βρούμε κλειδί.
                 
                cur++;
            }
            
            return cur<hashTable.length; // Στην θέση cur του hashTable βρέθηκε κλειδί που δεν είναι null και εφόσον το cur είναι < hashTable.length επιστρέφουμε true.
        }

        @Override
        public Entry<K, V> next() {
            if(!hasNext()){ // Αν δεν υπάρχει επόμενο στοιχείο, εμφανίζουμε exception.
                throw new NoSuchElementException();
            }
            while(hashTable[cur].getKey()==null){ // Άν η θέση cur του hashTable περιέχει null κλειδί τότε πάμε στην επόμενη για να δούμε αν υπάρχει κλειδί που δεν είναι null.
                cur++;
            }
            return hashTable[cur++]; // Αν υπάρχει κλειδί που δεν είναι null στην θέση cur το επιστρέφουμε και αυξάνουμε το cur κατά 1.
        }
        
        
    }
    
    public int hashFunction(K key){
        
        
        int hashcode;
        int position=0;
        int count=0;
        int i=0;
        
        hashcode=Math.abs(key.hashCode()); // Παίρνουμε την απόλυτη τιμή hashCode του κλειδιού(Χρησιμοποείται η απόλυτη τιμή γιατί η hashCode επιστρέφει και αρνητικούς αριθμούς) που θέλουμε να βάλουμε στον πίνακα.
        
        while (hashcode!=0){ // Μετατρέπω το hashCode σε 0 και 1 και το βάζω στον πίνακα input.
            
            input[i]=hashcode%2;
            count++; // Μετράω το πλήθος των εισαγωγών στον πίνακα input. Από την τελευταία εισαγωγή και μετά, δηλαδή αν το count είναι 10, τότε από την θέση 10 στο πίνακα input και μετά βάζω 0, που γίνεται παρακάτω.
            hashcode=(int)hashcode/2;
            i++;
        }
        for (int j=count; j<input.length; j++){
            input[j]=0;
        }
        
        int l=0;
        int sum=0;
        for (int j=0; j<h.length; j++){
            for (int k=0; k<32; k++){
                sum = (sum + (h[j][k]*input[l]))%2; // Πολλαπλασσιάζω την k στήλη του τυχαίου πίνακα h με την l γραμμή του πίνακα input. Επειδή, όμως κάνουμε δυδαδική πρόσθεση αυτή γίνεται με mod 2.
                l++;
            }
            output[j]=sum; // Βαζώ στην θέση j του πίνακα output το προηγούμενο αποτέλεσμα.
            l=0; // Θέτω πάλι το l=0 ώστε να πολλαπλασσιάσω την επόμενη γραμμή του πίνακα h πάλι με τον πίνακα input από την αρχή.
        }
        
        for (int j=output.length-1; j>-1; j--){
            position=position+(output[j]*(int)Math.pow(2, j)); // Βρίσκω την θέση που θα αποθηκευτεί το κλειδί στον πίνακα κατακερματισμού πολλαπλασσιάζοντας το αποτέλεσμα που βρίσκεται στην θέση j του πίνακα με την αντίστοιχη δύναμη 2^j.
        }
        
        for (int k=0; k<input.length; k++){
            input[k]=0;
            output[k]=0;
        }
        
        return position; // Επιστρέφω την θέση που πρέπει να αποθηκευτεί το στοιχείο στον πίνακα κατακερματισμού.
    }
    
    public void newHashFunction(int m){ // Όταν έχει κλειθεί αυτή η μέθοδος, σημαίνει ότι πρέπει να γίνει επανακατακερματισμός, αρά γεμίζουμε τον πίνακα h πάλι με 0 και 1.
        
       this.h = new int[m][32]; // Αν ο πίνακας κατακερματισμού έχει διπλασσιαστεί σε μέγεθος τότε αυξάνεται σε σχέση με πριν το πλήθος των γραμμών του πίνακα h κατά 1, αντίστοιχα αν ο πίνακας κατακερματισμού έχει υποδιπλασσιαστεί το πλήθος των γραμμών του πίνακα h μειώνεται κατά 1.
       for (int i=0; i<h.length; i++){
            for (int j=0; j<32; j++){
                h[i][j]= (int) (10*Math.random()) & 1;
            }
       }
    }
    @SuppressWarnings("unchecked")
    public void rehashIfNeeded(){
        
        int newLength=0;
        int newHashFunctionArrayLength=0; 
        boolean resized=false;
        int doubled=0;

        if(((double)size/hashTable.length)>=0.25 && ((double)size/hashTable.length)<0.5){ // Αν το πλήθος των στοιχείων που έχουν αποθηκευτεί στον πίνακα δεν είναι μικρότερο από 0,25 ή μεγαλύτερο από 0,5 της χωρητικότητας του πίνακα δεν χρειάζεται να γίνει επανακατακερματισμός.
            return;
        }
        if(size>=(hashTable.length/2)){ // Αν το πλήθος των στοιχείων που έχουν αποθηκευτεί στον πίνακα κατακερματισμού είναι >= του μισού του μεγέθους τότε ο πίνακας κατακερματισμού διπλασσιάζεται.
           newLength=hashTable.length*2;
           newHashFunctionArrayLength=h.length+1; // Αφού ο πίνακας κατακερματισμού διπλασσιάστηκε, αυξάνουμε το πλήθος των γραμμών του πίνακα h που περιέχει 0 και 1 κατά 1.
           doubled=1;
           resized=true; 
        }
        if((size/hashTable.length)<0.25 && hashTable.length>=(2*FIRST_SIZE) && doubled==0){ // Αν το πλήθος των στοιχείων που έχουν αποθηκευτεί στον πίνακα δεν ξεπερνούν το 25% της χωρητικότητας του και το μέγεθος του πίνακα κατακερματισμού είναι >= από το διπλάσσιο αρχικό μέγεθος του πίνακα κατακερματισμού και ταυτόχρονα αν ο πίνακας δεν διπλασσιάστηκε στο προηγούμενο if τότε υποδιπλασσιάζουμε το μέγεθος του πίνακα κατακερματισμού.
            newLength=hashTable.length/2;
            newHashFunctionArrayLength=h.length-1; // Αφού ο πίνακας κατακερματισμού υποδιπλασσιάστηκε, μειώνουμε το πλήθος των γραμμών του πίνακα h που περιέχει 0 και 1 κατά 1.
            resized=true; 
        }
        if(resized==false){ // Αν ο πίνακας κατακερματισμού δεν διπλασσιάστηκε ούτε υποδιπλασσιάστηκε σε μέγεθος τότε δεν χρειάζεται επανακατακερματισμός.
            return;
        }
        
        newHashFunction(newHashFunctionArrayLength); // Καλώ την newHashFunction ώστε να γεμίσει τον πίνακα h με 0 και 1 από την αρχή με το νέο πλήθος γραμμών, είτε αυξημένο κατά 1 είτε μειωμένο κατά 1. 
        EntryImpl<K,V> tmpEntry = new EntryImpl<>(null,null);
        Entry<K,V>[] oldHash=hashTable; // Αναθέτω σε ένα προσωρινό πίνακα με όνομα oldHash τον τρέχον πίνακα κατακερματισμού.
        this.size=0; // Μηδενίζω το μέγεθος του πίνακα κατακερματισμού.
        this.hashTable=(Entry<K,V>[]) new Entry[newLength]; // Κατασκευάζω από την αρχή τον πίνακα κατακερματισμού hashTable με το νέο μέγεθος.
        for (int i=0; i<hashTable.length; i++){
            hashTable[i]=tmpEntry; // Αρχικοποίηση του πίνακα κατακερματισμού hashTable με null Entries, δηλαδή null keys και values.
        }
        for (int j=0; j<oldHash.length; j++){
            if(oldHash[j].getKey()!=null){ // Αν η θέση i του oldHash που περιέχει τα στοιχεία του hashTable πριν δημιουργηθεί ξανά με νέο μέγεθος δεν περιέχει κλειδί null, τότε καλείται η insert ώστε να τοποθετήσει το Entry στο hashTable. Καλείται η insert και όχι η put γιατί δεν θέλουμε να γίνει έλεγχος ξανά για επανακατακερματισμό αφού ήδη έχει γίνει.
                insert(oldHash[j].getKey(), oldHash[j].getValue());
            }
        }
        
    }
    @SuppressWarnings("unchecked")
    public void hashTableRearrangement(int position){ // Η hashTableRearrangement καλείται ώστε μετά από την διαγραφή ενός στοιχείου, να εξετάζεται αν τα στοιχεία που βρίσκονται δεξιά από αυτό (και στην αρχή του πίνακα, κυκλικά) χρειάζονται επανατοποθέτηση σε άλλη θέση καθώς μπορεί κατά την εισαγωγή τους να συγκρούστηκαν με άλλο στοιχείο.
        
        EntryImpl<K,V> nullEntry = new EntryImpl<>(null,null);
        
        int startPosition=(position+1)%hashTable.length; // Θα ξεκίνησω να ψάχνω από την επόμενη θέση, από αυτήν που διαγράφτηκε το στοιχείο.
        
        while(hashTable[startPosition].getKey()!=null){ // Αν βρεθεί θέση στον πίνακα που το κλειδί είναι null σταματάμε καθώς δεν έχει γίνει σύγκρουση, άρα τα Entries στον πίνακα δεν χρειάζονται επανατοποθέτηση.
            
            EntryImpl<K,V> tmpEntry = new EntryImpl<>(hashTable[startPosition].getKey(), hashTable[startPosition].getValue()); // Αν η συνθήκη στο while loop ικανοποιήθηκε τότε βάζω στην θέση του επόμενου στοιχείου null keys και values αφού έχω κρατήσει σε προσωρινή μεταβλητή τις τιμές τους ώστε να καλέσω την put για να τις επανατοποθετήσει στον πίνακα.
            hashTable[startPosition]=nullEntry; 
            size--;
            put(tmpEntry.key,tmpEntry.value);
            startPosition=(startPosition+1)%hashTable.length; // Πάμε στην επόμενη θέση
        }
    }
    
    private static class EntryImpl<K,V> implements Dictionary.Entry<K,V>{
            
            private K key;
            private V value;

            public EntryImpl(K key, V value) {
                this.key = key;
                this.value = value;
            }

            @Override
            public K getKey() {

                return key;
            }

            @Override
            public V getValue() {

                return value;
            }
    }
    
}
