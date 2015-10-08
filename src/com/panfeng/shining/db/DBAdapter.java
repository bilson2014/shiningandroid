package com.panfeng.shining.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库适配器
 * 
 * @author Administrator
 * 
 */

public class DBAdapter {
	public static final String KEY_ID = "_id";

	public static final String KEY_audio_file_name = "audio_file_name";
	public static final String KEY_audio_name = "audio_name";
	public static final String KEY_audio_author = "audio_author";
	public static final String KEY_lenght = "audio_lenght";

	public static final String KEY_music_file_name = "music_file_name";
	public static final String KEY_music_name = "music_name";
	public static final String KEY_author_name = "author_name";
	
	//我的闪铃
	public static final String KEY_my_video_name = "hasMyVideo";
	


	public String str;
	private static final String TAG = "DBAdapter";
	private static final String KEY_DESC = "audio_base_list order by _id desc";
	private static final String KEY_DESC_MUSIC = "music_list order by _id desc";
	private static final String DATABASE_NAME = "shiningDB";
	private static final String DATABASE_TABLE = "audio_base_list";
	private static final String DATABASE_MUSIC = "music_list";
	private static final String DATABASE_MYVIDEO = "myVideo_list";
	private static final int DATABASE_VERSION = 6;
	private static final String DATABASE_CREATE = "create table audio_base_list (_id integer primary key autoincrement, "
			+ "audio_file_name text not null,audio_name text not null,"
			+ "audio_author integer not null, audio_lenght integer not null);";

	private static final String DATABASE_CREATE_MUSIC = "create table music_list (_id integer primary key autoincrement, "
			+ "music_file_name text not null,music_name text not null,author_name);";
	
	private static final String DATABASE_CREATE_MyVideo = "create table myVideo_list (_id integer primary key autoincrement, "
			+ "hasMyVideo integer not null);";

	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		// 建库
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
			db.execSQL(DATABASE_CREATE_MUSIC);
			db.execSQL(DATABASE_CREATE_MyVideo);
		}

		// 更新
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS audio_base_list");
			db.execSQL("DROP TABLE IF EXISTS music_list");
			db.execSQL("DROP TABLE IF EXISTS myVideo_list");
			onCreate(db);
		}
	}

	// ---打开数据库---

	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---关闭数据库---

	public void close() {
		DBHelper.close();
	}

	// ---向数据库中插入一个标题---

	public long insertDb(String audio_file_name, String audio_name,
			String audio_author, long lenght) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_audio_file_name, audio_file_name);
		initialValues.put(KEY_audio_name, audio_name);
		initialValues.put(KEY_audio_author, audio_author);
		initialValues.put(KEY_lenght, lenght);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}



	// ---删除一个指定标题---

	public boolean deleteDb(long rowId) {
		return db.delete(DATABASE_TABLE, KEY_ID + "=" + rowId, null) > 0;
	}

	// ---检索所有标题---by id

	public Cursor getAllDb() {
		return db.query(KEY_DESC, new String[] { KEY_ID, KEY_audio_file_name,
				KEY_audio_name, KEY_audio_author, KEY_lenght, }, null, null,
				null, null, null);
	}
	
	//检测我的闪铃
	public Cursor getMyVideo() {
		return db.query(DATABASE_MYVIDEO, new String[] { KEY_ID, KEY_my_video_name }, null, null,
				null, null, null);
	}
	//添加我的闪铃
	public long insertMyVideo(int video_name) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_my_video_name, video_name);
		return db.insert(DATABASE_MYVIDEO, null, initialValues);
	}
	//修改我的闪铃
	public boolean updateMyVideo(long rowId,int video_name) {
		ContentValues args = new ContentValues();

		args.put(KEY_my_video_name, video_name);


		return db.update(DATABASE_MYVIDEO, args, KEY_ID + "=" + rowId, null) > 0;
	}
	
	


	// 全标题根据内容
	public Cursor getAllDbFy(String str) {
		return db.query(str, new String[] { KEY_ID, KEY_audio_file_name,
				KEY_audio_name, KEY_audio_author, KEY_lenght, }, null, null,
				null, null, null);
	}

	// 素偶个数
	public Cursor getAllCount() {
		return db.rawQuery("select count(*) from "
				+ "audio_base_list group by people", null);
	}

	public Cursor getAllPeople(String people) {
		return db.rawQuery(
				"select * from  audio_base_list where audio_file_name ="
						+ people, null);
	}
	

	
	

	// 素偶个数
	public Cursor getAllGroup() {
		return db.rawQuery(
				"select * from message group by people order by date desc",
				null);
	}

	// 自定义语句
	public Cursor findAllCount(String strs) {
		return db.rawQuery(strs, null);
	}

	// ---检索一个指定标题---

	public Cursor getdb(long rowId) throws SQLException {
		Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] { KEY_ID,
				KEY_audio_file_name, KEY_audio_name, KEY_audio_author,
				KEY_lenght }, KEY_ID + "=" + rowId, null, null, null, null,
				null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	// ---更新一个标题---

	public boolean updatedb(long rowId, String audio_file_nam,
			String audio_name, String audio_author, long lenght) {
		ContentValues args = new ContentValues();

		args.put(KEY_audio_file_name, audio_file_nam);
		args.put(KEY_audio_name, audio_name);
		args.put(KEY_audio_author, audio_author);
		args.put(KEY_lenght, lenght);

		return db.update(DATABASE_TABLE, args, KEY_ID + "=" + rowId, null) > 0;
	}

	// ---更新閱讀狀態---

	public boolean updateRead(long rowId, Integer read) {
		ContentValues args = new ContentValues();
		args.put(KEY_audio_author, read);
		return db.update(DATABASE_TABLE, args, KEY_ID + "=" + rowId, null) > 0;
	}

	// ---接收更新閱讀狀態---

	String s = "\"";
	String d = "\"";

	public boolean updateReadByTime(String body, Integer read) {
		ContentValues args = new ContentValues();
		args.put(KEY_audio_author, read);
		return db.update(DATABASE_TABLE, args, KEY_audio_author + "=" + s
				+ body + d, null) > 0;
	}
	
	
	
	//音乐
	//TODO 插入音乐
	public long insertMusic(String music_file_name, String music_name,String author_name) {
		ContentValues initialValues = new ContentValues();

		initialValues.put(KEY_music_file_name, music_file_name);
		initialValues.put(KEY_music_name, music_name);
		initialValues.put(KEY_author_name, author_name);

		return db.insert(DATABASE_MUSIC, null, initialValues);
	}
	
	
	// ---检索所有音乐---by id

	public Cursor getAllMusic() {
		return db.query(KEY_DESC_MUSIC, new String[] { KEY_ID, KEY_music_file_name,KEY_music_name,KEY_author_name}, null, null,null, null, null);
	}
	
	String fen = "\"";
	
	
	//检索音乐
	public Cursor getMusic(String name) {
		return db.rawQuery(
				"select * from  music_list where music_file_name ="+fen+name+fen, null);
	}
	
	
	

}
