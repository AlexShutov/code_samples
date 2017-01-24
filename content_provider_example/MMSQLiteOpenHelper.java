package com.myapp.io.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.myapp.app.BuildConfig;
import com.myapp.io.provider.comment.CommentColumns;
import com.myapp.io.provider.commentauthors.CommentAuthorsColumns;
import com.myapp.io.provider.genre.GenreColumns;
import com.myapp.io.provider.member.MemberColumns;
import com.myapp.io.provider.message.MessageColumns;
import com.myapp.io.provider.playlist.PlaylistColumns;
import com.myapp.io.provider.room.RoomColumns;
import com.myapp.io.provider.song.SongColumns;
import com.myapp.io.provider.soundfeed.SoundFeedColumns;
import com.myapp.io.provider.user.UserColumns;
import com.myapp.io.provider.visiblelist.VisibleListColumns;

public class MMSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = MMSQLiteOpenHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "myapp.db";
    private static final int DATABASE_VERSION = 1;
    private static MMSQLiteOpenHelper sInstance;
    private final Context mContext;
    private final MMSQLiteOpenHelperCallbacks mOpenHelperCallbacks;

    // @formatter:off
    public static final String SQL_CREATE_TABLE_COMMENT = "CREATE TABLE IF NOT EXISTS "
            + CommentColumns.TABLE_NAME + " ( "
            + CommentColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CommentColumns.USER_ID + " TEXT, "
            + CommentColumns.USER_AVATAR + " TEXT, "
            + CommentColumns.USER_FULLNAME + " TEXT, "
            + CommentColumns.COMMENT_ID + " TEXT, "
            + CommentColumns.TEXT + " TEXT, "
            + CommentColumns.CREATED_DATE + " TEXT, "
            + CommentColumns.BLOCKED + " INTEGER, "
            + CommentColumns.ATTACHED_ID + " TEXT, "
            + CommentColumns.FEED_ID + " TEXT, "
            + CommentColumns.ATTACHED_TITLE + " TEXT, "
            + CommentColumns.ATTACHED_YOUTUBE_ID + " TEXT, "
            + CommentColumns.ATTACHED_NEXT_PAGE_TOKEN + " TEXT, "
            + CommentColumns.VERSION + " INTEGER, "
            + CommentColumns.HASHCODE + " INTEGER "
            + ", CONSTRAINT unique_name UNIQUE (comment_id) ON CONFLICT REPLACE"
            + " );";

    public static final String SQL_CREATE_TABLE_COMMENT_AUTHORS = "CREATE TABLE IF NOT EXISTS "
            + CommentAuthorsColumns.TABLE_NAME + " ( "
            + CommentAuthorsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CommentAuthorsColumns.USER_ID + " TEXT, "
            + CommentAuthorsColumns.FEED_ID + " TEXT, "
            + CommentAuthorsColumns.VERSION + " INTEGER, "
            + CommentAuthorsColumns.HASHCODE + " INTEGER "
            + ", CONSTRAINT unique_name UNIQUE (user_id, feed_id) ON CONFLICT REPLACE"
            + " );";

    public static final String SQL_CREATE_TABLE_GENRE = "CREATE TABLE IF NOT EXISTS "
            + GenreColumns.TABLE_NAME + " ( "
            + GenreColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + GenreColumns.NAME + " TEXT "
            + " );";

    public static final String SQL_CREATE_TABLE_MEMBER = "CREATE TABLE IF NOT EXISTS "
            + MemberColumns.TABLE_NAME + " ( "
            + MemberColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MemberColumns.ROOM_ID + " TEXT, "
            + MemberColumns.USER_ID + " TEXT, "
            + MemberColumns.FULL_NAME + " TEXT, "
            + MemberColumns.AVATAR + " TEXT, "
            + MemberColumns.PURL + " TEXT "
            + " );";

    public static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS "
            + MessageColumns.TABLE_NAME + " ( "
            + MessageColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MessageColumns.ROOM_ID + " TEXT, "
            + MessageColumns.MESSAGE_ID + " TEXT, "
            + MessageColumns.READ + " TEXT, "
            + MessageColumns.SONG_NEXT_PAGE_TOKEN + " TEXT, "
            + MessageColumns.SONG_YOUTUBE_ID + " TEXT, "
            + MessageColumns.SONG_TITLE + " TEXT, "
            + MessageColumns.SONG_ID + " TEXT, "
            + MessageColumns.USER_FROM_ID + " TEXT, "
            + MessageColumns.USER_FROM_FULL_NAME + " TEXT, "
            + MessageColumns.USER_FROM_AVATAR + " TEXT, "
            + MessageColumns.USER_FROM_PURL + " TEXT, "
            + MessageColumns.USER_FROM_BLACK_LIST + " TEXT, "
            + MessageColumns.CREATED_DATE + " TEXT, "
            + MessageColumns.TEXT + " TEXT, "
            + MessageColumns.HAS_SONG + " INTEGER DEFAULT 1 "
            + ", CONSTRAINT unique_name UNIQUE (message_id) ON CONFLICT REPLACE"
            + " );";

    public static final String SQL_CREATE_TABLE_PLAYLIST = "CREATE TABLE IF NOT EXISTS "
            + PlaylistColumns.TABLE_NAME + " ( "
            + PlaylistColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PlaylistColumns.PLAYLIST_ID + " TEXT, "
            + PlaylistColumns.PARENT_USER_ID + " TEXT, "
            + PlaylistColumns.USER_ID + " TEXT, "
            + PlaylistColumns.ATTACHED_ID + " TEXT, "
            + PlaylistColumns.ATTACHED_TITLE + " TEXT, "
            + PlaylistColumns.ATTACHED_YOUTUBE_ID + " TEXT, "
            + PlaylistColumns.ATTACHED_NEXT_PAGE_TOKEN + " TEXT, "
            + PlaylistColumns.CREATED_DATE + " TEXT, "
            + PlaylistColumns.REF_COUNT + " INTEGER, "
            + PlaylistColumns.TITLE + " TEXT, "
            + PlaylistColumns.USER_NAME + " TEXT, "
            + PlaylistColumns.SAVED_LIST + " INTEGER "
            + ", CONSTRAINT unique_name UNIQUE (parent_user_id, user_id, playlist_id) ON CONFLICT REPLACE"
            + " );";

    public static final String SQL_CREATE_TABLE_ROOM = "CREATE TABLE IF NOT EXISTS "
            + RoomColumns.TABLE_NAME + " ( "
            + RoomColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + RoomColumns.ROOM_ID + " TEXT, "
            + RoomColumns.ROOM_STATUS + " INTEGER, "
            + RoomColumns.LAST_STATUS_UPDATE + " TEXT, "
            + RoomColumns.CREATED_DATE + " TEXT, "
            + RoomColumns.ROOM_HASH + " TEXT, "
            + RoomColumns.OWNER_ID + " TEXT, "
            + RoomColumns.OWNER_AVATAR + " TEXT, "
            + RoomColumns.OWNER_NAME + " TEXT, "
            + RoomColumns.OWNER_PURL + " TEXT, "
            + RoomColumns.HIDE_TO_USER + " TEXT, "
            + RoomColumns.UNREAD + " INTEGER "
            + ", CONSTRAINT unique_name UNIQUE (room_id) ON CONFLICT REPLACE"
            + " );";

    public static final String SQL_CREATE_TABLE_SONG = "CREATE TABLE IF NOT EXISTS "
            + SongColumns.TABLE_NAME + " ( "
            + SongColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SongColumns.SONG_ID + " TEXT, "
            + SongColumns.S_ORDER + " INTEGER, "
            + SongColumns.PLAYLIST_ID + " TEXT, "
            + SongColumns.SONG_TITLE + " TEXT, "
            + SongColumns.SONG_IDENTIFIER + " TEXT, "
            + SongColumns.SONG_YOUTUBE_ID + " TEXT, "
            + SongColumns.SONG_NEXT_PAGE_TOKEN + " TEXT, "
            + SongColumns.SONG_ORDER + " INTEGER, "
            + SongColumns.USER_ID + " TEXT "
            + ", CONSTRAINT unique_name UNIQUE (song_id,playlist_id) ON CONFLICT REPLACE"
            + " );";

    public static final String SQL_CREATE_TABLE_SOUND_FEED = "CREATE TABLE IF NOT EXISTS "
            + SoundFeedColumns.TABLE_NAME + " ( "
            + SoundFeedColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SoundFeedColumns.TYPE + " INTEGER, "
            + SoundFeedColumns.TO_ID + " TEXT, "
            + SoundFeedColumns.USER_ID + " TEXT, "
            + SoundFeedColumns.FEED_ID + " TEXT, "
            + SoundFeedColumns.CREATED_DATE + " TEXT, "
            + SoundFeedColumns.BODY_TEXT + " TEXT, "
            + SoundFeedColumns.BODY_TITLE + " TEXT, "
            + SoundFeedColumns.BODY_USER_ID + " TEXT, "
            + SoundFeedColumns.BODY_USER_FULLNAME + " TEXT, "
            + SoundFeedColumns.BODY_POST_ID + " TEXT, "
            + SoundFeedColumns.BODY_ATTACHED_NEXT_PAGE_TOKEN + " TEXT, "
            + SoundFeedColumns.BODY_ATTACHED_YOUTUBE_ID + " TEXT, "
            + SoundFeedColumns.BODY_ATTACHED_TITLE + " TEXT, "
            + SoundFeedColumns.BODY_ATTACHED_ID + " TEXT, "
            + SoundFeedColumns.APPRECIATES_STATUS + " INTEGER, "
            + SoundFeedColumns.USER_FULL_NAME + " TEXT, "
            + SoundFeedColumns.USER_AVATAR + " TEXT, "
            + SoundFeedColumns.LIKE_COUNT + " INTEGER DEFAULT 0, "
            + SoundFeedColumns.COMMENT_COUNT + " INTEGER DEFAULT 0, "
            + SoundFeedColumns.IS_SHARED + " INTEGER, "
            + SoundFeedColumns.SHARED_ID + " TEXT, "
            + SoundFeedColumns.SHARED_BODY_TEXT + " TEXT, "
            + SoundFeedColumns.SHARED_BODY_ATTACHED_NEXT_PAGE_TOKEN + " TEXT, "
            + SoundFeedColumns.SHARED_BODY_ATTACHED_YOUTUBE_ID + " TEXT, "
            + SoundFeedColumns.SHARED_BODY_ATTACHED_TITLE + " TEXT, "
            + SoundFeedColumns.SHARED_BODY_ATTACHED_ID + " TEXT, "
            + SoundFeedColumns.SHARED_BODY_TITLE + " TEXT, "
            + SoundFeedColumns.SHARED_BODY_TYPE + " INTEGER, "
            + SoundFeedColumns.SHARED_BODY_CREATED_DATE + " TEXT, "
            + SoundFeedColumns.SHARED_FEED_USER_NAME + " TEXT, "
            + SoundFeedColumns.SHARED_FEED_USER_AVATAR + " TEXT, "
            + SoundFeedColumns.SHARED_FEED_USER_ID + " TEXT, "
            + SoundFeedColumns.COMMENTED_FIRST_USER_NAME + " TEXT, "
            + SoundFeedColumns.LIKED_FIRST_USER_ID + " TEXT, "
            + SoundFeedColumns.LIKED_FIRST_USER_NAME + " TEXT, "
            + SoundFeedColumns.VERSION + " INTEGER, "
            + SoundFeedColumns.HASHCODE + " INTEGER, "
            + SoundFeedColumns.PARENT_FEED_ID + " TEXT "
            + ", CONSTRAINT unique_name UNIQUE (feed_id,parent_feed_id,user_id) ON CONFLICT REPLACE"
            + " );";

    public static final String SQL_CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "
            + UserColumns.TABLE_NAME + " ( "
            + UserColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + UserColumns.USER_ID + " TEXT, "
            + UserColumns.AVATAR + " TEXT, "
            + UserColumns.BIRTH + " TEXT, "
            + UserColumns.FIRST_NAME + " TEXT, "
            + UserColumns.FULL_NAME + " TEXT, "
            + UserColumns.GENDER + " TEXT, "
            + UserColumns.LAST_NAME + " TEXT, "
            + UserColumns.PLAYLISTS + " INTEGER, "
            + UserColumns.FOLLOWS + " INTEGER, "
            + UserColumns.FOLLOWERS + " INTEGER, "
            + UserColumns.USER_CONNECTIONS + " INTEGER, "
            + UserColumns.PURL + " TEXT, "
            + UserColumns.STATUS_CONNECTION + " TEXT, "
            + UserColumns.STATUS_FOLLOWING + " INTEGER, "
            + UserColumns.CREATED + " TEXT, "
            + UserColumns.VERSION + " INTEGER, "
            + UserColumns.HASHCODE + " INTEGER, "
            + UserColumns.CURRENT_CITY + " TEXT, "
            + UserColumns.WEBPRESENCE + " TEXT, "
            + UserColumns.MUSICGENRES + " TEXT "
            + ", CONSTRAINT unique_name UNIQUE (user_id) ON CONFLICT REPLACE"
            + " );";

    public static final String SQL_CREATE_TABLE_VISIBLE_LIST = "CREATE TABLE IF NOT EXISTS "
            + VisibleListColumns.TABLE_NAME + " ( "
            + VisibleListColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + VisibleListColumns.USER_ID + " TEXT, "
            + VisibleListColumns.VISIBLE_ID + " TEXT, "
            + VisibleListColumns.VERSION + " INTEGER, "
            + VisibleListColumns.HASHCODE + " INTEGER "
            + ", CONSTRAINT unique_name UNIQUE (user_id,visible_id) ON CONFLICT REPLACE"
            + " );";

    // @formatter:on

    public static MMSQLiteOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    private static MMSQLiteOpenHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Pre Honeycomb.
     */
    private static MMSQLiteOpenHelper newInstancePreHoneycomb(Context context) {
        return new MMSQLiteOpenHelper(context);
    }

    private MMSQLiteOpenHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mOpenHelperCallbacks = new MMSQLiteOpenHelperCallbacks();
    }


    /*
     * Post Honeycomb.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static MMSQLiteOpenHelper newInstancePostHoneycomb(Context context) {
        return new MMSQLiteOpenHelper(context, new DefaultDatabaseErrorHandler());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private MMSQLiteOpenHelper(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new MMSQLiteOpenHelperCallbacks();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_COMMENT);
        db.execSQL(SQL_CREATE_TABLE_COMMENT_AUTHORS);
        db.execSQL(SQL_CREATE_TABLE_GENRE);
        db.execSQL(SQL_CREATE_TABLE_MEMBER);
        db.execSQL(SQL_CREATE_TABLE_MESSAGE);
        db.execSQL(SQL_CREATE_TABLE_PLAYLIST);
        db.execSQL(SQL_CREATE_TABLE_ROOM);
        db.execSQL(SQL_CREATE_TABLE_SONG);
        db.execSQL(SQL_CREATE_TABLE_SOUND_FEED);
        db.execSQL(SQL_CREATE_TABLE_USER);
        db.execSQL(SQL_CREATE_TABLE_VISIBLE_LIST);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            setForeignKeyConstraintsEnabled(db);
        }
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        } else {
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
    }
}
