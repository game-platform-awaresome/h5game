package com.proxy;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Cao on 2015/7/24.
 */
public class FindEmulator {
    // Need to check the format of these
    // Android emulator support up to 16 concurrent emulator
    // The console of the first emulator instance running on a given
    // machine uses console port 5554
    // Subsequent instances use port numbers increasing by two
    private static String[] known_numbers = {
            "15555215554", // Default emulator phone numbers
            "15555215556",
            "15555215558",
            "15555215560",
            "15555215562",
            "15555215564",
            "15555215566",
            "15555215568",
            "15555215570",
            "15555215572",
            "15555215574",
            "15555215576",
            "15555215578",
            "15555215580",
            "15555215582",
            "15555215584",
    };

    private static String[] known_imeis = {
            "012345678912345" // Default emulator imei
    };

    private static String[] known_device_ids = {
            "000000000000000" // Default emulator id
    };

    private static String[] known_imsi_ids = {
            "310260000000000" // Default imsi id
    };


    private static String[] known_pipes = {
            "/dev/socket/qemud",
            "/dev/qemu_pipe",
            "/dev/bst_gps",
            "/dev/bstacce",
            "/dev/bstcmd",
            "/dev/bstgyro",
            "/dev/vboxguest",
    };

    private static String[] known_files = {
            "/sys/qemu_trace",
    };

    private static String[] known_qemu_drivers = {
            "goldfish"
    };

    /**
     * The "known" props have the potential for false-positiving due to
     * interesting (see: poorly) made Chinese devices/odd ROMs. Keeping
     * this threshold low will result in better QEmu detection with possible
     * side affects.
     */
    private static int MIN_PROPERTIES_THRESHOLD = 0x5;

    /**
     * Check the existence of known pipes used
     * by the Android QEmu environment.
     *
     * @return {@code true} if any pipes where found to
     *      exist or {@code false} if not.
     */
    public static boolean hasPipes() {
        for (String pipe : known_pipes) {
            File qemu_socket = new File(pipe);
            if (qemu_socket.exists())
                return true;
        }

        return false;
    }

    /**
     * Reads in the driver file, then checks a list for
     * known QEmu drivers.
     *
     * @return {@code true} if any known drivers where
     *      found to exist or {@code false} if not.
     */
    public static boolean hasQEmuDriver() {
        File drivers_file = new File("/proc/tty/drivers");
        if(drivers_file.exists() && drivers_file.canRead()) {
            byte[] data =  new byte[(int) drivers_file.length()];
            try {
                InputStream is = new FileInputStream(drivers_file);
                is.read(data);
                is.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            String driver_data = new String(data);
            for(String known_qemu_driver : FindEmulator.known_qemu_drivers) {
                if(driver_data.indexOf(known_qemu_driver) != -1)
                    return true;
            }
        }

        return false;
    }

    public static boolean hasKnownPhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String phoneNumber = telephonyManager.getLine1Number();

        for(String number : known_numbers) {
            if(number.equalsIgnoreCase(phoneNumber)) {
                return true;
            }

        }
        return false;
    }

    public static boolean hasKnownDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String deviceId = telephonyManager.getDeviceId();

        for(String known_deviceId : known_device_ids) {
            if(known_deviceId.equalsIgnoreCase(deviceId)) {
                return true;
            }

        }
        return false;
    }

    public static boolean hasKnownImsi(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi = telephonyManager.getSubscriberId();

        for(String known_imsi : known_imsi_ids) {
            if(known_imsi.equalsIgnoreCase(imsi)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasEmulatorBuild(Context context) {
        String BOARD = android.os.Build.BOARD; //The name of the underlying board, like "unknown".
        String BRAND = android.os.Build.BRAND; //The brand (e.g., carrier) the software is customized for, if any. "generic"
        String DEVICE = android.os.Build.DEVICE; //  The name of the industrial design. "generic"
        String HARDWARE = android.os.Build.HARDWARE; //The name of the hardware (from the kernel command line or /proc). "goldfish"
        String MODEL = android.os.Build.MODEL; //The end-user-visible name for the end product. "sdk"
        String PRODUCT = android.os.Build.PRODUCT; //The name of the overall product.
        if (BOARD.equals("unknown") || BRAND.equals("generic") || DEVICE.equals("generic") || MODEL.equals("sdk") || PRODUCT.equals("sdk") || HARDWARE.equals("goldfish")) {
            return true;
        }
        return false;
    }

    public static boolean isOperatorNameAndroid(Context paramContext) {
        String szOperatorName = ((TelephonyManager)paramContext.getSystemService("phone")).getNetworkOperatorName();
        boolean isAndroid = szOperatorName.toLowerCase().equals("android");
        return isAndroid;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(
                    packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            //e.printStackTrace();
        }
        if(packageInfo ==null){
            return false;
        }else{
            return true;
        }
    }


    public static boolean isEmulator(Context context) {
        if(hasKnownDeviceId(context) ||
                hasKnownImsi(context) ||
                hasKnownPhoneNumber(context) ||
                hasPipes() ||
                hasQEmuDriver()){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isSydEmu(Context context) {
        return (isAppInstalled(context,"com.shouyoudao.launcher2") || isAppInstalled(context,"com.syd.IME"));
    }
}
