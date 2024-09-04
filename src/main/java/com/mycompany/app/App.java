/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.mycompany.app;

import com.mycompany.app.Dictionary.Entry;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author JohnBaizanis
 */
public class App {

    public static void main(String[] args) throws FileNotFoundException { 
        
        if(args.length<1){
            System.out.println("Please provide a txt file"); // Ελέγχω ότι ο χρήστης έχει δώσει μια παράμετρο. Ο χρήστης πρέπει να δώσει αρχείο.
            System.exit(1);
        }
        
        Dictionary<String, Integer> dict = new OpenAddressingHashTable<>();
		
		try (Scanner scanner = new Scanner(new File(args[0]))) {
			while(scanner.hasNext()) {
				String line = scanner.nextLine();
				StringTokenizer st = new StringTokenizer(line);
				while(st.hasMoreTokens()) { 
					String word  = st.nextToken();
					Integer curFreq = dict.get(word);
					if (curFreq == null) {
						curFreq = 1;
					} else {
						curFreq++;
					}
					dict.put(word, curFreq);
				}
			}
		}
                
                int count=0;
                for(Entry<String, Integer> e: dict) { 
			count=count+e.getValue(); // Μέτρηση των εμφάνισεων κάθε λέξης και πρόσθεση στην μεταβλητή count.
		}
                System.out.printf("Total words:%d%n", count); // Εμφάνιση του πλήθους των λέξεων που διαβάστηκαν.
		for(Entry<String, Integer> e: dict) { 
			System.out.println("Word " + e.getKey() + " appeared " + e.getValue() + " times");
		}
        System.exit(0);
    }
}
