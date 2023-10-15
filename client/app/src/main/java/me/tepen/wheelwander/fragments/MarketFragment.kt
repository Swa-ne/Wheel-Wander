package me.tepen.wheelwander.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import me.tepen.wheelwander.R
import me.tepen.wheelwander.adapters.UserMessageAdapter

class MarketFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_market, container, false)
//        val dialogFragment = layoutInflater.inflate(R.layout.add_product_dialog, null)
//        val dialog = Dialog(view.context, R.style.AppTheme_FullScreenDialog)
//        dialog.setContentView(dialogFragment)
        view.findViewById<FloatingActionButton>(R.id.floating_action_button).setOnClickListener {
            AddProductDialog().display(fragmentManager)
        }
        return view
    }


}