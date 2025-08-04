package com.jsijsling.androidx.initializerfragmentfactory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.jsijsling.androidx.fragment.fragmentFactory
import com.jsijsling.androidx.fragment.initializer
import com.jsijsling.androidx.initializerfragmentfactory.ui.TransactionDetailFragment
import com.jsijsling.androidx.initializerfragmentfactory.ui.TransactionListFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = fragmentFactory {
            initializer {
                TransactionListFragment(
                    onItemSelected = { selectedItem ->
                        val args = TransactionDetailFragment.args(selectedItem.id)
                        open<TransactionDetailFragment>(args)
                    },
                )
            }
        }
        super.onCreate(savedInstanceState)
    }

    private inline fun <reified T : Fragment> open(args: Bundle) {
        supportFragmentManager.commit {
            addToBackStack(null)
            setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            replace<T>(R.id.fragmentContainer, args = args)
        }
    }
}
