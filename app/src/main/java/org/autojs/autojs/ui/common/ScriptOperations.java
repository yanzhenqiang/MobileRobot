package org.autojs.autojs.ui.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import org.autojs.autojs.model.explorer.Explorer;
import org.autojs.autojs.model.explorer.ExplorerDirPage;
import org.autojs.autojs.model.explorer.ExplorerPage;
import org.autojs.autojs.model.explorer.Explorers;
import org.autojs.autojs.model.script.ScriptFile;

/**
 * Created by Stardust on 2017/7/31.
 */

@SuppressLint("CheckResult")
public class ScriptOperations {

    private static final String LOG_TAG = "ScriptOperations";
    private final ExplorerPage mExplorerPage;
    private final Context mContext;
    private final View mView;
    private final ScriptFile mCurrentDirectory;
    private final Explorer mExplorer;

    public ScriptOperations(Context context, View view, ScriptFile currentDirectory) {
        mContext = context;
        mView = view;
        mCurrentDirectory = currentDirectory;
        mExplorer = Explorers.workspace();
        mExplorerPage = new ExplorerDirPage(currentDirectory, null);
    }
}
