package com.demo.screen_locker.utils;

import android.util.Log;

public class SLog {
	public final static String TAG = "SLocker";
	public final static int RESULT_SUCCESS = 0;
	
	// -------------------------------------------------------------------------
	// Public static members
	// -------------------------------------------------------------------------
	/**
	 * @brief Log priority of none.
	 */
	public final static int PRIORITY_NONE = 0xFFFF;

	/**
	 * @brief Log priority of verbose.
	 */
	public final static int PRIORITY_VERBOSE = 2;
	/**
	 * @brief Log priority of debug.
	 */
	public final static int PRIORITY_DEBUG = 3;
	/**
	 * @brief Log priority of info.
	 */
	public final static int PRIORITY_INFO = 4;
	/**
	 * @brief Log priority of warning.
	 */
	public final static int PRIORITY_WARN = 5;
	/**
	 * @brief Log priority of error.
	 */
	public final static int PRIORITY_ERROR = 6;
	/**
	 * @brief Log priority of exception.
	 */
	public final static int PRIORITY_ASSERT = 7;

	private static int msLogPriority = PRIORITY_VERBOSE;// default

	public static boolean isLogEnable(int priority) {
		return (msLogPriority <= priority);
	}

	// -------------------------------------------------------------------------
	// Public static member methods
	// -------------------------------------------------------------------------
	public static int getLogPriority() {
		return msLogPriority;
	}

	public static void setLogPriority(int priority) {
		msLogPriority = priority;
	}


    public static int d(String tag, String message) {
		if (msLogPriority <= PRIORITY_DEBUG) {
			if (message == null) {
				message = "";
			}
			return Log.d(TAG + tag, message);
		} else {
			return RESULT_SUCCESS;
		}
	}

	public static int d(String tag, String message, Throwable tr) {
		if (msLogPriority <= PRIORITY_DEBUG) {
			if (message == null) {
				message = "";
			}
			return Log.d(TAG + tag, message, tr);
		} else {
			return RESULT_SUCCESS;
		}
	}

	public static int d(String tag, String format, Object... args) {
		if (msLogPriority <= PRIORITY_DEBUG) {
			String msg = String.format(format, args);
			return Log.d(TAG + tag, msg);
		} else {
			return RESULT_SUCCESS;
		}
	}

	public static int exception(String tag, Exception e, String message) {
		if (msLogPriority <= PRIORITY_ERROR) {
			if (message == null) {
				message = "";
			}
			return Log.e(TAG + tag, message, e);
		} else {
			return RESULT_SUCCESS;
		}
	}

	public static int e(String tag, String message) {
		if (msLogPriority <= PRIORITY_ERROR) {
			if (message == null) {
				message = "";
			}
			return Log.e(TAG + tag, message);
		} else {
			return RESULT_SUCCESS;
		}
	}

	public static int e(String tag, String format, Object... args) {
		if (msLogPriority <= PRIORITY_ERROR) {
			String msg = String.format(format, args);
			return Log.e(TAG + tag, msg);
		} else {
			return RESULT_SUCCESS;
		}
	}

	public static int e(String tag, String message, Throwable tr) {
		if (msLogPriority <= PRIORITY_ERROR) {
			if (message == null) {
				message = "";
			}
			return Log.e(TAG + tag, message, tr);
		} else {
			return RESULT_SUCCESS;
		}
	}
	
	public static int i(String tag, String message) {
		if (msLogPriority <= PRIORITY_INFO) {
			if (message == null) {
				message = "";
			}
			return Log.i(TAG + tag, message);
		} else {
			return RESULT_SUCCESS;
		}
	}

	public static int i(String tag, String format, Object... args) {
		if (msLogPriority <= PRIORITY_INFO) {
			String msg = String.format(format, args);
			return Log.d(TAG + tag, msg);
		} else {
			return RESULT_SUCCESS;
		}
	}

	// -------------------------------------------------------------------------
	public static int w(String tag, String message) {
		if (msLogPriority <= PRIORITY_WARN) {
			if (message == null) {
				message = "";
			}
			return Log.w(TAG + tag, message);
		} else {
			return RESULT_SUCCESS;
		}
	}

	public static int w(String tag, String format, Object... args) {
		if (msLogPriority <= PRIORITY_WARN) {
			String msg = String.format(format, args);
			return Log.w(TAG + tag, msg);
		} else {
			return RESULT_SUCCESS;
		}
	}

	public static int w(String tag, String message, Throwable tr) {
		if (msLogPriority <= PRIORITY_WARN) {
			if (message == null) {
				message = "";
			}
			return Log.w(TAG + tag, message, tr);
		} else {
			return RESULT_SUCCESS;
		}
	}
	
	public static int v(String tag, String message) {
		if (msLogPriority <= PRIORITY_VERBOSE) {
			if (message == null) {
				message = "";
			}
			return Log.v(TAG + tag, message);
		} else {
			return RESULT_SUCCESS;
		}
	}

	public static int println(int priority, String tag, String message) {
		if (message == null) {
			message = "";
		}
		return Log.println(priority, TAG + tag, message);
	}
}
