package org.example.toolsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{
    private Spinner spin1, spin2;
    private Moneda origenMoneda, destinoMoneda;

    int flags[] = {R.drawable.ic_eur, R.drawable.ic_usd,R.drawable.ic_gbp,R.drawable.ic_chf,R.drawable.ic_aud, R.drawable.ic_cad, R.drawable.ic_jpy, R.drawable.ic_mxn,R.drawable.ic_cny,R.drawable.ic_nzd,
            R.drawable.ic_rub, R.drawable.ic_inr, R.drawable.ic_brl, R.drawable.ic_ars, R.drawable.ic_cop, R.drawable.ic_clp, R.drawable.ic_pen, R.drawable.ic_uyu, R.drawable.ic_pyg, R.drawable.ic_bob};

    public ArrayList<Button> Botones = new ArrayList<Button>();
    public ArrayList<Float> numeros = new ArrayList<Float>();
    public ArrayList<String> operacion = new ArrayList<String>();
    public ArrayList<String> operacionesArray = new ArrayList<String>();


    public TextView pant_out, pant_in;

    public String cap = "", pIn="", pOut, aux="";
    public double operando1,operandoANS = 0, res =0;
    public int numSum = 0,numMul = 0,numDiv = 0,numRes = 0;;
    public boolean isOperacion = false, isWritting = false;

    //Servidor web
    Result resultadoCambio;
    private RequestQueue colaPeticiones;

    private Animation animation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Cargando todas las monedas
        listaDivisas(20);

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        spin1 = (Spinner) findViewById(R.id.simpleSpinner);
        spin2 = (Spinner) findViewById(R.id.simpleSpinner2);
        spin1.setOnItemSelectedListener(this);
        spin2.setOnItemSelectedListener(this);
        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),flags);
        spin1.setAdapter(customAdapter);
        spin2.setAdapter(customAdapter);

        listaDivisas(20);

        colaPeticiones = Volley.newRequestQueue(this);
        resultadoCambio = new Result();


        pant_in = findViewById(R.id.t_in);
        pant_out= findViewById(R.id.t_out);
        Log.d("Calculator","Entramos en onCreateView");

        Botones.add(0,(Button)findViewById(R.id.b_0));
        Botones.add(1,(Button)findViewById(R.id.b_1));
        Botones.add(2,(Button)findViewById(R.id.b_2));
        Botones.add(3,(Button)findViewById(R.id.b_3));
        Botones.add(4,(Button)findViewById(R.id.b_4));
        Botones.add(5,(Button)findViewById(R.id.b_5));
        Botones.add(6,(Button)findViewById(R.id.b_6));
        Botones.add(7,(Button)findViewById(R.id.b_7));
        Botones.add(8,(Button)findViewById(R.id.b_8));
        Botones.add(9,(Button)findViewById(R.id.b_9));
        Botones.add(10,(Button)findViewById(R.id.b_point));
        Botones.add(11,(Button)findViewById(R.id.b_M));
        Botones.add(12,(Button)findViewById(R.id.b_plas));
        Botones.add(13,(Button)findViewById(R.id.b_less));
        Botones.add(14,(Button)findViewById(R.id.b_mul));
        Botones.add(15,(Button)findViewById(R.id.b_div));
        Botones.add(16,(Button)findViewById(R.id.b_equal));
        Botones.add(17,(Button)findViewById(R.id.b_divisas));
        Botones.add(18,(Button)findViewById(R.id.b_C));

        for(int i=0; i<=18; i++){
            Botones.get(i).setOnClickListener((View.OnClickListener) this);
            Botones.get(i).setTag(i);
        }

        animation = AnimationUtils.loadAnimation(this, R.anim.giro_con_zoom);
        Botones.get(17).startAnimation(animation);
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        if(arg0.getId() == R.id.simpleSpinner){
            origenMoneda = Moneda.divisas.get(position);
            Toast.makeText(getApplicationContext(), origenMoneda.getName(), Toast.LENGTH_LONG).show();

        }else{
            destinoMoneda = Moneda.divisas.get(position);
            Toast.makeText(getApplicationContext(), destinoMoneda.getName(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onClick(View v) {
        /* Los tags del 0-9 son teclas númericas*/
        int b_numbers = (int)v.getTag();
        if(b_numbers>=0 && b_numbers<10){ /*Ingresando números*/
            if(!isOperacion && (!isWritting) ){
                clear();
            }
            /**Esto es para no concatenar el 0 al inicio*/
            if(!(cap.equals("")&& b_numbers ==0)){
                cap = cap+""+b_numbers;
                mostrarPantalla(b_numbers);
            }
            isOperacion = false;
            isWritting = true;

        }else{
            aux = aux+cap;
            // "."
            if(b_numbers == 10){
                punto();
            }
            // boton M = 11(modificar)
            if(b_numbers == 11){
                modificar(v);
            }
            // suma = 12
            if(b_numbers == 12){
                sumar();
            }
            // resta = 13
            if(b_numbers == 13){
                restar();
            }
            // multiplicacion = 14
            if(b_numbers == 14){
                multiplicar();
            }
            // dividir = 15
            if(b_numbers == 15){
                dividir();
            }
            // equals  = 16
            if(b_numbers == 16){
                equals();
            }
            if(b_numbers == 17){
                v.startAnimation(animation);
                conversorDivisas();
            }
            // Clear 17
            if(b_numbers == 18){
                clear();
            }

        }

    }

    public void punto (){
        if(cap.equals("")){
            cap = 0+".";
        }else {
            cap = cap+".";
        }
    }
    public void sumar(){

        if(!aux.equals("")&&!aux.equals("-") && (!isOperacion)){
            if(aux.equals(""+operandoANS)){
                operacionesArray.add(aux);
                operacionesArray.add("+");
            }
            aux = aux+"+";
            try{
                if(!cap.equals("")){
                    numeros.add(Float.parseFloat(cap));
                    operacion.add("+");
                    operacionesArray.add(cap);
                    operacionesArray.add("+");

                }

            }catch (NumberFormatException nfe){ }
            cap = "";
            numSum++;
            mostrarPantalla(12);
        }else{
            mostrarPantalla(20);
        }
        isOperacion = true;
        isWritting = false;

    }
    public void restar(){
        if(aux.equals(""+operandoANS)){
            operacionesArray.add(aux);
        }
        aux = aux+"-";
        try{
            if(!cap.equals("")){
                numeros.add(Float.parseFloat(cap));
                operacionesArray.add(cap);
            }

        }catch (NumberFormatException nfe){ }
        operacion.add("-");
        operacionesArray.add("-");
        cap = "";
        numRes++;
        //tipoOperacion = 2; //Setting operation type for suma
        mostrarPantalla(13);
        isOperacion = true;
    }
    public void multiplicar(){
        if(!aux.equals("")&&!aux.equals("-") && (!isOperacion)){
            if(aux.equals(""+operandoANS)){
                operacionesArray.add(aux);
                operacionesArray.add("*");
            }
            aux = aux+"*";
            try{
                if(!cap.equals("")){
                    //operando1 = Double.parseDouble(cap);
                    numeros.add(Float.parseFloat(cap));
                    operacion.add("*");
                    operacionesArray.add(cap);
                    operacionesArray.add("*");
                }

            }catch (NumberFormatException nfe){ }
            cap = "";
            numMul++;
            mostrarPantalla(14);
        }else{
            mostrarPantalla(20);
        }
        isOperacion = true;
        isWritting = false;
    }
    public void dividir(){
        if(!aux.equals("")&&!aux.equals("-")  && (!isOperacion)){
            if(aux.equals(""+operandoANS)){
                operacionesArray.add(aux);
                operacionesArray.add("/");
            }
            aux = aux+"/";
            try{
                if(!cap.equals("")){
                    //operando1 = Double.parseDouble(cap);
                    numeros.add(Float.parseFloat(cap));
                    operacion.add("/");
                    operacionesArray.add(cap);
                    operacionesArray.add("/");
                }

            }catch (NumberFormatException nfe){ }
            cap = "";
            numDiv++;
            mostrarPantalla(15);
        }else{
            mostrarPantalla(20);
        }
        isOperacion = true;
        isWritting = false;
    }
    public void conversorDivisas(){
        try{
            operando1 = 0.0;
            cap = pant_out.getText().toString();
            if(!cap.equals("")){
                try{
                    operando1 = Double.parseDouble(cap);
                }catch (NumberFormatException nfe){
                    operando1 = res;
                }
            }else{
                operando1 = 0;
            }
                //double cantidad = operando1;
                resultadoCambio.setSource(origenMoneda.getCode()); 
                resultadoCambio.setTarget(destinoMoneda.getCode());
                resultadoCambio.setQuantity(1);
                jsonParse(operando1);
                /*res = operando1* resultadoCambio.getAmount();
                pant_in.setText(operando1+" "+origenMoneda.getSigno()+"  "+origenMoneda.getCode());
                pant_out.setText(res+" "+destinoMoneda.getSigno()+"  "+destinoMoneda.getCode());*/
            //mostrarPantalla(18);

        }catch (NumberFormatException nfe){ } catch (Exception e) {
            e.printStackTrace();
        }
        cap = "";//pant.setText("");
    }

    public void equals (){

        try{
            if(!cap.equals("")){
                numeros.add(Float.parseFloat(cap));
                operacionesArray.add(cap);
                realizarOperaciones();
            }
        }catch (NumberFormatException nfe){ }
        cap = "";
        pOut = res+"";
        aux = res+"";
        operandoANS = res;
        mostrarPantalla(16);
        operacionesArray.clear();
        res = 0; pIn = "";
        isOperacion = false;
        numRes =0; numSum=0; numMul=0; numDiv=0;
        isWritting = false;
    }

    public void clear (){
        res=0;
        pIn = "";
        pOut = "0.0";
        aux = "";
        numRes =0; numSum=0; numMul=0; numDiv=0;
        operacion.clear();
        numeros.clear();
        operacionesArray.clear();
        isOperacion = false;
        mostrarPantalla(17);
        isWritting = true;
    }

    public void modificar (View v){
        if(!cap.equals("")){
            cap = (String)cap.subSequence(0,cap.length()-1);
        }
        mostrarPantalla(11);
    }


    public void realizarOperaciones(){
        double resultado = 0;
            for(int i= 0; i< operacionesArray.size(); i++){
                if(operacionesArray.get(i).equals("-")){
                    if(operacionesArray.get(i+1) != null){
                        operacionesArray.set(i+1, "-"+operacionesArray.get(i+1));
                        operacionesArray.remove(i);
                        i--;
                    }
                }

            }

            for(int i= 0; i< operacionesArray.size(); i++){

                if(numMul!=0){
                    if(operacionesArray.get(i).equals("*")){
                        try {
                            double num = Double.parseDouble(operacionesArray.get(i-1)) * Double.parseDouble(operacionesArray.get(i+1));
                            operacionesArray.set(i-1, ""+num);
                            operacionesArray.remove(i);
                            operacionesArray.remove(i);
                            i--;
                        }catch (Exception ex){ }
                    }
                }

                if(numDiv!=0){
                    if(operacionesArray.get(i).equals("/")){
                        try {
                            double num1 = Double.parseDouble(operacionesArray.get(i-1));
                            double num2 = Double.parseDouble(operacionesArray.get(i+1));
                            if(num2 != 0){
                                double num = num1/num2;
                                operacionesArray.set(i-1, ""+num);
                                operacionesArray.remove(i);
                                operacionesArray.remove(i);
                                i--;
                            }else{
                                mostrarPantalla(20);
                                operacionesArray.clear();
                                break;
                            }
                        }catch (Exception ex){ }
                    }
                }

            }

        for(int i= 0; i< operacionesArray.size(); i++){

            //if(!operacionesArray.get(i).equals("*") &&!operacionesArray.get(i).equals("/")){
                try {
                    double num = Double.parseDouble(operacionesArray.get(i));
                    resultado = resultado + num;
                }catch (Exception ex){

                }
           // }
        }
        res = resultado;

    }

    public void mostrarPantalla(int bn){

        switch (bn){

            case 12: // suma
                pant_out.setText(pOut);
                pant_in.setText(aux);
                cap = ""; /*cap es null cuando se pulsa +,c,pts,=*/
                break;
            case 13: // resta
                pant_out.setText(pOut);
                pant_in.setText(aux);
                cap = ""; /*cap es null cuando se pulsa +,c,pts,=*/
                break;
            case 14: // mul
                pant_out.setText(pOut);
                pant_in.setText(aux);
                cap = ""; /*cap es null cuando se pulsa +,c,pts,=*/
                break;
            case 15: // div
                pant_out.setText(pOut);
                pant_in.setText(aux);
                cap = ""; /*cap es null cuando se pulsa +,c,pts,=*/
                break;
            case 16:// =
                pant_out.setText(pOut);
                pant_in.setText(pIn);
                cap = ""; /*cap es null cuando se pulsa +,c,pts,=*/
                break;
            case 17:// C
                pant_in.setText("");
                pant_out.setText("0.0");
                cap = ""; /*cap es null cuando se pulsa +,c,pts,=*/
                break;
            case 18:// Divisas
                pant_in.setText(operando1+" "+origenMoneda.getSigno());
                pant_out.setText(res+" "+destinoMoneda.getSigno());
                cap = ""; /*cap es null cuando se pulsa +,c,pts,=*/
                break;
            case 20:// Error
                //pant_in.setText("");
                pant_out.setText("Error");
                cap = ""; /*cap es null cuando se pulsa +,c,pts,=*/
                break;
            default: /* Ingreso de números*/
                pant_out.setText(cap);
                break;
        }
    }

    public void jsonParse(final double parse ) throws UnsupportedEncodingException {

        String url = "https://api.cambio.today/v1/quotes/"+resultadoCambio.getSource()+"/"+resultadoCambio.getTarget()+"/json?quantity=" /*+r.getQuantity()*/
                + URLEncoder.encode(String.valueOf(resultadoCambio.getQuantity()) , "UTF-8")
                +"&key=2846|rEumpvM6CyWZXdd~B7WW3D9BrTjmq^gc";
        JsonObjectRequest request = new JsonObjectRequest(JsonObjectRequest.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("result");
                    Double respCantidad = jsonObject.getDouble("amount");
                    Double respValue = jsonObject.getDouble("value");
                    String respUpdate = jsonObject.getString("updated");

                    resultadoCambio.setAmount(respCantidad);
                    resultadoCambio.setValue(respValue);
                    resultadoCambio.setUpdated(respUpdate);
                    res = parse*respCantidad;

                    pant_in.setText(String.format("%.2f", operando1)+" "+origenMoneda.getSigno()+"  "+origenMoneda.getCode());
                    pant_out.setText( String.format("%.2f", res)+" "+destinoMoneda.getSigno()+"  "+destinoMoneda.getCode());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Ha ocurrido un ERROR",Toast.LENGTH_SHORT).show();
            }
        });
        colaPeticiones.add(request);
    }

    public void listaDivisas(int cantidad) {
        Moneda result;
        try {
            InputStream f = this.getAssets().open("carpeta/divisas");
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(f));
            int n = 0;
            String linea;
            do {
                linea = entrada.readLine();
                if (linea != null) {

                    StringTokenizer string = new StringTokenizer(linea,",");
                    String code = string.nextToken();
                    String name = string.nextToken();
                    String pais = string.nextToken();
                    String sing = string.nextToken();
                    //System.out.println(code+" "+name+" "+pais+" "+sing);
                    result = new Moneda(n,name,code,pais,sing);
                    Moneda.divisas.add(result);
                    n++;
                }
            } while (n < cantidad && linea != null);
            f.close();
        } catch (Exception e) {
            Log.e("Asteroides", e.getMessage(), e);
        }
        //return result;
    }

}
