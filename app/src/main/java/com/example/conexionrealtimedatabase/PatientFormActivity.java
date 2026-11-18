package com.example.conexionrealtimedatabase;
import android.app.*;import android.os.Bundle;import android.text.InputType;import android.view.View;import android.widget.*;
public class PatientFormActivity extends Activity{
 private PatientStore store;private String previous;private EditText name,surname,nuhsa;private Spinner blood;
 @Override public void onCreate(Bundle b){super.onCreate(b);store=new PatientStore(this);previous=getIntent().getStringExtra("nuhsa");getWindow().setStatusBarColor(Ui.BG);getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);LinearLayout page=Ui.page(this);page.addView(Ui.text(this,"FICHA DE PACIENTE",13,Ui.TEAL,true));page.addView(Ui.heading(this,previous==null?"Nuevo paciente":"Editar paciente"));page.addView(Ui.text(this,"Los campos con * son obligatorios.",15,Ui.MUTED,false));LinearLayout card=Ui.card(this);name=field(card,"Nombre *","Nombre del paciente");surname=field(card,"Apellidos *","Apellidos");nuhsa=field(card,"NUHSA *","Identificador NUHSA");card.addView(Ui.text(this,"Grupo sanguíneo *",16,Ui.INK,true));blood=new Spinner(this);String[] gs={"A+","A-","B+","B-","AB+","AB-","O+","O-"};ArrayAdapter<String>a=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,gs);a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);blood.setMinimumHeight(Ui.dp(this,48));blood.setAdapter(a);blood.setContentDescription("Selecciona el grupo sanguíneo");card.addView(blood);page.addView(card);Button save=Ui.button(this,"Guardar ficha",true);save.setOnClickListener(v->save());page.addView(save);Ui.showWithBack(this,page,previous==null?"pacientes":"la ficha");if(previous!=null){Patient p=store.get(previous);if(p!=null){name.setText(p.getNombre());surname.setText(p.getApellidos());nuhsa.setText(p.getNuhsa());for(int i=0;i<a.getCount();i++)if(a.getItem(i).equals(p.getGrupoSanguineo()))blood.setSelection(i);}}initialValues=values();}
 private java.util.List<String> initialValues;
 private java.util.List<String> values() {
     return java.util.Arrays.asList(name.getText().toString(), surname.getText().toString(),
         nuhsa.getText().toString(), (String) blood.getSelectedItem());
 }
 @Override public void onBackPressed() {
     if (values().equals(initialValues)) {
         finish();
     } else {
         Ui.confirmDiscard(this);
     }
 }
 private EditText field(LinearLayout parent,String label,String hint){parent.addView(Ui.text(this,label,16,Ui.INK,true));EditText e=new EditText(this);e.setSingleLine(true);e.setTextSize(17);e.setMinHeight(Ui.dp(this,52));e.setHint(hint);e.setContentDescription(label);if(label.startsWith("NUHSA"))e.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);parent.addView(e);return e;}
 private void save(){String n=name.getText().toString().trim(),s=surname.getText().toString().trim(),id=nuhsa.getText().toString().trim().toUpperCase();if(n.isEmpty()||s.isEmpty()||id.isEmpty()){Toast.makeText(this,"Completa nombre, apellidos y NUHSA",Toast.LENGTH_SHORT).show();if(n.isEmpty())name.setError("Campo obligatorio");else if(s.isEmpty())surname.setError("Campo obligatorio");else nuhsa.setError("Campo obligatorio");return;}if(previous==null&&store.get(id)!=null){nuhsa.setError("Este NUHSA ya existe");return;}Patient p=new Patient(n,s,(String)blood.getSelectedItem(),id);if(!store.save(p,previous)){nuhsa.setError("NUHSA duplicado o inválido");return;}Toast.makeText(this,"Ficha guardada",Toast.LENGTH_SHORT).show();finish();}
}
