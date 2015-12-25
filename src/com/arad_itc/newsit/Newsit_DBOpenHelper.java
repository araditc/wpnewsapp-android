package com.arad_itc.newsit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Newsit_DBOpenHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 3;
	private static String DB_PATH = "";
	private static String DB_NAME = "arad.db";
	private SQLiteDatabase mDataBase;
	private final Context mContext;

	public Newsit_DBOpenHelper(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		if (android.os.Build.VERSION.SDK_INT >= 17) {
			DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
		} else {
			DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		}
		this.mContext = context;
	}

	public void createDataBase() throws IOException {

		boolean mDataBaseExist = checkDataBase();
		if (!mDataBaseExist) {
			this.getReadableDatabase();
			this.close();
			try {
				copyDataBase();
			} catch (IOException mIOException) {
				throw new Error("ErrorCopyingDataBase");
			}
		} else {
		}
	}


	private boolean checkDataBase() {
		File dbFile = new File(DB_PATH + DB_NAME);
		return dbFile.exists();
	}

	private void copyDataBase() throws IOException {
		InputStream mInput = mContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[1024];
		int mLength;
		while ((mLength = mInput.read(mBuffer)) > 0) {
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}

	public SQLiteDatabase openDataBase() throws SQLException {
		String mPath = DB_PATH + DB_NAME;
		mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
		return mDataBase;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int ov, int nv) {

	}

	@Override
	public synchronized void close() {
		if (mDataBase != null)
			mDataBase.close();
		super.close();
	}
}
