// Взято из https://android.jlelse.eu/learn-kotlin-while-developing-an-android-app-part-1-e0f51fc1a8b3

package com.example.evgen.kedditbysteps

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.evgen.kedditbysteps.features.news.NewsFragment

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        /*
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/

        val context: Context? = null
        /*
        if (context != null) {
            val res = context.resources
            val appName = res?.getString(R.string.app_name)
            val shortName = appName?.substring(0, 5)
        }
        // OR..

        context?.let {
            val appName = context.resources?.getString(R.string.app_name)
            val shortName = appName?.substring(0, 5)
        }
        // OR..
        */

        try { // TODO Почему это не работает?
            val appName = context?.resources?.getString(R.string.app_name)
            val shortName = appName?.substring(0, 5)
            //var message = findViewById<TextView>(R.layout.) // TODO Сменить "Hello, world!"
            Log.i("TAG", shortName)
        } catch (e: Throwable) {
            Log.e("TAG", "Error message context value"/*e.message  ?: "Error message"*/) // TODO разобраться здесь
        }

        if (savedInstanceState == null) {
            changeFragment(NewsFragment())
            Log.i("TAG", "End onCreate")
        }
    }

    @SuppressLint("PrivateResource")
    private fun changeFragment(f: Fragment, cleanStack: Boolean = false) {
        val ft = supportFragmentManager.beginTransaction()
        if (cleanStack) {
            clearBackStack()
        }
        ft.setCustomAnimations(
                R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_popup_enter, R.anim.abc_popup_exit)
        ft.replace(R.id.activity_base_content, f)
        ft.addToBackStack(null)
        ft.commit()
    }

    private fun clearBackStack() {
        val manager = supportFragmentManager
        if (manager.backStackEntryCount > 0) {
            val first = manager.getBackStackEntryAt(0)
            manager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    /**
     * Finish activity when reaching the last fragment.
     */
    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStack()
        } else {
            finish()
        }
    }
    /*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    */
}
