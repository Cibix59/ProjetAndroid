package com.example.projetandroiddebbou;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import android.content.Context;

public class testUnaire {
    @Test
    public void testMethod() {
        // code de test ici
        // déclarez les variables et les objets dont vous avez besoin pour le test
        int a = 5;
        int b = 10;
        int expectedSum = 15;

        // exécutez le code que vous souhaitez tester
        int sum = a + b;

        // vérifiez si le résultat est correct en utilisant JUnit
        assertEquals(expectedSum, sum);
        //todo creer un objet, le stocker dans la bdd, l'extraire et verifier que c'est toujours le même objet
    }
}
