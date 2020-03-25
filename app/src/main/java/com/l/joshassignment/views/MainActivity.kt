package com.l.joshassignment.views

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.l.joshassignment.R
import com.l.joshassignment.helper.click
import com.l.joshassignment.helper.gone
import com.l.joshassignment.helper.show
import com.l.joshassignment.responseModel.FlickerData
import com.l.joshassignment.responseModel.QueryAlreadyInProgress
import com.l.joshassignment.viewModel.JoshViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_search_bar.*


class MainActivity : AppCompatActivity() {
    var model: JoshViewModel? = null
    private val subscriptions = CompositeDisposable()
    private var resultList: MutableList<FlickerData> = mutableListOf()
    private val adapter = FilckrAdapter()
    private var page = 1

    private enum class STATE {
        LOADING, LOADED, ERROR, EMPTY
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        model = ViewModelProviders.of(this).get(JoshViewModel::class.java)

        setUpRecycleView()

        search_edit_text.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyBoard(search_edit_text)
                    searchFlickerImages(1, true)
                    return true
                }
                return false
            }
        })

        search_text.click {
            hideKeyBoard(search_edit_text)
            searchFlickerImages(1, true)
        }
    }


    fun searchFlickerImages(page: Int, isClearAdapter: Boolean = false) {
        if (page == 1) {
            setState(STATE.LOADING)
        }

        model?.getPhotoBasedOnSearchTerm(search_edit_text.text.toString(), page)
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                setState(STATE.LOADED)
                if (isClearAdapter) {
                    resultList.clear()
                    adapter.clear()
                }
                resultList.addAll(it.photo ?: mutableListOf())
                if (it.photo?.isEmpty() == true) setState(STATE.EMPTY)
                adapter.setData(it.photo ?: mutableListOf())
            }, {
                when (it) {
                    is QueryAlreadyInProgress -> {
                    }
                    else -> {
                        setState(STATE.ERROR)
                    }
                }
            })
            ?.let { subscriptions.add(it) }
    }

    private fun setUpRecycleView() {

        explore_rv?.adapter = adapter
        explore_rv?.layoutManager = LinearLayoutManager(this)
        explore_rv?.itemAnimator = DefaultItemAnimator()
        explore_rv?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val position =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (position >= resultList.size - 1) {
                    searchFlickerImages(++page)
                }
            }
        })
    }

    fun hideKeyBoard(view: View) {
        view.let {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun setState(state: STATE, message: String? = null) {
        when (state) {
            STATE.LOADING -> {
                progress_bar.show()
                text_no_data.gone()
            }
            STATE.LOADED -> {
                progress_bar.gone()
                text_no_data.gone()
            }
            STATE.ERROR -> {
                if (adapter.itemCount == 0) {
                    text_no_data.text = getString(R.string.no_network)
                } else {
                    Toast.makeText(this, getString(R.string.no_network), Toast.LENGTH_LONG).show()
                }
                progress_bar.gone()
                text_no_data.show()
            }
            STATE.EMPTY -> {
                if (adapter.itemCount == 0) {
                    text_no_data.text = getString(R.string.no_data_found)
                } else {
                    Toast.makeText(this, getString(R.string.no_data_found), Toast.LENGTH_LONG)
                        .show()
                }
                text_no_data.show()
                progress_bar.gone()
            }
        }
    }

}


