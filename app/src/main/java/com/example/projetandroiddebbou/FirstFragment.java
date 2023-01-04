package com.example.projetandroiddebbou;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.projetandroiddebbou.databinding.FragmentFirstBinding;

import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private List<Photo> listePhotos;
    private int positionListe=0;
    final private int nbrElementsListe=5;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        /*final LinearLayout lm = (LinearLayout) findViewById(R.id.linearMain);*/



        //recupere les photos de la BD
        sqlLiteHelper db = new sqlLiteHelper(getContext());
        listePhotos = db.getAllPhotos();
        db.close();
        //remplirLLFragment(0,nbrElementsListe);
        actualiserListe();
        return binding.getRoot();
    }



    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
        binding.buttonPrecedent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionListe = positionListe-nbrElementsListe;
                if (positionListe<0){
                    positionListe=0;
                }
                actualiserListe();
            }
        });
        binding.buttonSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listePhotos.size()>positionListe+nbrElementsListe){
                    positionListe = positionListe+nbrElementsListe;
                    //todo : actualise bien ici ?
                    actualiserListe();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onResume(){
        super.onResume();
        actualiserListe();
    }
    public void actualiserListe(){
        binding.llFragment1.removeAllViews();
        remplirLLFragment(positionListe);
    }

    public void remplirLLFragment(int indiceDebut){
        // create the layout params that will be used to define how your
        // button will be displayed
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //cree les params pour chaque blocs
        LinearLayout.LayoutParams paramsBloc = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Log.d("lifecycle", "debut : "+indiceDebut+"   fin : "+indiceDebut+nbrElementsListe);
        int j = 0;
        int indiceMax = indiceDebut+nbrElementsListe;
        if(indiceMax>listePhotos.size()){
            indiceMax=listePhotos.size();
        }
        for (int i = indiceDebut;i<indiceMax;i++) {
            Log.d("lifecycle", "creation item "+i);
            Photo photo = listePhotos.get(i);

            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setLayoutParams(paramsBloc);
            // Create TextView
            TextView nom = new TextView(getActivity());
            nom.setText(photo.getNom());

            //binding.llFragment1.addView(nom);
            layout.addView(nom);

            // Create TextView
            TextView groupe = new TextView(getActivity());
            groupe.setText("("+photo.getGroupe()+")");
            //binding.llFragment1.addView(groupe);
            layout.addView(groupe);

            Drawable d ;
            try{
                d= Drawable.createFromPath(photo.getCheminPhoto());
                ImageView iv = new ImageView(getActivity());
                iv.setImageDrawable(d);
                layout.addView(iv);
            }catch (Exception e){
                //todo : d = image par defaut
            }
            // Create Button
/*            final Button btn = new Button(getActivity());
            // Give button an ID
            btn.setId(j);
            btn.setText("text");
            // set the layoutParams on the button
            btn.setLayoutParams(params);

            final int index = j;
            // Set click listener for button
            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Log.i("TAG", "index :" + index);

                }
            });
                        //Add button to LinearLayout
            //binding.llFragment1.addView(btn);
            layout.addView(btn);
            */

            final int index = j;
            // Set click listener for layout
            layout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Log.i("TAG", "index :" + index);

                }
            });

            binding.llFragment1.addView(layout);
            j++;
        }
    }


}