package com.example.reviewphim;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db"; // Tên cơ sở dữ liệu
    private static final int DATABASE_VERSION = 2; // Phiên bản cơ sở dữ liệu (tăng lên 2 để nâng cấp)

    // Tên bảng và cột cho bảng "theloai"
    private static final String TABLE_THELOAI = "theloai";
    private static final String COLUMN_MA_THELOAI = "ma_theloai";
    private static final String COLUMN_TEN_THELOAI = "ten_theloai";

    // Tên bảng và cột cho bảng "phim"
    private static final String TABLE_PHIM = "phim";
    private static final String COLUMN_MA_PHIM = "ma_phim";
    private static final String COLUMN_THELOAI_PHIM = "theloai";
    private static final String COLUMN_TEN_PHIM = "ten_phim";
    private static final String COLUMN_NGAY_BAT_DAU = "ngay_bat_dau";
    private static final String COLUMN_KET_THUC_CHUA = "ket_thuc_chua";
    private static final String COLUMN_TAP_DA_XEM = "tap_da_xem";
    private static final String COLUMN_DIEM_DANH_GIA = "diem_danh_gia"; // Điểm đánh giá

    // Câu lệnh tạo bảng "theloai"
    private static final String CREATE_TABLE_THELOAI =
            "CREATE TABLE " + TABLE_THELOAI + " (" +
                    COLUMN_MA_THELOAI + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TEN_THELOAI + " TEXT NOT NULL);";

    // Câu lệnh tạo bảng "phim"
    private static final String CREATE_TABLE_PHIM =
            "CREATE TABLE " + TABLE_PHIM + " (" +
                    COLUMN_MA_PHIM + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_THELOAI_PHIM + " INTEGER, " +
                    COLUMN_TEN_PHIM + " TEXT NOT NULL, " +
                    COLUMN_NGAY_BAT_DAU + " TEXT, " +
                    COLUMN_KET_THUC_CHUA + " INTEGER, " +
                    COLUMN_TAP_DA_XEM + " INTEGER, " +
                    COLUMN_DIEM_DANH_GIA + " REAL, " + // Thêm cột điểm đánh giá
                    "FOREIGN KEY (" + COLUMN_THELOAI_PHIM + ") REFERENCES " + TABLE_THELOAI + "(" + COLUMN_MA_THELOAI + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_THELOAI); // Tạo bảng thể loại
        db.execSQL(CREATE_TABLE_PHIM);   // Tạo bảng phim
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_PHIM + " ADD COLUMN " + COLUMN_DIEM_DANH_GIA + " REAL DEFAULT 0"); // Thêm cột nếu chưa có
        }
    }

    // Lấy danh sách tất cả các bộ phim
    public List<Movie> getAllMovies() {
        List<Movie> movieList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT phim.ma_phim, phim.ten_phim, phim.theloai, theloai.ten_theloai, " +
                "phim.ngay_bat_dau, phim.ket_thuc_chua, phim.tap_da_xem, phim.diem_danh_gia " +
                "FROM phim " +
                "LEFT JOIN theloai ON phim.theloai = theloai.ma_theloai";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MA_PHIM));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_PHIM));
                int genreId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_THELOAI_PHIM));
                String genreName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_THELOAI));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NGAY_BAT_DAU));
                int completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KET_THUC_CHUA));
                int episodesWatched = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TAP_DA_XEM));
                float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_DIEM_DANH_GIA)); // Điểm đánh giá

                // Tạo đối tượng Movie
                Movie movie = new Movie(id, name, genreId, startDate, completed, episodesWatched, rating);
                movieList.add(movie);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return movieList;
    }
}
