package com.myapp.io.provider;

import java.util.Arrays;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.myapp.app.BuildConfig;
import com.myapp.io.provider.base.BaseContentProvider;
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

public class myappProvider extends BaseContentProvider {
    private static final String TAG = myappProvider.class.getSimpleName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String TYPE_CURSOR_ITEM = "vnd.android.cursor.item/";
    private static final String TYPE_CURSOR_DIR = "vnd.android.cursor.dir/";

    public static final String AUTHORITY = "com.myapp";
    public static final String CONTENT_URI_BASE = "content://" + AUTHORITY;

    private static final int URI_TYPE_COMMENT = 0;
    private static final int URI_TYPE_COMMENT_ID = 1;

    private static final int URI_TYPE_COMMENT_AUTHORS = 2;
    private static final int URI_TYPE_COMMENT_AUTHORS_ID = 3;

    private static final int URI_TYPE_GENRE = 4;
    private static final int URI_TYPE_GENRE_ID = 5;

    private static final int URI_TYPE_MEMBER = 6;
    private static final int URI_TYPE_MEMBER_ID = 7;

    private static final int URI_TYPE_MESSAGE = 8;
    private static final int URI_TYPE_MESSAGE_ID = 9;

    private static final int URI_TYPE_PLAYLIST = 10;
    private static final int URI_TYPE_PLAYLIST_ID = 11;

    private static final int URI_TYPE_ROOM = 12;
    private static final int URI_TYPE_ROOM_ID = 13;

    private static final int URI_TYPE_SONG = 14;
    private static final int URI_TYPE_SONG_ID = 15;

    private static final int URI_TYPE_SOUND_FEED = 16;
    private static final int URI_TYPE_SOUND_FEED_ID = 17;

    private static final int URI_TYPE_USER = 18;
    private static final int URI_TYPE_USER_ID = 19;

    private static final int URI_TYPE_VISIBLE_LIST = 20;
    private static final int URI_TYPE_VISIBLE_LIST_ID = 21;



    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, CommentColumns.TABLE_NAME, URI_TYPE_COMMENT);
        URI_MATCHER.addURI(AUTHORITY, CommentColumns.TABLE_NAME + "/#", URI_TYPE_COMMENT_ID);
        URI_MATCHER.addURI(AUTHORITY, CommentAuthorsColumns.TABLE_NAME, URI_TYPE_COMMENT_AUTHORS);
        URI_MATCHER.addURI(AUTHORITY, CommentAuthorsColumns.TABLE_NAME + "/#", URI_TYPE_COMMENT_AUTHORS_ID);
        URI_MATCHER.addURI(AUTHORITY, GenreColumns.TABLE_NAME, URI_TYPE_GENRE);
        URI_MATCHER.addURI(AUTHORITY, GenreColumns.TABLE_NAME + "/#", URI_TYPE_GENRE_ID);
        URI_MATCHER.addURI(AUTHORITY, MemberColumns.TABLE_NAME, URI_TYPE_MEMBER);
        URI_MATCHER.addURI(AUTHORITY, MemberColumns.TABLE_NAME + "/#", URI_TYPE_MEMBER_ID);
        URI_MATCHER.addURI(AUTHORITY, MessageColumns.TABLE_NAME, URI_TYPE_MESSAGE);
        URI_MATCHER.addURI(AUTHORITY, MessageColumns.TABLE_NAME + "/#", URI_TYPE_MESSAGE_ID);
        URI_MATCHER.addURI(AUTHORITY, PlaylistColumns.TABLE_NAME, URI_TYPE_PLAYLIST);
        URI_MATCHER.addURI(AUTHORITY, PlaylistColumns.TABLE_NAME + "/#", URI_TYPE_PLAYLIST_ID);
        URI_MATCHER.addURI(AUTHORITY, RoomColumns.TABLE_NAME, URI_TYPE_ROOM);
        URI_MATCHER.addURI(AUTHORITY, RoomColumns.TABLE_NAME + "/#", URI_TYPE_ROOM_ID);
        URI_MATCHER.addURI(AUTHORITY, SongColumns.TABLE_NAME, URI_TYPE_SONG);
        URI_MATCHER.addURI(AUTHORITY, SongColumns.TABLE_NAME + "/#", URI_TYPE_SONG_ID);
        URI_MATCHER.addURI(AUTHORITY, SoundFeedColumns.TABLE_NAME, URI_TYPE_SOUND_FEED);
        URI_MATCHER.addURI(AUTHORITY, SoundFeedColumns.TABLE_NAME + "/#", URI_TYPE_SOUND_FEED_ID);
        URI_MATCHER.addURI(AUTHORITY, UserColumns.TABLE_NAME, URI_TYPE_USER);
        URI_MATCHER.addURI(AUTHORITY, UserColumns.TABLE_NAME + "/#", URI_TYPE_USER_ID);
        URI_MATCHER.addURI(AUTHORITY, VisibleListColumns.TABLE_NAME, URI_TYPE_VISIBLE_LIST);
        URI_MATCHER.addURI(AUTHORITY, VisibleListColumns.TABLE_NAME + "/#", URI_TYPE_VISIBLE_LIST_ID);
    }

    @Override
    protected SQLiteOpenHelper createSqLiteOpenHelper() {
        return MMSQLiteOpenHelper.getInstance(getContext());
    }

    @Override
    protected boolean hasDebug() {
        return DEBUG;
    }

    @Override
    public String getType(Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case URI_TYPE_COMMENT:
                return TYPE_CURSOR_DIR + CommentColumns.TABLE_NAME;
            case URI_TYPE_COMMENT_ID:
                return TYPE_CURSOR_ITEM + CommentColumns.TABLE_NAME;

            case URI_TYPE_COMMENT_AUTHORS:
                return TYPE_CURSOR_DIR + CommentAuthorsColumns.TABLE_NAME;
            case URI_TYPE_COMMENT_AUTHORS_ID:
                return TYPE_CURSOR_ITEM + CommentAuthorsColumns.TABLE_NAME;

            case URI_TYPE_GENRE:
                return TYPE_CURSOR_DIR + GenreColumns.TABLE_NAME;
            case URI_TYPE_GENRE_ID:
                return TYPE_CURSOR_ITEM + GenreColumns.TABLE_NAME;

            case URI_TYPE_MEMBER:
                return TYPE_CURSOR_DIR + MemberColumns.TABLE_NAME;
            case URI_TYPE_MEMBER_ID:
                return TYPE_CURSOR_ITEM + MemberColumns.TABLE_NAME;

            case URI_TYPE_MESSAGE:
                return TYPE_CURSOR_DIR + MessageColumns.TABLE_NAME;
            case URI_TYPE_MESSAGE_ID:
                return TYPE_CURSOR_ITEM + MessageColumns.TABLE_NAME;

            case URI_TYPE_PLAYLIST:
                return TYPE_CURSOR_DIR + PlaylistColumns.TABLE_NAME;
            case URI_TYPE_PLAYLIST_ID:
                return TYPE_CURSOR_ITEM + PlaylistColumns.TABLE_NAME;

            case URI_TYPE_ROOM:
                return TYPE_CURSOR_DIR + RoomColumns.TABLE_NAME;
            case URI_TYPE_ROOM_ID:
                return TYPE_CURSOR_ITEM + RoomColumns.TABLE_NAME;

            case URI_TYPE_SONG:
                return TYPE_CURSOR_DIR + SongColumns.TABLE_NAME;
            case URI_TYPE_SONG_ID:
                return TYPE_CURSOR_ITEM + SongColumns.TABLE_NAME;

            case URI_TYPE_SOUND_FEED:
                return TYPE_CURSOR_DIR + SoundFeedColumns.TABLE_NAME;
            case URI_TYPE_SOUND_FEED_ID:
                return TYPE_CURSOR_ITEM + SoundFeedColumns.TABLE_NAME;

            case URI_TYPE_USER:
                return TYPE_CURSOR_DIR + UserColumns.TABLE_NAME;
            case URI_TYPE_USER_ID:
                return TYPE_CURSOR_ITEM + UserColumns.TABLE_NAME;

            case URI_TYPE_VISIBLE_LIST:
                return TYPE_CURSOR_DIR + VisibleListColumns.TABLE_NAME;
            case URI_TYPE_VISIBLE_LIST_ID:
                return TYPE_CURSOR_ITEM + VisibleListColumns.TABLE_NAME;

        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (DEBUG) Log.d(TAG, "insert uri=" + uri + " values=" + values);
        return super.insert(uri, values);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (DEBUG) Log.d(TAG, "bulkInsert uri=" + uri + " values.length=" + values.length);
        return super.bulkInsert(uri, values);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (DEBUG) Log.d(TAG, "update uri=" + uri + " values=" + values + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.update(uri, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (DEBUG) Log.d(TAG, "delete uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.delete(uri, selection, selectionArgs);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (DEBUG)
            Log.d(TAG, "query uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs) + " sortOrder=" + sortOrder
                    + " groupBy=" + uri.getQueryParameter(QUERY_GROUP_BY) + " having=" + uri.getQueryParameter(QUERY_HAVING) + " limit=" + uri.getQueryParameter(QUERY_LIMIT));
        return super.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    protected QueryParams getQueryParams(Uri uri, String selection, String[] projection) {
        QueryParams res = new QueryParams();
        String id = null;
        int matchedId = URI_MATCHER.match(uri);
        switch (matchedId) {
            case URI_TYPE_COMMENT:
            case URI_TYPE_COMMENT_ID:
                res.table = CommentColumns.TABLE_NAME;
                res.idColumn = CommentColumns._ID;
                res.tablesWithJoins = CommentColumns.TABLE_NAME;
                res.orderBy = CommentColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_COMMENT_AUTHORS:
            case URI_TYPE_COMMENT_AUTHORS_ID:
                res.table = CommentAuthorsColumns.TABLE_NAME;
                res.idColumn = CommentAuthorsColumns._ID;
                res.tablesWithJoins = CommentAuthorsColumns.TABLE_NAME;
                res.orderBy = CommentAuthorsColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_GENRE:
            case URI_TYPE_GENRE_ID:
                res.table = GenreColumns.TABLE_NAME;
                res.idColumn = GenreColumns._ID;
                res.tablesWithJoins = GenreColumns.TABLE_NAME;
                res.orderBy = GenreColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_MEMBER:
            case URI_TYPE_MEMBER_ID:
                res.table = MemberColumns.TABLE_NAME;
                res.idColumn = MemberColumns._ID;
                res.tablesWithJoins = MemberColumns.TABLE_NAME;
                res.orderBy = MemberColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_MESSAGE:
            case URI_TYPE_MESSAGE_ID:
                res.table = MessageColumns.TABLE_NAME;
                res.idColumn = MessageColumns._ID;
                res.tablesWithJoins = MessageColumns.TABLE_NAME;
                res.orderBy = MessageColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_PLAYLIST:
            case URI_TYPE_PLAYLIST_ID:
                res.table = PlaylistColumns.TABLE_NAME;
                res.idColumn = PlaylistColumns._ID;
                res.tablesWithJoins = PlaylistColumns.TABLE_NAME;
                res.orderBy = PlaylistColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_ROOM:
            case URI_TYPE_ROOM_ID:
                res.table = RoomColumns.TABLE_NAME;
                res.idColumn = RoomColumns._ID;
                res.tablesWithJoins = RoomColumns.TABLE_NAME;
                res.orderBy = RoomColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_SONG:
            case URI_TYPE_SONG_ID:
                res.table = SongColumns.TABLE_NAME;
                res.idColumn = SongColumns._ID;
                res.tablesWithJoins = SongColumns.TABLE_NAME;
                res.orderBy = SongColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_SOUND_FEED:
            case URI_TYPE_SOUND_FEED_ID:
                res.table = SoundFeedColumns.TABLE_NAME;
                res.idColumn = SoundFeedColumns._ID;
                res.tablesWithJoins = SoundFeedColumns.TABLE_NAME;
                res.orderBy = SoundFeedColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_USER:
            case URI_TYPE_USER_ID:
                res.table = UserColumns.TABLE_NAME;
                res.idColumn = UserColumns._ID;
                res.tablesWithJoins = UserColumns.TABLE_NAME;
                res.orderBy = UserColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_VISIBLE_LIST:
            case URI_TYPE_VISIBLE_LIST_ID:
                res.table = VisibleListColumns.TABLE_NAME;
                res.idColumn = VisibleListColumns._ID;
                res.tablesWithJoins = VisibleListColumns.TABLE_NAME;
                res.orderBy = VisibleListColumns.DEFAULT_ORDER;
                break;

            default:
                throw new IllegalArgumentException("The uri '" + uri + "' is not supported by this ContentProvider");
        }

        switch (matchedId) {
            case URI_TYPE_COMMENT_ID:
            case URI_TYPE_COMMENT_AUTHORS_ID:
            case URI_TYPE_GENRE_ID:
            case URI_TYPE_MEMBER_ID:
            case URI_TYPE_MESSAGE_ID:
            case URI_TYPE_PLAYLIST_ID:
            case URI_TYPE_ROOM_ID:
            case URI_TYPE_SONG_ID:
            case URI_TYPE_SOUND_FEED_ID:
            case URI_TYPE_USER_ID:
            case URI_TYPE_VISIBLE_LIST_ID:
                id = uri.getLastPathSegment();
        }
        if (id != null) {
            if (selection != null) {
                res.selection = res.table + "." + res.idColumn + "=" + id + " and (" + selection + ")";
            } else {
                res.selection = res.table + "." + res.idColumn + "=" + id;
            }
        } else {
            res.selection = selection;
        }
        return res;
    }
}
