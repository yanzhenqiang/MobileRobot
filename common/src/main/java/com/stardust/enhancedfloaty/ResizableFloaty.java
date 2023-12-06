package com.stardust.enhancedfloaty;

import androidx.annotation.Nullable;
import android.view.View;

/**
 * Created by Stardust on 2017/4/30.
 */

public interface ResizableFloaty {


    View inflateView(FloatyService floatyService, ResizableFloatyWindow service);

    @Nullable
    View getResizerView(View view);

    @Nullable
    View getMoveCursorView(View view);

    abstract class AbstractResizableFloaty implements ResizableFloaty {
        @Nullable
        public View getResizerView(View view) {
            return null;
        }

        @Nullable
        public View getMoveCursorView(View view) {
            return null;
        }

    }
}
