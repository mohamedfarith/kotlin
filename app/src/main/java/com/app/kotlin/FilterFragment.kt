package com.app.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.app.kotlin.databinding.FilterFragmentBinding

class FilterFragment : DialogFragment() {

    lateinit var binding: FilterFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.filter_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.language.setOnClickListener {
            //Language selected
            binding.adapter = FilterRecyclerAdapter("language")
        }
        binding.year.setOnClickListener {
            //year selected
            binding.adapter = FilterRecyclerAdapter("year")

        }
        binding.applyButton.setOnClickListener(View.OnClickListener {
            binding.adapter?.getSelectedOption()
        })


    }

    override fun show(manager: FragmentManager, tag: String?) {
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.window?.setLayout(width, height)
        }
    }

    interface GenericInterface<T> {
        fun callback(t: T)
    }


    class FilterRecyclerAdapter(val type: String) :
        RecyclerView.Adapter<FilterViewHolder>() {
        val LANGUAGE_TEXT: Int = 1234
        val YEAR_TEXT: Int = 123
        val languageList: List<String>
        val yearList: List<Int>
        var selectedOption: HashMap<String, String> = HashMap()

        init {
            languageList = Constants.LANGUAGE_LIST.keys.toList()
            yearList = Constants.YEAR_LIST.values.toList()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
            val textView = TextView(parent.context)
            with(textView) {
                setTextColor(ContextCompat.getColor(parent.context, R.color.black))
                setTextSize(18f)
            }

            return FilterViewHolder(textView)
        }

        override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
            val textView = holder.itemView as TextView
            if (getItemViewType(position) == LANGUAGE_TEXT) {
                textView.setText(languageList.get(position))
                textView.setTag("language")
            } else {
                textView.setText(yearList.get(position).toString())
                textView.setTag("year")
            }
            textView.setOnClickListener {
                val tag = it.getTag() as String
                if (tag.equals("language")) {
                    var value = (it as TextView).text.toString()
                    Constants.LANGUAGE_LIST.get(value)?.let { it1 ->
                        LocalStorage.getStorageInstance(it.context)
                            .putString(Constants.SELECTED_LANGUAGE, it1)
                    }
                } else if (tag.equals("year"))
                    LocalStorage.getStorageInstance(it.context)
                        .putString(Constants.SELECTED_YEAR, (it as TextView).text.toString())
            }

        }

        fun getSelectedOption(): Map<String, String> {
            return selectedOption;
        }

        override fun getItemViewType(position: Int): Int {

            return if (type.equals("language")) LANGUAGE_TEXT else YEAR_TEXT

        }

        override fun getItemCount(): Int {
            if (type.equals("language")) {
                return Constants.LANGUAGE_LIST.size
            } else {
                return Constants.YEAR_LIST.size
            }
        }

    }

    class FilterViewHolder(itemView: TextView) : RecyclerView.ViewHolder(itemView) {

    }
}