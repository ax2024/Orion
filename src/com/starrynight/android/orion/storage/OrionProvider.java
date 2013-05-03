package com.starrynight.android.orion.storage;

import java.io.File;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDiskIOException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Environment;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import com.orion.android.R;
import com.starrynight.android.orion.OrionApplication;

public class OrionProvider extends ContentProvider {

	/**
	 * ParentManagement authority for content URIs
	 */
	public static final String AUTHORITY = "com.starrynight.android.orion.provider";
	public static final String DATABASE_NAME = "ORION";
	public static final int DATABASE_VERSION = 2;
	public static final String TAG = OrionApplication.getApplicationTag()
	        + "." + OrionApplication.class.getSimpleName();
	public static final String EXTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory() + "/" + ".orion";
	
	private static final UriMatcher sUriMatcher;
    private static final int MATCHER_RESET = 1;
    private static final String RESET_PATH = ".reset";

    /**
     * Message wrapper class for content provider
     */
    public static class Messages implements BaseColumns {
		/**
		 * The storage folder path
		 */
		public static final String STORAGE_FOLDER = 
			OrionProvider.getStoragePath() + "/messages";
        public static final String TABLE_NAME = "Messages";
    	public static final String PATH = "messages";
    	private static final int MATCHER      = 100;
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/" + PATH);

		/**
		 * The sender of the message
		 * <P>
		 * Type: STRING
		 * </P>
		 */
		public static final String SENDER = "SENDER";

		/**
		 * The timestamp for when the message was deposited
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String DEPOSIT_DATE = "DEPOSIT_DATE";

		/**
		 * The length of the message
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String DURATION = "DURATION";

		/**
		 * The IMAP Message ID
		 * <P>
		 * Type: STRING
		 * </P>
		 */
		public static final String MESSAGE_ID = "MESSAGE_ID";

    	/**
		 * The IMAP message UID
		 * <p>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String MESSAGE_UID = "MESSAGE_UID"; 

		/**
		 * The state of the message (read/unread)
		 * Possible states: 0 for read, 1 for unread
		 * <P>
		 * Type: INTEGER (int)
		 * </P>
		 */
		public static final String STATUS = "STATUS";

		/**
		 * The type of the message (message/missed call/infotainment)
		 * Possible values: Voice-message, X-empty-call-capture-message, X-voice-infotainment-message
		 * <P>
		 * Type: STRING
		 * </P>
		 */
		public static final String TYPE = "TYPE";
		
		/**
		 * The text of the message
		 * <p>
		 * Type: STRING
		 * </p>
		 */
		public static final String VTCTEXT = "VTCTEXT";
		
        /**
         * If v2t is requesting
         * 0 for not, 1 for yes.
         * <p>
         * Type: INTEGER (long)
         * </p>
         */
        public static final String VTCREQUESTING = "VTCREQUESTING";

		/**
		 * The subject of the message (unused)
		 * <P>
		 * Type: STRING
		 * </P>
		 */
		public static final String SUBJECT = "SUBJECT";

		/**
		 * The path to the media file of the message. This is supposed to be an URI.
		 * <P>
		 * Type: STRING
		 * </P>
		 */
		public static final String FILE = "FILE";

		/**
		 * The lifetime of the file while new (unread). 
		 * This is an absolute lifetime, not relative to deposit time
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String NEW_LIFETIME = "NEW_LIFETIME";

		/**
		 * The lifetime of the file once read 
		 * This is an absolute lifetime, not relative to deposit time
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String LIFETIME = "LIFETIME";

		/**
		 * The tag of the message (used to handle favorites/trash etc...)
		 * Possible values: null, "trash", "favorites"
		 * <P>
		 * Type: STRING 
		 * </P>
		 */
		public static final String TAG = "TAG";

		/**
		 * Is the message modified in the UI
		 * Values: 0 for CLEAN, 1 for partial (not downloaded yet), 
		 * 2 for deleted locally 
		 * <P>
		 * Type: INTEGER (int)
		 * </P>
		 */
		public static final String SYNCHRONIZATION = "SYNC";

		/**
		 * The contact ID of the sender 
		 * <P>
		 * Type: INTEGER (long)
		 * </P>
		 */
		public static final String CONTACT_ID = "CONTACT_ID";

		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = DEPOSIT_DATE + " DESC";
    }
	
	/**
	 * Greeting wrapper class for content provider
	 */
	public static final class Greetings implements BaseColumns {
        public static final String TABLE_NAME = "Greetings";
        public static final String PATH = "greetings";
        private static final int MATCHER = 102;
        /**
         * The content:// style URL for this table
         */
        public static final Uri CONTENT_URI = Uri.parse("content://"
                + AUTHORITY + "/" + PATH);
        /**
         * Full storage path to greetings
         */
        public static final String STORAGE_FOLDER = 
            OrionProvider.getStoragePath() + "/greetings";

        /**
         * Content-type for greetings
         */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.streamwide.vvmclient.greetings";


        /**
         * The name of the greeting
         * <P>
         * Type: STRING
         * </P>
         */
        public static final String NAME = "NAME";

        /**
         * The VVMG id of the greeting
         * <P>
         * Type: INTEGER (long)
         * </P>
         */
        public static final String SERVER_ID = "SERVER_ID";

        /**
         * The type of the greeting (name or message)
         * <P>
         * Type: INTEGER (int)
         * </P>
         */
        public static final String TYPE = "TYPE";     

        /**
         * The local greeting file path 
         * <P>
         * Type: STRING
         * </P>
         */
        public static final String FILE = "FILE";

        /**
         * Is the greeting active
         * Values: 0 for INACTIVE, 1 for ACTIVE (activated on the remote), 
         * 2 for PENDING (pending to be activated on the remote side) 
         * <P>
         * Type: INTEGER (bool)
         * </P>
         */
        public static final String ACTIVE = "ACTIVE";

        /**
         * The default sort order for this table
         */
        public static final String DEFAULT_SORT_ORDER = NAME;

        /**
         * Default greeting type
         */
        public static final int DEFAULT_TYPE = 0;

        /**
         * Default greeting filename
         */
        public static final String DEFAULT_NAME = "default_greeting";
	}

	private static class ManagementDatabaseHelper extends SQLiteOpenHelper {
		// Set to false to fall back to internal db
		private final static boolean EXTERNAL_DB = true;

		private SQLiteDatabase mDatabase = null;
		private boolean mIsInitializing = false;

		ManagementDatabaseHelper(final Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			if (! EXTERNAL_DB) {
				return;
			}
		}

		private static final String INTEGER = " INTEGER ";
        private static final String REAL = " REAL ";
		private static final String TEXT = " TEXT ";
		private static final String COMMA = ",";

		@Override
		public void onCreate(final SQLiteDatabase db) {

			try {
				createMessagesTable(db);
				createGreetingsTable(db);
			} catch (SQLException sqle) {
				Log.e(TAG, "unable to create Message content provider : "
						+ sqle.getMessage());
				throw sqle;
			}
		}

        private void createMessagesTable(final SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + Messages.TABLE_NAME + " ("
                + Messages._ID + INTEGER + "PRIMARY KEY,"
                + Messages.MESSAGE_ID + INTEGER + COMMA
                + Messages.MESSAGE_UID + INTEGER + COMMA
                + Messages.SENDER + TEXT + COMMA 
                + Messages.TYPE + TEXT + COMMA                
                + Messages.DEPOSIT_DATE + INTEGER + COMMA 
                + Messages.DURATION + INTEGER + COMMA 
                + Messages.FILE + TEXT + COMMA 
                + Messages.VTCTEXT + TEXT + COMMA
                + Messages.VTCREQUESTING + INTEGER + COMMA
                + Messages.SUBJECT + TEXT + COMMA 
                + Messages.CONTACT_ID + INTEGER + COMMA 
                + Messages.NEW_LIFETIME + INTEGER + COMMA 
                + Messages.LIFETIME + INTEGER + COMMA 
                + Messages.STATUS + INTEGER + COMMA 
                + Messages.TAG + TEXT + COMMA 
                + Messages.SYNCHRONIZATION + INTEGER 
                + ");");
        }
        
        private void createGreetingsTable(final SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + Greetings.TABLE_NAME + " ("
                + Greetings._ID  + INTEGER + "PRIMARY KEY,"
                + Greetings.NAME + TEXT + COMMA 
                + Greetings.SERVER_ID + TEXT + COMMA 
                + Greetings.TYPE + INTEGER + COMMA 
                + Greetings.FILE + TEXT + COMMA
                + Greetings.ACTIVE + INTEGER
                + ");");
        }

        private void clearDB(final SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + Messages.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + Greetings.TABLE_NAME);
        }
        
        @Override
        public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
                final int newVersion) {
//            if (newVersion < oldVersion) {
//                // DOWNGRADE:
//                Log.w(TAG, "Downgrading database from version " + oldVersion
//                    + "to version " + newVersion);
//                // lets drop all
//                clearDB(db);
//                // and recreate everything
//                onCreate(db);
//            } else if (oldVersion < newVersion) {
//                // UPGRADE:
//                Log.w(TAG, "Upgrading database from version " + oldVersion
//                    + "to version " + newVersion);
//                // Database version 4+: added greeting active status (on upgrade, keep messages)
//                Log.w(TAG, "Upgrading to DBv4+, re-creating greeting table");
//                // drop greetings table
//                db.execSQL("DROP TABLE IF EXISTS " + GREETINGS_TABLE_NAME);
//                // create new greetings table
//                createGreetingsTable(db);
//                
//             // Database version 5+: added media library database
//                Log.w(TAG, "Upgrading to DBv5+, creating media library table");
//                db.execSQL("DROP TABLE IF EXISTS " + MEDIALIB_TABLE_NAME);
//                createMediaLibTable(db);
//            }
        	
            // lets drop all
            clearDB(db);
            // and recreate everything
            onCreate(db);

        }
        
		synchronized void reset() {
			Log.i(TAG, "OpenHelper: reset");
			mDatabase = null;
			mIsInitializing = false;
		}
		/*
		 * (non-Javadoc)
		 * 
		 * @see android.database.sqlite.SQLiteOpenHelper#getReadableDatabase()
		 */
		@Override
		public synchronized SQLiteDatabase getReadableDatabase() {
			if (! EXTERNAL_DB) {
				return super.getReadableDatabase();
			}

			final String mount = Environment.getExternalStorageState();
			if (OrionApplication.DEBUG) {
//				Log.d(TAG, "getReadableDatabase, checking external storage: " + mount);
			}
			if (!Environment.MEDIA_MOUNTED.equals(mount)) {
				// lost our storage, reset everything
				reset();
				return null;
			}

			if (mDatabase != null && mDatabase.isOpen()) {
				return mDatabase; // The database is already open for business
			}

			if (mIsInitializing) {
				throw new IllegalStateException("getReadableDatabase called recursively");
			}

			try {
				return getWritableDatabase();
			} catch (SQLiteException e) {
				Log.e(TAG, "Couldn't open " + " for writing (will try read-only):", e);
			}

			SQLiteDatabase db = null;
			try {
				mIsInitializing = true;
				// Create external storage path if needed
				final File target = new File(OrionProvider.EXTERNAL_STORAGE_PATH);
				if (!target.exists()) {
				    target.mkdirs();
				}
				db = SQLiteDatabase.openDatabase(getDatabaseName(), null, SQLiteDatabase.OPEN_READONLY);
				if (db.getVersion() != DATABASE_VERSION) {
					Log.e(TAG, "Can't upgrade read-only database from version " + db.getVersion() + " to "
							+ DATABASE_VERSION + ": " + getDatabaseName());
					return null;
				}
				onOpen(db);
				Log.w(TAG, "Opened " + getDatabaseName() + " in read-only mode");
				mDatabase = db;
			} catch (SQLiteException sqle) {
				reset();
			} finally {
				mIsInitializing = false;
			}
			if (db != null && ! db.equals(mDatabase)) {
				db.close();
			}
			return mDatabase;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.database.sqlite.SQLiteOpenHelper#getWritableDatabase()
		 */
		@Override
		public synchronized SQLiteDatabase getWritableDatabase() {
			if (! EXTERNAL_DB) {
				return super.getWritableDatabase();
			}

			final String mount = Environment.getExternalStorageState();
			if (OrionApplication.DEBUG) {
//				Log.d(TAG, "getWritableDatabase, checking external storage: " + mount);
			}
			if (!Environment.MEDIA_MOUNTED.equals(mount)) {
				// lost our storage, reset everything
				reset();
				return null;
			}

			if (mDatabase != null && mDatabase.isOpen() && !mDatabase.isReadOnly()) {
				return mDatabase; // The database is already open for business
			}

			if (mIsInitializing) {
				throw new IllegalStateException("getWritableDatabase called recursively");
			}

			// If we have a read-only database open, someone could be using it
			// (though they shouldn't), which would cause a lock to be held on
			// the file, and our attempts to open the database read-write would
			// fail waiting for the file lock. To prevent that, we acquire the
			// lock on the read-only database, which shuts out other users.

			boolean success = false;
			SQLiteDatabase db = null;
			try {
				mIsInitializing = true;
				// Create external storage path if needed
				final File target = new File(OrionProvider.EXTERNAL_STORAGE_PATH);
				if (!target.exists()) {
                    boolean rtn = target.mkdirs();
                    if (!rtn) {
    	                Log.e(TAG, "Couldn't mkdir: " + OrionProvider.EXTERNAL_STORAGE_PATH);
                    }   
				}
				db = SQLiteDatabase.openOrCreateDatabase(OrionProvider.getStoragePath() + "/" + DATABASE_NAME +".sqlite", null);
				final int version = db.getVersion();
				if (version != DATABASE_VERSION) {
					db.beginTransaction();
					try {
						if (version == 0) {
							onCreate(db);
						} else {
							onUpgrade(db, version, DATABASE_VERSION);
						}
						db.setVersion(DATABASE_VERSION);
						db.setTransactionSuccessful();
					} finally {
						db.endTransaction();
					}
				}

				onOpen(db);
				success = true;
				
				return db;
			} finally {
				mIsInitializing = false;
				if (success) {
					if (mDatabase != null) {
						mDatabase.close();
					}
					mDatabase = db;
				} else {
					if (db != null) {
						db.close();
					}
				}
			}
		}
	}
	
	private ManagementDatabaseHelper mOpenHelper;

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
        final String tableName;
        switch (sUriMatcher.match(uri)) {
        case Messages.MATCHER:
            tableName = Messages.TABLE_NAME;
            break;
        case Greetings.MATCHER:
            tableName = Greetings.TABLE_NAME;
            break;
        default: 
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        try {
            final int count = mOpenHelper.getWritableDatabase().delete(
                    tableName, selection, selectionArgs);
            Log.i(TAG, "deleted " + count + " entries in " + tableName +" database");
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        } catch (NullPointerException npe) {
            // error, probably the external storage is not mounted
            Log.e(TAG, "NullPointerException while trying to delete entries in " + tableName +" database");
            return 0;
        } catch (SQLiteException sqlioe) {
            //we catch disk io that may happen if SD card full or faulty
            Log.e(TAG, "SQLiteException  while trying to delete entry in " + tableName +" database");
            // arm flag indicating that the application cannot write the db
            return 0;
        }
	}

	public static String getStoragePath() {
		// TODO Select Internal or external storage 
//		if("internal".equals(
//				OrionApplication.getServiceConfiguration().getParameter("storage_type"))) {
//			// Use internal storage
//			return OrionApplication.getInternalPath();
//		} else {
//			return EXTERNAL_STORAGE_PATH;
//		}
	    return EXTERNAL_STORAGE_PATH;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values) {
		switch (sUriMatcher.match(uri)){ // NOPMD
		case Messages.MATCHER:
			return insertInMessages(values);
        case Greetings.MATCHER:
            return insertInGreetings(values);
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		} 
	}

	private Uri insertInMessages(final ContentValues initialValues){
		ContentValues values;
		if (null == initialValues  || null == mOpenHelper ) {
			return null;
		} else {
			values = new ContentValues(initialValues);
		}

		final Long now = Long.valueOf(System.currentTimeMillis());
		// Make sure that the fields are set      
		if (   values.containsKey(Messages.SENDER)
		    // and not empty!..
            && !TextUtils.isEmpty((String)values.get(Messages.SENDER))) {
			if (values.containsKey(Messages.CONTACT_ID)) {
				values.put(Messages.CONTACT_ID, -1);
			} else {
//				values.put(Messages.CONTACT_ID, findContactFromCallNumber(
//						(String) values.get(Messages.SENDER)));
			}
		} else {
			values.put(Messages.SENDER, getContext().getString(R.string.operator_anonymous_sender));
			values.put(Messages.CONTACT_ID, -1);
		}
		if (!values.containsKey(Messages.TYPE)) {
			values.put(Messages.TYPE, "Voice-message");
		}
		if (!values.containsKey(Messages.DURATION)) {
			values.put(Messages.DURATION, 0);
		}
		if (!values.containsKey(Messages.DEPOSIT_DATE)) {
			values.put(Messages.DEPOSIT_DATE, now);
		}
//		if (!values.containsKey(Messages.STATUS)) {
//			values.put(Messages.STATUS, Message.Status.UNREAD.value());
//		}
	        //set lifetime to very far in the future, to prevent the soon deleted icon
        	// from appearing and ensure the message is properly shown in the inbox
	        if (!values.containsKey(Messages.LIFETIME)) {
        	    values.put(Messages.LIFETIME, Long.MAX_VALUE);
	        }
        	//set lifetime to very far in the future, to prevent the soon deleted icon
	        // from appearing and ensure the message is properly shown in the inbox
        	if (!values.containsKey(Messages.NEW_LIFETIME)) {
	            values.put(Messages.NEW_LIFETIME, Long.MAX_VALUE);
        	}
        	if (!values.containsKey(Messages.VTCTEXT)) {
        		values.put(Messages.VTCTEXT, (String)null);
        	}
        	if (!values.containsKey(Messages.VTCREQUESTING)) {
                values.put(Messages.VTCREQUESTING, 0);
            }
		try {
			final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
			final long rowId = db.insert(Messages.TABLE_NAME, "message", values);
			if (rowId > 0) {
				final Uri insertUri = ContentUris.withAppendedId( Messages.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(insertUri, null);
				return insertUri;
			}
		} catch (NullPointerException npe) {
			//we catch npe that may happen if the sd card is not present
			Log.e(TAG, "NullPointerException while trying to insert entry in " + Messages.TABLE_NAME +" database");
        } catch (SQLiteException sqlioe) {
            //we catch disk io that may happen if SD card full or faulty
            Log.e(TAG, "SQLiteException  while trying to insert entry in " + Messages.TABLE_NAME +" database");
            // arm flag indicating that the application cannot write the db
//            OrionApplication.readOnlyMode(true);
        }
		return null;
	}

//	private final long findContactFromCallNumber(final String phoneNumber) {
//		final long id = -1;
//		if(phoneNumber == null){
//			return id;
//		} 
//		final ContactWrapper c = ContactCache.getInstance().getContact(phoneNumber);
//
//
//		return (c==null)? id :c.getId();
//	}
	
	private Uri insertInGreetings(final ContentValues initialValues){
		if (null == initialValues || null == mOpenHelper) {
			Log.e(TAG,"Cannot add empty greeting row" );
			return null;
		}

		final ContentValues values = initialValues;

        // Make sure that the fields are all set
        if (!values.containsKey(Greetings.NAME)) {
            values.put(Greetings.NAME, Greetings.DEFAULT_NAME); 
        }
        if (!values.containsKey(Greetings.FILE)) {
            Log.e(TAG,"Cannot add configuration value with empty key" );
            return null; 
        }
        if (!values.containsKey(Greetings.TYPE)) {
            values.put(Greetings.TYPE, Greetings.DEFAULT_TYPE);
        }

		try {
			final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
			final long rowId = 
				db.insert(Greetings.TABLE_NAME, "greetings", values);
			if (rowId > 0) {
				final Uri insertUri = 
					ContentUris.withAppendedId(Greetings.CONTENT_URI, rowId);
				getContext().getContentResolver().notifyChange(insertUri, null);
				return insertUri;
			}
		} catch (NullPointerException npe) {
			//we catch npe that may happen if the sd card is not present
			Log.e(TAG, "NullPointerException while trying to insert entry in " + Greetings.TABLE_NAME +" database");	
        } catch (SQLiteException sqlioe) {
            //we catch disk io that may happen if SD card full or faulty
            Log.e(TAG, "SQLiteException  while trying to insert entry in " + Greetings.TABLE_NAME +" database");
            // arm flag indicating that the application cannot write the db
//            VVMApplication.readOnlyMode(true);
        }
		return null;
	}


	@Override
	public boolean onCreate() {
		try {
			mOpenHelper = new ManagementDatabaseHelper(getContext());
		} catch (SQLiteDiskIOException e) {
			return false;
		}
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
        final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        final String tableName;
        String orderBy;       
        switch (sUriMatcher.match(uri)) {
        case Messages.MATCHER:
            tableName = Messages.TABLE_NAME;
            orderBy = Messages.DEFAULT_SORT_ORDER;
            break;
        case Greetings.MATCHER:
            tableName = Greetings.TABLE_NAME;
            orderBy = Greetings.DEFAULT_SORT_ORDER;
            break;
        case MATCHER_RESET:
            // special uri used to invalidate and reset the provider when
            // the external storage gets unavailable
            if (null != mOpenHelper) {
                mOpenHelper.reset();
            }
            return null;
        default:
            return null;
        }
        qb.setTables(tableName);

        if (!TextUtils.isEmpty(sortOrder)) {
            orderBy = sortOrder;
        }
        // Get the database and run the query
        final Cursor queryResult;
        try {
            final SQLiteDatabase db = this.mOpenHelper.getReadableDatabase();
            queryResult = qb.query(db, projection, selection,
                    selectionArgs, null, null, orderBy);
            // Tell the cursor what uri to watch, so it knows when its source data
            // changes
            queryResult.setNotificationUri(getContext().getContentResolver(), uri);
            return queryResult;
        } catch (NullPointerException npe) {
            //this may happen if SD card is mounted
            Log.w(TAG, "NullPointerException while trying to query " + tableName +" database");
            return null;
        } catch (SQLiteException sqlioe) {
            //we catch disk io that may happen if SD card full or faulty
            Log.e(TAG, "SQLiteException  while trying to query " + tableName +" database");
            // arm flag indicating that the application cannot write the db
//            OrionApplication.readOnlyMode(true);
            return null;
        }
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
        final String tableName;
        switch (sUriMatcher.match(uri)) {
        case Messages.MATCHER:
            tableName = Messages.TABLE_NAME;
            break;
        case Greetings.MATCHER:
            tableName = Greetings.TABLE_NAME;
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        try {
            final SQLiteDatabase db = this.mOpenHelper.getWritableDatabase();
            final int count = db.update(
                    tableName, values, selection, selectionArgs);
            getContext().getContentResolver().notifyChange(uri, null);
            // We were able to modify the database, so we may be in read/write
            // mode
//            tryRestoreWriteMode();
            return count;
        } catch (NullPointerException npe) {
            Log.w(TAG, "NullPointerException while trying to update " + tableName +" database");
            return 0;
        } catch (SQLiteException sqlioe) {
            //we catch disk io that may happen if SD card full or faulty
            Log.e(TAG, "SQLiteException  while trying to update " + tableName +" database" + sqlioe.getMessage());
            // arm flag indicating that the application cannot write the db
            return 0;
        }
	}

	// initialize URI matcher
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(OrionProvider.AUTHORITY, Messages.PATH, Messages.MATCHER);
        sUriMatcher.addURI(OrionProvider.AUTHORITY, Greetings.PATH, Greetings.MATCHER);
		sUriMatcher.addURI(OrionProvider.AUTHORITY, RESET_PATH, MATCHER_RESET);
	}
}
