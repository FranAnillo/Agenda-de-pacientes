package com.example.conexionrealtimedatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Locale;

final class PatientStore extends SQLiteOpenHelper {
    PatientStore(Context c){super(c,"agenda_pacientes.db",null,1);}
    @Override public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE patients(nuhsa TEXT PRIMARY KEY, name TEXT NOT NULL, surname TEXT NOT NULL, blood TEXT NOT NULL)");
        db.execSQL("CREATE TABLE appointments(id INTEGER PRIMARY KEY AUTOINCREMENT, nuhsa TEXT NOT NULL, date TEXT NOT NULL, time TEXT NOT NULL, reason TEXT NOT NULL)");
        insert(db,"DEMO001","Lucía","García López","A+"); insert(db,"DEMO002","Daniel","Martín Ruiz","O-");
        insert(db,"DEMO003","Sofía","Pérez Moreno","B+"); insert(db,"DEMO004","Hugo","Sánchez Díaz","AB+");
    }
    private void insert(SQLiteDatabase db,String id,String n,String s,String b){ContentValues v=new ContentValues();v.put("nuhsa",id);v.put("name",n);v.put("surname",s);v.put("blood",b);db.insert("patients",null,v);}
    @Override public void onUpgrade(SQLiteDatabase db,int oldV,int newV){}
    Patient get(String id){try(Cursor c=getReadableDatabase().query("patients",null,"nuhsa=?",new String[]{id},null,null,null)){return c.moveToFirst()?new Patient(c.getString(1),c.getString(2),c.getString(3),c.getString(0)):null;}}
    ArrayList<Patient> find(String q,String blood){ArrayList<Patient> out=new ArrayList<>();try(Cursor c=getReadableDatabase().query("patients",null,null,null,null,null,"name COLLATE NOCASE")){String needle=norm(q);while(c.moveToNext()){Patient p=new Patient(c.getString(1),c.getString(2),c.getString(3),c.getString(0));String full=norm(p.getNombre()+" "+p.getApellidos()+" "+p.getNuhsa());if((needle.isEmpty()||full.contains(needle))&&(blood.isEmpty()||blood.equals(p.getGrupoSanguineo())))out.add(p);}}return out;}
    boolean save(Patient p,String previous){ContentValues v=new ContentValues();v.put("nuhsa",p.getNuhsa());v.put("name",p.getNombre());v.put("surname",p.getApellidos());v.put("blood",p.getGrupoSanguineo());SQLiteDatabase db=getWritableDatabase();if(previous==null)return db.insertWithOnConflict("patients",null,v,SQLiteDatabase.CONFLICT_IGNORE)!=-1; if(!previous.equals(p.getNuhsa())){db.beginTransaction();try{db.update("appointments",value("nuhsa",p.getNuhsa()),"nuhsa=?",new String[]{previous});db.delete("patients","nuhsa=?",new String[]{previous});long result=db.insertWithOnConflict("patients",null,v,SQLiteDatabase.CONFLICT_IGNORE);if(result!=-1)db.setTransactionSuccessful();return result!=-1;}finally{db.endTransaction();}}return db.update("patients",v,"nuhsa=?",new String[]{previous})>0;}
    private ContentValues value(String key,String val){ContentValues v=new ContentValues();v.put(key,val);return v;}
    void delete(String id){SQLiteDatabase db=getWritableDatabase();db.beginTransaction();try{db.delete("appointments","nuhsa=?",new String[]{id});db.delete("patients","nuhsa=?",new String[]{id});db.setTransactionSuccessful();}finally{db.endTransaction();}}
    ArrayList<String[]> appointments(String id){ArrayList<String[]> a=new ArrayList<>();try(Cursor c=getReadableDatabase().query("appointments",new String[]{"id","date","time","reason"},"nuhsa=?",new String[]{id},null,null,"date,time")){while(c.moveToNext())a.add(new String[]{c.getString(0),c.getString(1),c.getString(2),c.getString(3)});}return a;}
    boolean addAppointment(String id,String date,String time,String reason){ContentValues v=new ContentValues();v.put("nuhsa",id);v.put("date",date);v.put("time",time);v.put("reason",reason);return getWritableDatabase().insert("appointments",null,v)!=-1;}
    void cancelAppointment(String id){getWritableDatabase().delete("appointments","id=?",new String[]{id});}
    boolean slotTaken(String id,String date,String time){try(Cursor c=getReadableDatabase().rawQuery("SELECT 1 FROM appointments WHERE nuhsa=? AND date=? AND time=?",new String[]{id,date,time})){return c.moveToFirst();}}
    private static String norm(String s){return Normalizer.normalize(s==null?"":s,Normalizer.Form.NFD).replaceAll("\\p{M}+","").toLowerCase(Locale.ROOT).trim();}
}
