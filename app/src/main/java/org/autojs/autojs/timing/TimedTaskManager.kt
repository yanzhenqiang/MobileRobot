package org.autojs.autojs.timing

import android.annotation.SuppressLint
import android.app.Application
import android.text.TextUtils
import io.reactivex.Flowable
import io.reactivex.Observable
import org.autojs.autojs.App
import org.autojs.autojs.App.Companion.app
import org.autojs.autojs.storage.database.IntentTaskDatabase
import org.autojs.autojs.storage.database.ModelChange
import org.autojs.autojs.tool.Observers

/**
 * Created by Stardust on 2017/11/27.
 */
object TimedTaskManager {

    private var context: Application = app
    private val intentTaskDatabase: IntentTaskDatabase = IntentTaskDatabase(context)

    @SuppressLint("CheckResult")
    fun addTask(intentTask: IntentTask) {
        intentTaskDatabase.insert(intentTask)
            .subscribe({ i: Long? ->
                if (!TextUtils.isEmpty(intentTask.action)) {
                    app.dynamicBroadcastReceivers
                        .register(intentTask)
                }
            }) { obj: Throwable -> obj.printStackTrace() }
    }

    @SuppressLint("CheckResult")
    fun removeTask(intentTask: IntentTask) {
        intentTaskDatabase.delete(intentTask)
            .subscribe({ i: Int? ->
                if (!TextUtils.isEmpty(intentTask.action)) {
                    app.dynamicBroadcastReceivers
                        .unregister(intentTask.action)
                }
            }) { obj: Throwable -> obj.printStackTrace() }
    }

    fun getIntentTaskOfAction(action: String?): Flowable<IntentTask> {
        return intentTaskDatabase.query("action = ?", action)
    }

    @SuppressLint("CheckResult")
    fun updateTask(task: IntentTask) {
        intentTaskDatabase.update(task)
            .subscribe({ i: Int ->
                if (i > 0 && !TextUtils.isEmpty(task.action)) {
                    app.dynamicBroadcastReceivers
                        .register(task)
                }
            }) { obj: Throwable -> obj.printStackTrace() }
    }

    val allIntentTasksAsList: List<IntentTask>
        get() = intentTaskDatabase.queryAll()
    val intentTaskChanges: Observable<ModelChange<IntentTask>>
        get() = intentTaskDatabase.modelChange

    fun getIntentTask(intentTaskId: Long): IntentTask {
        return intentTaskDatabase.queryById(intentTaskId)
    }

    val allIntentTasks: Flowable<IntentTask>
        get() = intentTaskDatabase.queryAllAsFlowable()

}