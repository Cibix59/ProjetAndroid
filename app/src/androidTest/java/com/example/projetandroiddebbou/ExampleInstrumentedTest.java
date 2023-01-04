package com.example.projetandroiddebbou;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.projetandroiddebbou", appContext.getPackageName());
    }

    @Test
    public void testBDD(){//verifie qu'un objet stocké dans la bdd conserve bien tout ses attribut et qu'il est possible de le récuperer apres
        String cheminPhoto="chemin";
        double latitude = 10.0;
        double longitude = 20.0;
        String groupe="groupeTest";
        String nom="nomTest";

        //creer un objet, le stocker dans la bdd, l'extraire et verifier que c'est toujours le même objet
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        sqlLiteHelper db = new sqlLiteHelper(appContext);
        long id = db.addPhoto(new Photo(cheminPhoto,latitude,longitude,groupe,nom));
        Photo retourTest = db.getPhoto(id);
        db.deletePhoto(id);
        db.close();

        assertEquals(cheminPhoto,retourTest.getCheminPhoto());
        assertEquals(latitude,retourTest.getLatitude(),0.001);
        assertEquals(longitude,retourTest.getLongitude(),0.001);
        assertEquals(groupe,retourTest.getGroupe());
        assertEquals(nom,retourTest.getNom());

    }
}