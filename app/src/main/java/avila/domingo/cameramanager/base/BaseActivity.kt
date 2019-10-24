package avila.domingo.cameramanager.base

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import avila.domingo.cameramanager.R
import avila.domingo.cameramanager.ui.data.ResourceState
import kotlinx.android.synthetic.main.activity_base.*

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var activityView: View

    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getLayoutId() == 0) {
            throw RuntimeException("Invalid Layout ID")
        }

        setContentView(R.layout.activity_base)

        activityView = layoutInflater.inflate(getLayoutId(), null)
        (view as FrameLayout).addView(
            activityView,
            LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        )

        view_empty.emptyListener = checkAgain()
        view_error.errorListener = tryAgain()

        initializeToolbar()
    }

    private fun initializeToolbar() {}

    protected abstract fun getLayoutId(): Int

    protected fun managementResourceState(resourceState: ResourceState, message: String?) {
        when (resourceState) {
            ResourceState.LOADING -> {
                view.visibility = VISIBLE
                view_error.visibility = GONE
                view_empty.visibility = GONE
                view_progress.visibility = VISIBLE
            }
            ResourceState.SUCCESS -> {
                view.visibility = VISIBLE
                view_error.visibility = GONE
                view_empty.visibility = GONE
                view_progress.visibility = GONE
            }
            ResourceState.EMPTY -> {
                view.visibility = GONE
                view_error.visibility = GONE
                view_empty.visibility = VISIBLE
                view_progress.visibility = GONE
            }
            ResourceState.ERROR -> {
//                view.visibility = GONE
//                view_error.visibility = VISIBLE
//                error_message.text = message ?: ""
//                view_empty.visibility = GONE
//                view_progress.visibility = GONE
                toast?.cancel()
                toast = Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
                    setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                    show()
                }
            }
        }
    }

    abstract fun checkAgain(): () -> Unit

    abstract fun tryAgain(): () -> Unit
}