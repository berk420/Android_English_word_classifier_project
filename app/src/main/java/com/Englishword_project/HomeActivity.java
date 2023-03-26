package com.Englishword_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PaintFlagsDrawFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {

    DBHelper DB;
    Button pdf;

    public String pdfdata;
    TextView showpdf,kolayk,ortak,zork,ornk,spinnerdata;
    public String[] takendata = new String[3];
    public String extractedText,anketsoru1_data,anketsoru2_data;
    public int kull_anket_dur=0;
    Spinner anketsoru2;
    public String anketenablity;


    EditText anketsoru1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        pdf = (Button) findViewById(R.id.pdf);
        kolayk=findViewById(R.id.kolayk);
        ortak=findViewById(R.id.Ortak);
        zork=findViewById(R.id.Zork);
        ornk=findViewById(R.id.textView);
        anketsoru2=findViewById(R.id.anketsoru2);

        anketsoru1=findViewById(R.id.Anketsoru1);
        spinnerdata=findViewById(R.id.spinnerdata);


        String[] meslek = new String[]{"ogretmen", "ogrenci"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(HomeActivity.this, android.R.layout.simple_spinner_dropdown_item, meslek);
        anketsoru2.setAdapter(adapter);

        ActivityCompat.requestPermissions(  this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PackageManager.PERMISSION_GRANTED);


        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, 1);
                }
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            takendata = bundle.getStringArray("data");
            anketenablity = bundle.getString("anketvis");

        }

        ornk.setText(anketenablity);

        /*
        if (anketenablity.equals("0")){
            anketsoru2.setEnabled(false);
            anketsoru1.setEnabled(false);
        }


         */

//Bu da veri tabanına koyacağımız anket verilerini aldık
         anketsoru1_data = anketsoru1.getText().toString();

        anketsoru2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //veri tabanına eklenecek spinner verimiz
                anketsoru2_data = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                // PDF dosyasını oku
                PdfReader reader = new PdfReader(inputStream);

                // PDF dosyasından text ayıkla
                PdfReaderContentParser parser = new PdfReaderContentParser(reader);
                TextExtractionStrategy strategy;
                StringBuilder text = new StringBuilder();

                for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                    strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
                    text.append(strategy.getResultantText());
                }

                // Ayıklanan text'i String'e dönüştür
                extractedText = text.toString();

                try {
                    pdfdata=showpdf.getText().toString();
                }
                catch (Exception e){
                    Toast.makeText(HomeActivity.this, "e", Toast.LENGTH_SHORT).show();

                }

                DB = new DBHelper(HomeActivity.this);

                //Main activity sayfasından kullanıcının verileri veri tabanına kayıt olamk için geliyor






                ArrayList<String> Kolay_words = new ArrayList<String>();
                ArrayList<String> Orta_words = new ArrayList<String>();
                ArrayList<String> Zor_words = new ArrayList<String>();

                String[] words = extractedText.split("\\s+");

                for (String word : words) {
                    String level = calculateLevel(word);
                    if (level.equals("Kolay")) {
                        Kolay_words.add(word);
                    } else if (level.equals("Orta")) {
                        Orta_words.add(word);
                    } else if (level.equals("Zor")) {
                        Zor_words.add(word);
                    }
                }

                //kendini tekrar eden kelimeleri kaldırmak için hash set kullandık
                HashSet<String> hashSet_kolay = new HashSet<String>(Kolay_words);
                ArrayList<String> Kolay_words_uniq = new ArrayList<String>(hashSet_kolay);

                HashSet<String> hashSet_orta = new HashSet<String>(Orta_words);
                ArrayList<String>  Orta_words_uniq= new ArrayList<String>(hashSet_orta);

                HashSet<String> hashSet_zor = new HashSet<String>(Zor_words);
                ArrayList<String>  Zor_words_uniq= new ArrayList<String>(hashSet_zor);


                kolayk.setText(Kolay_words_uniq.toString());
                ortak.setText(Orta_words_uniq.toString());
                zork.setText(Zor_words_uniq.toString());



                //eğerki kullanıcı Main activityden gelirse zaen intent den gelen  "frommainac[2]" string verisi 1 olacak (Bunu da takendata[2] olarak alıyoruz)
















                Toast.makeText(HomeActivity.this, "takendatas ile veri tbanına veri koyuldu  ", Toast.LENGTH_SHORT).show();
                boolean success =DB.insertData(takendata[0],takendata[1],anketsoru1_data,anketsoru2_data,extractedText,Kolay_words_uniq.toString(),Orta_words_uniq.toString(),Zor_words_uniq.toString());

                Toast.makeText(HomeActivity.this, "Veri tabanına :"+success, Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static String calculateLevel(String word) {
        String level = "";

        if (isA1(word) || isA2(word)) {
            level = "Kolay";
        } else if (isB1(word) || isB2(word)) {
            level = "Orta";
        } else if (isC1(word) || isC2(word)) {
            level = "Zor";
        }


        return level;
    }

    public static boolean isA1(String word) {
        // A1 seviye kelimelerin örnek listesi: https://www.bbc.co.uk/bitesize/guides/zg8gbk7/revision/1
        String[] a1Words = {"apple", "book", "cat", "dog", "egg", "fish", "girl", "hat", "ice", "juice"};
        for (String a1Word : a1Words) {
            if (word.equalsIgnoreCase(a1Word)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isA2(String word) {
        // A2 seviye kelimelerin örnek listesi: https://www.bbc.co.uk/bitesize/guides/zq3dwmn/revision/2
        String[] a2Words = {"bag", "box", "cloud", "desk", "elephant", "flower", "glass", "hotel", "island", "growing"};
        for (String a2Word : a2Words) {
            if (word.equalsIgnoreCase(a2Word)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isB1(String word) {
        // B1 seviye kelimelerin örnek listesi: https://www.bbc.co.uk/bitesize/guides/zyq3k2p/revision/2
        String[] b1Words = {"adventure", "bored", "celebrate", "dangerous", "enjoy", "famous", "generous", "history", "important", "journey"};
        for (String b1Word : b1Words) {
            if (word.equalsIgnoreCase(b1Word)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isB2(String word) {
        // B1 seviye kelimelerin örnek listesi: https://www.bbc.co.uk/bitesize/guides/zyq3k2p/revision/2
        String[] b2Words = {"adequate", "comprehensive", "conscious", "diverse", "equivalent", "innovative", "prosperous", "sophisticated", "sustainable", "versatile","data"};
        for (String b2Word : b2Words) {
            if (word.equalsIgnoreCase(b2Word)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isC1(String word) {
        // B1 seviye kelimelerin örnek listesi: https://www.bbc.co.uk/bitesize/guides/zyq3k2p/revision/2
        String[] c1Words = {"abundance", "ambiguity", "coherence", "diligence", "eloquence", "expedite", "indispensable", "intricacy", "paradoxical", "quintessential","accelerating","insights"};
        for (String c1Word : c1Words) {
            if (word.equalsIgnoreCase(c1Word)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isC2(String word) {
        // B1 seviye kelimelerin örnek listesi: https://www.bbc.co.uk/bitesize/guides/zyq3k2p/revision/2
        String[] c2Words = {"acquiesce", "circumspect", "disseminate", "efficacious", "fastidious", "magnanimous", "ostensibly", "perfidious", "sagacious", "ubiquitous","reliant","artificial intelligence"};
        for (String c2Word : c2Words) {
            if (word.equalsIgnoreCase(c2Word)) {
                return true;
            }
        }
        return false;
    }


}