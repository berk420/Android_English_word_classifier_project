package com.Englishword_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    Button pdf,comic,pay;
    TextView kolayk,ortak,zork;
    EditText anketsoru1,anketsoru3;
    private static final int PICK_PDF_FILE = 1;
    public String receivedData,anket_onay,username,extractedText,anketsoru1_data,anketsoru2_data,anketsoru3_data,anketsoru4_data,take;
    public int butonsecim=0;
    public HashSet<String> hashSet_kolay = new HashSet<>();
    public HashSet<String> hashSet_orta = new HashSet<>();
    public HashSet<String> hashSet_zor = new HashSet<>();
    public String [] gelenveri;
    TextView textView;
    public StringBuilder extractedText_resim = new StringBuilder();

    private RecyclerView recyclerView_kolay,recyclerView_orta,recyclerView_zor;
    private static final String[] mediumWords = {"medium"};
    private static final String[] hardWords = {"hard"};


    private static final String[] easyWords = {"easy"};
    PaymentSheet paymentsheet;
    String paymentIntentClientSecret;
    PaymentSheet.CustomerConfiguration configuration;



    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pdf = findViewById(R.id.pdf);
        comic=findViewById(R.id.comic);
        kolayk=findViewById(R.id.kolayk);
        ortak=findViewById(R.id.Ortak);
        zork=findViewById(R.id.Zork);
        pay=findViewById(R.id.pay_now);


        recyclerView_zor = findViewById(R.id.recyclerView_zor);
        recyclerView_zor.setLayoutManager(new LinearLayoutManager(this));

        recyclerView_orta = findViewById(R.id.recyclerView_orta);
        recyclerView_orta.setLayoutManager(new LinearLayoutManager(this));

        recyclerView_kolay = findViewById(R.id.recyclerView_kolay);
        recyclerView_kolay.setLayoutManager(new LinearLayoutManager(this));


        comic.setEnabled(false);
        fetchApi();
        paymentsheet=new PaymentSheet(this,this::onPaymentSheetResult);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (paymentIntentClientSecret != null){
                paymentsheet.presentWithPaymentIntent(paymentIntentClientSecret,new PaymentSheet.Configuration("Ödeme ekranı"
                        ,configuration));
                }
                else{
                    Toast.makeText(getApplicationContext(), "Api yukleniyor", Toast.LENGTH_SHORT).show();
                }
            }
        });


        pdf.setOnClickListener(v -> {
            butonsecim=1;
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            intent.setType("application/pdf");
            startActivityForResult(intent, 1);
            });
        comic.setOnClickListener(v -> {
            butonsecim=2;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(intent, PICK_PDF_FILE);
        });
        //register olunduğunda 1 verisi login olundupunda 0 verisi geir


        Intent intent = getIntent();
        if (intent != null) {
            receivedData = intent.getStringExtra("data");
            // Alınan veriyi kullanın veya görüntüleyin

        }


            if ("true".equals(receivedData)) {
                try {


                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Veri Kullanım sözleşmesi");
                    builder.setMessage("Kullanıcı Onayı\n" +
                            "\n" +
                            "Kullanıcı, [Şirket Adı]'nin veri kullanım politikalarını okuduğunu ve anladığını onaylar. Kullanıcı, ayrıca kişisel verilerinin Şirket tarafından toplanmasına, kullanılmasına, saklanmasına ve paylaşılmasına izin verir.\n" +
                            "\n" +
                            "Kişisel Veriler\n" +
                            "\n" +
                            "Kullanıcının kişisel verileri, kullanıcının adı, e-posta adresi, telefon numarası ve adres bilgileri gibi bilgileri içerebilir. Şirket, kullanıcının kişisel verilerini yalnızca yasal amaçlar için kullanacaktır.\n" +
                            "\n" +
                            "Kişisel Veri İşlemi\n" +
                            "\n" +
                            "Şirket, kullanıcının kişisel verilerini yasal amaçlar için işleyecektir. Bu amaçlar arasında, hizmetlerin sunulması, hizmetlerin iyileştirilmesi, kullanıcının isteklerinin karşılanması, yasal gereksinimlerin yerine getirilmesi ve Şirket'in yasal haklarının korunması yer alabilir.\n" +
                            "\n" +
                            "Kişisel Veri Paylaşımı\n" +
                            "\n" +
                            "Şirket, kullanıcının kişisel verilerini, yasal olarak gerektiği durumlarda paylaşabilir. Paylaşım, yasal taleplerin yanı sıra, Şirket'in hizmetlerinin sunulması, işlevselliğinin iyileştirilmesi ve müşteri hizmetlerinin sağlanması için gereken durumlarda gerçekleştirilebilir.\n" +
                            "\n" +
                            "Kişisel Veri Saklama\n" +
                            "\n" +
                            "Şirket, kullanıcının kişisel verilerini yalnızca gerekli olan süre boyunca saklayacaktır. Bu süre, yasal gereksinimlere göre belirlenir. Şirket, kişisel verilerin güvenliği için uygun tedbirleri alacaktır.\n" +
                            "\n" +
                            "Kullanıcının Hakları\n" +
                            "\n" +
                            "Kullanıcı, Şirket tarafından tutulan kişisel verilerinin doğruluğunu doğrulama hakkına sahiptir. Kullanıcı ayrıca, kişisel verilerinin Şirket tarafından işlenmesini veya saklanmasını engellemek için Şirket'e başvurma hakkına sahipt");

                    builder.setPositiveButton("Onay Veriyorum", (dialog, which) -> {
                        AlertDialog.Builder anketsoruBuilder = new AlertDialog.Builder(HomeActivity.this);
                        anketsoruBuilder.setTitle("Anket Soruları");
                        anketsoruBuilder.setMessage("Yaşınız kaç?");
                        final EditText input1 = new EditText(HomeActivity.this);
                        anketsoruBuilder.setView(input1);
                        anketsoruBuilder.setPositiveButton("diğer soruya geç", (dialog1, which1) -> {
                            anketsoru1_data = input1.getText().toString();

                            AlertDialog.Builder anketsoruBuilder2 = new AlertDialog.Builder(HomeActivity.this);
                            anketsoruBuilder2.setTitle("Anket Soruları");
                            anketsoruBuilder2.setMessage("Medeni durumunuz nedir?");

                            // Define the options for the spinner
                            String[] medeni_durum = {"evli", "bekar"};

                            // Set up the spinner adapter
                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(HomeActivity.this, android.R.layout.simple_spinner_dropdown_item, medeni_durum);

                            // Create the spinner
                            Spinner anketsoru2Spinner = new Spinner(HomeActivity.this);
                            anketsoru2Spinner.setAdapter(spinnerAdapter);

                            anketsoruBuilder2.setView(anketsoru2Spinner);
                            anketsoruBuilder2.setPositiveButton("diğer soruya geç", (dialog2, which2) -> {
                                // Get the selected value from the spinner
                                anketsoru2_data = medeni_durum[anketsoru2Spinner.getSelectedItemPosition()];

                                AlertDialog.Builder anketsoruBuilder3 = new AlertDialog.Builder(HomeActivity.this);
                                anketsoruBuilder3.setTitle("Anket Soruları");
                                anketsoruBuilder3.setMessage("Aylık gelirin nedir?");
                                final EditText input3 = new EditText(HomeActivity.this);
                                anketsoruBuilder3.setView(input3);
                                anketsoruBuilder3.setPositiveButton("diğer soruya geç", (dialog3, which3) -> {
                                    anketsoru3_data = input3.getText().toString();
                                    AlertDialog.Builder anketsoruBuilder4 = new AlertDialog.Builder(HomeActivity.this);
                                    anketsoruBuilder4.setTitle("Anket Soruları");
                                    anketsoruBuilder4.setMessage("Hangi bölgede yaşıyorsunuz?");
                                    String[] bolge = {"Akdeniz", "Karadeniz", "Ege", "Marmara", "İç Anadolu", "Doğu Anadolu", "Güney Anadolu"};
                                    ArrayAdapter<String> spinnerAdapter2 = new ArrayAdapter<>(HomeActivity.this, android.R.layout.simple_spinner_dropdown_item, bolge);
                                    Spinner anketsoru4Spinner = new Spinner(HomeActivity.this);
                                    anketsoru4Spinner.setAdapter(spinnerAdapter2);

                                    anketsoruBuilder4.setView(anketsoru4Spinner);
                                    anketsoruBuilder4.setPositiveButton("Anketi bitir", (dialog4, position4) -> {
                                        anketsoru4_data = bolge[anketsoru4Spinner.getSelectedItemPosition()];
                                        anket_onay = "true";
                                        AgeDBHelper(anket_onay, anketsoru1_data, anketsoru2_data, anketsoru3_data, anketsoru4_data);

                                    });

                                    anketsoruBuilder4.show();
                                });

                                anketsoruBuilder3.show();
                            });

                            anketsoruBuilder2.show();
                        });

                        anketsoruBuilder.show();
                    });

                    builder.setNegativeButton("Onay vermiyorum", (dialog, which) -> {
                        anketsoru1_data = null;
                        anketsoru2_data = null;
                        anketsoru3_data = null;
                        anketsoru4_data = null;
                        anket_onay = "false";
                        AgeDBHelper(anket_onay, anketsoru1_data, anketsoru2_data, anketsoru3_data, anketsoru4_data);
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                catch (Exception e) {
                    //Toast.makeText(HomeActivity.this, "Hatamız buymus:"+e, Toast.LENGTH_SHORT).show();
                }
            } else {

            }


    }

    public void fetchApi(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://demo.codeseasy.com/apis/stripe/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject =new JSONObject(response);
                            configuration=new PaymentSheet.CustomerConfiguration(
                                    jsonObject.getString("customer"),
                                    jsonObject.getString("ephemeralKey")

                            );
                            paymentIntentClientSecret = jsonObject.getString("paymentIntent");
                            PaymentConfiguration.init(getApplicationContext(), jsonObject.getString("publishableKey"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            protected Map<String, String> getParams(){
                Map<String, String> paramV = new HashMap<>();
                paramV.put("authKey", "abc");
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    private void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult){

        if (paymentSheetResult instanceof PaymentSheetResult.Canceled){
            Toast.makeText(this, "ödeme alınamadaı", Toast.LENGTH_SHORT).show();

        }
        if (paymentSheetResult instanceof PaymentSheetResult.Failed){
            Toast.makeText(this, "sıkıntı oldu", Toast.LENGTH_SHORT).show();

        }
        if (paymentSheetResult instanceof PaymentSheetResult.Completed){
            fetchApi(); // birden fazla ödeme yapabilrisin bu olursa
            Toast.makeText(this, "ödeme alındı", Toast.LENGTH_SHORT).show();
            comic.setEnabled(true);

        }

    }




    @SuppressLint("MissingSuperCall")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

//resimliyi alıyor
        if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK && data != null && data.getData() != null&& butonsecim==2) {
            Uri uri = data.getData();
            try {
                PdfRenderer renderer = new PdfRenderer(getContentResolver().openFileDescriptor(uri, "r"));
                int pageCount = renderer.getPageCount();


                for (int i = 0; i < pageCount; i++) {
                    PdfRenderer.Page page = renderer.openPage(i);

                    int width = getResources().getDisplayMetrics().widthPixels;
                    int height = getResources().getDisplayMetrics().heightPixels;

                    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                    // Ekrana bastırma işlemi (İstediğiniz bir ImageView'e atayabilirsiniz)
                    //pdf_first_page.setImageBitmap(bitmap);

                    // OCR işlemi
                    TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                    if (!textRecognizer.isOperational()) {
                        // TextRecognizer kullanılamıyorsa hata işleme yapabilirsiniz
                    } else {
                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                        SparseArray<TextBlock> textSparseArray = textRecognizer.detect(frame);

                        for (int j = 0; j < textSparseArray.size(); j++) {
                            Text text = textSparseArray.valueAt(j);
                            extractedText_resim.append(text.getValue()).append("\n");
                        }
                    }

                    page.close();
                }

                renderer.close();



                extractedText=extractedText_resim.toString();


                word_spreader(extractedText);


                List<String> wordList_kolay = new ArrayList<>(hashSet_kolay);
                ArrayAdapter<String> adapter_kolay = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordList_kolay);
                recyclerView_kolay.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                        return new RecyclerView.ViewHolder(view) {};
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        ((TextView) holder.itemView.findViewById(android.R.id.text1)).setText(wordList_kolay.get(position));
                    }

                    @Override
                    public int getItemCount() {
                        return wordList_kolay.size();
                    }
                });

                List<String> wordList_orta = new ArrayList<>(hashSet_orta);
                ArrayAdapter<String> adapter_orta = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordList_orta);
                recyclerView_orta.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                        return new RecyclerView.ViewHolder(view) {};
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        ((TextView) holder.itemView.findViewById(android.R.id.text1)).setText(wordList_orta.get(position));
                    }

                    @Override
                    public int getItemCount() {
                        return wordList_orta.size();
                    }
                });

                List<String> wordList_zor = new ArrayList<>(hashSet_zor);
                ArrayAdapter<String> adapter_zor = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordList_zor);
                recyclerView_zor.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                        return new RecyclerView.ViewHolder(view) {};
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        ((TextView) holder.itemView.findViewById(android.R.id.text1)).setText(wordList_zor.get(position));
                    }

                    @Override
                    public int getItemCount() {
                        return wordList_zor.size();
                    }
                });

                anketsoru1_data = anketsoru1.getText().toString();
                anketsoru3_data = anketsoru3.getText().toString();

                UserPDFDataDBHelper(extractedText,hashSet_kolay.toString(),hashSet_orta.toString(),hashSet_zor.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

//textliyi alıyor
        if (requestCode == 1 && resultCode == RESULT_OK&&butonsecim==1) {

            try {
                Uri uri = data.getData();
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

                extractedText = text.toString();

                word_spreader(extractedText);
                List<String> wordList_kolay = new ArrayList<>(hashSet_kolay);
                ArrayAdapter<String> adapter_kolay = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordList_kolay);
                recyclerView_kolay.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                        return new RecyclerView.ViewHolder(view) {};
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        ((TextView) holder.itemView.findViewById(android.R.id.text1)).setText(wordList_kolay.get(position));
                    }

                    @Override
                    public int getItemCount() {
                        return wordList_kolay.size();
                    }
                });

                List<String> wordList_orta = new ArrayList<>(hashSet_orta);
                ArrayAdapter<String> adapter_orta = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordList_orta);
                recyclerView_orta.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                        return new RecyclerView.ViewHolder(view) {};
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        ((TextView) holder.itemView.findViewById(android.R.id.text1)).setText(wordList_orta.get(position));
                    }

                    @Override
                    public int getItemCount() {
                        return wordList_orta.size();
                    }
                });

                List<String> wordList_zor = new ArrayList<>(hashSet_zor);
                ArrayAdapter<String> adapter_zor = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordList_zor);
                recyclerView_zor.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

                    @NonNull
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
                        return new RecyclerView.ViewHolder(view) {};
                    }

                    @Override
                    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                        ((TextView) holder.itemView.findViewById(android.R.id.text1)).setText(wordList_zor.get(position));
                    }

                    @Override
                    public int getItemCount() {
                        return wordList_zor.size();
                    }
                });

                anketsoru1_data = anketsoru1.getText().toString();
                anketsoru3_data = anketsoru3.getText().toString();

                UserPDFDataDBHelper(extractedText,hashSet_kolay.toString(),hashSet_orta.toString(),hashSet_zor.toString());
            } catch (Exception e) {
                //Toast.makeText(HomeActivity.this, "Hatamız buymus:"+e, Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void word_spreader(String extractedText){
    //Main activity sayfasından kullanıcının verileri veri tabanına kayıt olamk için geliyo
        String[] words = extractedText.split("\\s+");
        ArrayList<String> Kolay_words = new ArrayList<>();
        ArrayList<String> Orta_words = new ArrayList<>();
        ArrayList<String> Zor_words = new ArrayList<>();

        for (String word : words) {
            String level = calculateLevel(word);
            switch (level) {
                case "Kolay":
                    Kolay_words.add(word);
                    break;
                case "Orta":
                    Orta_words.add(word);
                    break;
                case "Zor":
                    Zor_words.add(word);
                    break;
                default:
                    // Geçersiz seviye durumu
                    break;
            }
        }

        //kendini tekrar eden kelimeleri kaldırmak için hash set kullandık
        hashSet_kolay.addAll(Kolay_words);
        hashSet_orta.addAll(Orta_words);
        hashSet_zor.addAll(Zor_words);
     }

    private void AgeDBHelper(String age,String onay,String medenidrm,String gelir,String bolge) {
        Anket_DBHelper anket_tablo = new Anket_DBHelper(this);
        boolean success = anket_tablo.addAgeData(getCurrentUserId(),onay,age,medenidrm,gelir,bolge);

        if (success) {
            Toast.makeText(this, "Anket verisi kaydedildi"+success, Toast.LENGTH_SHORT).show();
            // Başarılı kayıt durumunda yapılacak işlemleri buraya ekleyebilirsiniz
        } else {
            Toast.makeText(this, "Anket verisi kaydedilemedi"+success, Toast.LENGTH_SHORT).show();
        }
    }

    private void UserPDFDataDBHelper (String pdfData,String KOLAY_KELİME,String ORTA_KELİME,String ZOR_KELİME){
        UserPDFDataDBHelper dbhelper_pdf_data =new UserPDFDataDBHelper(HomeActivity.this);
        boolean success = dbhelper_pdf_data.addPDFData(getCurrentUserId(),pdfData,KOLAY_KELİME,ORTA_KELİME,ZOR_KELİME);
        if (success) {
            Toast.makeText(this, "PDF verisi kaydedildi"+success, Toast.LENGTH_SHORT).show();
            // Başarılı kayıt durumunda yapılacak işlemleri buraya ekleyebilirsiniz
        } else {
            Toast.makeText(this, "PDF verisi kaydedilemedi"+success, Toast.LENGTH_SHORT).show();
        }
    }

    public int getCurrentUserId() {
        // DBHelper sınıfını kullanarak kullanıcının ID değerini elde et
        DBHelper dbHelper = new DBHelper(HomeActivity.this);
        // 'this' ifadesi, Home Activity'nin bir örneğini temsil eder, bağlamı doğru şekilde ayarlamalısınız
        return dbHelper.getCurrentUserId(username); // Kullanıcının ID değerini döndür
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
        String[] a1Words = {"a" ,"about","search" ,"keep","above", "after", "again", "all", "am", "an", "and", "any", "are", "as", "at", "away", "be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "came", "can", "come", "could", "day", "did", "do", "down", "each", "end", "even", "find", "first", "for", "from", "get", "give", "go", "good", "great", "had", "has", "have", "he", "help", "her", "here", "him", "his", "how", "I", "if", "in", "into", "is", "it", "its", "just", "know", "large", "last", "left", "like", "little", "long", "look", "made", "make", "man", "many", "may", "me", "men", "might", "more", "most", "Mr.", "must", "my", "name", "never", "new", "next", "no", "not", "now", "of", "off", "on", "one", "only", "or", "other", "our", "out", "over", "own", "part", "people", "place", "put", "read", "right", "said", "same", "saw", "say", "see", "she", "should", "show", "small", "so", "some", "something", "sound", "still", "such", "take", "tell", "than", "that", "the", "them", "then", "there", "these", "they", "thing", "think", "this", "those", "thought", "three", "through", "time", "to", "today", "together", "too", "two", "under", "up", "us", "use", "very", "want", "was", "water", "way", "we", "well", "went", "were", "what", "when", "where", "which", "while", "who", "why", "will", "with", "word", "work", "world", "would", "write", "year", "you", "your"};
        for (String a1Word : a1Words) {
            if (word.equalsIgnoreCase(a1Word)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isA2(String word) {
        // A2 seviye kelimelerin örnek listesi: https://www.bbc.co.uk/bitesize/guides/zq3dwmn/revision/2
        String[] a2Words = {"able", "about", "above", "accept", "according to", "account", "across", "act", "action", "activity", "add", "address", "admit", "adult", "adventure", "advice", "afford", "afraid", "after", "afternoon", "again", "against", "age", "ago", "agree", "ahead", "aim", "air", "airport", "alarm", "all", "allow", "almost", "alone", "along", "already", "also", "although", "always", "amazing", "among", "amount", "ancient", "angry", "animal", "anniversary", "announce", "another", "answer", "any", "anybody", "anyone", "anything", "anyway", "apartment", "apologize", "app", "appear", "apple", "application", "apply", "appointment", "approach", "area", "argue", "arm", "around", "arrange", "arrival", "art", "article", "artist", "as", "ask", "asleep", "assistant", "at", "attack", "attempt", "attention", "attitude", "attract", "auction", "aunt", "autumn", "available", "avoid", "awake", "away", "awful", "baby", "back", "bad", "bag", "bake", "balance", "ball", "balloon", "banana", "bank", "bar", "barely", "base", "baseball", "basic", "basket", "bat", "bath", "be", "beach", "bear", "beard", "beat", "beautiful", "because", "become", "bed", "bedroom", "beer", "before", "begin", "behind", "believe", "bell", "belong", "below", "belt", "bench", "benefit", "beside", "best", "better", "between", "beyond", "bicycle", "big", "bill", "biology", "bird", "birth", "birthday", "bit", "bite", "black", "blame", "blanket", "bleed", "bless", "blind", "block", "blood", "blow", "blue", "board", "boat", "body", "boil", "bomb", "bone", "book", "border", "born", "borrow", "boss", "both", "bother", "bottle", "bottom", "bowl", "box", "boy", "brain", "branch", "brave", "bread", "break", "breakfast", "breath", "breathe", "bridge", "brief", "bright", "bring", "brother", "brown", "brush", "build", "building", "burn", "bury", "bus", "business", "busy", "but", "butter", "button", "buy", "by", "cabin", "cabinet", "cake", "call", "calm", "camera", "camp", "campaign", "can", "cancel", "cancer", "candidate", "candle"};
        for (String a2Word : a2Words) {
            if (word.equalsIgnoreCase(a2Word)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isB1(String word) {
        // B1 seviye kelimelerin örnek listesi: https://www.bbc.co.uk/bitesize/guides/zyq3k2p/revision/2
        String[] b1Words = {"ability","able", "abroad", "absence", "absolute", "acceptance", "access", "accident", "accordingly", "accountable", "accuracy", "accusation", "achieve", "achievement", "acid", "acquire", "acquisition", "act", "action", "active", "activity", "actor", "actual", "adapt", "add", "addition", "adequate", "adjust", "admiration", "admission", "adoption", "adult", "advance", "advantage", "adventure", "advertising", "advice", "advisable", "advocate", "affect", "affection", "afford", "afterward", "agency", "aggressive", "aid", "aim", "aircraft", "alert", "alien", "align", "alike", "alive", "alliance", "allocate", "allowance", "ally", "alter", "alternative", "although", "altogether", "aluminum", "amaze", "amendment", "amuse", "analyze", "ancestor", "angel", "anger", "angle", "angry", "animation", "anniversary", "announce", "annoy", "annual", "anticipate", "anxiety", "anxious", "anyhow", "anyway", "apart", "apartment", "apologize", "apparent", "appeal", "appear", "appetite", "applaud", "appliance", "application", "appoint", "appointee", "appreciate", "approach", "appropriate", "approval", "approve", "approximate", "arch", "architecture", "archive", "area", "arena", "argue", "arise", "armed", "armor", "army", "arouse", "arrange", "array", "arrest", "arrival", "arrogant", "artificial", "as", "ashamed", "aside", "aspect", "assemble", "assembly", "assert", "assess", "assign", "assignment", "assist", "associate", "assorted", "assume", "assumption", "assurance", "assure", "astonish", "athlete", "atmosphere", "attach", "attachment", "attack", "attempt", "attend", "attendant", "attention", "attic", "attribute", "auction", "audience", "audit", "authentic", "authorize", "auto", "autobiography", "autonomous", "available", "avenue", "average", "aviation", "avoid", "await", "awake", "award", "aware", "away", "awkward", "back", "background", "backward", "bacteria", "baggage", "bake", "balance", "ballot", "ban", "band", "bang", "bankruptcy", "banner", "bare", "bargain", "barrier", "baseball", "basement", "basic", "basket", "batch", "battle", "beast", "beat", "bedroom", "beer", "beg", "beginning", "behalf", "behave", "belief", "belong", "bench", "beneficial"};
        for (String b1Word : b1Words) {
            if (word.equalsIgnoreCase(b1Word)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isB2(String word) {
        // B1 seviye kelimelerin örnek listesi: https://www.bbc.co.uk/bitesize/guides/zyq3k2p/revision/2
        String[] b2Words = {"abandon","abuse", "accommodate", "accomplish", "accountable", "accumulate", "accurate", "accustomed", "acknowledge", "acquaintance", "acquire", "adapt", "adequate", "adhere", "adjacent", "admission", "admit", "adopt", "advance", "adverse", "advocate", "affect", "affluent", "aggravate", "aggregate", "agitate", "alienate", "allege", "allocate", "allowance", "ally", "alter", "alternate", "amend", "analogous", "analyze", "anguish", "animate", "annoy", "anticipate", "apparent", "appeal", "applaud", "applicable", "appreciate", "apprehension", "arbitrary", "architect", "archive", "arduous", "arise", "array", "articulate", "ascertain", "ascribe", "aspire", "assassinate", "assert", "assign", "assimilate", "assortment", "assume", "assumption", "assurance", "assure", "astonish", "attain", "attribute", "authentic", "authorize", "automate", "avenge", "banish", "bargain", "barren", "bear", "become", "befit", "beget", "belated", "belligerent", "benefactor", "beneficiary", "berate", "beset", "betray", "bewilder", "bias", "blend", "blunt", "boast", "bolster", "border", "bother", "bountiful", "brace", "breach", "breakthrough", "breed", "brevity", "bridle", "brood", "brutal", "burgeon", "calibrate", "callous", "calm", "camouflage", "cancel", "candid", "capable", "capitalism", "capitulate", "captivate", "careless", "carnage", "carve", "castigate", "casual", "catalyst", "categorical", "catholic", "caution", "cease", "celebrate", "censor", "centralize", "ceremonial", "chafe", "challenge", "channel", "characterize", "charisma", "chase", "cherish", "chide", "chronicle", "circuitous", "circumscribe", "cite", "civil", "clandestine", "clarify", "clarity", "classify", "claw", "cleave", "clumsy", "coerce", "coexist", "cohesive", "coincide", "collapse", "collateral", "collective", "colossal", "combat", "combine", "comical", "commemorate", "commend", "commensurate", "commingle", "committed", "commodious", "commonplace", "communicate", "commute", "compelling", "compensate", "compile", "complement", "compliance", "compound", "comprehensive", "compromise"};
        for (String b2Word : b2Words) {
            if (word.equalsIgnoreCase(b2Word)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isC1(String word) {
        // B1 seviye kelimelerin örnek listesi: https://www.bbc.co.uk/bitesize/guides/zyq3k2p/revision/2
        String[] c1Words = {"abrupt", "accelerate", "accessible", "accommodating", "accountability", "acquiesce", "acrimonious", "adhere", "admonish", "adversary", "advocate", "aesthetic", "affable", "affluent", "alacrity", "alleviate", "alliance", "allocate", "altruistic", "ameliorate", "analogous", "anathema", "annul", "antipathy", "apathetic", "apex", "apocryphal", "appease", "apprehensive", "approbation", "arbitrary", "archaic", "ardent", "arduous", "articulate", "ascendancy", "aspiration", "assiduous", "assimilate", "assuage", "assumption", "astute", "atone", "atrophy", "attest", "audacious", "augment", "auspicious", "austere", "autonomous", "avarice", "avert", "avocation", "awry", "banal", "barrage", "bastion", "beguile", "benevolent", "benign", "bequeath", "berate", "bereave", "beseech", "beset", "blighted", "boisterous", "bolster", "bombastic", "boon", "brandish", "brazen", "broach", "bucolic", "buffet", "bulwark", "bumptious", "burgeon", "buttress", "cacophony", "cadence", "cajole", "callous", "calumny", "camaraderie", "candid", "canonical", "cantankerous", "capitulate", "capricious", "carnal", "carouse", "castigate", "categorical", "cathartic", "caustic", "cavalier", "celestial", "celibate", "censure", "chagrin", "charisma", "chasm", "chicanery", "chivalrous", "chronic", "circumscribe", "circumvent", "clandestine", "clarity", "clemency", "cloying", "coalesce", "coercion", "cogent", "coherent", "collaborate", "collusion", "colossal", "commodious", "commoditize", "communal", "complacency", "compliant", "composure", "compunction", "concatenate", "conceit", "conciliatory", "condescending", "condone", "confabulate", "confidant", "conflagration", "confluence", "congeal", "congenial", "conjecture", "consensus", "consign", "consolation", "conspicuous", "constituent", "construe", "consummate", "contemplate", "contravene", "contrite", "conundrum", "convoke", "convoluted", "corpulent", "cosmopolitan", "coterie", "countenance", "covert"};
        for (String c1Word : c1Words) {
            if (word.equalsIgnoreCase(c1Word)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isC2(String word) {
        // B1 seviye kelimelerin örnek listesi: https://www.bbc.co.uk/bitesize/guides/zyq3k2p/revision/2
        String[] c2Words = {"aberration","abjure", "abrogate", "abscond", "abstemious", "abstruse", "accede", "accost", "accretion", "acerbic", "acme","acquiesce", "acrimonious", "adhere", "admonish", "adversary", "advocate", "aesthetic", "admonitory", "adulatory", "adumbrate", "adventitious", "aegis", "aeolian", "affectation", "affiance", "aggrieve", "albatross", "aleatory", "allay", "altercation", "amalgamate", "amenable", "amortize", "anathematize", "ancillary", "animadversion", "animus", "annex", "antediluvian", "anthropogenic", "antithetical", "apogee", "aposiopesis", "apostate", "apotheosis", "appellation", "apposite", "approbatory", "arabesque", "arrogate", "ascetic", "asperity", "assiduity", "assonance", "astringent", "atavistic", "attenuate", "audaciousness", "augury", "aureole", "autodidact", "autocratic", "automaton", "auxiliary", "aversive", "avocation", "axiomatic", "bacchanalian", "baleful", "banausic", "beatific", "bellicose", "benediction", "beneficent", "bereft", "bibliophile", "bilious", "boorish", "bowdlerize", "brevity", "bridle", "brusque", "byzantine", "cabal", "cadge", "calipers", "calumniate", "campanology", "canard", "canon", "canvass", "capitulation", "caprice", "captious", "cardinal", "carom", "cartography", "cataclysm", "cathexis", "causality", "cavil", "censorious", "centrifugal", "centripetal", "cerebral", "chary", "chicanery", "chimerical", "choleric", "churlish", "circumlocution", "clerisy", "clout", "cloy", "cockamamie", "codify", "coeval", "cognate", "cohesion", "colloquy", "comity", "compendious", "complaisant", "concinnity", "concomitant", "concordance", "concupiscent", "conduce", "confabulate", "confluent", "congealment", "congruity", "conjectural", "connoisseur", "consequent", "conspectus", "consternation", "contiguity", "contrarian", "contumacious", "convivial", "copious", "corollary", "coruscate", "countervail", "coxcomb", "credo", "credulity", "cupidity", "cynosure"};
        for (String c2Word : c2Words) {
            if (word.equalsIgnoreCase(c2Word)) {
                return true;
            }
        }
        return false;
    }

}