package udacity.gas.com.solveaproblem.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class PailContract {

	public static final String CONTENT_AUTHORITY = "udacity.gas.com.solveaproblem";

	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

	public static final String PATH_PROBLEM = "problems";
	public static final String PATH_ATTACHMENT = "attachments";

	//-------------------------BASE COLUMNS-------------------------\\
	public static class EntryBaseColumns implements BaseColumns {
		/*int*/
		public static final String COLUMN_DATE = "date";
		/*int*/
		public static final String COLUMN_DATE_MODIFIED = "date_modified";
	}

	public static interface AttachmentInterface {
		public String getPath();
	}

	private static class AttachmentEntry extends EntryBaseColumns {

		/*int*/
		private static final String COLUMN_PROB_ID = "prob_id";
		/*int*/
		public static final String COLUMN_PROB_KEY = COLUMN_PROB_ID;
		/*int*/
		public static final String COLUMN_ATTACH_ID = "attach_id";
		/*int*/
		public static final String COLUMN_RELEVANCE = "relevance";
		/*int*/
		public static final String COLUMN_PRIVACY = "privacy";
	}

	private static class AttachmentFileEntry extends AttachmentEntry {
		/*string*/
		public static final String COLUMN_MIME_TYPE = "file_mime_type";
		/*string*/
		public static final String COLUMN_FILE_NAME = "file_name";
		/*string*/
		public static final String COLUMN_FILE_URI = "file_uri";
		/*float*/
		public static final String COLUMN_FILE_SIZE = "file_size";
		/*int*/
		public static final String COLUMN_FILE_TYPE = "file_type";
		/*text*/
		public static final String COLUMN_FILE_DESCRIPTION = "file_description";
	}
	//-------------------------END OF BASE COLUMNS-------------------------\\
	//-------------------------MAIN COLUMNS ----------------------------\\
	public static final class ProblemEntry extends EntryBaseColumns {

		public static final Uri CONTENT_URI =
				BASE_CONTENT_URI.buildUpon().appendPath(PATH_PROBLEM).build();

		public static final String CONTENT_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROBLEM;

		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PROBLEM;

		// Table name
		public static final String TABLE_NAME = "problems";
		/*string*/
		public static final String COLUMN_TITLE = "title";
		/*string*/
		public static final String COLUMN_DESCRIPTION = "description";
		/*int*/
		public static final String COLUMN_PROBLEM_STATUS = "status";
		/*int*/
		public static final String COLUMN_PRIVACY = "privacy";
		/*int*/
		public static final int VAL_PRIVACY_PRIVATE = 1;
		public static final int VAL_PRIVACY_PUBLIC = 0;
		public static final int VAL_PROBLEM_STATUS_PENDING = 0;
		public static final int VAL_PROBLEM_STATUS_SOLVED = 1;
		public static final int VAL_PROBLEM_STATUS_NOT_SOLVED = 2;

		//problem
		public static Uri buildProblemsUri() {
			return CONTENT_URI;
		}

		//problem/id/
		public static Uri buildProblemWithIdUri(long id) {
			return ContentUris.withAppendedId(buildProblemsUri(), id);
		}
		//long
		public static long getProblemIdFromUri(Uri uri) {
			return Long.parseLong(uri.getPathSegments().get(1));
		}

		//problems/id/attachments/
		public static Uri buildProblemWithAttachmentUri(long problemid) {
			return buildProblemWithIdUri(problemid).buildUpon().appendPath(PailContract.PATH_ATTACHMENT).build();
		}

		//problems/id/attachments/attachment_type
		public static Uri buildProblemWithAttachmentTypeUri(long problemid, AttachmentInterface attachmentInterface) {
			return buildProblemWithAttachmentUri(problemid).buildUpon().appendPath(attachmentInterface.getPath()).build();
		}
		//string
		public static String getAttachmentTypeFromProblemUri(Uri uri) {
			return uri.getPathSegments().get(3);
		}
		//problems/id/attachments/attachment_type/attachment_id
		public static Uri buildProblemWithAttachmentTypeAndAttachmentIDUri(long problemid, AttachmentInterface attachmentInterface, long attachmentId) {
			return ContentUris.withAppendedId(buildProblemWithAttachmentTypeUri(problemid, attachmentInterface), attachmentId);
		}
		//long
		public static long getAttachmentIdFromProblemUri(Uri uri) {
			return Long.parseLong(uri.getPathSegments().get(4));
		}
	}

	public static final class Attachment extends AttachmentEntry {

		//attachments
		private static final Uri CONTENT_URI =
				BASE_CONTENT_URI.buildUpon().appendPath(PATH_ATTACHMENT).build();

		public static final String CONTENT_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTACHMENT;

		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTACHMENT;

		//attachments
		public static Uri buildAttachmentsUri() {
			return CONTENT_URI;
		}

		//attachments/id
		public static Uri buildAttachmentWithIdUri(long id) {
			return ContentUris.withAppendedId(buildAttachmentsUri(), id);
		}

		public static long getAttachmentIdFromUri(Uri uri) {
			return Long.parseLong(uri.getPathSegments().get(1));
		}

		//attachments/attachmentType/
		public static Uri buildAttachmentWithAttachmentTypeUri(AttachmentInterface attachmentInterface) {
			return CONTENT_URI.buildUpon().appendPath(attachmentInterface.getPath()).build();
		}

		public static String getAttachmentTypeFromUri(Uri uri) {
			return uri.getPathSegments().get(1);
		}

		//attachments/attachmentType/attachmentId
		public static Uri buildAttachmentWithAttachmentTypeWithIdUri(AttachmentInterface attachmentInterface, long id) {
			return ContentUris.withAppendedId(buildAttachmentWithAttachmentTypeUri(attachmentInterface), id);
		}

		public static long getAttachmentIdFromAttachmentTypeUri(Uri uri) {
			return Long.parseLong(uri.getPathSegments().get(2));
		}
	}

	public static final class NoteAttachmentEntry extends AttachmentEntry implements AttachmentInterface {

		public static final String TABLE_NAME = "notes";

		public static final String PATH = TABLE_NAME;
		/*int*/
		public static final String COLUMN_TITLE = "title";
		/*int*/
		public static final String COLUMN_CONTENT = "content";

		//attachment/attachment_type
		public static final Uri CONTENT_URI =
				Attachment.CONTENT_URI.buildUpon().appendPath(PATH).build();

		public static final String CONTENT_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTACHMENT + "/" + PATH;

		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTACHMENT + "/" + PATH;

		public static Uri buildNoteUri(long id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}

		@Override
		public String getPath() {
			return PATH;
		}
	}

	public static final class LinkAttachmentEntry extends AttachmentEntry implements AttachmentInterface {

		public static final String TABLE_NAME = "links";

		public static final String PATH = TABLE_NAME;
		/*text*/
		public static final String COLUMN_LINK_TITLE = "title";
		/*text*/
		public static final String COLUMN_LINK_URL = "url";
		/*text*/
		public static final String COLUMN_LINK_SCREENSHOT = "image_url";

		public static final Uri CONTENT_URI =
				Attachment.CONTENT_URI.buildUpon().appendPath(PATH).build();

		public static final String CONTENT_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTACHMENT + "/" + PATH;

		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTACHMENT + "/" + PATH;

		public static Uri buildLinkUri(long id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}

		@Override
		public String getPath() {
			return PATH;
		}
	}

	public static final class ImageAttachmentEntry extends AttachmentFileEntry implements AttachmentInterface {
		public static final String TABLE_NAME = "images";

		public static final String PATH = TABLE_NAME;

		public static final Uri CONTENT_URI =
				Attachment.CONTENT_URI.buildUpon().appendPath(PATH).build();

		public static final String CONTENT_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTACHMENT + "/" + PATH;

		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTACHMENT + "/" + PATH;

		public static Uri buildImageUri(long id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}

		@Override
		public String getPath() {
			return PATH;
		}
	}

	public static final class VideoAttachmentEntry extends AttachmentFileEntry implements AttachmentInterface {
		public static final String TABLE_NAME = "videos";

		public static final String PATH = TABLE_NAME;

		public static final Uri CONTENT_URI =
				Attachment.CONTENT_URI.buildUpon().appendPath(PATH).build();

		public static final String CONTENT_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTACHMENT + "/" + PATH;

		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTACHMENT + "/" + PATH;

		public static Uri buildVideoUri(long id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}

		@Override
		public String getPath() {
			return PATH;
		}
	}

	public static final class AudioAttachmentEntry extends AttachmentFileEntry implements AttachmentInterface {
		public static final String TABLE_NAME = "audios";

		public static final String PATH = TABLE_NAME;

		public static final Uri CONTENT_URI =
				Attachment.CONTENT_URI.buildUpon().appendPath(PATH).build();

		public static final String CONTENT_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTACHMENT + "/" + PATH;

		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTACHMENT + "/" + PATH;

		public static Uri buildVideoUri(long id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}

		@Override
		public String getPath() {
			return PATH;
		}
	}

	public static final class FileAttachmentEntry extends AttachmentFileEntry implements AttachmentInterface {
		public static final String TABLE_NAME = "files";

		public static final String PATH = TABLE_NAME;

		public static final Uri CONTENT_URI =
				Attachment.CONTENT_URI.buildUpon().appendPath(PATH).build();

		public static final String CONTENT_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTACHMENT + "/" + PATH;

		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ATTACHMENT + "/" + PATH;

		public static Uri buildVideoUri(long id) {
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}

		@Override
		public String getPath() {
			return PATH;
		}
	}
}