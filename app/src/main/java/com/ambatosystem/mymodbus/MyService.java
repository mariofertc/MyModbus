package com.ambatosystem.mymodbus;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
//import com.serotonin.modbus4j.ModbusLocator;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.base.SlaveAndRange;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.ambatosystem.mymodbus.R;

import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//import java.util.logging.Handler;
//import com.serotonin.modbus4j.ModbusLocator;

public class MyService extends Service {

    private boolean mAllowRebind; // indicates whether onRebind should be used
    private BatchRead<String> mCurrentBatch;
    private MyServiceRunnable mMyServiceRunnable;
    //private PollingRunnable mPollingRunnable;
    //private ; //connection data or modbus factory/master
    private boolean mConnected = false;
    Handler timerHandler = new Handler();
//    ModbusFactory modbusFactory = new ModbusFactory();
//    ModbusMaster master;
    Thread threadModbus;
    //private ThreadPoolExecutor mThreadPool;
    public ModbusFactory modbusFactory;
    public ModbusMaster master;

    public SharedPreferences prefs = null;
    private int preferencesBasicMenu;

    Intent intent;
    public static final String BROADCAST_ACTION = "com.ambatosystem.mymodbus.reponse";

    /*public MyService() {
        super();
    }*/
    private class MyServiceHandler extends Handler
    {
        public MyServiceHandler(Looper looper, Callback callback) {
            super(looper, callback);
        }

        public MyServiceHandler() {
            super();
        }
        @Override
        public void handleMessage(Message msg) {
            // Eventually we need to check what the message is, and respond according.

            // Case statement to check message type, basically we are looking for a "Batch Update"
            //  to update our batch info.

            // Remove this later
            //stopSelf(msg.arg1);
        }
    }

    /*@Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }*/

    @Override
    public void onCreate() {
        // start new thread and you your work there
        Log.d("TAG", "Service created.");
        //prefs = getSharedPreferences("com.ambatosystem.mymodbus", MODE_PRIVATE);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean("firstrun", true).commit();
        createPool();

        intent = new Intent(BROADCAST_ACTION);

        /*mMyServiceRunnable = new MyServiceRunnable();
        timerHandler.postDelayed(mMyServiceRunnable, 5000);*/

        //mMyServiceRunnable.run();
        //mMyServiceRunnable.

        //mMyServiceRunnable

        /*mMyServiceRunnable = new MyServiceRunnable();
        new Thread(mMyServiceRunnable).start();*/

        //mMyServiceRunnable.run();
        //mMyServiceRunnable.

        /*handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 5000);*/

        // prepare a notification for user and start service foreground
        //Notification notification = ...
        // this will ensure your service won't be killed by Android
        //startForeground(R.id.notification, notification);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // We need to do some logic here to return the interface if someone exists the client app,
        //  but wants to continue polling in the background.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        Log.d("TAG", "Service started command.");
        return super.onStartCommand(intent, flags, startId);
        //return START_STICKY;
    }
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if(prefs.getBoolean("firstrun", true)) {
            Log.d("TAG", "Service started. 1rst, nothing to do.");
            prefs.edit().putBoolean("firstrun", false).commit();
        }else{
            Log.d("TAG", "Service started. more than one, connect Pool.");
            createPool();
        }
    }

    @Override
    public void onDestroy() {
        closeConnection();
        Log.d("TAG", "Service stop.");
        //new Thread(mMyServiceRunnable).stop();
        super.onDestroy();
        // The service is no longer used and is being destroyed
    }
    public void createPool(){
        //TODO: Check if there are a thread to run a new.
        if(mMyServiceRunnable == null)
            mMyServiceRunnable = new MyServiceRunnable();

        //mThreadPool.execute(mMyServiceRunnable);
        //mThreadPool = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),Runtime.getRuntime().availableProcessors(), 1,TimeUnit.SECONDS);

        if(threadModbus != null){
            if(threadModbus.isAlive()) {
                Log.d("TAG", "Esta el pool ya iniciado.");
                return;
            }
            Log.d("TAG", "Thread iniciada pero no esta en alive.");
            try {
                closeConnection();
                threadModbus = null;
            }catch (Exception e) {
                Log.e( getClass().getSimpleName(), e.getMessage() );
                mConnected = false;
            }
            return;
            //threadModbus = new Thread(mMyServiceRunnable);
            /*threadModbus.start();
            return;*/
        }
        else {
            threadModbus = new Thread(mMyServiceRunnable);
        }
        Log.d("TAG", "Se inicia el hilo.");
        threadModbus.start();
        super.onCreate();
    }

    public void createConnection(){
        IpParameters tcpParameters = new IpParameters();
        //R.layout.modbus_data_fragment
        //R.xml.preferences_basic_menu()
        //R.xml.preferences_basic_menu..getSystem().getString(android.R.string.cancel)

        /*Resources res = getResources();
        preferencesBasicMenu = R.xml.preferences_basic_menu;
        int i= res.getInteger(preferencesBasicMenu.edit_text_preference_1);*/


        //-Preference edit1= findPreference("edit1");
        //getPersistedInt( )

        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //String ip = sharedPreferences.getString("edit_text_preference_1", "default");
        String ip = prefs.getString("edit_text_preference_1", "default");
        Integer port = prefs.getInt("pref_key_modbus_port",1);




        //tcpParameters.setHost("172.16.5.194");
        //tcpParameters.setHost("192.168.1.6");
        //tcpParameters.setPort(502);
        tcpParameters.setHost(ip);
        tcpParameters.setPort(port);
        Log.d("TAG", "Creating Modbus Connection. IP: " + ip + "port:"+ port);

        if(master != null) {
            if(master.isConnected()) {
                Log.d("TAG", "Modbus is connected.");
                mConnected = true;
                return;
            }
            master.destroy();
        }
        if(modbusFactory == null)
            modbusFactory = new ModbusFactory();
        //ModbusMaster master = modbusFactory.createTcpMaster(tcpParameters, true);

        master = modbusFactory.createTcpMaster(tcpParameters, true);

    }

    public void closeConnection(){
        //mMyServiceRunnable.wait(10);
        //timerHandler.removeCallbacks(mMyServiceRunnable);
//
//handler.removeCallbacksAndMessages(null);
        threadModbus.interrupt();
        //threadModbus.stop();
        //threadModbus.destroy();
        //master.destroy();
        prefs.edit().putBoolean("firstrun", true).commit();
        mConnected = false;
    }

    public void setCurrentBatch(BatchRead batch) {
        this.mCurrentBatch = batch;
    }

    private class MyServiceRunnable implements Runnable {

        @Override
        public void run() {
            // I do stuff at regular intervals

            // This should probably all go somewhere else - and should be done based on
            //  the user selection of modbus connection type.

            Log.d("TAG", "Runnable Started.");

            // End of parameters that need to be abstracted somewhere better

            if (!mConnected) {
                try {
                      createConnection();
                      Log.d("TAG", "Send connection with modbus.");
                      master.init();
                }
                catch (Exception e) {
                    Log.e( getClass().getSimpleName(), e.getMessage() );
                }

                mConnected = true;

            }

            while (mConnected) {
                try {
                    // Dummy data
                    if (mCurrentBatch == null) {
                        mCurrentBatch = new BatchRead<String>();
                    }
                    /*mCurrentBatch.addLocator("40001",BaseLocator.holdingRegister(1, 40001, DataType.TWO_BYTE_INT_SIGNED));
                    mCurrentBatch.addLocator("40002",BaseLocator.holdingRegister(1, 40002, DataType.TWO_BYTE_INT_SIGNED));
                    mCurrentBatch.addLocator("40003",BaseLocator.holdingRegister(1, 40003, DataType.TWO_BYTE_INT_SIGNED));
                    mCurrentBatch.addLocator("40004",BaseLocator.holdingRegister(1, 40004, DataType.TWO_BYTE_INT_SIGNED));
                    mCurrentBatch.addLocator("40005",BaseLocator.holdingRegister(1, 40005, DataType.TWO_BYTE_INT_SIGNED));
                    mCurrentBatch.addLocator("40006",BaseLocator.holdingRegister(1, 40006, DataType.TWO_BYTE_INT_SIGNED));
                    mCurrentBatch.addLocator("40007",BaseLocator.holdingRegister(1, 40007, DataType.TWO_BYTE_INT_SIGNED));
                    mCurrentBatch.addLocator("40008",BaseLocator.holdingRegister(1, 40008, DataType.TWO_BYTE_INT_SIGNED));
                    mCurrentBatch.addLocator("40009",BaseLocator.holdingRegister(1, 40009, DataType.TWO_BYTE_INT_SIGNED));
                    mCurrentBatch.addLocator("40010",BaseLocator.holdingRegister(1, 40010, DataType.TWO_BYTE_INT_SIGNED));
                    mCurrentBatch.addLocator("40011",BaseLocator.holdingRegister(1, 40011, DataType.TWO_BYTE_INT_SIGNED));*/
                    //mCurrentBatch.addLocator("40063",BaseLocator.holdingRegister(1, 40063, DataType.TWO_BYTE_INT_SIGNED));
                    //mCurrentBatch.addLocator("40064",BaseLocator.holdingRegister(1, 40064, DataType.TWO_BYTE_INT_SIGNED));
                    mCurrentBatch.addLocator("63",BaseLocator.holdingRegister(1, 63, DataType.TWO_BYTE_INT_SIGNED));
                    mCurrentBatch.addLocator("64",BaseLocator.holdingRegister(1, 64, DataType.TWO_BYTE_INT_SIGNED));
                    BatchResults<String> results = master.send(mCurrentBatch);
                    intent.putExtra("63",results.getValue("63").toString());
                    intent.putExtra("64", results.getValue("64").toString());
                    sendBroadcast(intent);
                    //results.getDoubleValue("64");

                    //intent.putStringArrayListExtra("response",(ArrayList<String>) results);
                    //intent.putIntegerArrayListExtra("results",results);
                    //intent.putParcelableArrayListExtra("res", results);


                    /*intent.putExtra("time", new Date().toLocaleString());
                    intent.putExtra("counter", String.valueOf(++counter));*/


                    //(TextView)findViewById(R.id.Modbus);

                    //results.getValue(1);

                    /*mCurrentBatch.addLocator("40001 sb -1968",
                            BaseLocator.holdingRegister(1, 40000, DataType.TWO_BYTE_INT_SIGNED));
                    Number results_read = master.getValue(BaseLocator.holdingRegister(1, 40001, DataType.TWO_BYTE_INT_SIGNED));*/
                    Log.d("TAG", "Get Modbus Registers." + results );
                    Thread.sleep(1000);

                    //handler.postDelayed(this, 5000);
                }
                catch(InterruptedException ex) {
                    System.err.println("An InterruptedException was caught: " + ex.getMessage());
                    //Log.e("yo"+getClass().getSimpleName(),getClass().getSimpleName());
                    Log.e("yo"+getClass().getSimpleName(), "Error no controlado");
                    mConnected = false;
                }
                catch (Exception e) {
                    Log.e( getClass().getSimpleName(), "Error no controlado" );
                    mConnected = false;
                }
            }
        }
    }
}
