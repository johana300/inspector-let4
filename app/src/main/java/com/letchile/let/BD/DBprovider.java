package com.letchile.let.BD;

/**
 * Created by Mauricio on 06/01/2018.
 */
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;


public class DBprovider extends SQLiteOpenHelper{

    //instanciar la base de datos
    private static final int VERSION_BASE_DATOS = 3;
    private static final String NOMBRE_BASE_DATOS = "datosbase";

    //crear tablas
    private static final String TABLA_USUARIO = "CREATE TABLE USUARIO ( usr TEXT PRIMARY KEY, pwd TEXT, perfil int )";
    private static final String TABLA_INSPECCION = "CREATE TABLE INSPECCION " +
            "(id_inspeccion INTEGER, asegurado TEXT, apellidoPaterno TEXT, apellidoMaterno TEXT, rut TEXT, comuna_cita TEXT," +
            "marca TEXT, modelo TEXT, patente TEXT, ramo TEXT, fecha_cita DATE, hora TEXT, fecha_inspeccion TEXT" +
            "fecha_cierre DATETIME, fono INTEGER, comuna TEXT, direccion TEXT, cia TEXT, corredor TEXT, comentario TEXT," +
            "direccion_cita TEXT, enviado INTEGER, pac TEXT, estado INTEGER, email TEXT)";

    private static final String TABLA_FOTO="CREATE TABLE FOTO(id_inspeccion INTEGER,id_foto INTEGER, foto TEXT, comentario TEXT,enviado INTEGER)";
    private static final String TABLA_VALOR="CREATE TABLE VALOR(id_inspeccion INTEGER, id_campo INTEGER,valor TEXT, enviado INTEGER)";
    private static final String TABLA_HITO="CREATE TABLE HITO(id_hito INTEGER, glosa TEXT, orden INTEGER)";
    private static final String TABLA_BITACORA="CREATE TABLE BITACORA(id_inspeccion INTEGER, id_hito INTEGER, glosa TEXT, fecha TEXT, enviado INTEGER)";
    private static final String TABLA_COMUNA = "CREATE TABLE COMUNA(id_region INTEGER, region TEXT, comuna TEXT)";


    public DBprovider(Context context){
        super(context,NOMBRE_BASE_DATOS,null,VERSION_BASE_DATOS);
    }


    //CUANDO SE EJECUTA POR PRIMERA VEZ, SE CREA LA BASE DE DATOS Y LAS TABLAS CORRESPONDIENTES
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TABLA_USUARIO);
        db.execSQL(TABLA_INSPECCION);
        db.execSQL(TABLA_FOTO);
        db.execSQL(TABLA_VALOR);
        db.execSQL(TABLA_HITO);
        db.execSQL(TABLA_BITACORA);
        db.execSQL(TABLA_COMUNA);
    }

    //en caso de que se necesite actualizar la base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldDb, int newDb)
    {
        db.execSQL("DROP TABLE IF EXISTS USUARIO");
        db.execSQL("DROP TABLE IF EXISTS INSPECCION");
        db.execSQL("DROP TABLE IF EXISTS FOTO");
        db.execSQL("DROP TABLE IF EXISTS VALOR");
        db.execSQL("DROP TABLE IF EXISTS HITO");
        db.execSQL("DROP TABLE IF EXISTS BITACORA");
        db.execSQL("DROP TABLE IF EXISTS COMUNA");
        onCreate(db);
    }


    //MÉTODOS PARA EL USUARIO
    public void inserUsuario(String usr, String pwd){
        SQLiteDatabase db = getWritableDatabase();

        if(db != null)
        {
            db.execSQL("INSERT INTO USUARIO (usr,pwd) VALUES('"+usr+"','"+pwd+"');");
        }
        db.close();
    }
    //asd
    public String existeUsuario(String usr, String pwd){
        String resp = "";
        //prueba
        SQLiteDatabase db = getWritableDatabase();

        if(db != null) {
            Cursor cur = db.rawQuery("SELECT usr FROM USUARIO WHERE usr = '" +usr+"' and pwd ='"+pwd+"';", null);

            cur.moveToFirst();

            if (cur != null) {
                resp = cur.getString(0);
            } else {
                resp = "";
            }
            cur.close();
        }
        db.close();
        return resp;
    }


    //verifica usuario
    public String obtenerUsuario(){
        String respuesta="";
        SQLiteDatabase db = getReadableDatabase();
        Cursor aRS = db.rawQuery("SELECT usr FROM USUARIO", null);
        if(aRS.getCount() > 0)
        {
            aRS.moveToFirst();
            respuesta= aRS.getString(0);
        }
        aRS.close();
        db.close();
        return respuesta;
    }


    //INSERTA INSPECCIONES RESCATADAS DEL SERVIDOR
    public String insertaInspecciones(Integer id_inspeccion, String asegurado, String paternoAsegurado, String maternoAsegurado, String rut, String comunaAsegurado,
                                      String direccionAsegurado, Integer fono, String marca, String modelo, String direccionCita, String fechaCita, String horaCita,
                                      String comunaCita,String comentarioCita, String idRamo, String patente, String cia, String corredor, String pac){
        String resp = "";
        SQLiteDatabase db = getWritableDatabase();

        if(db != null)
        {
            try{
                db.execSQL("INSERT INTO INSPECCION (id_inspeccion,asegurado,apellidoPaterno,apellidoMaterno,rut,comuna,direccion,fono,marca,modelo,direccion_cita,fecha_cita," +
                        "hora,comuna_cita,comentario,ramo,patente,cia,corredor,pac,enviado,estado)" +
                        " VALUES("+id_inspeccion+",'"+asegurado+"','"+paternoAsegurado+"','"+maternoAsegurado+"','"+rut+"','"+comunaAsegurado+"','"+direccionAsegurado+"',"+fono+",'"+marca+"','"+modelo+"'," +
                        "'"+direccionCita+"','"+fechaCita+"','"+horaCita+"','"+comunaCita+"','"+comentarioCita+"','"+idRamo+"','"+patente+"'," +
                        "'"+cia+"','"+corredor+"',"+pac+","+0+","+0+");");

                resp="Ok";
            }catch (Exception e){
                resp = "Error: "+e.getMessage();
            }
        }else{
            resp = "no hay base de datos";
        }
        db.close();
        return resp;
    }


    //lista de inspecciones que no esten enviadas
    public String[][] listaInspecciones(){
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        Cursor aRS = db.rawQuery("SELECT * FROM INSPECCION WHERE enviado<>2 ORDER BY fecha_cita", null);

        if(aRS.getCount()>0){
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()){
                aData[count] = new String[8];
                aData[count][0] = Integer.toString(aRS.getInt(aRS.getColumnIndex("id_inspeccion")));
                aData[count][1] = aRS.getString(aRS.getColumnIndex("fecha_cita"));
                aData[count][2] = aRS.getString(aRS.getColumnIndex("hora"));
                aData[count][3] = aRS.getString(aRS.getColumnIndex("ramo"));
                aData[count][4] = aRS.getString(aRS.getColumnIndex("direccion"));
                aData[count][5] = aRS.getString(aRS.getColumnIndex("comuna"));
                aData[count][6] = aRS.getString(aRS.getColumnIndex("patente"));
                aData[count][7] = aRS.getString(aRS.getColumnIndex("enviado"));
                count++;
            }
        }else{
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }

    //lista datos inspeccion x id_inspeccion
    public String[][] BuscaDatosInspeccion(String id_inspeccion)
    {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;

        Cursor aRs = db.rawQuery("SELECT * FROM INSPECCION where id_inspeccion="+Integer.parseInt(id_inspeccion),null);
        if(aRs.getCount()>0)
        {
            aData = new String[aRs.getCount()][];
            while (aRs.moveToNext()){
                aData[count] = new String[12];
                aData[count][0] = Integer.toString(aRs.getInt(aRs.getColumnIndex("id_inspeccion")));
                aData[count][1] = aRs.getString(aRs.getColumnIndex("asegurado"));
                aData[count][2] = Integer.toString(aRs.getInt(aRs.getColumnIndex("fono")));
                aData[count][3] = aRs.getString(aRs.getColumnIndex("comentario"));
                aData[count][4] = aRs.getString(aRs.getColumnIndex("direccion"));
                aData[count][5] = aRs.getString(aRs.getColumnIndex("comuna"));
                aData[count][6] = aRs.getString(aRs.getColumnIndex("patente"));
                aData[count][7] = aRs.getString(aRs.getColumnIndex("ramo"));
                aData[count][8] = aRs.getString(aRs.getColumnIndex("apellidoPaterno"));
                aData[count][9] = aRs.getString(aRs.getColumnIndex("apellidoMaterno"));
                aData[count][10] = aRs.getString(aRs.getColumnIndex("rut"));
                aData[count][11] = aRs.getString(aRs.getColumnIndex("email"));
                count++;
            }
        }else{
            aData = new String[0][];
        }
        aRs.close();
        db.close();
        return (aData);
    }

    //borrar registros de la tabla inspeccion
    public void borrarInspeccion(int id_inspeccion){
        SQLiteDatabase db = getWritableDatabase();

        if(db != null)
        {
            db.execSQL("DELETE FROM INSPECCION WHERE id_inspeccion="+id_inspeccion);
        }
        db.close();
    }


    //borrar registros de la tabla comuna
    public void borrarComunas(){
        SQLiteDatabase db = getWritableDatabase();

        if(db != null)
        {
            db.execSQL("DELETE FROM COMUNA");
        }
        db.close();
    }

    //Insertar comunas
    public String insertarComuna(int id_region, String region , String comuna)
    {
        String resp="";
        SQLiteDatabase db = getWritableDatabase();

        if(db!=null)
        {
            db.execSQL("INSERT INTO COMUNA (id_region,region,comuna) VALUES ("+id_region+",'"+region+"','"+comuna+"')");
            resp="Ok";
        }else{
            resp="No";
        }
        db.close();
        return resp;
    }

    //lista total de comunas y regiones
    public Integer listatotalComunasRegiones(){
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();

        //db.rawQuery("SELECT COUNT(comuna) FROM COMUNA", null);
        long i = DatabaseUtils.queryNumEntries(db,"COMUNA");

        count = safeLongToInt(i);

        db.close();
        return count;
    }


    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }


    //SON MUCHOS REGIONES PARA DISTINTOS ID_COMUNA
    public String[][] listaRegiones(){
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        Cursor aRS = db.rawQuery("SELECT DISTINCT region FROM COMUNA", null);

        if(aRS.getCount()>0){
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()){
                aData[count] = new String[3];
                aData[count][0] = aRS.getString(aRS.getColumnIndex("region"));
                count++;
            }
        }else{
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }


    //SI SE ACTUALIZAN LAS COMUNAS (OSEA MÁS 1)SE BORRAN TODAS LAS COMUNAS Y SE VUELVE A CARGAR
    //LISTA TODAS LAS COMUNAS
    public String[][] listaComunas(String region){
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        Cursor aRS = db.rawQuery("SELECT DISTINCT comuna FROM COMUNA WHERE region = '"+region+"'", null);

        if(aRS.getCount()>0){
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()){
                aData[count] = new String[3];
                aData[count][0] = aRS.getString(aRS.getColumnIndex("comuna"));
                count++;
            }
        }else{
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }

    public String[][] obtenerRegion(String comuna){
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String[][] aData = null;
        Cursor aRS = db.rawQuery("SELECT DISTINCT region FROM COMUNA WHERE comuna = '"+comuna+"'", null);

        if(aRS.getCount()>0){
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()){
                aData[count] = new String[3];
                aData[count][0] = aRS.getString(aRS.getColumnIndex("region"));
                count++;
            }
        }else{
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }



    public String[][] listaFotos(String id_inspeccion) {
        int count = 0;
        String[][] aData =null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor aRS = db.rawQuery("SELECT * FROM FOTO WHERE id_inspeccion ="+Integer.parseInt(id_inspeccion),null);

        if(aRS.getCount()>0)
        {
            aData = new String[aRS.getCount()][];
            while (aRS.moveToNext()){
                aData[count] = new String[4];
                aData[count][0] = Integer.toString(aRS.getInt(aRS.getColumnIndex("id_foto")));
                aData[count][1] = aRS.getString(aRS.getColumnIndex("foto"));
                aData[count][2] = aRS.getString(aRS.getColumnIndex("comentario"));
                aData[count][0] = Integer.toString(aRS.getInt(aRS.getColumnIndex("enviado")));
            }
        }else{
            aData = new String[0][];
        }
        aRS.close();
        db.close();
        return (aData);
    }


    //Obtener el ultimo id_foto para armar el nombre incrementando en 1
    public int obtieneUltimoIdfoto(){
        int respuesta = 0;
        SQLiteDatabase db = getReadableDatabase();
        Cursor ars = db.rawQuery("SELECT id_foto FROM FOTO",null);
        if(ars.getCount()>0)
        {
            ars.moveToLast();
            respuesta = ars.getInt(0);
            ars.close();
            db.close();
        }
        return respuesta;
    }


    //función para insertar foto
    public String insertarFoto(int id_inspeccion,int id_foto,String foto){
        String respuesta = "";
        SQLiteDatabase db = getWritableDatabase();

        if(db != null)
        {
            db.rawQuery("INSERT INTO FOTO (id_inspeccion,id_foto,foto,enviado)" +
                    "VALUES ("+id_inspeccion+","+id_foto+",'"+foto+"',"+0+")",null);
            respuesta = "Ok";
        }else{
            respuesta = "";
        }
        return respuesta;
    }

    public void actualizarAsegInspeccion(Integer id_inspeccion, String asegurado,String apellidoP, String apellidoM, String rut,String direccion,
                                         Integer fono, String email, String comuna){
        SQLiteDatabase db = getWritableDatabase();

        if(db != null)
        {
            db.execSQL("UPDATE INSPECCION SET asegurado = '"+asegurado+"', apellidoPaterno = '"+apellidoP+"', apellidoMaterno ='"+apellidoM+"'," +
                    "rut = '"+rut+"', direccion='"+direccion+"', fono="+fono+", email='"+email+"', comuna='"+comuna+"' WHERE id_inspeccion="+id_inspeccion);
        }
        db.close();
    }





    /*
    *   // Adding new image
    public void addImage(Image image) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE_PATH, image.imagePath); // Image path

        // Inserting Row
        db.insert(TABLE_IMAGE, null, values);
        db.close(); // Closing database connection
    }



    // Getting single image
    public Image getImage(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_IMAGE, new String[] { KEY_ID,
                        KEY_IMAGE_PATH}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Image image = new Image(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1));
        // return image
        return image;
    }
}
    * */




}
