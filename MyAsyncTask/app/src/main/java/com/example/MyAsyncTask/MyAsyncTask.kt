package com.example.MyAsyncTask


import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.os.AsyncTask
import android.util.Log
import kotlin.properties.Delegates

class MyAsyncTask : AsyncTask<String,Int,Long>,SearchManager.OnCancelListener,DialogInterface.OnCancelListener,SearchManager.OnDismissListener{

    var TAG:String = "MyAsyncTask"
    var dialog:ProgressDialog by Delegates.notNull<ProgressDialog>()
    var context:Context by Delegates.notNull<Context>()

    constructor(context: Context){
        this.context = context
    }


    override fun onPreExecute() {
       // super.onPreExecute()
       /* Log.d(TAG,"onPreExecute")
        dialog.setTitle("Please wait")
        dialog.setMessage("Londing data....")
        dialog.setProgressStyle((ProgressDialog.STYLE_HORIZONTAL))
        dialog.setCancelable(true)
        dialog.setOnCancelListener(this)
        dialog.max = 100
        dialog.setProgressStyle(0)
        dialog.show()
*/

        Log.d(TAG,"onPreExecute")
        dialog = ProgressDialog(context)
        dialog.setTitle("Please wait")
        dialog.setMessage("Loding data...")
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        dialog.setCancelable(true)
//        dialog.setOnCancelListener(this as DialogInterface.OnCancelListener)
        dialog.max = 100
        dialog.setProgress(0)
        dialog.show()

    }

    override fun doInBackground(vararg params: String?): Long {
        Log.d(TAG, "doInBackground - " + params[0])
        try {
            Log.d(TAG, "Try - " + params[0])
            for (i in 0..9){
                Log.d(TAG, "forEach - " + i)
                if (isCancelled) {
                    Log.d(TAG, "Cancelled!")
                    break
                }
                Thread.sleep(1000)
                publishProgress((i + 1) * 10)
            }
        }catch (e:InterruptedException){
            Log.d(TAG, "InterruptedException in doInBackground")

        }
        return 123L
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        Log.d(TAG, "onProgressUpdate - "  + values[0])
        var value:Int = values[0]!!.toInt()
        dialog.setProgress(value)
    }

    override fun onCancelled() {
        super.onCancelled()
        Log.d(TAG, "onCancelled")
    }


    override fun onCancel(dialog: DialogInterface?) {

        //Log.d(TAG, "Dialog onCancell...calling cancel(true)")
        Log.d(TAG,"Dialog on Cancell" )


    }

    override fun onCancel(){

    }

    override fun onDismiss() {
     //   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}