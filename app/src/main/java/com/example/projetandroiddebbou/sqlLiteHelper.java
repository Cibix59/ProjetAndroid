package com.example.projetandroiddebbou;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

        import com.example.projetandroiddebbou.Photo;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;


public class sqlLiteHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "Photo_Manager";

    // Table name: Note.
    private static final String TABLE_PHOTO = "Photo";

    private static final String COLUMN_PHOTO_ID ="Photo_Id";
    private static final String COLUMN_PHOTO_CHEMIN ="Photo_Chemin";
    private static final String COLUMN_LATITUDE = "Latitude";
    private static final String COLUMN_LONGITUDE = "Longitude";
    private static final String COLUMN_GROUPE = "Groupe";
    private static final String COLUMN_NOM = "Nom";

    public sqlLiteHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        Log.d("lifecycle", "creation table");
        // Script.
        String script = "CREATE TABLE " + TABLE_PHOTO + "("
                + COLUMN_PHOTO_ID + " INTEGER PRIMARY KEY," + COLUMN_PHOTO_CHEMIN + " TEXT,"
                + COLUMN_LATITUDE + " REAL,"
                + COLUMN_LONGITUDE + " REAL,"+COLUMN_GROUPE+" REAL,"+COLUMN_NOM+" REAL"+")";
        // Execute Script.
        db.execSQL(script);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO);

        // Create tables again
        onCreate(db);
    }


//Si la base est vide, on insere des choses
    public void createDefaultPhotoIfNeed()  {
        int count = this.getPhotosCount();
        Log.d("lifecycle", "count : "+count);
        if(count ==0 ) {
            Photo photo1 = new Photo("/storage/emulated/0/Pictures/yourTitle (1).jpg",10.5,20.2,"vacance","photovac1");
            Photo photo2 = new Photo("/storage/emulated/0/Pictures/yourTitle.jpg",17.5,55.2,"groupeTruc","truc1");
            this.addPhoto(photo1);
            this.addPhoto(photo2);
        }
    }


    public long addPhoto(Photo photo) {
        Log.i(TAG, "MyDatabaseHelper.addPhoto ... " );

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PHOTO_CHEMIN, photo.getCheminPhoto());
        values.put(COLUMN_LATITUDE, photo.getLatitude());
        values.put(COLUMN_LONGITUDE, photo.getLongitude());
        values.put(COLUMN_GROUPE, photo.getGroupe());
        values.put(COLUMN_NOM, photo.getNom());

        Log.d("lifecycle", "values : "+values.toString());

        // Inserting Row
        long tmp =db.insert(TABLE_PHOTO, null, values);

        // Closing database connection
        db.close();
        return tmp;
    }


    public Photo getPhoto(long id) {
        Log.i(TAG, "MyDatabaseHelper.getNote ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_PHOTO+" WHERE Photo_Id ="+id;
        Cursor cursor = db.rawQuery(selectQuery, null);
/*        Cursor cursor = db.query(TABLE_PHOTO, new String[] { COLUMN_PHOTO_ID,
                        COLUMN_PHOTO_CHEMIN, COLUMN_LATITUDE }, COLUMN_PHOTO_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cursor != null)
            cursor.moveToFirst();

        //si bug, essayer Double.parseDouble...
        Photo photo = new Photo(cursor.getString(1),cursor.getDouble(2), cursor.getDouble(3), cursor.getString(4), cursor.getString(5));

        // Closing database connection
        cursor.close();
        db.close();
        // return note
        return photo;
    }


    public List<Photo> getAllPhotos() {
        Log.i(TAG, "MyDatabaseHelper.getAllPhotos ... " );

        List<Photo> photoList = new ArrayList<Photo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PHOTO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("lifecycle", "la? : "+selectQuery);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Photo photo = new Photo(cursor.getString(1),cursor.getDouble(2), cursor.getDouble(3), cursor.getString(4), cursor.getString(5));
                // Adding note to list
                Log.d("lifecycle", "retrouve : "+photo.getNom());
                Log.d("lifecycle", "cursor0 : "+cursor.getString(0));
                Log.d("lifecycle", "cursor1 : "+cursor.getString(1));
                Log.d("lifecycle", "cursor2 : "+cursor.getString(2));
                Log.d("lifecycle", "cursor3 : "+cursor.getString(3));
                Log.d("lifecycle", "cursor4 : "+cursor.getString(4));
                Log.d("lifecycle", "cursor4 : "+cursor.getString(5));
                photoList.add(photo);
            } while (cursor.moveToNext());
        }

        // Closing database connection
        cursor.close();
        db.close();
        // return photo list
        return photoList;
    }

    public int getPhotosCount() {
        Log.i(TAG, "MyDatabaseHelper.getPhotosCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_PHOTO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        // Closing database connection
        db.close();
        // return count
        return count;
    }


    public int updatePhoto(int id,Photo photo) {
        Log.i(TAG, "MyDatabaseHelper.updatePhoto ... "  + photo.getCheminPhoto());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PHOTO_CHEMIN, photo.getCheminPhoto());
        values.put(COLUMN_LATITUDE, photo.getLatitude());
        values.put(COLUMN_LONGITUDE, photo.getLongitude());
        values.put(COLUMN_GROUPE, photo.getGroupe());
        values.put(COLUMN_NOM, photo.getNom());

        db.close();
        // updating row
        return db.update(TABLE_PHOTO, values, COLUMN_PHOTO_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void deletePhoto(long id) {
        Log.i(TAG, "MyDatabaseHelper.deletePhoto ... " + id );

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PHOTO, COLUMN_PHOTO_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }


}
