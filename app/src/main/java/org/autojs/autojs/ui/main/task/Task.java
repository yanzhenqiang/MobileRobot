package org.autojs.autojs.ui.main.task;

import com.stardust.app.GlobalAppContext;
import com.stardust.autojs.engine.ScriptEngine;
import com.stardust.autojs.execution.ScriptExecution;
import com.stardust.autojs.script.AutoFileSource;
import com.stardust.autojs.script.JavaScriptSource;
import com.stardust.pio.PFiles;

import org.autojs.autoxjs.R;
import org.autojs.autojs.timing.IntentTask;
import org.autojs.autojs.timing.TimedTaskManager;

import org.joda.time.format.DateTimeFormat;

/**
 * Created by Stardust on 2017/11/28.
 */

public abstract class Task {


    public abstract String getName();

    public abstract String getDesc();

    public abstract void cancel();

    public abstract String getEngineName();

    public static class PendingTask extends Task {


        private IntentTask mIntentTask;

        public PendingTask(IntentTask intentTask) {
            mIntentTask = intentTask;
        }

        public boolean taskEquals(Object task) {
            return mIntentTask.equals(task);
        }

        @Override
        public String getName() {
            return PFiles.getSimplifiedPath(getScriptPath());
        }

        @Override
        public String getDesc() {
            {
                assert mIntentTask != null;
                return mIntentTask.getAction();
            }

        }

        @Override
        public void cancel() {
            {
                TimedTaskManager.INSTANCE.removeTask(mIntentTask);
            }
        }

        private String getScriptPath() {
            {
                assert mIntentTask != null;
                return mIntentTask.getScriptPath();
            }
        }

        @Override
        public String getEngineName() {
            if (getScriptPath().endsWith(".js")) {
                return JavaScriptSource.ENGINE;
            } else {
                return AutoFileSource.ENGINE;
            }
        }

        public void setIntentTask(IntentTask intentTask) {
            mIntentTask = intentTask;
        }

        public long getId() {
            return mIntentTask.getId();
        }
    }

    public static class RunningTask extends Task {
        private final ScriptExecution mScriptExecution;

        public RunningTask(ScriptExecution scriptExecution) {
            mScriptExecution = scriptExecution;
        }

        public ScriptExecution getScriptExecution() {
            return mScriptExecution;
        }

        @Override
        public String getName() {
            return mScriptExecution.getSource().getName();
        }

        @Override
        public String getDesc() {
            return mScriptExecution.getSource().toString();
        }

        @Override
        public void cancel() {
            ScriptEngine engine = mScriptExecution.getEngine();
            if (engine != null) {
                engine.forceStop();
            }
        }

        @Override
        public String getEngineName() {
            return mScriptExecution.getSource().getEngineName();
        }
    }
}
