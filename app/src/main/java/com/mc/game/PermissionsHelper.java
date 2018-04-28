package com.mc.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * @author caozhentian 权限帮助
 */

public class PermissionsHelper {

	/**
	 * 验证是否已经授权
	 *
	 * @param
	 * @param
	 *
	 * @return true : 授权 false : 未授权
	 */
	@SuppressLint("NewApi")
	public static boolean checkSelfPermission(Activity activity, String permission) {
		// android 6版本以下 ， 采用静态授权。 返回True
		if (Build.VERSION.SDK_INT < 23) {
			return true;
		}

		if (activity == null || permission == null) {
			return true;
		}
		// int hasWriteContactsPermission =
		// ContextCompat.checkSelfPermission(context,
		// Manifest.permission.WRITE_CONTACTS);
		int hasWriteContactsPermission = activity.checkSelfPermission(permission);
		if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
			return false;
		}
		return true;
	}

	/**
	 * 请求获取指定的权限
	 *
	 * @param activity
	 * @param persission
	 *            权限
	 * @param requestCode
	 */
	@SuppressLint("NewApi")
	public static void requestSelfPermissions(Activity activity, String persission, int requestCode) {
		// android 6版本以下 ， 采用静态授权。 返回True
		if (Build.VERSION.SDK_INT < 23) {
			return;
		}
		if (activity == null || persission == null) {
			return;
		}
		// ActivityCompat.requestPermissions(activity, (new String[] {
		// persission }), requestCode);
		activity.requestPermissions((new String[] { persission }), requestCode);
	}

	/**
	 * 请求获取的一组权限
	 *
	 * @param activity
	 * @param persissions
	 *            权限集
	 * @param requestCode
	 */
	@TargetApi(23)
	public static void requestSelfPermissions(Activity activity, String[] persissions, int requestCode) {
		// android 6版本以下 ， 采用静态授权。 返回True
		if (Build.VERSION.SDK_INT < 23) {
			return;
		}
		if (activity == null || persissions == null || persissions.length == 0) {
			return;
		}
		activity.requestPermissions(persissions, requestCode);
	}

	/**
	 * 请求获取的一组权限
	 *
	 * @param activity
	 * @param persissions
	 * @param requestCode
	 */
	@TargetApi(23)
	public static void requestSelfPermissions(Activity activity, List<String> persissions, int requestCode) {
		// android 6版本以下 ， 采用静态授权。 返回True
		if (Build.VERSION.SDK_INT < 23) {
			return;
		}
		if (activity == null || persissions == null || persissions.size() == 0) {
			return;
		}
		activity.requestPermissions(persissions.toArray(new String[persissions.size()]), requestCode);
	}

	/**
	 * 从授权列表中返回未授权的权限
	 *
	 * @param
	 * @param persissions
	 *            授权的列表
	 * @return 未授权的列表
	 */
	public static String[] getNotGrantPermissions(Activity activity, String[] persissions) {
		if (activity == null || persissions == null || persissions.length == 0) {
			return new String[0];
		}
		List<String> notGrantPermissions = new ArrayList<String>(persissions.length);
		for (String persission : persissions) {
			if (!checkSelfPermission(activity, persission)) { // 如果没有授权
				notGrantPermissions.add(persission);
			}
		}
		return notGrantPermissions.toArray(new String[notGrantPermissions.size()]);
	}

	/**
	 * 检查是否需要弹出请求权限的提示对话框
	 *
	 * @param
	 * @param persission
	 * @return
	 */
	@TargetApi(23)
	public static boolean shouldShowRequestPermissionRationale(Activity activity, String persission) {
		if (activity != null || persission == null) {
			return false;
		}

		return activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS);
	}

	/**
	 * @return 需要授权的权限
	 */
	public static String[] getPermissions() {
		List<String> permissions = new LinkedList<String>();

		// 数据网路
		permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
		permissions.add(Manifest.permission.INTERNET);
		permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
		permissions.add(Manifest.permission.CHANGE_WIFI_STATE);
		permissions.add(Manifest.permission.READ_PHONE_STATE);

		// 短信
		permissions.add(Manifest.permission.SEND_SMS);
		permissions.add(Manifest.permission.RECEIVE_SMS);

		// 存储
		permissions.add(Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
		permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

		// 系统设置
		permissions.add(Manifest.permission.WRITE_SETTINGS);

		// 悬浮窗
		permissions.add(Manifest.permission.GET_TASKS);
		permissions.add(Manifest.permission.SYSTEM_ALERT_WINDOW);

		return permissions.toArray(new String[permissions.size()]);
	}

	/**
	 * 判断是否有访问通讯录的权限
	 *
	 * @return
	 */
	public static boolean isGrantedContancts(Activity activity) {
		if (activity == null) {
			return false;
		}

		return checkSelfPermission(activity, Manifest.permission.WRITE_CONTACTS);
	}

	/**
	 * 判断是否有访问短信权限
	 *
	 * @param
	 * @return
	 */
	public static boolean isGrantedSMS(Activity activity) {
		if (activity == null) {
			return false;
		}

		return checkSelfPermission(activity, Manifest.permission.READ_SMS);
	}

	/**
	 * 判断是否有访问通话记录的权限
	 *
	 * @param
	 * @return
	 */
	public static boolean isGrantedCallLog(Activity activity) {
		if (activity == null) {
			return false;
		}

		return checkSelfPermission(activity, Manifest.permission.WRITE_CALL_LOG);
	}

	/**
	 * 判断是否有录音的权限
	 *
	 * @param
	 * @return
	 */
	public static boolean isGrantedRecordAudio(Activity activity) {
		if (activity == null) {
			return false;
		}

		return checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO);
	}

	/**
	 * 访问照片 文件 或者视频的权限
	 *
	 * @param
	 * @return
	 */
	public static boolean isGrantedStorage(Activity activity) {
		if (activity == null) {
			return false;
		}

		return checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
	}

	/**
	 * 请求权限 caozhentian
	 */
	public static void requestPermissions(Activity activity, int requestCode) {

		// android 6版本以下 ， 采用静态授权。 返回True
		if (Build.VERSION.SDK_INT < 23) {
			return;
		}
		String[] needGrantPersissions = PermissionsHelper.getPermissions();
		String[] notGrantPersissions = PermissionsHelper.getNotGrantPermissions(activity, needGrantPersissions);
		PermissionsHelper.requestSelfPermissions(activity, notGrantPersissions, requestCode);

		// if (Build.VERSION.SDK_INT >= 23 ) {
		// Intent intent = new
		// Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
		// intent.setData(Uri.parse("package:" + getPackageName()));
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// startActivity(intent);
		// }

	}
}
