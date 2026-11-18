package com.example.conexionrealtimedatabase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;

public class MainActivity extends Activity {
 private PatientStore store; private LinearLayout rows; private EditText search; private Spinner bloodFilter;
 @Override public void onCreate(Bundle b){super.onCreate(b);store=new PatientStore(this);getWindow().setStatusBarColor(Ui.BG);getWindow().setNavigationBarColor(Ui.BG);getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
  LinearLayout page=Ui.page(this);page.addView(Ui.text(this,"AGENDA CLÍNICA  ·  DEMO",13,Ui.TEAL,true));page.addView(Ui.heading(this,"Pacientes"));page.addView(Ui.text(this,"Busca y gestiona las fichas de forma sencilla.",16,Ui.MUTED,false));
  LinearLayout searchCard=Ui.card(this);search= new EditText(this);search.setSingleLine(true);search.setTextSize(17);search.setMinHeight(Ui.dp(this,54));search.setHint("Nombre, apellidos o NUHSA");search.setContentDescription("Buscar pacientes por nombre, apellidos o NUHSA");searchCard.addView(search);bloodFilter=new Spinner(this);String[] groups={"Todos los grupos sanguíneos","A+","A-","B+","B-","AB+","AB-","O+","O-"};ArrayAdapter<String> ad=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,groups);ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);bloodFilter.setMinimumHeight(Ui.dp(this,48));bloodFilter.setAdapter(ad);bloodFilter.setContentDescription("Filtrar por grupo sanguíneo");searchCard.addView(bloodFilter);page.addView(searchCard);
  Button add=Ui.button(this,"＋  Añadir paciente",true);add.setOnClickListener(v->startActivity(new Intent(this,PatientFormActivity.class)));page.addView(add);page.addView(Ui.text(this,"DIRECTORIO",13,Ui.MUTED,true));ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);rows=new LinearLayout(this);rows.setOrientation(1);scroll.addView(rows);page.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));setContentView(page);
  search.addTextChangedListener(new TextWatcher(){public void beforeTextChanged(CharSequence s,int st,int c,int a){}public void onTextChanged(CharSequence s,int st,int before,int count){render();}public void afterTextChanged(Editable e){}});bloodFilter.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onNothingSelected(android.widget.AdapterView<?> p){}public void onItemSelected(android.widget.AdapterView<?> p,View v,int pos,long id){render();}});
 }
 @Override protected void onResume(){super.onResume();if(store!=null)render();}
 private void render(){if(rows==null)return;rows.removeAllViews();String f=(String)bloodFilter.getSelectedItem();if(f==null||f.startsWith("Todos"))f="";ArrayList<Patient> list=store.find(search.getText().toString(),f);if(list.isEmpty()){LinearLayout c=Ui.card(this);c.addView(Ui.text(this,"No hay pacientes con esos criterios.",17,Ui.INK,true));c.addView(Ui.text(this,"Prueba otra búsqueda o añade una ficha nueva.",15,Ui.MUTED,false));rows.addView(c);return;}for(Patient p:list){LinearLayout c=Ui.card(this);TextView n=Ui.text(this,p.getNombre()+" "+p.getApellidos(),19,Ui.INK,true);c.addView(n);c.addView(Ui.text(this,"NUHSA  "+p.getNuhsa()+"    ·    Grupo "+p.getGrupoSanguineo(),15,Ui.MUTED,false));c.setFocusable(true);c.setClickable(true);c.setMinimumHeight(Ui.dp(this,88));c.setContentDescription("Paciente "+p.getNombre()+" "+p.getApellidos()+", NUHSA "+p.getNuhsa()+", grupo sanguíneo "+p.getGrupoSanguineo()+". Abrir ficha");c.setOnClickListener(v->{Intent i=new Intent(this,PatientProfileActivity.class);i.putExtra("nuhsa",p.getNuhsa());startActivity(i);});rows.addView(c);}}
}
