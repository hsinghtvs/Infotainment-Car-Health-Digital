package com.mytvs.infotainmentcarhealthdigital;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.database.DatabaseReference;
import com.hoho.android.usbserial.driver.SerialTimeoutException;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.mytvs.infotainmentcarhealthdigital.data.DBCParam;
import com.mytvs.infotainmentcarhealthdigital.data.MyDataModelClass;
import com.mytvs.infotainmentcarhealthdigital.serviceKit.Constants;
import com.mytvs.infotainmentcarhealthdigital.serviceKit.CustomProber;
import com.mytvs.infotainmentcarhealthdigital.serviceKit.SerialListener;
import com.mytvs.infotainmentcarhealthdigital.serviceKit.SerialService;
import com.mytvs.infotainmentcarhealthdigital.serviceKit.SerialSocket;
import com.mytvs.infotainmentcarhealthdigital.serviceKit.TextUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.realm.Realm;

public class DataFromDevice implements ServiceConnection, SerialListener {
    private enum Connected {False, Pending, True}

    public String usbData;
    private int deviceId, portNum, baudRate;
    private UsbSerialPort usbSerialPort;
    private SerialService service;
    private boolean initializationDone = false;
    private int request_index = 0;
    private int UDSresponse = 0;
    private String reqstr = "";

    private String spn = "";
    private int receive_complete = 0;
    private String data_new = "";
    private String battery = "";


    //    private ControlLines controlLines;
    private TextUtil.HexWatcher hexWatcher;

    private Connected connected = Connected.False;
    private boolean initialStart = true;
    private boolean hexEnabled = false;
    private boolean controlLinesEnabled = false;
    private boolean pendingNewline = false;
    private String newline = TextUtil.newline_crlf;

    private DatabaseReference firebaseDatabaseRef;

    private String serverAddress = "devcvfeed.mytvs.in";
    private int serverPort = 7788;

    private Socket socket;

    private OutputStream outputStream;

    private PrintWriter out;

    private Timer timer;

    private LocationManager locationManager;

    private final Executor executor = Executors.newSingleThreadExecutor();

    static Context context;

    private Handler handler;
    private Runnable locationUpdater;

    private Double lat = 0.0;
    private Double lon = 0.0;
    private float bearingSt = 0;
    private float speedSt = 0;
    private int satellite = 0;
    private float hdopSt = 0;

    // dateTime, gnssFixStatus, gsmSignalStrength
    private String dateTimeSt = "";
    private String gnssFixStatusSt = "";
    private int gsmSignalStrengthSt = 0;
    private SpannableStringBuilder spn_new;
    private int count = 0;
    public static DataFromDevice instance;
    private Realm realm;
    ConnectivityReceiver connectivityReceiver;
    public List<DBCParam> dbcList = new ArrayList<>();
    private boolean dataEnded = true;
    private StringBuilder strBuilder = new StringBuilder();

    public static DataFromDevice getInstance(Context ctx) {
        context = ctx;
        if (instance == null) {
            instance = new DataFromDevice();
        }
        return instance;
    }

    public void setDBCList(List<DBCParam> dbcParamList) {
        dbcList = dbcParamList;
    }

    public void StartService() {
        if (service != null)
            service.attach(this);
        else
            context.startService(new Intent(context, SerialService.class));

        context.bindService(new Intent(context, SerialService.class), this, Context.BIND_AUTO_CREATE);
        SharedPreferences sh = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);

        deviceId = sh.getInt("device", 0);
        portNum = sh.getInt("port", 0);
        baudRate = 500;

        connectivityReceiver = new ConnectivityReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        ContextCompat.registerReceiver(context, connectivityReceiver, intentFilter, ContextCompat.RECEIVER_NOT_EXPORTED);

        Realm.init(context);
        realm = Realm.getDefaultInstance();

//        startPeriodicOperations();


        spn_new = new SpannableStringBuilder();

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        Log.d("HarmanSir", "onLocationChanged:locationManager " + locationManager);

        if (locationManager != null) {
//            Toast.makeText(context, "Checking location permission", Toast.LENGTH_SHORT).show();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            handler = new Handler();
            locationUpdater = new Runnable() {
                @Override
                public void run() {
//                    Log.d("HarmanSir", "onLocationChanged:latitude inside run");
                    requestLocationUpdates();
                    handler.postDelayed(this, 2000);
                }
            };
            handler.postDelayed(locationUpdater, 0);
        }


    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();
        service.attach(this);
        if (initialStart) {
            initialStart = false;
            this.connecting();
        }
    }

    private void connecting() {
        connect(null);
    }

    //Check for end delimiter
    private boolean checkForEnd(byte[] inputArray) {
        for (byte b : inputArray) {
            if (b == (byte) 0x03) {
                return true;
            }
        }
        return false;
    }

    //Check for start delimiter
    private boolean checkForStart(byte[] inputArray) {
        for (byte b : inputArray) {
            if (b == (byte) 0x02) {
                return true;
            }
        }
        return false;
    }

    public void connect(Boolean permissionGranted) {
        UsbDevice device = null;
        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        for (UsbDevice v : usbManager.getDeviceList().values())
            if (v.getDeviceId() == deviceId)
                device = v;
        if (device == null) {
            status("connection failed: device not found");
            return;
        }
        UsbSerialDriver driver = UsbSerialProber.getDefaultProber().probeDevice(device);
        if (driver == null) {
            driver = CustomProber.getCustomProber().probeDevice(device);
        }
        if (driver == null) {
            status("connection failed: no driver for device");
            return;
        }
        if (driver.getPorts().size() < portNum) {
            status("connection failed: not enough ports at device");
            return;
        }
        usbSerialPort = driver.getPorts().get(portNum);
        UsbDeviceConnection usbConnection = usbManager.openDevice(driver.getDevice());
        if (usbConnection == null && permissionGranted == null && !usbManager.hasPermission(driver.getDevice())) {
            int flags = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0;
            PendingIntent usbPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(Constants.INTENT_ACTION_GRANT_USB), flags);
            usbManager.requestPermission(driver.getDevice(), usbPermissionIntent);
            return;
        }
        if (usbConnection == null) {
            if (!usbManager.hasPermission(driver.getDevice()))
                status("connection failed: permission denied");
            else
                status("connection failed: open failed");
            return;
        }

        connected = Connected.Pending;
        try {
            usbSerialPort.open(usbConnection);
            usbSerialPort.setParameters(baudRate, UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            SerialSocket socket = new SerialSocket(context.getApplicationContext(), usbConnection, usbSerialPort);
            service.connect(socket);
            onSerialConnect();
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }

    void status(String str) {
        Log.d("TAG", "status: " + str);
    }

    private void disconnect() {
        connected = Connected.False;
//        controlLines.stop();
        service.disconnect();
        usbSerialPort = null;
    }

    private void startPeriodicOperations() {
        hexEnabled = true;
        timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
        Runnable backgroundTask = () -> {
            try {
//                    sendParametersWithDelay();
                sendCommands();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("PeriodicOperations", "Error in periodic operation: " + e.getMessage());
            }
        };
        Thread thread = new Thread(backgroundTask);
        thread.start();

//            }
//        }, 10, 200); // 10 seconds in milliseconds
    }

    private void sendCommands() {
//        if (!initializationDone) {
//            sendInitializationCommands();
//            initializationDone = true;
//        }
        sendIntervalCommands();
    }

    private void sendInitializationCommands() {
//        String CodeKlen = "ATTP5";    //KLine
//        String CodeCan = "ATTP7";  //Extended
// String StandardCAN500kbps = "ATTP6";
// String AutoProtocol = "ATTP0";
        try {
            String request = "ATTP6";
            send(request);
            while (UDSresponse != 1) {
                Thread.sleep(500); // Delay for 500 milliseconds
                if (UDSresponse == 1) {
                    break;
                }
                if (UDSresponse == 2) {
                    send(request);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendIntervalCommands() {
        String request = "";
        String[] parameters = {
                "0C", "0D", "5B", "05", "2F"
//                "33", "42", "04", "05",
//                "0B", "0C", "0D", "0F",
//                "10", "21", "23", "2C",
//                "14", "31", "4A", "0E",
//                "03", "BAT"
        };
        try {
            if (request_index > parameters.length) {
                request_index = 0;
            }
            if (request_index > parameters.length - 1) {
                request = parameters[request_index];
            } else {
                request = "01" + parameters[request_index];
            }

            //Forming data for the request
            try {
                JSONObject outerRequestJsonObject = getOuterRequestJsonObject(parameters);
                UDSresponse = 0;
                send(outerRequestJsonObject.toString());
            } catch (Exception ex) {
                Log.i("INFOTAINMENT", "CAR HEALTH FORMING REQUEST JSON EXCEPTION - " + ex.toString());
            }

            while (UDSresponse != 1) {
                Thread.sleep(500); // Delay for 10 milliseconds
                if (UDSresponse == 1) {
                    break;
                }
                Thread.sleep(500); // Delay for 10 milliseconds
                if (UDSresponse == 2) {
                    // Select Any one of Them Until Akhilesh Changes to Auto Protocol
                    // Klen Vehicle
                    send("ATTP6");
                    Thread.sleep(500); // Delay for 10 milliseconds
                    send(request);
                    Thread.sleep(500); // Delay for 10 milliseconds
                }
            }
            request_index++;
            if (request_index == 18) {
                request_index = 0;
                Thread.sleep(30); // Delay for 10 milliseconds
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private static JSONObject getOuterRequestJsonObject(String[] parameters) throws JSONException {
        JSONObject outerRequestJsonObject = new JSONObject();
        JSONObject requestJsonObject = new JSONObject();
        JSONArray insideRequestJsonArray = new JSONArray();
        for (int i = 0; i < parameters.length - 1; i++) {
            JSONObject insideRequestJsonObject = new JSONObject();
            insideRequestJsonObject.put("C11", "7DF");
            insideRequestJsonObject.put("C13", "0201"
                    + parameters[i] +
                    "0000000000");
            insideRequestJsonObject.put("C32", 200);
            insideRequestJsonArray.put(insideRequestJsonObject);
        }
        requestJsonObject.put("C31", insideRequestJsonArray);
        outerRequestJsonObject.put("C01", requestJsonObject);
        return outerRequestJsonObject;
    }


//    private void sendParametersWithDelay() {
//        String[] parameters = {
//                "31", "33", "42", "04", "05", "0B", "0C", "0D",
//                "0F", "10", "11", "1F", "21", "23", "2C", "2D"
//        };
//
//        for (String parameter : parameters) {
//            try {
//                String request = "02 01 " + parameter + " 00 00 00 00 00 ";
//                send(request);
//                Thread.sleep(10); // Delay for 10 milliseconds
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private void send(String str) {
//        if (connected != Connected.True) {
//            return;
//        }
        try {
            String msg;
            byte[] data;
            Charset charset = StandardCharsets.UTF_8;
            if (hexEnabled) {
                StringBuilder sb = new StringBuilder();
                TextUtil.toHexString(sb, TextUtil.fromHexString(str));
                TextUtil.toHexString(sb, newline.getBytes());
                //msg = sb.toString();
                //data = TextUtil.fromHexString(msg);
                reqstr = str;
//                msg = reqstr + "\r";
                msg = reqstr;
//                msg = str + "\r";
                //data = (str + newline).getBytes();
                data = msg.getBytes(charset);
            } else {
                //msg = sb.toString();
                //data = TextUtil.fromHexString(msg);
                reqstr = str;
                msg = reqstr + "\r";
//                msg = str + "\r";
                //data = (str + newline).getBytes();
                data = msg.getBytes(charset);
            }
            service.write(data);
            status("write data: " + str);
        } catch (SerialTimeoutException e) {
            status("write timeout: " + e.getMessage());
        } catch (Exception e) {
            onSerialIoError(e);
        }
    }

    private void receive(ArrayDeque<byte[]> datas) {
        //SpannableStringBuilder spn = new SpannableStringBuilder();
        String str = "";
        String req_res = "";

        Charset charset = StandardCharsets.UTF_8;
        for (byte[] data : datas) {
            //Check for start/end delimiters
            boolean startDelimiterAvailable = checkForStart(data);
            boolean endDelimiterAvailable = checkForEnd(data);
            if (startDelimiterAvailable) {
                strBuilder = new StringBuilder();
            }
            if (hexEnabled) {
                // spn.append(TextUtil.toHexString(data)).append('\n');
                byte[] newArray = new byte[data.length];
                int newIndex = 0;
                for (byte dt : data) {
                    if (dt != (byte) 0x02 && dt != (byte) 0x03) {
                        newArray[newIndex++] = dt;
                    }
                }
                str = new String(newArray, charset);
                strBuilder.append(str);
            } else {
//                    String msg = new String(data);
//                    int msgLength = msg.length();
//
//                    if (newline.equals(TextUtil.newline_crlf) && msgLength > 0) {
//                        if (pendingNewline && msg.charAt(0) == '\n') {
//                            int charactersToDelete = 2;
//                            if (spn.length() >= charactersToDelete) {
//                                //   spn.delete(spn.length() - charactersToDelete, spn.length());
//                            } else {
//                                Editable edt = receiveText.getEditableText();
//                                int edtLength = edt != null ? edt.length() : 0;
//                                if (edtLength >= charactersToDelete) {
//                                    edt.delete(edtLength - charactersToDelete, edtLength);
//                                }
//                            }
//                        }
//                        pendingNewline = msg.charAt(msgLength - 1) == '\r';
//                    }
//
//                    if (!pendingNewline) {
//                        // spn.append(TextUtil.toCaretString(msg, newline.length() != 0));
//                    }
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYMMDDHHMMSS");
            Date currentDate = new Date();

            String dateTime = dateFormat.format(currentDate);
//        long unixTime = convertToUnixTime(dateTime);

            Log.d("Formatted Date Time: ", dateTime);
            Log.d("Unix Time: ", String.valueOf(dateTime));

           /* SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss", Locale.getDefault());
            String dateTime = dateFormat.format(new Date());*/

          /*  long epoch;
            try {
                epoch = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse("01/01/1970 01:00:00").getTime() / 1000;
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            String unixTime =String.valueOf(epoch);*/

            if (endDelimiterAvailable) {
                spn = spn + strBuilder.toString();
                if (!spn.isEmpty()) {
                    receive_complete = 1;
                    Intent i = new Intent("USBData");
                    try {
                        JSONObject jsonObject = new JSONObject(spn);
                        if (jsonObject.has("F01")) {
                            JSONObject f01Object = jsonObject.getJSONObject("F01");
                            if (f01Object.has("C01")) {
                                JSONArray jsonArray = f01Object.getJSONArray("C01");
                                for (int k = 0; k < jsonArray.length(); k++) {
                                    JSONObject responseObject = jsonArray.getJSONObject(k);
                                    for (DBCParam dbcParam : dbcList) {
                                        if (responseObject.has("C13")) {
                                            if (dbcParam.getName().contains("_" + responseObject.getString("C13").substring(4, 6) + "_")) {
                                                String value = parseData(responseObject.getString("C13"), dbcParam);
                                                Toast.makeText(context, "Parsed data - " + dbcParam.getName() + " - " + responseObject.getString("C13") + " - " + value, Toast.LENGTH_LONG).show();
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Log.e("CAR-HEALTH", "Parsing exception from the JSON object " + ex.toString());
                    }
                    i.putExtra("Response_data", spn);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                }


                //spn_new.append(spn);
                //count++;
                // Log.e("count", String.valueOf(count));
                // Log.e("Response_spn_logs", String.valueOf(spn_new));
//        Toast.makeText(context, "reqstr : " + reqstr + "," + "spn" + spn, Toast.LENGTH_SHORT).show();
                if (receive_complete == 1) {
                    receive_complete = 0;
                    if (spn.contains("NO DATA") || spn.contains("SUCCESS")) {
                        UDSresponse = 1;
                    } else if (spn.contains("NOT FOUND") || spn.contains("ERROR") || spn.contains("TIME OUT")) {
                        UDSresponse = 2;
                    } else if (spn.contains("OK")) {
                        UDSresponse = 1;
                    } else if (spn.contains("BUS INIT") || spn.contains("WAIT")) {
                        // Log.e("Response_data", String.valueOf(spn));
                        UDSresponse = 3;
                        //sendCommands();
                    } else if (reqstr.contains("BAT")) {
                        data_new = "0341BB" + strBuilder.toString() + "00000000";
                        Toast.makeText(context, "data_new " + data_new, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent("USBData");
                        i.putExtra("Response_data", data_new);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                        UDSresponse = 1;
                    } else {
                        // Log.e("Response_data", String.valueOf(spn));
                        if (spn.contains("41") || spn.contains("7F") || spn.contains("43") || spn.contains("47")) {
                            UDSresponse = 1;
                            spn = spn.substring(0, spn.length() - 1);
                            spn = spn.replaceAll(">", "");
                            spn = spn.replaceAll("\\s+", "");
                            if (spn.length() < 16) {
                                while (spn.length() < 16) {
                                    spn = spn + "0";
                                }
                                // spn= spn.padEnd(16-spn.length(),"0");
                            }
//                            Toast.makeText(context, "data_response " + strBuilder.toString(), Toast.LENGTH_SHORT).show();
                            String gpsValid = "V";
                            if (lat != 0) {
                                gpsValid = "A";
                            } else {
                                gpsValid = "V";
                            }

                            if ((request_index == 0)) {
                                // first_service = 0;
                                data_new = "";   //clear the string
                                String Package_Header = "$$CLIENT_1NS,862843041050881,1," + lat + "," + lon + "," + dateTime + "," + gpsValid + gnssFixStatusSt + "," + gsmSignalStrengthSt + "," + speedSt + ",583,3," + satellite + "," + hdopSt + ",0,0,12181,2050,12181,3960,0,0,0,10023,21,";

                                if (!reqstr.equals("0103")) {
                                    req_res = "|" + reqstr + ":" + spn;
                                    data_new = Package_Header + req_res;
                                } else {
                                    data_new = Package_Header;
                                }
                            } else {
                                req_res = "|" + reqstr + ":" + spn;
                                data_new = data_new + req_res;
                            }

                            if ((request_index == 16)) {

                                data_new = data_new + "|*66";

                                Log.e("Response__data_length", String.valueOf(spn_new.length()));

                                usbData = data_new;
//                                sendMessageToServer(serverAddress, serverPort, data_new);

                                Intent i = new Intent("USBData");
                                i.putExtra("data_new", data_new);
                                LocalBroadcastManager.getInstance(context).sendBroadcast(i);
                                Log.e("Response_data", String.valueOf(data_new));
                                //receiveText.setText(spn_new);

                                //responses.clear();
                                spn_new.clear();


                            }
                        }
                    }
                    spn = "";
                }
            }
        }
    }

    private long convertToUnixTime(String dateTime) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
            Date date = dateFormat.parse(dateTime);
            long unixTime = date.getTime() / 1000;
            return unixTime;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private byte[] hexStringToByteArray(String hex) {
        byte[] result = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            result[i / 2] = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
        }
        return result;
    }

    private byte[] padTo8Bytes(byte[] originalArray) {
        if (originalArray.length >= 8) return originalArray;
        return Arrays.copyOf(originalArray, 8);
    }

    private Long extractBits(byte[] canData, int startBit, int bitlength,
                             boolean isLittleEndian) {
        byte[] data = padTo8Bytes(canData);
        int startIndex = startBit / 8;
        int startBitIndex = startBit % 8;
        int endIndex = (startBit + bitlength - 1) / 8;
        Long result = 0L;

        if (isLittleEndian) {
            // Little-endian byte order
            for (int i = endIndex; i >= startIndex; i--) {
                long b = data[i] & 0xFFL;
                int shift = (i == endIndex) ? startBitIndex : 0;
                result = (result << 8) | (b >>> shift);
            }
        } else {
            // Big-endian byte order
            for (int i = startIndex; i <= endIndex; i++) {
                long b = data[i] & 0xFFL;
                int shift = (i == startIndex) ? startBitIndex : 0;
                result = (result << 8) | (b >>> shift);
            }
        }
        return result;
    }

    public String parseData(String data, DBCParam dbcParam) {
        byte[] canDataByte = hexStringToByteArray(data);
        Long extractedData = extractBits(canDataByte, dbcParam.getStartBit(), dbcParam.getBitLength(), dbcParam.isLittleEndian());
        if (dbcParam.isSigned() && extractedData != 0 & (1L << (dbcParam.getBitLength() - 1)) != 0) {
            extractedData -= (1L << dbcParam.getBitLength());
        }
        // Apply factor and offset
        float scaledData = extractedData * dbcParam.getFactor() + dbcParam.getOffset();
        String result = String.valueOf(scaledData);
        return result;
    }


//    private void receive(ArrayDeque<byte[]> datas) {
//
//            SpannableStringBuilder spn = new SpannableStringBuilder();
//
//
//            for (byte[] data : datas) {
//                if (hexEnabled) {
//                    spn.append(TextUtil.toHexString(data)).append('\n');
//                } else {
//                    String msg = new String(data);
//                    int msgLength = msg.length();
//
//                    if (newline.equals(TextUtil.newline_crlf) && msgLength > 0) {
//                        if (pendingNewline && msg.charAt(0) == '\n') {
//                            int charactersToDelete = 2;
//                            if (spn.length() >= charactersToDelete) {
//                                spn.delete(spn.length() - charactersToDelete, spn.length());
//                            } else {
//
//                            }
//                        }
//                        pendingNewline = msg.charAt(msgLength - 1) == '\r';
//                    }
//
//                    if (!pendingNewline) {
//                        spn.append(TextUtil.toCaretString(msg, newline.length() != 0));
//                    }
//                }
//            }
//
//
//
//
//            spn_new.append(spn);
//            count++;
//            Log.e("count", String.valueOf(count));
//            Log.e("Response_spn_logs", String.valueOf(spn_new));
//
//
//            if (spn_new.length() >= 16*24) {
//                Log.e("Response__data_length", String.valueOf(spn_new.length()));
//                String data_new = "$$CLIENT_1NS,862843041050881,1," + lat + "," + lon + "," + dateTimeSt + "," + gnssFixStatusSt + "," + gsmSignalStrengthSt + "," + speedSt + ",583,3," + satellite + "," + hdopSt + ",0,0,12181,2050,12181,3960,10023,21," +
//                        "1|0131:" + spn_new.subSequence(0, 24).toString() + "|0133:" + spn_new.subSequence(24, 48).toString() + "|0142:" + spn_new.subSequence(48, 72) + "|0104:" + spn_new.subSequence(72, 96) + "|0105:" + spn_new.subSequence(96, 120) + "|010B:" + spn_new.subSequence(120, 144) + "" +
//                        "|010C:" + spn_new.subSequence(144, 168) + "|010D:" + spn_new.subSequence(168, 192) + "|010F:" + spn_new.subSequence(192, 216) + "|0110:" + spn_new.subSequence(216, 240) + "|0111:" + spn_new.subSequence(240, 264) + "|011F:" + spn_new.subSequence(264, 288) + "" +
//                        "|0121:" + spn_new.subSequence(288, 312) + "|0123:" + spn_new.subSequence(312, 336) + "|012C:" + spn_new.subSequence(336, 360) + "|012D:" + spn_new.subSequence(360, 384) + "|*66";
//
//               usbData = data_new;
//
//               Intent i = new Intent("USBData");
//               i.putExtra("data_new", data_new);
//               LocalBroadcastManager.getInstance(context).sendBroadcast(i);
//
//                Log.d("TAG", "receive: data_new " + data_new);
//
//                Log.e("Response_data", String.valueOf(data_new));
//
//            }
//    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        service = null;
    }

    @Override
    public void onSerialConnect() {
        status("connected");
        connected = Connected.True;
//        if (controlLinesEnabled)
//            controlLines.start();
        startPeriodicOperations();
    }

    @Override
    public void onSerialConnectError(Exception e) {
        status("connection failed: " + e.getMessage());
        disconnect();
    }

    @Override
    public void onSerialRead(byte[] data) {
        ArrayDeque<byte[]> datas = new ArrayDeque<>();
        datas.add(data);
        receive(datas);
    }

    @Override
    public void onSerialRead(ArrayDeque<byte[]> datas) {
        receive(datas);
    }

    @Override
    public void onSerialIoError(Exception e) {
        status("connection lost: " + e.getMessage());
        disconnect();
    }

//    class ControlLines {
//        private static final int refreshInterval = 200; // msec
//
//        private final Handler mainLooper;
//        private final Runnable runnable;
//        private final LinearLayout frame;
//        private final ToggleButton rtsBtn, ctsBtn, dtrBtn, dsrBtn, cdBtn, riBtn;
//
//        ControlLines(View view) {
//            mainLooper = new Handler(Looper.getMainLooper());
//            runnable = this::run;
//
////            frame = view.findViewById(R.id.controlLines);
////            rtsBtn = view.findViewById(R.id.controlLineRts);
////            ctsBtn = view.findViewById(R.id.controlLineCts);
////            dtrBtn = view.findViewById(R.id.controlLineDtr);
////            dsrBtn = view.findViewById(R.id.controlLineDsr);
////            cdBtn = view.findViewById(R.id.controlLineCd);
////            riBtn = view.findViewById(R.id.controlLineRi);
////            rtsBtn.setOnClickListener(this::toggle);
////            dtrBtn.setOnClickListener(this::toggle);
//        }
//
//        private void toggle(View v) {
//            ToggleButton btn = (ToggleButton) v;
//            if (connected != Connected.True) {
//                btn.setChecked(!btn.isChecked());
//                return;
//            }
//            String ctrl = "";
//            try {
//                if (btn.equals(rtsBtn)) {
//                    ctrl = "RTS";
//                    usbSerialPort.setRTS(btn.isChecked());
//                }
//                if (btn.equals(dtrBtn)) {
//                    ctrl = "DTR";
//                    usbSerialPort.setDTR(btn.isChecked());
//                }
//            } catch (IOException e) {
//                status("set" + ctrl + " failed: " + e.getMessage());
//            }
//        }
//
//        private void run() {
//            if (connected != Connected.True)
//                return;
//            try {
//                EnumSet<UsbSerialPort.ControlLine> controlLines = usbSerialPort.getControlLines();
////                rtsBtn.setChecked(controlLines.contains(UsbSerialPort.ControlLine.RTS));
////                ctsBtn.setChecked(controlLines.contains(UsbSerialPort.ControlLine.CTS));
////                dtrBtn.setChecked(controlLines.contains(UsbSerialPort.ControlLine.DTR));
////                dsrBtn.setChecked(controlLines.contains(UsbSerialPort.ControlLine.DSR));
////                cdBtn.setChecked(controlLines.contains(UsbSerialPort.ControlLine.CD));
////                riBtn.setChecked(controlLines.contains(UsbSerialPort.ControlLine.RI));
//                mainLooper.postDelayed(runnable, refreshInterval);
//            } catch (IOException e) {
//                status("getControlLines() failed: " + e.getMessage() + " -> stopped control line refresh");
//            }
//        }
//
//        void start() {
////            frame.setVisibility(View.VISIBLE);
//            if (connected != Connected.True)
//                return;
//            try {
//                EnumSet<UsbSerialPort.ControlLine> controlLines = usbSerialPort.getSupportedControlLines();
////                if (!controlLines.contains(UsbSerialPort.ControlLine.RTS))
////                    rtsBtn.setVisibility(View.INVISIBLE);
////                if (!controlLines.contains(UsbSerialPort.ControlLine.CTS))
////                    ctsBtn.setVisibility(View.INVISIBLE);
////                if (!controlLines.contains(UsbSerialPort.ControlLine.DTR))
////                    dtrBtn.setVisibility(View.INVISIBLE);
////                if (!controlLines.contains(UsbSerialPort.ControlLine.DSR))
////                    dsrBtn.setVisibility(View.INVISIBLE);
////                if (!controlLines.contains(UsbSerialPort.ControlLine.CD))
////                    cdBtn.setVisibility(View.INVISIBLE);
////                if (!controlLines.contains(UsbSerialPort.ControlLine.RI))
////                    riBtn.setVisibility(View.INVISIBLE);
//                run();
//            } catch (IOException e) {
//            }
//        }
//
//        void stop() {
////            frame.setVisibility(View.GONE);
//            mainLooper.removeCallbacks(runnable);
////            rtsBtn.setChecked(false);
////            ctsBtn.setChecked(false);
////            dtrBtn.setChecked(false);
////            dsrBtn.setChecked(false);
////            cdBtn.setChecked(false);
////            riBtn.setChecked(false);
//        }
//    }

    public class ConnectivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//
//            if (activeNetwork != null && activeNetwork.isConnected()) {
//                Log.e("check", "checking connection");
//
//                Toast.makeText(context, "internet connection is available...", Toast.LENGTH_SHORT).show();
//            } else {
//                Log.e("check", "checking  issue  connection");
//                // Internet is not available, save data locally (e.g., in a Room database).
//            //    saveDataLocally(serverAddress, serverPort, data_new);
//            }

            if (isInternetAvailable()) {
//                Toast.makeText(context, "Internet is available", Toast.LENGTH_SHORT).show();


                MyDataModelClass myData = realm.where(MyDataModelClass.class).findFirst();
                String responseData = (myData != null) ? myData.getData_new() : null;
                //  String responseData = myData.getData_new();

                if (myData != null /*&& !myData.getData_new().isEmpty()*/) {
                    sendMessageToServer(serverAddress, serverPort, responseData);
                    Log.d("TAG", "onReceive22: " + responseData);
                    // uploadToFirebase("responseData : " + responseData);
                    //sendMessageToServer(serverAddress,serverPort,"responseData = "+responseData);
                    Toast.makeText(context, responseData, Toast.LENGTH_LONG).show();
//                    receiveText.setText("Offline Data:" +responseData );

//                    clearDataInRealm();
                } else {
//                    Toast.makeText(context, "No data..", Toast.LENGTH_SHORT).show();
                }


//                MyDataModelClass myData = realm.where(MyDataModelClass.class).findFirst();
//                String responseData = (myData != null) ? myData.getData_new() : null;
//             //  String responseData = myData.getData_new();
//
//                if (!responseData.isEmpty())
//                {
//                    sendMessageToServer(serverAddress,serverPort,responseData);
//                    Log.d("TAG", "onReceive22: "+responseData);
//                    clearDataInRealm();
//                }
//                else {
//                    Toast.makeText(context, "No data..", Toast.LENGTH_SHORT).show();
//                }
//            // parallely we send the live_data to server
//                sendMessageToServer(serverAddress,serverPort,data_new);

            } else {

                Handler backgroundHandler = new Handler(Looper.getMainLooper()); // Use a separate thread if necessary
                backgroundHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        realm.beginTransaction();
//                        MyDataModelClass modal = new MyDataModelClass();
//                        MyDataModelClass myData = realm.createObject(MyDataModelClass.class);
//                        modal.setData_new(data_new);
//                        myData.setData_new(data_new);
//                        realm.commitTransaction();

                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                MyDataModelClass modal = new MyDataModelClass();
                                modal.setData_new(data_new);
                                realm.copyToRealm(modal);
                                Toast.makeText(context, "testing...." + data_new, Toast.LENGTH_SHORT).show();
                            }
                        });
//                        realm.executeTransaction(new Realm.Transaction() {
//                            @Override
//                            public void execute(Realm realm) {
//                                // inside on execute method we are calling a method
//                                // to copy to real m database from our modal class.
//                                realm.copyToRealm(modal);
//                                realm.copyFromRealm(myData);
//                                Toast.makeText(getActivity(), ""+modal, Toast.LENGTH_SHORT).show();
//
//                            }
//                        });
                    }
                });


                Log.e("offline1", data_new);

                Toast.makeText(context, "no internet available", Toast.LENGTH_SHORT).show();

            }
//            else {
//                Toast.makeText(getActivity(), "in network block", Toast.LENGTH_SHORT).show();
//
//
//            }
        }

//        public void clearDataInRealm() {
//            Realm realm = Realm.getDefaultInstance();
//            realm.beginTransaction();
//            realm.delete(MyDataModelClass.class);
//            realm.commitTransaction();
//            realm.close();
//        }

        private boolean isInternetAvailable() {
//            ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//            if (connectivityManager != null) {
//                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//                return networkInfo != null && networkInfo.isConnected();
//            }
//            return false;
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }


    }

    public void sendMessageToServer(String serverAddress, int serverPort, final String message) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {

                    Socket socket = new Socket(serverAddress, serverPort);

                    OutputStream outputStream = socket.getOutputStream();
                    PrintWriter out = new PrintWriter(outputStream, true);


                    out.println(message);
                    Log.d("Harsh", "sendMessageToServer  " + message);
//                    Toast.makeText(context, "sendMessageToServer", Toast.LENGTH_SHORT).show();

                    try {
                        out.close();
                        outputStream.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("SocketException", "Error while closing socket: " + e.getMessage());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        Log.d("HarmanSir", "onLocationChanged:requestLocationUpdates ");
//        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
        locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 2000L, 10f, locationListener, null);
    }

    private final LocationListener locationListener = new LocationListener() {
        boolean hasUpdatedLocation = false;


        /* @Override
         public void onLocationChanged(Location location) {

             Toast.makeText(context, "fetching the GPS coordinates", Toast.LENGTH_SHORT).show();

             if (hasUpdatedLocation) {
                 hasUpdatedLocation = true;

                 //Toast.makeText(context, "locationManager - 3", Toast.LENGTH_SHORT).show();

                 double latitude = location.getLatitude();
                 double longitude = location.getLongitude();
                 String lat = String.valueOf(latitude);
                 String lon = String.valueOf(longitude);

                 SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss", Locale.getDefault());
                 String dateTime = dateFormat.format(new Date());

                 String deviceInfo = String.format("LAT=%s,LON=%s,DATETIME=%s", lat, lon, dateTime);

                 uploadToFirebase(deviceInfo);


                 Log.e(TAG, "DeviceInfoUpload: " + deviceInfo);
                 Toast.makeText(context, "GPS coordinates uploaded to Cloud", Toast.LENGTH_SHORT).show();
             }
         }*/

        @Override
        public void onLocationChanged(Location location) {
//            Log.d("HarmanSir", "onLocationChanged:onLocationChanged " + location.getLatitude());
            if (!hasUpdatedLocation) {
                hasUpdatedLocation = true;

                double latitude = location.getLatitude();
//                Log.d("HarmanSir", "onLocationChanged:latitude " + latitude);
                double longitude = location.getLongitude();
                float speed = location.getSpeed(); // Speed in meters/second
                float bearing = location.getBearing(); // Bearing in degrees
                if (location.getExtras() != null) {
//                    int satellitesUsed = location.getExtras().getInt("satellites", -1); // Number of satellites used for fix
//                    float hdop = location.getExtras().getFloat("hdop", -1.0f); // Horizontal dilution of precision
                    satellite = location.getExtras().getInt("satellites", -1); // Number of satellites used for fix
                    hdopSt = location.getExtras().getFloat("hdop", -1.0f); // Horizontal dilution of precision

                    // GNSS Fix status
//                    String gnssFixStatus;
                    if (location.getExtras().getBoolean("hasGnssFix", false)) {
                        gnssFixStatusSt = "GNSS Fix Acquired";
                    } else {
                        gnssFixStatusSt = "No GNSS Fix";
                    }
                }

                // GSM Signal strength
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                int gsmSignalStrength = 0; // Signal strength in dBm
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                    gsmSignalStrength = telephonyManager.getSignalStrength().getGsmSignalStrength();
//                }


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss", Locale.getDefault());
                String dateTime = dateFormat.format(new Date());

                lat = latitude;
                lon = longitude;
                speedSt = speed;
                bearingSt = bearing;
//                gnssFixStatusSt = gnssFixStatus;
                gsmSignalStrengthSt = gsmSignalStrength;
                dateTimeSt = dateTime;


                String deviceInfo = String.format("LAT=%s,LON=%s,DATETIME=%s,GNSS=%s,GSM_SIGNAL=%ddBm,SPEED=%.2f m/s,BEARING=%.2f°,SATELLITES_USED=%d,HDOP=%.2f", latitude, longitude, dateTime, gnssFixStatusSt, gsmSignalStrength, speed, bearing, satellite, hdopSt);

                Log.e("DeviceInfoUpload: ", deviceInfo);
                Toast.makeText(context, "GPS coordinates uploaded to Cloud", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
