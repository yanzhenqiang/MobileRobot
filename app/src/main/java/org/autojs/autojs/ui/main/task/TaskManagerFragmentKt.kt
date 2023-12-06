package org.autojs.autojs.ui.main.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import org.autojs.autojs.theme.widget.ThemeColorSwipeRefreshLayout
import org.autojs.autojs.ui.widget.fillMaxSize

class TaskManagerFragmentKt : Fragment() {

    private val swipeRefreshLayout by lazy {
        ThemeColorSwipeRefreshLayout(requireContext()).apply {
            fillMaxSize()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        init()
        return ComposeView(requireContext()).apply {
            setContent {
                this@TaskManagerFragmentKt.Content()
            }
        }
    }

    @Composable
    private fun Content() {
        Scaffold(
            floatingActionButton = {
            },
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = {
                        swipeRefreshLayout
                    },
                )
            }
        }
    }

    private fun init() {
        swipeRefreshLayout.setOnRefreshListener {
        }
    }

    init {
        arguments = Bundle()
    }
}