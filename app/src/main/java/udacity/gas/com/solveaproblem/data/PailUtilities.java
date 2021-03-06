package udacity.gas.com.solveaproblem.data;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.text.format.Time;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import udacity.gas.com.solveaproblem.R;

/**
 * Created by Fagbohungbe on 02/03/2015.
 */
public class PailUtilities {


	public static void validateCurentRecord(String tag_name, Cursor valueCursor, ContentValues expectedValues) {
		Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
		for (Map.Entry<String, Object> entry : valueSet) {
			String columnName = entry.getKey();
			int idx = valueCursor.getColumnIndex(columnName);
			if (idx == -1) {
				Log.e(tag_name, "Column '" + columnName + "' found.");
			} else {
				Log.e(tag_name, "Column '" + columnName + "' not found.");
			}
			String expectedValue = entry.getValue().toString();
			if (valueCursor.getString(idx).equals(expectedValue)) {
				Log.e(tag_name, "Value '" + entry.getValue().toString() + "' did matched the expected value '" + expectedValue + "'");
			} else {
				Log.e(tag_name, "Value '" + entry.getValue().toString() + "' did not match the expected value '" + expectedValue + "'");
			}
		}
	}

	public static boolean isTablet(Context context) {
		boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
		boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
		return (xlarge || large);
	}

	//Read from sharedPreference
	public static void saveToPreference(Context context, String PREF_FILE_NAME, String preferenceName, String preferenceValue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(preferenceName, preferenceValue);
		editor.apply();
	}

	//Write to shared preference
	public static String readFromPreference(Context context, String PREF_FILE_NAME, String prefName, String defaultvalue) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getString(prefName, defaultvalue);
	}

	public static void openLink(Activity activity, String url) {
		if (url.length() > 1 && Patterns.WEB_URL.matcher(url).matches()) {
			try {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				if (browserIntent.resolveActivity(activity.getPackageManager()) != null) {
					activity.startActivity(browserIntent);
				} else {
					Toast.makeText(activity, "Either the url you provided is invalid or you do not have any app for displaying websites", Toast.LENGTH_LONG).show();
				}
			} catch (ActivityNotFoundException e) {
				Toast.makeText(activity, "Invalid url specified, cannot open browser", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(activity, "Invalid url specified, cannot open browser", Toast.LENGTH_LONG).show();
		}
	}

	public static String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit) return bytes + "B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "1");
		return String.format("%.1f%sB", bytes / Math.pow(unit, exp), pre);
	}

	public static Bitmap setPic(ContentResolver cr, Uri mCurrentPhotoPath, ImageView mImageView) throws FileNotFoundException {
		InputStream in = cr.openInputStream(mCurrentPhotoPath);
		// Get the dimensions of the View
		int targetW = mImageView.getMeasuredWidth();
		int targetH = mImageView.getMeasuredHeight();
//
		// Get the dimensions of the bitmap
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
//
//		Determine how much to scale down the image
		int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
//
		// Decode the image file into a Bitmap sized to fill the View
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;
//
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath.toString());
//		mImageView.setImageBitmap(bitmap);
		return bitmap;
	}

	public static Bitmap getBitmapFromUri(Activity activity, Uri uri) throws IOException {
		ParcelFileDescriptor parcelFileDescriptor =
				activity.getContentResolver().openFileDescriptor(uri, "r");
		FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
		Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
		parcelFileDescriptor.close();
		return image;
	}

	public static boolean hideKeyBoardFromScreen(Activity activity, View view) {
		InputMethodManager IMM = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		return IMM.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	public static long normalizeDate(long date) {
		Time time = new Time();
		time.setToNow();
		int julianDay = Time.getJulianDay(date, time.gmtoff);
		return time.setJulianDay(julianDay);
	}

	public static String getReadableDateString(long time) {
		// Because the API returns a unix timestamp (measured in seconds),
		// it must be converted to milliseconds in order to be converted to valid date.
		Date date = new Date(time * 1000);
		SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
		return format.format(date).toString();
	}

	public static void shareText(Activity activity, String shareTitle, String shareText) {
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(Intent.EXTRA_TEXT, shareText);
		if (sharingIntent.resolveActivity(activity.getPackageManager()) != null) {
			activity.startActivity(Intent.createChooser(sharingIntent, shareTitle));
		} else {
			Toast.makeText(activity, "Cannot share content", Toast.LENGTH_LONG).show();
		}
	}

	public static Intent getShareTextIntent(String shareText) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		//Prevent the activity we're sharing to from being placed on the activity stack
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
				.setType("text/plain")
				.putExtra(Intent.EXTRA_TEXT, shareText);
		return shareIntent;
	}

	public static ContentValues normalizeData(ContentValues values) {
		//normalize the data value
		if (values.containsKey(PailContract.EntryBaseColumns.COLUMN_DATE)) {
			long dateValue = values.getAsLong(PailContract.ProblemEntry.COLUMN_DATE);
			values.put(PailContract.EntryBaseColumns.COLUMN_DATE, PailUtilities.normalizeDate(dateValue));
		} else {
			values.put(PailContract.EntryBaseColumns.COLUMN_DATE, new Date().getTime());
		}
		if (values.containsKey(PailContract.EntryBaseColumns.COLUMN_DATE_MODIFIED)) {
			long dateValue = values.getAsLong(PailContract.ProblemEntry.COLUMN_DATE_MODIFIED);
			values.put(PailContract.EntryBaseColumns.COLUMN_DATE_MODIFIED, PailUtilities.normalizeDate(dateValue));
		} else {
			values.put(PailContract.EntryBaseColumns.COLUMN_DATE_MODIFIED, new Date().getTime());
		}

		return values;
	}

	public static long currentDate() {
		return new Date().getTime();
	}

	public static int switchPrivacy(Activity activity, int initialPrivacy, ImageButton view, Uri uri, String column) {
		//device_access_not_secure|device_access_secure
		if (initialPrivacy == PailContract.VAL_PRIVACY_PRIVATE) {
			view.setImageDrawable(activity.getResources().getDrawable(R.drawable.device_access_not_secure));
			initialPrivacy = PailContract.VAL_PRIVACY_PUBLIC;
		} else {
			view.setImageDrawable(activity.getResources().getDrawable(R.drawable.device_access_secure));
			initialPrivacy = PailContract.VAL_PRIVACY_PRIVATE;
		}
		ContentValues cn = new ContentValues();
		cn.put(column, Integer.toString(initialPrivacy));
		activity.getContentResolver().update(uri, cn, null, null);
		return initialPrivacy;
	}

	public static int switchPrivacy(Activity activity, int initialPrivacy, ActionMenuItemView view, Uri uri, String column) {
		//device_access_not_secure|device_access_secure
		if (initialPrivacy == PailContract.VAL_PRIVACY_PRIVATE) {
			view.setIcon(activity.getResources().getDrawable(R.drawable.device_access_not_secure));
			initialPrivacy = PailContract.VAL_PRIVACY_PUBLIC;
		} else {
			view.setIcon(activity.getResources().getDrawable(R.drawable.device_access_secure));
			initialPrivacy = PailContract.VAL_PRIVACY_PRIVATE;
		}
		ContentValues cn = new ContentValues();
		cn.put(column, Integer.toString(initialPrivacy));
		activity.getContentResolver().update(uri, cn, null, null);
		return initialPrivacy;
	}

	public static int switchPrivacy(Activity activity, int initialPrivacy, MenuItem view, Uri uri, String column) {
		//device_access_not_secure|device_access_secure
		if (initialPrivacy == PailContract.VAL_PRIVACY_PRIVATE) {
			view.setIcon(activity.getResources().getDrawable(R.drawable.device_access_not_secure));
			initialPrivacy = PailContract.VAL_PRIVACY_PUBLIC;
		} else {
			view.setIcon(activity.getResources().getDrawable(R.drawable.device_access_secure));
			initialPrivacy = PailContract.VAL_PRIVACY_PRIVATE;
		}
		ContentValues cn = new ContentValues();
		cn.put(column, Integer.toString(initialPrivacy));
		activity.getContentResolver().update(uri, cn, null, null);
		return initialPrivacy;
	}

	public static int switchPrivacy(Activity activity, int initialPrivacy, ImageButton view, Uri uri) {
		return switchPrivacy(activity, initialPrivacy, view, uri, PailContract.EntryBaseColumns.COLUMN_PRIVACY);
	}
}