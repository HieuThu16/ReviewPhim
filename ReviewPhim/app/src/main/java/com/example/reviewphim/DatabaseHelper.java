package com.example.reviewphim;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db"; // Tên cơ sở dữ liệu
    private static final int DATABASE_VERSION = 3; // Tăng phiên bản lên 3 để thêm cột ghi chú

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
    private static final String COLUMN_DIEM_DANH_GIA = "diem_danh_gia";
    private static final String COLUMN_GHI_CHU = "ghi_chu"; // Thêm cột ghi chú

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
                    COLUMN_DIEM_DANH_GIA + " REAL, " +
                    COLUMN_GHI_CHU + " TEXT, " + // Thêm cột ghi chú
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
            db.execSQL("ALTER TABLE " + TABLE_PHIM + " ADD COLUMN " + COLUMN_DIEM_DANH_GIA + " REAL DEFAULT 0"); // Thêm cột điểm đánh giá
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE " + TABLE_PHIM + " ADD COLUMN " + COLUMN_GHI_CHU + " TEXT DEFAULT NULL"); // Thêm cột ghi chú
        }
    }

    // Lấy danh sách tất cả các bộ phim (cập nhật để bao gồm cột ghi chú)
    public List<Movie> getAllMovies() {
        List<Movie> movieList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT phim.ma_phim, phim.ten_phim, phim.theloai, theloai.ten_theloai, " +
                "phim.ngay_bat_dau, phim.ket_thuc_chua, phim.tap_da_xem, phim.diem_danh_gia, phim.ghi_chu " +
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
                float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_DIEM_DANH_GIA));
                String note = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GHI_CHU)); // Lấy ghi chú

                // Tạo đối tượng Movie
                Movie movie = new Movie(id, name, genreId, startDate, completed, episodesWatched, rating, note);
                movieList.add(movie);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return movieList;
    }
    public List<Movie> searchMoviesByName(String query) {
        List<Movie> movieList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        // Thêm tên bảng "phim" vào câu truy vấn
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PHIM + " WHERE " + COLUMN_TEN_PHIM + " LIKE ?", new String[]{"%" + query + "%"});

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MA_PHIM)));
                movie.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_PHIM)));
                movie.setGenreId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_THELOAI_PHIM)));
                movie.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NGAY_BAT_DAU)));
                movie.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_KET_THUC_CHUA)));
                movie.setEpisodesWatched(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TAP_DA_XEM)));
                movie.setRating(cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_DIEM_DANH_GIA)));
                movie.setGhiChu(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GHI_CHU))); // Lấy ghi chú
                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return movieList; // Trả về danh sách, có thể rỗng nếu không tìm thấy kết quả
    }

    public List<String> getAllGenres() {
        List<String> genreList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_TEN_THELOAI + " FROM " + TABLE_THELOAI, null);

        if (cursor.moveToFirst()) {
            do {
                String genreName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEN_THELOAI));
                genreList.add(genreName);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return genreList;
    }
    public List<Movie> searchMoviesByNameAndGenre(String query, String genre) {
        List<Movie> movieList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Lấy mã thể loại từ bảng thể loại
        String genreId = null;
        Cursor genreCursor = db.rawQuery("SELECT " + COLUMN_MA_THELOAI +
                " FROM " + TABLE_THELOAI +
                " WHERE " + COLUMN_TEN_THELOAI + " = ?", new String[]{genre});

        if (genreCursor.moveToFirst()) {
            genreId = genreCursor.getString(genreCursor.getColumnIndex(COLUMN_MA_THELOAI));
        }
        genreCursor.close();

        if (genreId != null) {
            // Lấy danh sách phim từ bảng phim theo mã thể loại
            Cursor movieCursor = db.rawQuery("SELECT * FROM " + TABLE_PHIM +
                    " WHERE " + COLUMN_TEN_PHIM + " LIKE ? " +
                    " AND " + COLUMN_THELOAI_PHIM + " = ?", new String[]{"%" + query + "%", genreId});

            if (movieCursor.moveToFirst()) {
                do {
                    Movie movie = new Movie();
                    movie.setId(movieCursor.getInt(movieCursor.getColumnIndex("ma_phim")));
                    movie.setName(movieCursor.getString(movieCursor.getColumnIndex("ten_phim")));
                    movie.setGenreId(movieCursor.getInt(movieCursor.getColumnIndex("theloai")));
                    movie.setStartDate(movieCursor.getString(movieCursor.getColumnIndex("ngay_bat_dau")));
                    movie.setCompleted(movieCursor.getInt(movieCursor.getColumnIndex("ket_thuc_chua")));
                    movie.setEpisodesWatched(movieCursor.getInt(movieCursor.getColumnIndex("tap_da_xem")));
                    movie.setRating(movieCursor.getFloat(movieCursor.getColumnIndex("diem_danh_gia")));
                    movieList.add(movie);
                } while (movieCursor.moveToNext());
            }
            movieCursor.close();
        }

        db.close();
        return movieList;
    }


    public String getGenreNameById(int genreId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM genres WHERE id = ?", new String[]{String.valueOf(genreId)});
        if (cursor != null && cursor.moveToFirst()) {
            String genreName = cursor.getString(cursor.getColumnIndex("name"));
            cursor.close();
            return genreName;
        }
        return null;
    }
    // Phương thức lấy tổng số lượng phim trong ngày
    public int getTotalMoviesInDay(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM phim WHERE ngay_bat_dau = ?", new String[]{date});
        int totalMovies = 0;
        if (cursor.moveToFirst()) {
            totalMovies = cursor.getInt(0);
        }
        cursor.close();
        return totalMovies;
    }

    // Phương thức lấy số lượng phim theo thể loại trong ngày
    public int getMoviesCountByGenreInDay(String date, String genre) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM phim WHERE ngay_bat_dau = ? AND theloai = ?", new String[]{date, genre});
        int genreCount = 0;
        if (cursor.moveToFirst()) {
            genreCount = cursor.getInt(0);
        }
        cursor.close();
        return genreCount;
    }
    public List<String> getAvailableDates() {
        List<String> dateList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT ngay_bat_dau FROM phim ORDER BY ngay_bat_dau ASC", null);

        if (cursor.moveToFirst()) {
            do {
                dateList.add(cursor.getString(0)); // Lấy ngày từ cột đầu tiên
            } while (cursor.moveToNext());
        }

        cursor.close();
        return dateList;
    }

    // Phương thức lấy thống kê số lượng phim theo thể loại trong ngày

    public List<GenreStatistic> getGenreStatisticsForDate(String date) {
        List<GenreStatistic> stats = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        List<Integer> colors = getDefaultColors(); // Lấy danh sách màu

        Cursor cursor = db.rawQuery(
                "SELECT theloai.ten_theloai, COUNT(phim.ma_phim) AS so_luong " +
                        "FROM phim " +
                        "JOIN theloai ON phim.theloai = theloai.ma_theloai " +
                        "WHERE phim.ngay_bat_dau = ? " +
                        "GROUP BY theloai.ten_theloai",
                new String[]{date});

        int colorIndex = 0; // Dùng để lặp qua danh sách màu
        if (cursor.moveToFirst()) {
            do {
                String genreName = cursor.getString(0); // Lấy tên thể loại
                int count = cursor.getInt(1);          // Lấy số lượng phim
                int color = colors.get(colorIndex % colors.size()); // Lấy màu theo thứ tự tuần hoàn
                stats.add(new GenreStatistic(genreName, count, color));
                colorIndex++; // Tăng chỉ số màu
            } while (cursor.moveToNext());
        }

        cursor.close();
        return stats;
    }

    private List<Integer> getDefaultColors() {
        return Arrays.asList(
                Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN,
                Color.YELLOW, Color.LTGRAY, Color.DKGRAY, Color.BLACK, Color.WHITE
        );
    }


}
