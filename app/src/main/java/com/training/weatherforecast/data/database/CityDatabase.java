package com.training.weatherforecast.data.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.training.weatherforecast.model.CityModel;

@Database(entities = {CityModel.class} , version = 1, exportSchema = false)
public abstract class CityDatabase extends RoomDatabase {
    private static CityDatabase INSTANCE;

    public abstract CityDao cityDao();

    public static synchronized CityDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),CityDatabase.class,"Weather")
                    .addCallback(roomCallback)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
        private CityDao cityDao;

        public PopulateDbAsyncTask(CityDatabase db) {
            cityDao = db.cityDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cityDao.insert(new CityModel("Amman".toUpperCase()));
            cityDao.insert(new CityModel("London".toUpperCase()));
            cityDao.insert(new CityModel("New York".toUpperCase()));
            cityDao.insert(new CityModel("Barcelona".toUpperCase()));
            return null;
        }
    }
}
