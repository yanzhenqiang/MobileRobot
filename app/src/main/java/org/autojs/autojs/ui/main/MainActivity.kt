package org.autojs.autojs.ui.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.autojs.autoxjs.R
import org.autojs.autojs.external.foreground.PorcupineService
import org.autojs.autojs.ui.compose.theme.AutoXJsTheme
import org.autojs.autojs.ui.log.LogActivityKt
import org.autojs.autojs.ui.main.drawer.DrawerPage
import org.autojs.autojs.ui.main.scripts.ScriptListFragment
import org.autojs.autojs.ui.main.task.TaskManagerFragmentKt
import org.autojs.autojs.ui.widget.fillMaxSize

class MainActivity : FragmentActivity() {

    private val scriptListFragment by lazy { ScriptListFragment() }
    private val taskManagerFragment by lazy { TaskManagerFragmentKt() }
    private var drawerState: DrawerState? = null
    private val viewPager: ViewPager2 by lazy { ViewPager2(this) }
    private var scope: CoroutineScope? = null


    private val RECORD_PERMISSION_REQUEST_CODE = 0

    private fun hasRecordPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestRecordPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            RECORD_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startService()
        } else {
            onPorcupineInitError("Microphone permission is required for this demo")
        }
    }

    private fun startService() {
        val serviceIntent = Intent(this, PorcupineService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    private fun onPorcupineInitError(message: String) {
    }

    
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (hasRecordPermission()) {
            startService()
        } else {
            requestRecordPermission()
        }

        setContent {
            scope = rememberCoroutineScope()
            AutoXJsTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val permission = rememberExternalStoragePermissionsState {
                        if (it) {
                            scriptListFragment.explorerView.onRefresh()
                        }
                    }
                    LaunchedEffect(key1 = Unit, block = {
                        permission.launchMultiplePermissionRequest()
                    })
                    MainPage(
                        activity = this,
                        scriptListFragment = scriptListFragment,
                        taskManagerFragment = taskManagerFragment,
                        onDrawerState = {
                            this.drawerState = it
                        },
                        viewPager = viewPager
                    )
                }
            }
        }
    }

}

@Composable
fun MainPage(
    activity: FragmentActivity,
    scriptListFragment: ScriptListFragment,
    taskManagerFragment: TaskManagerFragmentKt,
    onDrawerState: (DrawerState) -> Unit,
    viewPager: ViewPager2
) {
    val scaffoldState = rememberScaffoldState()
    onDrawerState(scaffoldState.drawerState)
    val scope = rememberCoroutineScope()

    var currentPage by remember {
        mutableStateOf(0)
    }

    SetSystemUI(scaffoldState)

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        topBar = {
            Surface(elevation = 4.dp, color = MaterialTheme.colors.primarySurface) {
                Column {
                    Spacer(
                        modifier = Modifier
                            .windowInsetsTopHeight(WindowInsets.statusBars)
                    )
                    TopBar(
                        requestOpenDrawer = {
                            scope.launch { scaffoldState.drawerState.open() }
                        },
                    )
                }
            }
        },
        drawerContent = {
            DrawerPage()
        },

        ) {
        AndroidView(
            modifier = Modifier.padding(it),
            factory = {
                viewPager.apply {
                    fillMaxSize()
                    adapter = ViewPager2Adapter(
                        activity,
                        scriptListFragment,
                        taskManagerFragment
                    )
                    isUserInputEnabled = false
                    ViewCompat.setNestedScrollingEnabled(this, true)
                }
            },
            update = { viewPager0 ->
                viewPager0.currentItem = currentPage
            }
        )
    }
}

fun showExternalStoragePermissionToast(context: Context) {
    Toast.makeText(
        context,
        context.getString(R.string.text_please_enable_external_storage),
        Toast.LENGTH_SHORT
    ).show()
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberExternalStoragePermissionsState(onPermissionsResult: (allAllow: Boolean) -> Unit) =
    rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ),
        onPermissionsResult = { map ->
            onPermissionsResult(map.all { it.value })
        })

@Composable
private fun SetSystemUI(scaffoldState: ScaffoldState) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons =
        if (MaterialTheme.colors.isLight) {
            scaffoldState.drawerState.isOpen || scaffoldState.drawerState.isAnimationRunning
        } else false

    val navigationUseDarkIcons = MaterialTheme.colors.isLight
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )
        systemUiController.setNavigationBarColor(
            Color.Transparent,
            darkIcons = navigationUseDarkIcons
        )
    }
}

@Composable
private fun TopBar(
    requestOpenDrawer: () -> Unit,
) {
    val context = LocalContext.current
    TopAppBar(elevation = 0.dp) {
        CompositionLocalProvider(
            LocalContentAlpha provides ContentAlpha.high,
        ) {
            IconButton(onClick = requestOpenDrawer) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(id = R.string.text_menu),
                )
            }

            ProvideTextStyle(value = MaterialTheme.typography.h6) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.app_name)
                )
            }
            IconButton(onClick = { LogActivityKt.start(context) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_logcat),
                    contentDescription = stringResource(id = R.string.text_logcat)
                )
            }
            var expanded by remember {
                mutableStateOf(false)
            }
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = stringResource(id = R.string.desc_more)
                    )
                }
            }
        }
    }
}